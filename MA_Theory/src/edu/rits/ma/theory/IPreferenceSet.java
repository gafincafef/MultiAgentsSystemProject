package edu.rits.ma.theory;

import java.util.List;
import java.util.Set;

public interface IPreferenceSet {
	public void addPreference(IPreference preference);
	public void removePreference(IPreference preference);
	
	public void getAllPreference(List<IPreference> preferences);
	public Set<Integer> getAllAgentIds();
	
	public void clear();
	
	public void getSubPreferencesForAgents(Set<Integer> agentIds, List<IPreference> subPreferences);
	public void getContainerPreferences(IPreference subPreference, List<IPreference> containerPreferences);
}
