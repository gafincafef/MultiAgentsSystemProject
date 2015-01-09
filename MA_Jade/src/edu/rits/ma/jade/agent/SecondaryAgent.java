package edu.rits.ma.jade.agent;

import jade.core.Agent;
import edu.rits.ma.jade.behaviour.SingleTaskProcessorBehaviour;
import edu.rits.ma.jade.concurrency.CondVarManager;
import edu.rits.ma.jade.concurrency.ICondVarReleaser;
import edu.rits.ma.jade.dataobject.CondVar;
import edu.rits.ma.jade.util.LogUtil;

public class SecondaryAgent extends Agent implements IAgentProtocol {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1509623737210577104L;

	private CondVar mCondVarToRelease = null;
	
	@Override
	protected void setup() {
		super.setup();
		AgentOrganizer setupOrganizer = new AgentOrganizer();
		setupOrganizer.setupAgentAndNotify(this, new CondVarManager());
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
		if(args != null && args.length > 0) {
			mCondVarToRelease = (CondVar) args[0];
		}
	}

	@Override
	public void onSetupArgumentsParseFailed() {
		doDelete();
	}

	@Override
	public void onSetupEnd() {
		addBehaviour(new SingleTaskProcessorBehaviour(this));
	}
	
	@Override
	public void notifySetupFinish(ICondVarReleaser condVarReleaser) {
		if(mCondVarToRelease != null) {
			condVarReleaser.release(mCondVarToRelease);
		}
	}
}
