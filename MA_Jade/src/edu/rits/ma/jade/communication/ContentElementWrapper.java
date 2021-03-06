package edu.rits.ma.jade.communication;

import java.util.ArrayList;
import java.util.List;

import jade.content.ContentElement;

public class ContentElementWrapper {
	private ContentElement mContentElement = null;
	private String mOntologyName = null;
	private List<String> mReceiverAgentNames = new ArrayList<String>();
	
	public ContentElementWrapper(ContentElement contentElement, String ontologyName) {
		mContentElement = contentElement;
		mOntologyName = ontologyName;
	}

	public ContentElement getContentElement() {
		return mContentElement;
	}

	public String getOntologyName() {
		return mOntologyName;
	}
	
	public void addReceiverAgentName(String agentName) {
		mReceiverAgentNames.add(agentName);
	}
	
	public void getReceiverAgentNames(List<String> receiverAgentNames) {
		receiverAgentNames.clear();
		receiverAgentNames.addAll(mReceiverAgentNames);
	}
	
}
