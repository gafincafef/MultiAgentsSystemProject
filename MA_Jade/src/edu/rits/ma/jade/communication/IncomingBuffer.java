package edu.rits.ma.jade.communication;

import jade.lang.acl.ACLMessage;

import java.util.ArrayDeque;
import java.util.Deque;

public class IncomingBuffer {
	private Deque<ACLMessage> mReceivedMessages = new ArrayDeque<ACLMessage>();
	private Deque<InProcessObjectMessage> mReceivedObjectMessages = new ArrayDeque<InProcessObjectMessage>();
	
	public boolean hasReceivedData() {
		return !mReceivedMessages.isEmpty() || !mReceivedObjectMessages.isEmpty();
	}
	
	public int getNumberOfReceivedMessages() {
		return mReceivedMessages.size();
	}
	
	public int getNumberOfReceivedInProcessObjects() {
		return mReceivedObjectMessages.size();
	}
	
	public void addReceivedMessage(ACLMessage receivedMessage) {
		mReceivedMessages.add(receivedMessage);
	}
	
	public ACLMessage extractReceivedMessage() {
		if(mReceivedMessages.isEmpty()) {
			return null;
		}
		return mReceivedMessages.pop();
	}
	
	public void addReceivedObjectMessage(InProcessObjectMessage objectMessage) {
		mReceivedObjectMessages.add(objectMessage);
	}
	
	public InProcessObjectMessage extractObjectReceived() {
		if(mReceivedObjectMessages.isEmpty()) {
			return null;
		}
		return mReceivedObjectMessages.pop();
	}
}