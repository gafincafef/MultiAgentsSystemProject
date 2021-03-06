package edu.rits.ma.jade.communication;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContentIncomingBuffer {
	private Deque<ContentElementWrapper> mReceivedElements = new ArrayDeque<ContentElementWrapper>();
	private Deque<ContentObjectWrapper> mReceivedObjects = new ArrayDeque<ContentObjectWrapper>();
	
	public boolean hasReceivedData() {
		return hasReceivedContentElement() || hasReceivedContentObject();
	}
	
	public boolean hasReceivedContentElement() {
		return !mReceivedElements.isEmpty();
	}
	
	public void addReceivedContentElement(ContentElementWrapper cew) {
		mReceivedElements.add(cew);
	}
	
	public ContentElementWrapper extractRetrievedContentElement() {
		return mReceivedElements.pop();
	}
	
	public boolean hasReceivedContentObject() {
		return !mReceivedObjects.isEmpty();
	}
	
	public void addReceivedContentObject(ContentObjectWrapper obj) {
		mReceivedObjects.add(obj);
	}
	
	public ContentObjectWrapper extractRetrievedContentObject() {
		return mReceivedObjects.pop();
	}
}
