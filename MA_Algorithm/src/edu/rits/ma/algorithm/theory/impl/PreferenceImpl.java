package edu.rits.ma.algorithm.theory.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;

public class PreferenceImpl implements IPreference 	{

	private Map<Integer, Action> mAgentActionMap = new HashMap<Integer, Action>();
	private int mHashCode = 0;
	
	@Override
	public void addAgentAction(int agentId, Action action) {
		mAgentActionMap.put(agentId, action);
		
		//A solution to create hash code
		mHashCode += agentId * action.getId();
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
		System.out.println("Equal check");
		if(!(other instanceof IPreference)) {
			return false;
		}
		IPreference otherPreference = (IPreference) other;
		Set<Integer> otherPreferenceAgentIdSet = otherPreference.getAllAgentIds();
		
		System.out.println("---------------------------Found equalilty---------------------------");
		for(int agentId : otherPreferenceAgentIdSet) {
			System.out.println("1 Agent-action" + agentId + " " + getActionOfAgent(agentId).getId());
		}
		for(int agentId : getAllAgentIds()) {
			System.out.println("2 Agent-action" + agentId + " " + otherPreference.getActionOfAgent(agentId).getId());
		}
		System.out.println("----------------------------------------------------------------------");
		
		if(!otherPreferenceAgentIdSet.equals(getAllAgentIds())) {
			System.out.println("False 1");
			return false;
		}
		for(int agentId : otherPreferenceAgentIdSet) {
			Action action = getActionOfAgent(agentId);
			Action otherPreferenceAction = otherPreference.getActionOfAgent(agentId);
			if(!action.equals(otherPreferenceAction)) {
				System.out.println("False 2 " + otherPreferenceAction.getId() + "!=" + action.getId());
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return mHashCode;
	}
	
	protected IPreference newInstance() {
		return new PreferenceImpl();
	}
}
