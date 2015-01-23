/*
 * Provide interface for gateway to agents
 * Implementations should be found in platform-dependent module, such as JADE module
 */
package edu.rits.ma.common.abstr;

import java.util.List;

public interface IAgentGateway {
	void prepareAgents(int numberOfAgents);
	void runTasksOnAgents(List<ITask> tasks);
}
