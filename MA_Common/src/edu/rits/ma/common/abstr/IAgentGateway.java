package edu.rits.ma.common.abstr;

import java.util.List;

public interface IAgentGateway {
	void prepareAgents(int numberOfAgents);
	void runTasksOnAgents(List<ITask> tasks);
}
