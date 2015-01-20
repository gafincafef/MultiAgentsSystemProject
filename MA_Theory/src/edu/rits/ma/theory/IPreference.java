package edu.rits.ma.theory;

import java.util.Set;

public interface IPreference {
	public void addAgentAction(int agentId, Action action);
	public Action getActionOfAgent(int agentId);
	
	public boolean isIndexed();
	public int getIndex();
	
	public IPreference getSubPreference(Set<Integer> agentIds);
	public Set<Integer> getAllAgentIds();
	
	public boolean contains(IPreference subPreference);
	
	//Implement these two methods to serve the purpose of searching preference
	public boolean equals(Object other);
	public int hashCode();
}
