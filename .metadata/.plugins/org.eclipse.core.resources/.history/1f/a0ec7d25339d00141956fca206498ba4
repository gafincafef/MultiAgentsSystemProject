package edu.rits.ma.algorithm.nashequil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.algorithm.theory.IPreference;
import edu.rits.ma.algorithm.theory.IPreferenceSet;
import edu.rits.ma.algorithm.theory.IUtilitiesMap;
import edu.rits.ma.common.abstr.ITask;

public class FindBestPreferencesTaskImpl implements ITask {

	private int mPrimaryAgentId;
	private IPreference mSubPreference = null;
	private IPreferenceSet mPreferenceSet = null;
	private IUtilitiesMap mUtilitiesMap = null;
	
	private List<IPreference> mCandidatePreferences = new ArrayList<IPreference>();
	
	public FindBestPreferencesTaskImpl(int primaryAgentId, IPreference subPreference, IPreferenceSet preferenceSet, IUtilitiesMap utilitiesMap) {
		mPrimaryAgentId = primaryAgentId;
		mSubPreference = subPreference;
		mPreferenceSet = preferenceSet;
		mUtilitiesMap = utilitiesMap;
	}
	
	@Override
	public long getTimeOutMs() {
		return 1000;
	}

	@Override
	public void execute() {
		List<IPreference> subPreferenceContainers = new ArrayList<IPreference>();
		IPreferenceSet preferenceSet = mPreferenceSet;
		preferenceSet.getContainerPreferences(mSubPreference, subPreferenceContainers);
		
		findBestPrefenrencesForPrimaryAgent(subPreferenceContainers, mCandidatePreferences);
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public Object[] getResults() {
		return mCandidatePreferences.toArray();
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
		Set<Integer> otherAgentIds = mSubPreference.getAllAgentsIds();
		for(int agentId : otherAgentIds) {
			ITask task = new BestPreferenceCheckerTaskImpl(agentId, mCandidatePreferences, mPreferenceSet, mUtilitiesMap);
			subTasks.add(task);
		}
	}
	
	//TODO Consider moving this work to utilities map
	private void findBestPrefenrencesForPrimaryAgent(List<IPreference> preferences, List<IPreference> outputs) {
		int bestUtilities = Integer.MIN_VALUE;
		IUtilitiesMap utilityMap = mUtilitiesMap;
		
		for(IPreference preference : preferences) {
			int utility = utilityMap.getUtilityOfAgent(preference, mPrimaryAgentId);
			if(utility > bestUtilities) {
				bestUtilities = utility;
			}
		}
		
		for(IPreference preference : preferences) {
			int utility = utilityMap.getUtilityOfAgent(preference, mPrimaryAgentId);
			if(utility == bestUtilities) {
				outputs.add(preference);
			}
		}
		
	}

}
