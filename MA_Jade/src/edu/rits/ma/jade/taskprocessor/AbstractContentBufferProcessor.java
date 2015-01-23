package edu.rits.ma.jade.taskprocessor;

import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;

public abstract class AbstractContentBufferProcessor implements IContentBufferProcessor{
	
	protected static final int ACTION_PHASE_TO_STOP = Integer.MAX_VALUE;
	
	private int mInternalState = -1;
	
	@Override
	public void processCommunicationDataBuffer(ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer) {
		mInternalState = updateInternalState(receiveBuffer);
		try {
			int nextActionPhase = processNewState(mInternalState, receiveBuffer, sendBuffer);
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
	
	protected int getCurrentState() {
		return mInternalState;
	}
	
	protected abstract int processNewState(int newState, ContentIncomingBuffer receiveBuffer, ContentOutcomingBuffer sendBuffer);
	
	private int updateInternalState(ContentIncomingBuffer receivedBuffer) {
		int newState = getCurrentState();
		if(receivedBuffer.hasReceivedContentElement()) {
			ContentElementWrapper cew = receivedBuffer.extractRetrievedContentElement();
			return onContentElementReceived(cew);
		}
		if(receivedBuffer.hasReceivedContentObject()) {
			ContentObjectWrapper cow = receivedBuffer.extractRetrievedContentObject();
			return onContentObjectReceived(cow);
		}
		
		return newState;
	}

	protected abstract int onContentObjectReceived(ContentObjectWrapper cow);

	protected abstract int onContentElementReceived(ContentElementWrapper cew);
}
