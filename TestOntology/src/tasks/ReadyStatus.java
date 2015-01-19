package tasks;

import jade.content.Predicate;

public class ReadyStatus implements Predicate {
	private static final long serialVersionUID = 2771396532034246866L;
	
	public static final int STATUS_NOT_READY = -1;
	public static final int STATUS_READY = 0;
	
	private String mAgentName;
	private int mReadyStatus;
	
	public void setAgentName(String agentName) {
		mAgentName = agentName;
	}
	
	public String getAgentName() {
		return mAgentName;
	}

	public int getReadyStatus() {
		return mReadyStatus;
	}

	public void setReadyStatus(int mReadyStatus) {
		this.mReadyStatus = mReadyStatus;
	}
	
}