package edu.rits.ma.algorithm.theory.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;

public class PreferenceImpl implements IPreference 	{

	private Map<Integer, Action> mAgentActionMap = new HashMap<Integer, Action>();
	
	@Override
	public void addAgentAction(int agentId, Action action) {
		mAgentActionMap.put(agentId, action);
	}

	@Override
	public Action getActionOfAgent(int agentId) {
		return mAgentActionMap.get(agentId);
	}

	@Override
	public int getIndex() {
		return -1;
	}

	@Override
	public boolean isIndexed() {
		return false;
	}
	
	@Override
	public IPreference getSubPreference(Set<Integer> agentIds) {
		IPreference preference = newInstance();
		for(int agentId : agentIds) {
			preference.addAgentAction(agentId, mAgentActionMap.get(agentId));
		}
		return preference;
	}

	@Override
	public Set<Integer> getAllAgentIds() {
		Set<Integer> agentIdSet = new HashSet<Integer>();
		Set<Integer> backedAgentIdSet = mAgentActionMap.keySet();
		for(int agentId : backedAgentIdSet) {
			agentIdSet.add(agentId);
		}
		return agentIdSet;
	}

	@Override
	public boolean contains(IPreference subPreference) {
		Set<Integer> agentIdSubSet = subPreference.getAllAgentIds();
		for(int agentId : agentIdSubSet) {
			if(mAgentActionMap.containsKey(agentId)) {
				Action action = mAgentActionMap.get(agentId);
				Action otherAction = subPreference.getActionOfAgent(agentId);
				if(action != otherAction) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof IPreference)) {
			return false;
		}
		IPreference otherPreference = (IPreference) other;
		Set<Integer> otherPreferenceAgentIdSet = otherPreference.getAllAgentIds();
		if(!otherPreferenceAgentIdSet.equals(getAllAgentIds())) {
			return false;
		}
		for(int agentId : otherPreferenceAgentIdSet) {
			Action action = getActionOfAgent(agentId);
			Action otherPreferenceAction = otherPreference.getActionOfAgent(agentId);
			if(action != otherPreferenceAction) {
				return false;
			}
		}
		return true;
	}
	
	protected IPreference newInstance() {
		return new PreferenceImpl();
	}
}
