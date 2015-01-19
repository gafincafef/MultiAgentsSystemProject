package edu.rits.ma.algorithm.nashequil;

import java.util.ArrayList;
import java.util.Iterator;
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
		for(Iterator<IPreference> pIter = mCandidatePreferences.iterator(); pIter.hasNext();) {
			IPreference preference = pIter.next();
			
			Set<Integer> complementAgentIdSet = mPreferenceSet.getAllAgentIds();
			complementAgentIdSet.remove(mAgentId);
			
			IPreference subPreference = preference.getSubPreference(complementAgentIdSet);
			int candidateUtilValueForAgent = mUtilitiesMap.getUtilityOfAgent(preference, mAgentId);
			
			if(subPreference != null) {
				List<IPreference> preferences = new ArrayList<IPreference>();
				mPreferenceSet.getContainerPreferences(subPreference, preferences);
				
				for(IPreference oPreference : preferences) {
					if(mUtilitiesMap.getUtilityOfAgent(oPreference, mAgentId) > candidateUtilValueForAgent) {
						pIter.remove();
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
	public List<Object> getResults() {
		List<Object> results = new ArrayList<Object>();
		results.addAll(mCandidatePreferences);
		return results;
	}
	
	@Override
	public void processSubTaskResults(List<Object>[] subTasksResults) {
		//Do nothing
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
	}

}
