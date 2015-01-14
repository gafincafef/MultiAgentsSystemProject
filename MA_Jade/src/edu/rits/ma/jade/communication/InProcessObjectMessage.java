package edu.rits.ma.jade.communication;

public class InProcessObjectMessage {
	private String mTargetAgentName;
	private Object mObject;
	
	public String getTargetAgentName() {
		return mTargetAgentName;
	}
	
	public void setTargetAgentName(String targetAgentName) {
		this.mTargetAgentName = targetAgentName;
	}
	
	public Object getObject() {
		return mObject;
	}
	
	public void setObject(Object object) {
		this.mObject = object;
	}
}
