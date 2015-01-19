package behaviour;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

import tasks.ITask;
import tasks.ReadyStatus;

import communication.ReadinessOntology;

public class OntologySenderBehaviour extends Behaviour {

	private static final long serialVersionUID = -4671854788864070637L;
	
	private ITask mTask;
	private boolean mMessageSent = false;
	
	public OntologySenderBehaviour(Agent agent, ITask task) {
		setAgent(agent);
		mTask = task;
	}
	
	@Override
	public void action() {
		AID receiverAID = getReceiverAgent();
		if(receiverAID == null) {
			return;
		}
		sendObjectsInMessage(receiverAID);
		//sendMessageWithOntology(receiverAID);
	}
	
	@Override
	public boolean done() {
		return mMessageSent;
	}
	
	private void sendObjectsInMessage(AID receiverAID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(receiverAID);
		message.setOntology(null);
		try {
			message.setContentObject((Serializable) mTask);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Message is going to be sent to " + receiverAID.getName());
		getAgent().send(message);
		mMessageSent = true;
	}
	
	private void sendMessageWithOntology(AID receiverAID) {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(receiverAID);
		message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		message.setOntology(ReadinessOntology.READINESS_ONTOLOGY_NAME);
		
		ReadyStatus status = new ReadyStatus();
		status.setAgentName(getAgent().getName());
		status.setReadyStatus(ReadyStatus.STATUS_READY);
		
		try {
			getAgent().getContentManager().fillContent(message, status);
			getAgent().send(message);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		
		mMessageSent = true;
	}
	
	private AID getReceiverAgent() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("messageListener");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(getAgent(),
					template);
			if(result != null && result.length > 0) {
				return result[0].getName();
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return null;
	}

}
