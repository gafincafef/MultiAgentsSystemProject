package edu.rits.ma.jade.taskprocessor;

import jade.content.ContentElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.jade.communication.AgentState;
import edu.rits.ma.jade.communication.AgentTrackingOntology;
import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;
import edu.rits.ma.jade.util.LogUtil;

public class PrimaryContentBufferProcessorImpl extends AbstractContentBufferProcessor {

	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY = 0;
	private static final int ACTION_PHASE_TO_PROCESS_READY_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK = 3;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE = 4;
	
	private List<ITask> mTasks = null;
	private List<ITaskResult> mSubTasksResultBuffer;

	private List<String> mSecondaryAgentNames = null;

	private TaskExecutingStatus mExecutingStatus = null;
	private String mPrimaryAgentName;

	public PrimaryContentBufferProcessorImpl(List<ITask> tasks, String primaryAgentName, List<String> secondaryAgentNames) {
		mTasks = tasks;

		mPrimaryAgentName = primaryAgentName;
		mSecondaryAgentNames = secondaryAgentNames;

		mSubTasksResultBuffer = new ArrayList<ITaskResult>() ;

		mExecutingStatus = new TaskExecutingStatus(mTasks.size());
		mExecutingStatus.setNumberOfSecondaryAgentsNotReady(secondaryAgentNames.size());
	}

	@Override
	public int processNewState(int newState, ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		int nextActionPhase = newState;
		switch (newState) {
		case ACTION_PHASE_TO_PROCESS_READY_MESSAGE:
			nextActionPhase = onOneSecondaryAgentReady(sendBuffer);
			break;
		case ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE:
			nextActionPhase = onOneSecondaryAgentDoneTask(receiveBuffer, sendBuffer);
			break;
		default:
			break;
		}
		return nextActionPhase;
	}
	
	protected int onContentObjectReceived(ContentObjectWrapper cow) {
		//Handle the results object from secondary agents
		
		ITaskResult subTaskResult = (ITaskResult) cow.getSerializable();
		mSubTasksResultBuffer.add(subTaskResult);
		return ACTION_PHASE_TO_PROCESS_TASK_DONE_MESSAGE;
	}

	protected int onContentElementReceived(ContentElementWrapper cew) {
		//Handle the ready message (using ontology)
		
		ContentElement contentElement = cew.getContentElement();

		AgentState agentState = (AgentState) contentElement;
		int state = agentState.getState();

		switch (state) {
		case AgentState.STATE_READY:
			return ACTION_PHASE_TO_PROCESS_READY_MESSAGE;
		default:
			return getCurrentState();
		}
	}

	private int onOneSecondaryAgentReady(ContentOutcomingBuffer sendBuffer) {
		mExecutingStatus.decrementNumberOfSecondaryAgentsNotReady();
		if(mExecutingStatus.allSecondaryAgentReady()) {
			LogUtil.logInfo(this, "All secondary agent ready");
			runNextTask(sendBuffer);
			return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_READY;
	}

	private int onOneSecondaryAgentDoneTask(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		mExecutingStatus.decrementNumberOfSubTasksToWait();

		if(mExecutingStatus.allSubTasksDone()) {
			LogUtil.logInfo(this, "All secondary done task");
			return onOneTaskDone(sendBuffer);
		}
		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}

	private int onOneTaskDone(ContentOutcomingBuffer sendBuffer) {
		//Process results from sub task of the current task
		ITask currentTask = mTasks.get(mExecutingStatus.getPreviousTaskIndex());
		processSubTasksResults(currentTask);

		//Check if to run next task or there is no more tasks
		if(mExecutingStatus.allTasksDone()) {
			return onAllTasksDone(sendBuffer);
		}
		else {
			runNextTask(sendBuffer);
		}

		return ACTION_PHASE_TO_WAIT_SECONDARY_AGENTS_DONE_TASK;
	}

	private int onAllTasksDone(ContentOutcomingBuffer sendBuffer) {
		notifyStopStateToSecondaryAgents(sendBuffer);
		return ACTION_PHASE_TO_STOP;
	}

	private void processSubTasksResults(ITask currentTask) {
		LogUtil.logInfo(this, "Going to process " + mSubTasksResultBuffer.size() + " sub tasks results");
		currentTask.processSubTaskResults(mSubTasksResultBuffer);
		mSubTasksResultBuffer.clear();
	}

	private void runNextTask(ContentOutcomingBuffer sendBuffer) {
		ITask nextTask = mTasks.get(mExecutingStatus.getNextTaskIndex());
		int numberOfSubTasksToWait = runNextTask(nextTask, sendBuffer);
		mExecutingStatus.incrementNextTaskIndex();
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

	private void notifyStopStateToSecondaryAgents(ContentOutcomingBuffer sendBuffer) {
		AgentState stopState = createStopState();
		ContentElementWrapper contentElementWrapper = new ContentElementWrapper(stopState, AgentTrackingOntology.ONTOLOGY_NAME);

		for(String secondaryAgentName : mSecondaryAgentNames) {
			contentElementWrapper.addReceiverAgentName(secondaryAgentName);
		}

		sendBuffer.addContentElementToSend(contentElementWrapper);
	}

	private AgentState createStopState() {
		AgentState stopState = new AgentState();
		stopState.setAgentName(mPrimaryAgentName);
		stopState.setState(AgentState.STATE_TO_STOP);
		return stopState;
	}
}
