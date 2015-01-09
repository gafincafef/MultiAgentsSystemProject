package edu.rits.ma.jade.agent;

import edu.rits.ma.jade.concurrency.ICondVarReleaser;

public class AgentOrganizer {
	
	public void setupAgentAndNotify(IAgentProtocol agentCallback, ICondVarReleaser condVarReleaser) {
		agentCallback.onSetupStart();
		
		try {
			agentCallback.onSetupArgumentsParsed();
		}
		catch(Exception e) {
			e.printStackTrace();
			agentCallback.onSetupArgumentsParseFailed();
		}
		
		agentCallback.onSetupEnd();
		
		if(condVarReleaser != null) {
			agentCallback.notifySetupFinish(condVarReleaser);
		}
	}
}