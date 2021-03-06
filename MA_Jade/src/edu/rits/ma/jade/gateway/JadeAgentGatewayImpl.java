package edu.rits.ma.jade.gateway;

import jade.core.Profile;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import java.util.List;

import edu.rits.ma.common.abstr.IAgentGateway;
import edu.rits.ma.common.abstr.ITask;

public class JadeAgentGatewayImpl implements IAgentGateway {

	private String mHost;
	private String mPort;
	private String mPrimaryAgentClassName;

	public JadeAgentGatewayImpl(String host, String port, String primaryAgentClassName) {
		mHost = host;
		mPort = port;
		mPrimaryAgentClassName = primaryAgentClassName;
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
		JadeGateway.init(mPrimaryAgentClassName, new Object[] {numberOfSecondaryAgents}, pp);
	}
}
