package edu.rits.ma.common;

import java.util.List;

public interface IAgentGateway {
	void prepareAgents(int numberOfAgents);
	void runTasksOnAgents(List<ITask> tasks);
}
