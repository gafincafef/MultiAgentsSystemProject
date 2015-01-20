/**
 * 
 * Class associated to the predicate schema of an agent state
 *
 */
package edu.rits.ma.jade.communication;

import jade.content.Predicate;

public class AgentState implements Predicate {

	private static final long serialVersionUID = 8175666842573311809L;

	public static final int STATE_NOT_READY = -1;
	public static final int STATE_READY = 0;
	public static final int STATE_TO_DO_SUB_TASK = 1;
	public static final int STATE_SUB_TASK_DONE = 2;
	public static final int STATE_TO_STOP = 3;
	
	private String mAgentName;
	private int mReadyStatus;
	
	public void setAgentName(String agentName) {
		mAgentName = agentName;
	}
	
	public String getAgentName() {
		return mAgentName;
	}

	public int getState() {
		return mReadyStatus;
	}

	public void setState(int state) {
		this.mReadyStatus = state;
	}
}
