package edu.rits.ma.jade.agent;

import edu.rits.ma.jade.concurrency.ICondVarReleaser;

public interface IAgentProtocol {
	void onSetupStart();
	void onSetupArgumentsParsed() throws Exception;
	void onSetupArgumentsParseFailed();
	void onSetupEnd();
	void notifySetupFinish(ICondVarReleaser condVarReleaser);
}
