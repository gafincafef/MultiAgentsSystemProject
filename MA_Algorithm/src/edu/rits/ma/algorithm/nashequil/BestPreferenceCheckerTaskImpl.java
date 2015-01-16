package edu.rits.ma.algorithm.nashequil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;

public class BestPreferenceCheckerTaskImpl implements ITask {

	private int mAgentId;
	
	private IPreferenceSet mPreferenceSet = null;
	private IUtilitiesMap mUtilitiesMap = null;
	private List<IPreference> mCandidatePreferences = null;
	
	public BestPreferenceCheckerTaskImpl(int agentId, List<IPreference> candidatePreferences, IPreferenceSet preferenceSet, IUtilitiesMap utilitiesMap) {
		mAgentId = agentId;
		mPreferenceSet = preferenceSet;
		mUtilitiesMap = utilitiesMap;
		mCandidatePreferences = candidatePreferences;
	}

	@Override
	public void execute() {
		for(IPreference preference : mCandidatePreferences) {
			Set<Integer> complementAgentIdSet = mPreferenceSet.getAllAgentIds();
			complementAgentIdSet.remove(mAgentId);
			
			IPreference subPreference = preference.getSubPreference(complementAgentIdSet);
			int candidateUtilValueForAgent = mUtilitiesMap.getUtilityOfAgent(preference, mAgentId);
			
			if(subPreference != null) {
				List<IPreference> preferences = new ArrayList<IPreference>();
				mPreferenceSet.getContainerPreferences(subPreference, preferences);
				
				for(IPreference oPreference : preferences) {
					if(mUtilitiesMap.getUtilityOfAgent(oPreference, mAgentId) > candidateUtilValueForAgent) {
						//TODO Do some things to eliminate the candidate
					}
				}
			}
		}
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public Object[] getResults() {
		return null;
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
	}

}