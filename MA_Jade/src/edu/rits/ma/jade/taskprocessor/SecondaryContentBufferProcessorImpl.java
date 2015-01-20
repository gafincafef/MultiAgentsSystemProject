package edu.rits.ma.jade.taskprocessor;

import java.io.Serializable;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.AgentState;
import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;
import edu.rits.ma.jade.util.LogUtil;

public class SecondaryContentBufferProcessorImpl extends AbstractContentBufferProcessor {
	
	private static final int ACTION_PHASE_TO_WAIT = 0;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_STOP = 2;

	private String mPrimaryAgentName;
	private ITask mTask = null;
	
	public SecondaryContentBufferProcessorImpl(String primaryAgentName, String agentName) {
		mPrimaryAgentName = primaryAgentName;
	}
	
	@Override
	public int processNewState(int newState, ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		int nextState = newState;
		switch (newState) {
		case ACTION_PHASE_TO_PROCESS_TASK_MESSAGE:
			nextState = onTaskAssigned(receiveBuffer, sendBuffer);
			break;
		default:
			break;
		}
		return nextState;
	}
	
	private int onTaskAssigned(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		mTask.execute();
												
		notifyTaskDone(mTask, sendBuffer);
		
		return ACTION_PHASE_TO_WAIT;
	}
	
	@Override
	protected int onContentElementReceived(ContentElementWrapper cew) {
		int newState = getCurrentState();
		if(cew.getContentElement() instanceof AgentState) {
			//Stop if primary agent stops
			AgentState agentState = (AgentState) cew.getContentElement();
			if(mPrimaryAgentName.equals(agentState.getAgentName()) && agentState.getState() == AgentState.STATE_TO_STOP) {
				LogUtil.logInfo(this, "Stop due to primary agent stopping");
				newState = ACTION_PHASE_TO_STOP;
			}
		}
		return newState;
	}
	
	@Override
	protected int onContentObjectReceived(ContentObjectWrapper cow) {
		//Immediately get task from the content element wrapper
		mTask = (ITask) cow.getSerializable();
		return ACTION_PHASE_TO_PROCESS_TASK_MESSAGE;
	}
	
	private void notifyTaskDone(ITask task, ContentOutcomingBuffer sendBuffer) {
		LogUtil.logInfo(this, "Task done. Going to notify primary agent");
		ContentObjectWrapper cow = new ContentObjectWrapper((Serializable) task.getResult());
		cow.addReceiverAgentName(mPrimaryAgentName);
		sendBuffer.addContentObjectToSend(cow);
	}
}
