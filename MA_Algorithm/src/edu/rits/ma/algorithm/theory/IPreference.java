package edu.rits.ma.algorithm.theory;

import java.util.Set;

public interface IPreference {
	public void addAgentAction(int agentId, Action action);
	public Action getActionOfAgent(int agentId);
	public IPreference removeAgent(int agentId);
	public Set<Integer> getAllAgentsIds();
	public boolean contains(Set<Integer> agentsIds);
}
