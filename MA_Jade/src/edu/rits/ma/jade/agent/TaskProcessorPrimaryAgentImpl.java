package edu.rits.ma.jade.agent;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.behaviour.GatewayAgentCommunicatingBehaviour;
import edu.rits.ma.jade.communication.AgentTrackingOntology;
import edu.rits.ma.jade.taskprocessor.IContentBufferProcessor;
import edu.rits.ma.jade.taskprocessor.PrimaryContentBufferProcessorImpl;

public class TaskProcessorPrimaryAgentImpl extends AbstractPrimaryAgent {

	private static final long serialVersionUID = 8019767118623600485L;
	private Codec mCodec = new SLCodec();
	private Ontology mOntology = AgentTrackingOntology.getInstance();
	
	@Override
	public void setup() {
		super.setup();
		getContentManager().registerLanguage(mCodec);
		getContentManager().registerOntology(mOntology);
	}
	
	@Override
	protected String getSecondaryAgentClassName(int secondaryAgentId) {
		return SecondaryAgent.class.getName();
	}

	@Override
	protected Object[] createSecondarySetupParams(String primaryAgentName) {
		return new Object[]{primaryAgentName};
	}

	@Override
	protected Behaviour createBehaviourForCommand(Object command) throws StaleProxyException {
		@SuppressWarnings("unchecked")
		List<ITask> tasks = (List<ITask>) command;
		List<String> secondaryAgentNames = new ArrayList<String>();
		for(AgentController ac : mSecondaryAgentControllers) {
			secondaryAgentNames.add(ac.getName());
		}
		
		IContentBufferProcessor processor = new PrimaryContentBufferProcessorImpl(tasks, getName(), secondaryAgentNames);
		
		Behaviour behaviour = new GatewayAgentCommunicatingBehaviour(this, processor, this, command);
		
		return behaviour;
	}

}
