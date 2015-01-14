package edu.rits.ma.jade.agent;

import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.behaviour.GatewayAgentCommunicatingBehaviour;
import edu.rits.ma.jade.behaviour.ICommunicationDataStoreProcessor;
import edu.rits.ma.jade.behaviour.PrimaryTaskProcessorImpl;

public class TaskProcessorPrimaryAgentImpl extends AbstractPrimaryAgent {

	private static final long serialVersionUID = 8019767118623600485L;

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
		
		ICommunicationDataStoreProcessor processor = new PrimaryTaskProcessorImpl(tasks, secondaryAgentNames);
		
		Behaviour behaviour = new GatewayAgentCommunicatingBehaviour(this, processor, mSecondaryAgentControllers, this, command);
		
		return behaviour;
	}

}
