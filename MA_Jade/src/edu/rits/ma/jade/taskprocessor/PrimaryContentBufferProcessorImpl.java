package edu.rits.ma.jade.taskprocessor;

import jade.content.ContentElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;
import edu.rits.ma.jade.communication.SecondaryAgentState;
import edu.rits.ma.jade.util.LogUtil;

public class PrimaryContentBufferProcessorImpl implements IContentBufferProcessor {

	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY = 0;
	private static final int ACTION_PHASE_TO_PROCESS_READY_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK = 3;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE = 4;
	private static final int ACTION_PHASE_TO_STOP = 5;

	private int mInternalState = ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	private List<ITask> mTasks = null;
	private List<String> mSecondaryAgentNames = null;

	private TaskExecutingStatus mExecutingStatus = null;

	public PrimaryContentBufferProcessorImpl(List<ITask> tasks, List<String> secondaryAgentNames) {
		mTasks = tasks;
		mSecondaryAgentNames = secondaryAgentNames;
		mExecutingStatus = new TaskExecutingStatus(mTasks.size());
		mExecutingStatus.setNumberOfSecondaryAgentsNotReady(secondaryAgentNames.size());
	}
	
	@Override
	public void processCommunicationDataStore(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
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

	private void updateInternalState(ContentIncomingBuffer receiveBuffer) {
		if(receiveBuffer.hasReceivedContentElement()) {
			ContentElementWrapper contentElement = receiveBuffer.extractRetrievedContentElement();
			mInternalState = detectStateByReceivedMessage(contentElement); 
		}
	}

	private int onReadyMessage(ContentOutcomingBuffer sendBuffer) {
		mExecutingStatus.decrementNumberOfSecondaryAgentsNotReady();
		if(mExecutingStatus.allSecondaryAgentReady()) {
			LogUtil.logInfo(this, "All secondary agent ready");
			runNextTask(sendBuffer);
			return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	}

	private int onTaskDoneMessage(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		//receiveBuffer.extractReceivedMessage();
		mExecutingStatus.decrementNumberOfSubTasksToWait();
		
		if(mExecutingStatus.allSubTasksDone()) {
			LogUtil.logInfo(this, "All secondary done task");
			return onAllSubTaskDone(sendBuffer);
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}
	
	private int onAllSubTaskDone(ContentOutcomingBuffer sendBuffer) {
		if(mExecutingStatus.allTasksDone()) {
			return onAllTasksDone(sendBuffer);
		}
		else {
			runNextTask(sendBuffer);
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}

	private int onAllTasksDone(ContentOutcomingBuffer sendBuffer) {
		//stopSecondaryAgents(sendBuffer);
		return ACTION_PHASE_TO_STOP;
	}
	
	private void runNextTask(ContentOutcomingBuffer sendBuffer) {
		ITask nextTask = mTasks.get(mExecutingStatus.getNextTaskId());
		int numberOfSubTasksToWait = runNextTask(nextTask, sendBuffer);
		mExecutingStatus.incrementNextTaskId();
		mExecutingStatus.setNumberOfSubTasksToWait(numberOfSubTasksToWait);
	}
	
	private int runNextTask(ITask task, ContentOutcomingBuffer sendBuffer) {
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
				LogUtil.logInfo(this, "Create to send a task object to " + mSecondaryAgentNames.get(secondaryAgentIndex));
				
				//Add task object to secondary agents by adding task to queue
				assignTaskToSecondaryAgent(sendBuffer, mSecondaryAgentNames.get(secondaryAgentIndex), subTasks.get(i));
			}
		}
		
		return newSecondaryTasks;
	}

	private void assignTaskToSecondaryAgent(ContentOutcomingBuffer sendBuffer, String secondaryAgentName, ITask task) {
		ContentObjectWrapper cow = new ContentObjectWrapper((Serializable) task);
		cow.addReceiverAgentName(secondaryAgentName);
		sendBuffer.addContentObjectToSend(cow);
	}
	
	/*private void stopSecondaryAgents(ContentOutcomingBuffer sendBuffer) {
		ContentElement secondaryAgentStopAction = new SecondaryAgentStopAction();
		ContentElementWrapper contentElementWrapper = new ContentElementWrapper(secondaryAgentStopAction, AgentTrackingOntology.ONTOLOGY_NAME);
		
		for(String secondaryAgentName : mSecondaryAgentNames) {
			contentElementWrapper.addReceiverAgentName(secondaryAgentName);
		}
		
		sendBuffer.addContentElementToSend(contentElementWrapper);
	}*/

	private int detectStateByReceivedMessage(ContentElementWrapper contentElementWrapper) {
		ContentElement contentElement = contentElementWrapper.getContentElement();
		
		SecondaryAgentState secondaryAgentState = (SecondaryAgentState) contentElement;
		int state = secondaryAgentState.getState();
		
		switch (state) {
		case SecondaryAgentState.STATE_READY:
			return ACTION_PHASE_TO_PROCESS_READY_MESSAGE;
		case SecondaryAgentState.STATE_SUB_TASK_DONE:
			return ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE;
		default:
			return mInternalState;
		}
	}
}