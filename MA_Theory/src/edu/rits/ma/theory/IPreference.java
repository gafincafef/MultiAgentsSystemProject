package edu.rits.ma.theory;

import java.util.Set;

public interface IPreference {
	public void addAgentAction(int agentId, Action action);
	public Action getActionOfAgent(int agentId);
	
	public int getIndex();
	public boolean isIndexed();
	
	public IPreference getSubPreference(Set<Integer> agentIds);
	public Set<Integer> getAllAgentIds();
	
	public boolean contains(IPreference subPreference);
	public boolean equals(Object other);
}
