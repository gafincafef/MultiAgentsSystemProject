package edu.rits.ma.jade.behaviour;

import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.InProcessObjectMessage;
import edu.rits.ma.jade.communication.IncomingBuffer;
import edu.rits.ma.jade.communication.OutCommingBuffer;
import edu.rits.ma.jade.util.LogUtil;

public class PrimaryTaskProcessorImpl implements ICommunicationDataStoreProcessor {

	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY = 0;
	private static final int ACTION_PHASE_TO_PROCESS_READY_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK = 3;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE = 4;
	private static final int ACTION_PHASE_TO_STOP = 5;

	private int mInternalState = ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	private List<ITask> mTasks = null;
	private List<String> mSecondaryAgentNames = null;

	private int mNumberOfSecondaryAgentsNotReady = 0;
	private int mNumberOfSecondaryTasksNotDoneTask = 0;

	public PrimaryTaskProcessorImpl(List<ITask> tasks, List<String> secondaryAgentNames) {
		mTasks = tasks;
		mSecondaryAgentNames = secondaryAgentNames;
		mNumberOfSecondaryAgentsNotReady = mSecondaryAgentNames.size();
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
				nextActionPhase = onTaskDoneMessage(receiveBuffer);
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
		mNumberOfSecondaryAgentsNotReady--;
		if(mNumberOfSecondaryAgentsNotReady <= 0) {
			LogUtil.logInfo(this, "All secondary agent ready!");
			runTasks(sendBuffer);
			return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	}

	private int onTaskDoneMessage(IncomingBuffer receiveBuffer) {
		receiveBuffer.extractReceivedMessage();
		mNumberOfSecondaryTasksNotDoneTask--;
		if( mNumberOfSecondaryTasksNotDoneTask <= 0) {
			return ACTION_PHASE_TO_STOP;
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}

	private void runTasks(OutCommingBuffer sendBuffer) {
		int newSecondaryTasks = 0;
		for(ITask task : mTasks) {
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
		}

		if(newSecondaryTasks > 0) {
			mNumberOfSecondaryTasksNotDoneTask += newSecondaryTasks;
		}
	}

	private void assignTaskToSecondaryAgent(OutCommingBuffer sendBuffer, String secondaryAgentName, ITask task) {
		InProcessObjectMessage objectMessage = new InProcessObjectMessage();
		objectMessage.setObject(task);
		objectMessage.setTargetAgentName(secondaryAgentName);
		sendBuffer.addObjectToSend(objectMessage);
	}

	private int detectStateByReceivedMessage(ACLMessage message) {
		//TODO Implement using ontology
		if(message.getContent().equals("Ready")) {
			return ACTION_PHASE_TO_PROCESS_READY_MESSAGE;
		}
		return ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE;
	}
}