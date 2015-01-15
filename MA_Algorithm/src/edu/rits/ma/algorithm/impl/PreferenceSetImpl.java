package edu.rits.ma.algorithm.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.algorithm.theory.IPreference;
import edu.rits.ma.algorithm.theory.IPreferenceSet;

public class PreferenceSetImpl implements IPreferenceSet {
	
	private List<IPreference> mPreferences = new LinkedList<IPreference>();
	
	@Override
	public void addPreference(IPreference preference) {
		mPreferences.add(preference);
	}

	@Override
	public void removePreference(IPreference preference) {
		//TODO Revise this method
		mPreferences.remove(preference);
	}

	@Override
	public void clear() {
		mPreferences.clear();
	}

	@Override
	public void getContainerPreferences(IPreference subPreference, List<IPreference> containerPreferences) {
		containerPreferences.clear();
		Set<Integer> subSetOfAgentsIds = subPreference.getAllAgentsIds();
		for(IPreference preference : mPreferences) {
			if(preference.contains(subSetOfAgentsIds)) {
				containerPreferences.add(preference);
			}
		}
	}

}
