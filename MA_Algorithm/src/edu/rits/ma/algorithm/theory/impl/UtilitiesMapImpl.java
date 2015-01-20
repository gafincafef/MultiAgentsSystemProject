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
		else {
			System.out.println("Key already existed");
		}
		Map<Integer, Integer> agentUtilityMap = mUtilitiesMap.get(preference);
		agentUtilityMap.put(agentId, utilityValue);
	}

	@Override
	public Integer getUtilityOfAgent(IPreference preference, int agentId) {
		System.out.println("Search for hash code " + preference.hashCode());
		System.out.println("Map key set size " + mUtilitiesMap.keySet().size());
		
		for(IPreference prefKey : mUtilitiesMap.keySet()) {
			System.out.println("Key hashcode " + prefKey.hashCode());
			System.out.println("Matched key = " + (prefKey.equals(preference)));
		}
		
		Map<Integer, Integer> agentUtilityMap = mUtilitiesMap.get(preference);
		if(agentUtilityMap != null) {
			return agentUtilityMap.get(agentId);
		}
		return null;
	}

}
