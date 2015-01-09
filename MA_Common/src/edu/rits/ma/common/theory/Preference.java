package edu.rits.ma.common.theory;

import java.util.List;


public class Preference {
	private int[] mActionIds = null;
	private int[] mUtilities = null;
	
	public Preference(int numberOfAgent) {
		mActionIds = new int[numberOfAgent];
		mUtilities = new int[numberOfAgent];
		for(int i = 0; i < numberOfAgent; i++) {
			mActionIds[i] = i;
			mUtilities[i] = 0;
		}
	}
	
	public void setActions(List<Integer> actionIds) {
		int i = 0;
		for(int actionId : actionIds) {
			mActionIds[i++] = actionId; 
		}
	}
	
	public void setUtilityForAgent(int agentId, int utility) {
		mUtilities[agentId] = utility;
	}
	
	public int getActionIdOfAgent(int agentId) {
		return mActionIds[agentId];
	}
	
	public int getUtilityOfAgent(int agentId) {
		return mUtilities[agentId];
	}
}
