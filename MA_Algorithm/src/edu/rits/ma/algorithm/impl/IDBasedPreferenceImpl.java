package edu.rits.ma.algorithm.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;

public class IDBasedPreferenceImpl implements IPreference {

	private long mId;
	private Map<Integer, Action> mAgentActionMap = new HashMap<Integer, Action>();

	public IDBasedPreferenceImpl(long id) {
		mId = id;
	}
	
	@Override
	public void addAgentAction(int agentId, Action action) {
		mAgentActionMap.put(agentId, action);
	}

	@Override
	public Action getActionOfAgent(int agentId) {
		return mAgentActionMap.get(agentId);
	}

	@Override
	public IPreference getSubPreference(Set<Integer> agentIds) {
		IPreference preference = new PreferenceImpl();
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
	public boolean contains(Set<Integer> agentsIds) {
		Set<Integer> backedAgentIdSet = mAgentActionMap.keySet();
		return backedAgentIdSet.containsAll(agentsIds);
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof IPreference)) {
			return false;
		}
		if(other instanceof IDBasedPreferenceImpl) {
			return mId == ((IDBasedPreferenceImpl)other).mId;
		}
		
		IPreference otherPreference = (IPreference) other;
		Set<Integer> otherPreferenceAgentIdSet = otherPreference.getAllAgentIds();
		if(!otherPreferenceAgentIdSet.equals(getAllAgentIds())) {
			return false;
		}
		for(int agentId : otherPreferenceAgentIdSet) {
			Action action = getActionOfAgent(agentId);
			Action otherPreferenceAction = otherPreference.getActionOfAgent(agentId);
			if(action.getId() != otherPreferenceAction.getId()) {
				return false;
			}
		}
		return true;
	}
}
