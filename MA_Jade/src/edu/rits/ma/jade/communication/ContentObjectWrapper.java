package edu.rits.ma.jade.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentObjectWrapper  {
	private List<String> mReceiverAgentNames = new ArrayList<String>();
	private Serializable mSerializable;

	public ContentObjectWrapper(Serializable serializable) {
		mSerializable = serializable;
	}

	public void getReceiverAgentNames(List<String> receiverAgentNames) {
		receiverAgentNames.clear();
		receiverAgentNames.addAll(mReceiverAgentNames);
	}

	public void addReceiverAgentName(String agentName) {
		mReceiverAgentNames.add(agentName);
	}
	
	public Serializable getSerializable() {
		return mSerializable;
	}
}
