package edu.rits.ma.jade.behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.List;

import edu.rits.ma.jade.communication.InProcessObjectMessage;
import edu.rits.ma.jade.communication.IncomingBuffer;
import edu.rits.ma.jade.communication.OutCommingBuffer;

public class AgentCommunicatingBehaviour extends Behaviour {

	private static final long serialVersionUID = 2906637694654715113L;

	private IncomingBuffer mReceiveBuffer = new IncomingBuffer();
	private OutCommingBuffer mSendBuffer = new OutCommingBuffer();

	private ICommunicationBufferProcessor mProcessor;
	private List<AgentController> mAgentControllers;

	public AgentCommunicatingBehaviour(Agent agent, ICommunicationBufferProcessor processor, List<AgentController> agentControllers) {
		setAgent(agent);
		mProcessor = processor;
		mAgentControllers = agentControllers;
	}

	@Override
	public void action() {
		updateCommunicationDataStore();
		if(messageReceived()) {
			processCommunicationDataStore();
			if(hasMessageToSend()) {
				try {
					sendMessagesFromAgent();
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			block();
		}
	}

	@Override
	public boolean done() {
		return mProcessor.done();
	}

	private void updateCommunicationDataStore() {
		//Receive ACL message
		ACLMessage message = getAgent().receive();

		//Receive in-process object via controller
		Object object = getAgent().getO2AObject();

		if(message != null) {
			mReceiveBuffer.addReceivedMessage(message);
		}
		if(object != null) {
			mReceiveBuffer.addReceivedObjectMessage((InProcessObjectMessage) object);
		}
	}

	private boolean messageReceived() {
		return mReceiveBuffer.hasReceivedData();
	}

	private void processCommunicationDataStore() {
		mProcessor.processCommunicationDataStore(mReceiveBuffer, mSendBuffer);
	}

	private boolean hasMessageToSend() {
		return mSendBuffer.hasDataToSend();
	}

	private void sendMessagesFromAgent() throws StaleProxyException {
		//Send ACL message
		if(mSendBuffer.getNumberOfMessageToSend() > 0) {
			ACLMessage messageToSend = mSendBuffer.extractMessageToSend();
			getAgent().send(messageToSend);
		}
		
		//Send in-process object to agent controllers
		if(mSendBuffer.getNumberOfInProcessObjectToSend() > 0) {
			InProcessObjectMessage objectMessage = mSendBuffer.extractObjectToSend();
			for(AgentController ac : mAgentControllers) {
				if(ac.getName().equals(objectMessage.getTargetAgentName())) {
					ac.putO2AObject(objectMessage, AgentController.ASYNC);
				}
			}
		}
	}
}
