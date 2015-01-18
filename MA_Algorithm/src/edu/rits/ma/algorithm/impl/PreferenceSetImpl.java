package edu.rits.ma.algorithm.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;

public class PreferenceSetImpl implements IPreferenceSet {
	
	private List<IPreference> mPreferences = new LinkedList<IPreference>();
	
	@Override
	public void addPreference(IPreference preference) {
		mPreferences.add(preference);
	}

	@Override
	public void removePreference(IPreference preference) {
		mPreferences.remove(preference);
	}
	
	@Override
	public Set<Integer> getAllAgentIds() {
		Set<Integer> agentIdSet = new HashSet<Integer>();
		for(IPreference preference : mPreferences) {
			agentIdSet.addAll(preference.getAllAgentIds());
		}
		return agentIdSet;
	}
	
	@Override
	public void getAllPreference(List<IPreference> preferences) {
		preferences.addAll(mPreferences);
	}

	@Override
	public void clear() {
		mPreferences.clear();
	}

	@Override
	public void getSubPreferencesForAgents(Set<Integer> agentIds, List<IPreference> subPreferences) {
		HashSet<IPreference> subPreferenceSet = new HashSet<IPreference>();
		for(IPreference preference : mPreferences) {
			if(preference.contains(agentIds)) {
				subPreferenceSet.add(preference);
			}
		}
		subPreferences.clear();
		subPreferences.addAll(subPreferenceSet);
	}
	
	@Override
	public void getContainerPreferences(IPreference subPreference, List<IPreference> containerPreferences) {
		containerPreferences.clear();
		Set<Integer> subSetOfAgentsIds = subPreference.getAllAgentIds();
		for(IPreference preference : mPreferences) {
			if(preference.contains(subSetOfAgentsIds)) {
				containerPreferences.add(preference);
			}
		}
	}

}