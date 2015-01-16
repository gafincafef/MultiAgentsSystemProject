package edu.rits.ma.jade.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.InProcessObjectMessage;
import edu.rits.ma.jade.communication.IncomingBuffer;
import edu.rits.ma.jade.communication.OutCommingBuffer;
import edu.rits.ma.jade.util.LogUtil;

public class PrimaryTaskByTaskProcessorImpl implements ICommunicationBufferProcessor {
	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY = 0;
	private static final int ACTION_PHASE_TO_PROCESS_READY_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK = 3;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE = 4;
	private static final int ACTION_PHASE_TO_STOP = 5;

	private int mInternalState = ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	private List<ITask> mTasks = null;
	private List<String> mSecondaryAgentNames = null;

	private TaskExecutingStatus mExecutingStatus = new TaskExecutingStatus();

	public PrimaryTaskByTaskProcessorImpl(List<ITask> tasks, List<String> secondaryAgentNames) {
		mTasks = tasks;
		mSecondaryAgentNames = secondaryAgentNames;
		mExecutingStatus.setNumberOfSecondaryAgentsNotReady(secondaryAgentNames.size());
	}

	@Override
	public void processCommunicationDataStore(IncomingBuffer receiveBuffer, OutCommingBuffer sendBuffer) {
		updateInternalState(receiveBuffer);
		try {
			int nextActionPhase = mInternalState;
			switch (mInternalState) {
			case ACTION_PHASE_TO_PROCESS_READY_MESSAGE:
				nextActionPhase = onReadyMessage(sendBuffer);
				break;
			case ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE:
				nextActionPhase = onTaskDoneMessage(receiveBuffer, sendBuffer);
				break;
			default:
				break;
			}
			mInternalState = nextActionPhase;
		} catch (Exception e) {
			e.printStackTrace();
			mInternalState = ACTION_PHASE_TO_STOP;
		}
	}

	@Override
	public boolean done() {
		return mInternalState == ACTION_PHASE_TO_STOP;
	}

	private void updateInternalState(IncomingBuffer receiveBuffer) {
		if(receiveBuffer.hasReceivedData()) {
			ACLMessage message = receiveBuffer.extractReceivedMessage();
			mInternalState = detectStateByReceivedMessage(message); 
		}
	}

	private int onReadyMessage(OutCommingBuffer sendBuffer) {
		mExecutingStatus.decrementNumberOfSecondaryAgentsNotReady();
		if(mExecutingStatus.allSecondaryAgentReady()) {
			LogUtil.logInfo(this, "All secondary agent ready!");
			runNextTask(sendBuffer);
			return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	}

	private int onTaskDoneMessage(IncomingBuffer receiveBuffer, OutCommingBuffer sendBuffer) {
		receiveBuffer.extractReceivedMessage();
		mExecutingStatus.decrementNumberOfSubTasksToWait();
		
		if(mExecutingStatus.allSubTasksDone()) {
			LogUtil.logInfo(this, "All sub tasks done " + mExecutingStatus.getNextTaskId() + " / " + mTasks.size());
			if(mExecutingStatus.allTasksDone()) {
				stopSecondaryAgents(sendBuffer);
				return ACTION_PHASE_TO_STOP;
			}
			else {
				runNextTask(sendBuffer);
			}
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}

	private void runNextTask(OutCommingBuffer sendBuffer) {
		ITask nextTask = mTasks.get(mExecutingStatus.getNextTaskId());
		int numberOfSubTasksToWait = runNextTask(nextTask, sendBuffer);
		mExecutingStatus.incrementNextTaskId();
		mExecutingStatus.setNumberOfSubTasksToWait(numberOfSubTasksToWait);
	}
	
	private int runNextTask(ITask task, OutCommingBuffer sendBuffer) {
		int newSecondaryTasks = 0;
		
		//Firstly execute the task assigned to primary agent
		task.execute();

		if(task.getStatus() == ITask.TASK_STATUS_SUCCEEDED) {
			List<ITask> subTasks = new ArrayList<ITask>();

			//Each task spawns a list of sub task after finished
			task.createSubTasks(subTasks);

			for(int i = 0; i < subTasks.size(); i++) {
				int secondaryAgentIndex = i; 
				newSecondaryTasks++;

				//Add task object to secondary agents by adding task to queue
				assignTaskToSecondaryAgent(sendBuffer, mSecondaryAgentNames.get(secondaryAgentIndex), subTasks.get(i));
			}
		}
		
		return newSecondaryTasks;
	}

	private void assignTaskToSecondaryAgent(OutCommingBuffer sendBuffer, String secondaryAgentName, ITask task) {
		InProcessObjectMessage objectMessage = new InProcessObjectMessage();
		objectMessage.setObject(task);
		objectMessage.setTargetAgentName(secondaryAgentName);
		sendBuffer.addObjectToSend(objectMessage);
	}
	
	private void stopSecondaryAgents(OutCommingBuffer sendBuffer) {
		//TODO Implement using ontology
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		for(String secondaryAgentName : mSecondaryAgentNames) {
			message.addReceiver(new AID(secondaryAgentName, AID.ISGUID));
		}
		message.setContent("Stop");
		sendBuffer.addMessageToSend(message);
	}

	private int detectStateByReceivedMessage(ACLMessage message) {
		//TODO Implement using ontology
		if(message.getContent().equals("Ready")) {
			return ACTION_PHASE_TO_PROCESS_READY_MESSAGE;
		}
		return ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE;
	}
	
	private class TaskExecutingStatus {
		private int mNextTaskId = 0;
		private int mNumberOfSubTasksToWait = 0;
		private int mNumberOfSecondaryAgentsNotReady = 0;

		public int getNextTaskId() {
			return mNextTaskId;
		}
		
		public void incrementNextTaskId() {
			mNextTaskId++;
		}
		
		public boolean allTasksDone() {
			return mNextTaskId >= mTasks.size();
		}
		
		public boolean allSubTasksDone() {
			return mNumberOfSubTasksToWait <= 0;
		}
		
		public void setNumberOfSubTasksToWait(int numberOfSubTasksToWait) {
			mNumberOfSubTasksToWait = numberOfSubTasksToWait;
		}
		
		public void decrementNumberOfSubTasksToWait() {
			mNumberOfSubTasksToWait--;
		}

		public boolean allSecondaryAgentReady() {
			return mNumberOfSecondaryAgentsNotReady <= 0;
		}

		public void setNumberOfSecondaryAgentsNotReady(int mNumberOfSecondaryAgentsNotReady) {
			this.mNumberOfSecondaryAgentsNotReady = mNumberOfSecondaryAgentsNotReady;
		}
		
		public void decrementNumberOfSecondaryAgentsNotReady() {
			this.mNumberOfSecondaryAgentsNotReady--;
		}
	}
}