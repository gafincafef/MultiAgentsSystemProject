package edu.rits.ma.jade.agent;

import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.GatewayAgent;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.jade.util.LogUtil;

public abstract class AbstractPrimaryAgent extends GatewayAgent implements IAgentProtocol {

	private static final long serialVersionUID = 4166260446398994126L;
	private static final String SECONDARY_AGENT_NAME_PREFIX = "SecondaryAgent";
	
	private int mNumberOfSecondaryAgents = 0;
	
	protected List<AgentController> mSecondaryAgentControllers = new ArrayList<AgentController>();

	@Override
	protected void setup() {
		super.setup();
		AgentOrganizer setupOrganizer = new AgentOrganizer();
		setupOrganizer.setupAgent(this);
	}

	@Override
	protected void processCommand(java.lang.Object command) {
		try {
			createAndStartSecondaryAgents(mNumberOfSecondaryAgents);
		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
			releaseCommand(command);
		}
		
		Behaviour behaviour = null;
		try {
			behaviour = createBehaviourForCommand(command);
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		addBehaviour(behaviour);
	}

	@Override
	protected void takeDown() {
		cleanSecondaryAgents();
		super.takeDown();
	}

	@Override
	public void onSetupStart() {
		LogUtil.logInfo(this, "Setup called on primary agent " + getAID().getLocalName()
				+ " in container " + getAMS().getName());
		setEnabledO2ACommunication(true, 0);
	}
	
	@Override
	public void onSetupEnd() {
	}
	
	@Override
	public void onSetupArgumentsParseFailed() {
		doDelete();
	}

	@Override
	public void onSetupArgumentsParsed() throws Exception {
		Object[] args = getArguments();
		if(args != null) {
			mNumberOfSecondaryAgents = (Integer) args[0];
		}
	}
	
	private void createAndStartSecondaryAgents(int numberOfSecondaryAgents) throws StaleProxyException {
		cleanSecondaryAgents();
		
		for(int i = 0; i < numberOfSecondaryAgents; i++) {
			String secondaryAgentInstanceName = getName() + "_" +SECONDARY_AGENT_NAME_PREFIX + i;
			AgentController ac = getContainerController().createNewAgent(secondaryAgentInstanceName, getSecondaryAgentClassName(i), createSecondarySetupParams(getName()));
			ac.start();
			mSecondaryAgentControllers.add(ac);
		}
	}

	private void cleanSecondaryAgents() {
		mSecondaryAgentControllers.clear();
	}
	
	protected abstract String getSecondaryAgentClassName(int secondaryAgentId);
	protected abstract Object[] createSecondarySetupParams(String string);
	protected abstract Behaviour createBehaviourForCommand(Object command) throws StaleProxyException;
}
