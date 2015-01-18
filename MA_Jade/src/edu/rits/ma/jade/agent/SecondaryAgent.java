package edu.rits.ma.jade.agent;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import edu.rits.ma.jade.behaviour.AgentOntologyCommunicatingBehaviour;
import edu.rits.ma.jade.communication.AgentTrackingOntology;
import edu.rits.ma.jade.communication.ContentElementWrapper;
import edu.rits.ma.jade.communication.SecondaryAgentState;
import edu.rits.ma.jade.taskprocessor.IContentBufferProcessor;
import edu.rits.ma.jade.taskprocessor.SecondaryContentBufferProcessorImpl;
import edu.rits.ma.jade.util.LogUtil;

public class SecondaryAgent extends Agent implements IAgentProtocol {

	private static final long serialVersionUID = 1509623737210577104L;
	private String mPrimaryAgentName = null;
	
	private Codec mCodec = new SLCodec();
	private Ontology mOntology = AgentTrackingOntology.getInstance();

	@Override
	protected void setup() {
		super.setup();
		getContentManager().registerLanguage(mCodec);
		getContentManager().registerOntology(mOntology);
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
		IContentBufferProcessor processor = new SecondaryContentBufferProcessorImpl(mPrimaryAgentName, getName());
		AgentOntologyCommunicatingBehaviour behaviour = new AgentOntologyCommunicatingBehaviour(this, processor);
		addBehaviour(behaviour);
		
		SecondaryAgentState readyState = createReadyMessage();
		ContentElementWrapper cew = new ContentElementWrapper(readyState, AgentTrackingOntology.ONTOLOGY_NAME);
		cew.addReceiverAgentName(mPrimaryAgentName);
		
		ACLMessage readyMessage = behaviour.createMessageFromContentElement(cew);
		
		LogUtil.logInfo(this, "Going to send ready message to " + mPrimaryAgentName);
		send(readyMessage);
	}
	
	private SecondaryAgentState createReadyMessage() {
		SecondaryAgentState agentState = new SecondaryAgentState();
		agentState.setAgentName(getName());
		agentState.setState(SecondaryAgentState.STATE_READY);
		return agentState;
	}

}
