package edu.rits.ma.test.main;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.IAgentGateway;
import edu.rits.ma.common.ITask;
import edu.rits.ma.jade.agent.PrimaryAgent;
import edu.rits.ma.jade.agent.SecondaryAgent;
import edu.rits.ma.jade.impl.JadeAgentGatewayImpl;
import edu.rits.ma.test.impl.TaskSampleImpl;

public class Main {
	public static void main(String[] args) {
		String host = "localhost";
		String port = "1099";
		String primaryAgentClassName = PrimaryAgent.class.getName();
		String secondaryAgentClassName = SecondaryAgent.class.getName();
		
		IAgentGateway agentGateway = new JadeAgentGatewayImpl(host, port, primaryAgentClassName, secondaryAgentClassName);
		int nbAgents = 4;
		agentGateway.prepareAgents(nbAgents);
		
		List<ITask> tasks = new ArrayList<ITask>();
		tasks.add(new TaskSampleImpl(nbAgents - 1));
		
		agentGateway.runTasksOnAgents(tasks);
		
		Object[] results = tasks.get(0).getResults();
		if(results != null) {
			for(int i = 0; i < results.length; i++) {
				int rv = (int) results[i];
				System.out.println("Result " + i + " " + rv);
			}
		}
	}
}