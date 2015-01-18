package edu.rits.ma.jade.behaviour;

import jade.core.Agent;
import jade.wrapper.gateway.GatewayAgent;
import edu.rits.ma.jade.taskprocessor.IContentBufferProcessor;

public class GatewayAgentCommunicatingBehaviour extends AgentOntologyCommunicatingBehaviour {

	private static final long serialVersionUID = -6373415624378748676L;

	private GatewayAgent mCbGatewayAgent = null;
	private Object mCommand;
	
	public GatewayAgentCommunicatingBehaviour(Agent agent,
			IContentBufferProcessor processor,
			GatewayAgent cbGatewayAgent,
			Object command) {
		super(agent, processor);
		mCbGatewayAgent = cbGatewayAgent;
		mCommand = command;
	}

	@Override
	public int onEnd() {
		//Command released when behavior end
		mCbGatewayAgent.releaseCommand(mCommand);
		return super.onEnd();
	}
}
