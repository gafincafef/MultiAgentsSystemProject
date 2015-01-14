package edu.rits.ma.jade.agent;


public class AgentOrganizer {
	
	public void setupAgent(IAgentProtocol agentCallback) {
		agentCallback.onSetupStart();
		
		try {
			agentCallback.onSetupArgumentsParsed();
		}
		catch(Exception e) {
			e.printStackTrace();
			agentCallback.onSetupArgumentsParseFailed();
		}
		
		agentCallback.onSetupEnd();
	}
}
