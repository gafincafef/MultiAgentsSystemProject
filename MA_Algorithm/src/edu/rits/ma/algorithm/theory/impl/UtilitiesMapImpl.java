package edu.rits.ma.algorithm.theory.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IUtilitiesMap;

public class UtilitiesMapImpl implements IUtilitiesMap, Serializable {

	private static final long serialVersionUID = 2171760270300206118L;
	
	private Map<IPreference, Map<Integer, Integer> > mUtilitiesMap = new HashMap<IPreference, Map<Integer,Integer>>();
	
	@Override
	public void addPreferenceUtilityForAgent(IPreference preference, int agentId, int utilityValue) {
		if(!mUtilitiesMap.containsKey(preference)) {
			mUtilitiesMap.put(preference, new HashMap<Integer,Integer>());
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
		System.out.println("-----------------------------Going to return null");
		System.out.println("-----------------------------Cause preference info");
		for(int aid : preference.getAllAgentIds()) {
			System.out.println("Agent " + aid + " action " + preference.getActionOfAgent(aid).getId());
		}
		return null;
	}

}
