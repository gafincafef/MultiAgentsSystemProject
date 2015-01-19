package edu.rits.ma.algorithm.nashequil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;

public class FindBestPreferencesTaskImpl implements ITask, Serializable {

	private static final long serialVersionUID = -3652229282002452976L;

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
	public List<Object> getResults() {
		List<Object> results = new ArrayList<Object>();
		results.addAll(mCandidatePreferences);
		return results;
	}

	@Override
	public void processSubTaskResults(List<Object>[] subTasksResults) {
		/*
		 *  Expected sub tasks results : 
		 *  - Each sub task return list of preferences in the candidate preferences list which it accepts 
		 * 
		 *  Any candidate is not accepted by at least one of the sub tasks would be eliminated
		 */
		for(Iterator<IPreference> pIter = mCandidatePreferences.iterator(); pIter.hasNext();) {
			IPreference candidatePreference = pIter.next();
			for(List<Object> candidateAcceptedBySubTask : subTasksResults) {
				pIter.remove();
				if(!candidateAcceptedBySubTask.contains(candidatePreference)) {
					break;
				}
			}
		}
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
		Set<Integer> otherAgentIds = mSubPreference.getAllAgentIds();
		for(int agentId : otherAgentIds) {
			ITask task = new BestPreferenceCheckerTaskImpl(agentId, mCandidatePreferences, mPreferenceSet, mUtilitiesMap);
			subTasks.add(task);
		}
	}

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
