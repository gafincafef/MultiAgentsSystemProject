package edu.rits.ma.algorithm.theory.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;

public class PreferenceSetImpl implements IPreferenceSet, Serializable {
	
	private static final long serialVersionUID = 7368098042100968103L;
	
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
				subPreferenceSet.add(preference.getSubPreference(agentIds));
		}
		subPreferences.clear();
		subPreferences.addAll(subPreferenceSet);
	}
	
	@Override
	public void getContainerPreferences(IPreference subPreference, List<IPreference> containerPreferences) {
		containerPreferences.clear();
		for(IPreference preference : mPreferences) {
			if(preference.contains(subPreference)) {
				containerPreferences.add(preference);
			}
		}
	}

}