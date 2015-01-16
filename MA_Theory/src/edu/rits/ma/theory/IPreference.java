package edu.rits.ma.theory;

import java.util.Set;

public interface IPreference {
	public void addAgentAction(int agentId, Action action);
	public Action getActionOfAgent(int agentId);
	public IPreference getSubPreference(Set<Integer> agentIds);
	public Set<Integer> getAllAgentIds();
	public boolean contains(Set<Integer> agentsIds);
	public boolean equals(Object other);
}
