package edu.rits.ma.jade.agent;


public interface IAgentProtocol {
	void onSetupStart();
	void onSetupArgumentsParsed() throws Exception;
	void onSetupArgumentsParseFailed();
	void onSetupEnd();
}
