package edu.rits.ma.jade.communication;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContentOutcomingBuffer {
	private Deque<ContentElementWrapper> mToSendElements = new ArrayDeque<ContentElementWrapper>();
	private Deque<ContentObjectWrapper> mToSendObjects = new ArrayDeque<ContentObjectWrapper>();
	
	public boolean hasDataToSend() {
		return hasContentElementToSend() || hasContentObjectToSend();
	}
	
	public boolean hasContentElementToSend() {
		return !mToSendElements.isEmpty();
	}

	public void addContentElementToSend(ContentElementWrapper cew) {
		mToSendElements.add(cew);
	}

	public ContentElementWrapper extractContentElementToSend() {
		return mToSendElements.pop();
	}
	
	public boolean hasContentObjectToSend() {
		return !mToSendObjects.isEmpty();
	}
	
	public void addContentObjectToSend(ContentObjectWrapper obj) {
		mToSendObjects.add(obj);
	}
	
	public ContentObjectWrapper extractContentObjectToSend() {
		return mToSendObjects.pop();
	}
}
