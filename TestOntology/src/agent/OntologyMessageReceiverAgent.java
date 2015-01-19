package agent;

import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Map;

import tasks.ITask;
import tasks.ReadyStatus;

import communication.ReadinessOntology;

public class OntologyMessageReceiverAgent extends Agent{

	private static final long serialVersionUID = 7490980904997460253L;

	private static final String SERVICE_TYPE = "messageListener";

	private Codec mCodec = new SLCodec();
	private Ontology mOntology = ReadinessOntology.getInstance();

	@Override
	public void setup() {
		super.setup();
		getContentManager().registerLanguage(mCodec);
		getContentManager().registerOntology(mOntology);

		System.out.println(this.getName() + " setup");
		registerService();
		addBehaviour(new CyclicBehaviour() {

			private static final long serialVersionUID = 2062374618180100562L;

			@Override
			public void action() {
				ACLMessage message = receive();
				if(message != null) {
					receiveObjectsMessage(message);
					//retrieveMessageWithOntology(message);
				}
				else {
					block();
				}
			}

			private void receiveObjectsMessage(ACLMessage message) {
				//Test retrieve object
				try {
					ITask task = (ITask) message.getContentObject();
					@SuppressWarnings("unchecked")
					Map<Integer, Integer> map = (Map<Integer, Integer>) task.getResults()[0];
					for(int k : map.keySet()) {
						System.out.println("K-V:" + k + " " + map.get(k));
					}
				} 
				catch (UnreadableException e) {
					e.printStackTrace();
				}
			}

			private void retrieveMessageWithOntology(ACLMessage message) {
				ContentManager contentManager = getContentManager();
				try {
					ContentElement contentElement = null;
					contentElement = contentManager.extractContent(message);
					ReadyStatus readyStatus = (ReadyStatus) contentElement;
					System.out.println("Received message from " + readyStatus.getAgentName() + " readiness = " + readyStatus.getReadyStatus());

				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void registerService() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(SERVICE_TYPE);
		sd.setName(getLocalName() + "-" + SERVICE_TYPE); 
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}