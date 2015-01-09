package edu.rits.ma.jade.impl;

import jade.core.Profile;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import java.util.List;

import edu.rits.ma.common.IAgentGateway;
import edu.rits.ma.common.ITask;
import edu.rits.ma.jade.concurrency.CondVarManager;
import edu.rits.ma.jade.concurrency.ICondVarAcquirer;
import edu.rits.ma.jade.dataobject.CondVar;

public class JadeAgentGatewayImpl implements IAgentGateway {

	private String mHost;
	private String mPort;
	private String mPrimaryAgentClassName;
	private String mSecondaryAgentClassName;

	public JadeAgentGatewayImpl(String host, String port, String primaryAgentClassName, String secondaryAgentClassName) {
		mHost = host;
		mPort = port;
		mPrimaryAgentClassName = primaryAgentClassName;
		mSecondaryAgentClassName = secondaryAgentClassName;
	}

	@Override
	public void prepareAgents(int numberOfAgents) {
		Properties pp = new Properties();
		pp.setProperty(Profile.MAIN_HOST, mHost);
		pp.setProperty(Profile.MAIN_PORT, mPort);
		initAndWaitForGatewayAgent(numberOfAgents - 1, pp);
		System.out.println("Agent started");
	}

	@Override
	public void runTasksOnAgents(List<ITask> tasks) {
		try {
			if(tasks != null) {
				JadeGateway.execute(tasks); 
			}
		}
		catch (ControllerException | InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			JadeGateway.shutdown();
		}
	}
	
	private void initAndWaitForGatewayAgent(int numberOfSecondaryAgents, Properties pp) {
		CondVar condVar = new CondVar();
		condVar.setReleaseThreshold(1);
		JadeGateway.init(mPrimaryAgentClassName, new Object[] {condVar, numberOfSecondaryAgents, mSecondaryAgentClassName}, pp);
		ICondVarAcquirer condVarAcquirer = new CondVarManager();
		condVarAcquirer.acquire(condVar);
	}
}
