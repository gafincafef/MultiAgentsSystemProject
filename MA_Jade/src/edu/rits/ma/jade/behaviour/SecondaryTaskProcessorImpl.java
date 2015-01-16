package edu.rits.ma.jade.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.communication.InProcessObjectMessage;
import edu.rits.ma.jade.communication.IncomingBuffer;
import edu.rits.ma.jade.communication.OutCommingBuffer;

public class SecondaryTaskProcessorImpl implements ICommunicationBufferProcessor {

	private static final int ACTION_PHASE_TO_WAIT = 0;
	private static final int ACTION_PHASE_TO_PROCESS_TASK_MESSAGE = 1;
	private static final int ACTION_PHASE_TO_STOP = 2;

	private int mInternalState = ACTION_PHASE_TO_WAIT;
	
	private String mPrimaryAgentName;
	
	public SecondaryTaskProcessorImpl(String primaryAgentName) {
		mPrimaryAgentName = primaryAgentName;
	}
	
	@Override
	public void processCommunicationDataStore(IncomingBuffer receiveBuffer, OutCommingBuffer sendBuffer) {
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

	private int onTaskAssigned(IncomingBuffer receiveBuffer, OutCommingBuffer sendBuffer) {
		InProcessObjectMessage objectMessage = receiveBuffer.extractObjectReceived();
		ITask task = (ITask) objectMessage.getObject();
		task.execute();
		ACLMessage taskDoneMessage = createTaskDoneMessage();
		sendBuffer.addMessageToSend(taskDoneMessage);
		return ACTION_PHASE_TO_WAIT;
	}

	@Override
	public boolean done() {
		return mInternalState == ACTION_PHASE_TO_STOP;
	}

	private void updateInternalState(IncomingBuffer receivedBuffer) {
		//TODO Implement using ontology
		if(receivedBuffer.hasReceivedData()) {
			if(receivedBuffer.getNumberOfReceivedInProcessObjects() > 0) {
				mInternalState = ACTION_PHASE_TO_PROCESS_TASK_MESSAGE;
			}
			else {
				if(receivedBuffer.getNumberOfReceivedMessages() > 0) {
					//TODO Check stop message
					mInternalState = ACTION_PHASE_TO_STOP;
				}
			}
		}
	}

	private ACLMessage createTaskDoneMessage() {
		//TODO Implement using ontology
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		AID receiver = new AID(mPrimaryAgentName, AID.ISGUID);
		message.addReceiver(receiver);

		message.setContent("TaskDone");
		return message;
	}	
}
