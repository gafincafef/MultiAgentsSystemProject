package edu.rits.ma.jade.behaviour;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.gateway.GatewayAgent;

import java.util.List;

public class GatewayAgentCommunicatingBehaviour extends AgentCommunicatingBehaviour {

	private static final long serialVersionUID = -6373415624378748676L;

	private GatewayAgent mCbGatewayAgent = null;
	private Object mCommand;
	
	public GatewayAgentCommunicatingBehaviour(Agent agent,
			ICommunicationDataStoreProcessor processor,
			List<AgentController> agentControllers,
			GatewayAgent cbGatewayAgent,
			Object command) {
		super(agent, processor, agentControllers);
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
