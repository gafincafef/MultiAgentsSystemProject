package edu.rits.ma.jade.agent;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import edu.rits.ma.jade.behaviour.AgentCommunicatingBehaviour;
import edu.rits.ma.jade.behaviour.ICommunicationBufferProcessor;
import edu.rits.ma.jade.behaviour.SecondaryTaskProcessorImpl;
import edu.rits.ma.jade.util.LogUtil;

public class SecondaryAgent extends Agent implements IAgentProtocol {

	private static final long serialVersionUID = 1509623737210577104L;
	private String mPrimaryAgentName = null;
	
	@Override
	protected void setup() {
		super.setup();
		AgentOrganizer setupOrganizer = new AgentOrganizer();
		setupOrganizer.setupAgent(this);
	}

	@Override
	public void onSetupStart() {
		LogUtil.logInfo(this, "Setup called on secondary agent" + getAID().getLocalName()
				+ " in container " + getAMS().getName());
		setEnabledO2ACommunication(true, 0);
	}

	@Override
	public void onSetupArgumentsParsed() throws Exception {
		Object[] args = getArguments();
		if(args != null) {
			mPrimaryAgentName = (String) args[0];
		}
	}

	@Override
	public void onSetupArgumentsParseFailed() {
		doDelete();
	}

	@Override
	public void onSetupEnd() {
		ICommunicationBufferProcessor processor = new SecondaryTaskProcessorImpl(mPrimaryAgentName);
		List<AgentController> agentControllers = new ArrayList<AgentController>();
		Behaviour behaviour = new AgentCommunicatingBehaviour(this, processor, agentControllers);
		addBehaviour(behaviour);
		ACLMessage readyMessage = createReadyMessage();
		LogUtil.logInfo(this, "Going to send ready message to " + mPrimaryAgentName);
		send(readyMessage);
	}
	
	private ACLMessage createReadyMessage() {
		//TODO Change string content to ontology
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		
		AID receiver = new AID(mPrimaryAgentName, AID.ISGUID);
		message.addReceiver(receiver);
		
		message.setContent("Ready");
		return message;
	}

}
