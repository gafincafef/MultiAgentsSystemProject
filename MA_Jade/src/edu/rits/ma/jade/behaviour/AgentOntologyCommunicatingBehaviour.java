package edu.rits.ma.jade.behaviour;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.ContentIncomingBuffer;
import edu.rits.ma.jade.communication.ContentObjectWrapper;
import edu.rits.ma.jade.communication.ContentOutcomingBuffer;
import edu.rits.ma.jade.taskprocessor.IContentBufferProcessor;
import edu.rits.ma.jade.util.LogUtil;

public class AgentOntologyCommunicatingBehaviour extends Behaviour {

	private static final long serialVersionUID = 3662010610519584453L;

	private ContentIncomingBuffer mReceiveBuffer = new ContentIncomingBuffer();
	private ContentOutcomingBuffer mSendBuffer = new ContentOutcomingBuffer();

	private IContentBufferProcessor mProcessor = null;

	public AgentOntologyCommunicatingBehaviour(Agent agent, IContentBufferProcessor processor) {
		setAgent(agent);
		mProcessor = processor;
	}

	@Override
	public void action() {
		updateCommunicationDataStore();
		if(dataReceived()) {
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

	public void sendMessagesFromAgent() throws StaleProxyException {
		if(mSendBuffer.hasDataToSend()) {
			//Send ACL message parsed from content object
			
			while(mSendBuffer.hasContentElementToSend()) {
				ContentElementWrapper contentElement = mSendBuffer.extractContentElementToSend();
				ACLMessage message = createMessageFromContentElement(contentElement);
				getAgent().send(message);
			}
			
			//Send ACL message parsed from content object
			
			while(mSendBuffer.hasContentObjectToSend()) {
				LogUtil.logInfo(this, "Going to send content object");
				ContentObjectWrapper contentObject = mSendBuffer.extractContentObjectToSend();
				ACLMessage message = createMessageFromContentObject(contentObject);
				getAgent().send(message);
			}
		}
	}

	public ContentElementWrapper parseContentElementFromMessage(ACLMessage message) {
		ContentManager contentManager = getAgent().getContentManager();
		try {
			//Extract ontology content from message instance
			ContentElement contentElement = contentManager.extractContent(message);

			return new ContentElementWrapper(contentElement, message.getOntology());
		} 
		catch (CodecException | OntologyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ACLMessage createMessageFromContentElement(ContentElementWrapper contentElement) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		//Set language and ontology
		message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		message.setOntology(contentElement.getOntologyName());

		//Add receivers
		List<String> receiverAgentNames = new ArrayList<String>();
		contentElement.getReceiverAgentNames(receiverAgentNames);
		for(String receiverAgentName : receiverAgentNames) {
			AID receiverAID = new AID(receiverAgentName, AID.ISGUID);
			message.addReceiver(receiverAID);
		}

		//Set content to message
		
		try {
			getAgent().getContentManager().fillContent(message, contentElement.getContentElement());
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		return message;
	}
	
	public ACLMessage createMessageFromContentObject(ContentObjectWrapper cow) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);

		//Set null ontology
		message.setOntology(null);

		//Add receivers
		List<String> receiverAgentNames = new ArrayList<String>();
		cow.getReceiverAgentNames(receiverAgentNames);
		for(String receiverAgentName : receiverAgentNames) {
			AID receiverAID = new AID(receiverAgentName, AID.ISGUID);
			message.addReceiver(receiverAID);
		}

		//Set content to message
		try {
			message.setContentObject(cow.getSerializable());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}

	private void updateCommunicationDataStore() {
		//Receive ACL message
		ACLMessage message = getAgent().receive();
		if(message != null) {
			LogUtil.logInfo(this, "Message received from " + message.getSender().getName());
			if(isOntologyMessage(message)) {
				//If message has ontology, parse message content element
				mReceiveBuffer.addReceivedContentElement(parseContentElementFromMessage(message));
			}
			else {
				//Otherwise retrieve serializable object from the message (e.g. the case of task object)
				try {
					mReceiveBuffer.addReceivedContentObject(new ContentObjectWrapper(message.getContentObject()));
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean dataReceived() {
		return mReceiveBuffer.hasReceivedData();
	}

	private void processCommunicationDataStore() {
		mProcessor.processCommunicationDataStore(mReceiveBuffer, mSendBuffer);
	}

	private boolean hasMessageToSend() {
		return mSendBuffer.hasDataToSend();
	}

	private boolean isOntologyMessage(ACLMessage message) {
		return message.getOntology() != null;
	}
}
