package edu.rits.ma.jade.agent;

import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.GatewayAgent;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.jade.behaviour.TaskProcessorBehaviour;
import edu.rits.ma.jade.concurrency.CondVarManager;
import edu.rits.ma.jade.concurrency.ICondVarAcquirer;
import edu.rits.ma.jade.concurrency.ICondVarReleaser;
import edu.rits.ma.jade.dataobject.CondVar;
import edu.rits.ma.jade.util.LogUtil;

public class PrimaryAgent extends GatewayAgent implements IAgentProtocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4166260446398994126L;

	private CondVar mCondVarToRelease = null;
	private List<AgentController> mSecondaryAgentControllers = new ArrayList<AgentController>();

	@Override
	protected void setup() {
		super.setup();
		AgentOrganizer setupOrganizer = new AgentOrganizer();
		setupOrganizer.setupAgentAndNotify(this, new CondVarManager());
	}

	@Override
	protected void processCommand(java.lang.Object command) {
		addBehaviour(new TaskProcessorBehaviour(command, mSecondaryAgentControllers, this));
	}

	@Override
	protected void takeDown() {
		stopSecondaryAgents();
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
	public void notifySetupFinish(ICondVarReleaser condVarReleaser) {
		if(mCondVarToRelease != null) {
			condVarReleaser.release(mCondVarToRelease);
		}
	}
	
	@Override
	public void onSetupArgumentsParseFailed() {
		doDelete();
	}

	@Override
	public void onSetupArgumentsParsed() throws Exception {
		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			mCondVarToRelease = (CondVar) args[0];
			int numberOfSecondaryAgents = (Integer) args[1];
			String secondaryAgentClassName = (String) args[2];
			createAndStartSecondaryAgents(numberOfSecondaryAgents, secondaryAgentClassName);
		}
	}
	
	private void createAndStartSecondaryAgents(int numberOfSecondaryAgents, String secondaryAgentClassName) throws StaleProxyException {
		//Stop any secondary agents if applicable
		stopSecondaryAgents();
		
		CondVar condVarToAcquire = new CondVar();
		condVarToAcquire.setReleaseThreshold(numberOfSecondaryAgents);
		
		for(int i = 0; i < numberOfSecondaryAgents; i++) {
			String secondaryAgentInstanceName = "SecondaryAgent" + i;
			AgentController ac = getContainerController().createNewAgent(secondaryAgentInstanceName, secondaryAgentClassName, new Object[] {condVarToAcquire});
			ac.start();
			mSecondaryAgentControllers.add(ac);
		}
		
		//Wait for all secondary agents start
		ICondVarAcquirer condVarAcquirer = new CondVarManager();
		condVarAcquirer.acquire(condVarToAcquire);
	}

	private void stopSecondaryAgents() {
		//TODO Implement
	}
}
