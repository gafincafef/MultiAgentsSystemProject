package edu.rits.ma.jade.communication;

import jade.lang.acl.ACLMessage;

import java.util.ArrayDeque;
import java.util.Deque;

public class OutCommingBuffer {
	private Deque<ACLMessage> mMessagesToSend = new ArrayDeque<ACLMessage>();
	private Deque<InProcessObjectMessage> mObjectsToSend = new ArrayDeque<InProcessObjectMessage>();
	
	public int getNumberOfMessageToSend() {
		return mMessagesToSend.size();
	}
	
	public int getNumberOfInProcessObjectToSend() {
		return mObjectsToSend.size();
	}
	
	public boolean hasDataToSend() {
		return !mMessagesToSend.isEmpty() || !mObjectsToSend.isEmpty();
	}
	
	public void addMessageToSend(ACLMessage receivedMessage) {
		mMessagesToSend.add(receivedMessage);
	}
	
	public ACLMessage extractMessageToSend() {
		if(mMessagesToSend.isEmpty()) {
			return null;
		}
		return mMessagesToSend.pop();
	}
	
	public void addObjectToSend(InProcessObjectMessage objectMessage) {
		mObjectsToSend.add(objectMessage);
	}
	
	public InProcessObjectMessage extractObjectToSend() {
		if(mObjectsToSend.isEmpty()) {
			return null;
		}
		return mObjectsToSend.pop();
	}
}