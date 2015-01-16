package edu.rits.ma.test.main;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.IAgentGateway;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.agent.TaskProcessorPrimaryAgentImpl;
import edu.rits.ma.jade.impl.JadeAgentGatewayImpl;
import edu.rits.ma.jade.util.LogUtil;
import edu.rits.ma.test.impl.TaskSampleImpl;

public class TestJade {
	public static void main(String[] args) {
		String host = "localhost";
		String port = "1099";
		String primaryAgentClassName = TaskProcessorPrimaryAgentImpl.class.getName();
		
		IAgentGateway agentGateway = new JadeAgentGatewayImpl(host, port, primaryAgentClassName);
		int nbAgents = 6;
		agentGateway.prepareAgents(nbAgents);
		
		List<ITask> tasks = new ArrayList<ITask>();
		tasks.add(new TaskSampleImpl(nbAgents - 1));
		tasks.add(new TaskSampleImpl(nbAgents - 1));
		
		agentGateway.runTasksOnAgents(tasks);
		
		Object[] results = tasks.get(0).getResults();
		if(results != null) {
			for(int i = 0; i < results.length; i++) {
				int rv = (int) results[i];
				LogUtil.logInfo(TestJade.class, "Result " + i + " " + rv);
			}
		}
	}
}
