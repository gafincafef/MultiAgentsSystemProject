package edu.rits.ma.algorithm.theory.impl;

import java.io.Serializable;
import java.util.Set;

import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;

public class IndexBasedPreferenceImpl extends PreferenceImpl implements Serializable {

	private static final long serialVersionUID = -5058945633704891446L;
	
	private int mId;
	
	public IndexBasedPreferenceImpl() {
		mId  = -1;
	}
	
	public IndexBasedPreferenceImpl(int id) {
		mId = id;
	}
	
	@Override
	public int getIndex() {
		return mId;
	}

	@Override
	public boolean isIndexed() {
		return mId > 0;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof IPreference)) {
			return false;
		}
		if(other instanceof IndexBasedPreferenceImpl && mId > 0 && ((IndexBasedPreferenceImpl)other).mId >0) {
			return mId == ((IndexBasedPreferenceImpl)other).mId;
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
