package edu.rits.ma.jade.taskprocessor;

import jade.content.ContentElement;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.AgentTrackingOntology;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;
import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.SecondaryAgentState;
import edu.rits.ma.jade.communication.SecondaryAgentStopAction;
import edu.rits.ma.jade.util.LogUtil;

public class SecondaryContentBufferProcessorImpl implements IContentBufferProcessor {
	
	private static final int ACTION_PHASE_TO_WAIT = 0;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_STOP = 2;

	private int mInternalState = ACTION_PHASE_TO_WAIT;
	
	private String mPrimaryAgentName;
	private String mAgentName;
	
	public SecondaryContentBufferProcessorImpl(String primaryAgentName, String agentName) {
		mPrimaryAgentName = primaryAgentName;
		mAgentName = agentName;
	}
	
	@Override
	public void processCommunicationDataStore(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		updateInternalState(receiveBuffer);
		int newInternalState = mInternalState;
		switch (mInternalState) {
		case ACTION_PHASE_TO_PROCESS_TASK_MESSAGE:
			newInternalState = onTaskAssigned(receiveBuffer, sendBuffer);
			break;
		default:
			break;
		}
		mInternalState = newInternalState;
	}
	
	@Override
	public boolean done() {
		return mInternalState == ACTION_PHASE_TO_STOP;
	}
	
	private int onTaskAssigned(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		ContentObjectWrapper cow = receiveBuffer.extractRetrievedContentObject();
		ITask task = (ITask) cow.getSerializable();
		task.execute();
												
		notifyTaskDone(sendBuffer);
		
		return ACTION_PHASE_TO_WAIT;
	}

	private void updateInternalState(ContentIncomingBuffer receivedBuffer) {
		if(receivedBuffer.hasReceivedContentElement()) {
			ContentElementWrapper cew = receivedBuffer.extractRetrievedContentElement();
			if(cew.getContentElement() instanceof SecondaryAgentStopAction) {
				mInternalState = ACTION_PHASE_TO_STOP;
			}
		}
		if(receivedBuffer.hasReceivedContentObject()) {
			mInternalState = ACTION_PHASE_TO_PROCESS_TASK_MESSAGE;
		}
	}
	
	private void notifyTaskDone(ContentOutcomingBuffer sendBuffer) {
		LogUtil.logInfo(this, "Task done. Going to notify primary agent");
		ContentElement taskDoneContentElement = createTaskDoneContentElement();
		ContentElementWrapper contentElementWrapper = new ContentElementWrapper(taskDoneContentElement, AgentTrackingOntology.ONTOLOGY_NAME);
		contentElementWrapper.addReceiverAgentName(mPrimaryAgentName);
		sendBuffer.addContentElementToSend(contentElementWrapper);
	}
	
	private SecondaryAgentState createTaskDoneContentElement() {
		SecondaryAgentState agentState = new SecondaryAgentState();
		agentState.setAgentName(mAgentName);
		agentState.setState(SecondaryAgentState.STATE_SUB_TASK_DONE);
		return agentState;
	}
}
