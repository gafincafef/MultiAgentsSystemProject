package edu.rits.ma.algorithm.impl;

import java.util.HashMap;
import java.util.Map;

import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IUtilitiesMap;

public class UtilitiesMapImpl implements IUtilitiesMap {

	private Map<IPreference, Map<Integer, Integer> > mUtilitiesMap = new HashMap<IPreference, Map<Integer,Integer>>();
	
	@Override
	public void addPreferenceUtilityForAgent(IPreference preference, int agentId, int utilityValue) {
		if(!mUtilitiesMap.containsKey(preference)) {
			mUtilitiesMap.put(preference, new HashMap<Integer, Integer>());
		}
		Map<Integer, Integer> agentUtilityMap = mUtilitiesMap.get(preference);
		agentUtilityMap.put(agentId, utilityValue);
	}

	@Override
	public Integer getUtilityOfAgent(IPreference preference, int agentId) {
		Map<Integer, Integer> agentUtilityMap = mUtilitiesMap.get(preference);
		if(agentUtilityMap != null) {
			return agentUtilityMap.get(agentId);
		}
		return null;
	}

}