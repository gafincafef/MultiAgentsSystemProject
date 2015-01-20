package edu.rits.ma.algorithm.nashequil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;

public class BestPreferenceCheckerTaskImpl implements ITask, Serializable {

	private static final long serialVersionUID = -1948631945519846420L;

	private int mAgentId;

	private IPreferenceSet mPreferenceSet = null;
	private IUtilitiesMap mUtilitiesMap = null;
	private List<IPreference> mCandidatePreferences = new LinkedList<IPreference>();

	public BestPreferenceCheckerTaskImpl(int agentId, List<IPreference> candidatePreferences, IPreferenceSet preferenceSet, IUtilitiesMap utilitiesMap) {
		mAgentId = agentId;
		mPreferenceSet = preferenceSet;
		mUtilitiesMap = utilitiesMap;
		mCandidatePreferences.addAll(candidatePreferences);
	}

	@Override
	public void execute() {
		for(Iterator<IPreference> pIter = mCandidatePreferences.iterator(); pIter.hasNext();) {
			IPreference preference = pIter.next();
			if(!isBestPreferenceForAgent(preference)) {
				pIter.remove();
			}
		}
	}

	private boolean isBestPreferenceForAgent(IPreference candidatePreference) {
		Set<Integer> complementAgentIdSet = mPreferenceSet.getAllAgentIds();
		complementAgentIdSet.remove(mAgentId);

		IPreference subPreference = candidatePreference.getSubPreference(complementAgentIdSet);
		int candidateUtilValueForAgent = mUtilitiesMap.getUtilityOfAgent(candidatePreference, mAgentId);

		if(subPreference != null) {
			List<IPreference> preferences = new ArrayList<IPreference>();
			mPreferenceSet.getContainerPreferences(subPreference, preferences);

			for(IPreference oPreference : preferences) {
				if(oPreference == null) {
					System.out.println("----------------------------Null preference !!!");
				}
				if(mUtilitiesMap.getUtilityOfAgent(oPreference, mAgentId) > candidateUtilValueForAgent) {
					System.out.println("-----------------------------For agent " + 
							mAgentId +
							" candidate util = " +
							candidateUtilValueForAgent + 
							" < utility value "  +
							mUtilitiesMap.getUtilityOfAgent(oPreference, mAgentId) +
							" by action " +
							oPreference.getActionOfAgent(mAgentId).getId());
					return false;
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public ITaskResult getResult() {
		List<Object> results = new ArrayList<Object>();
		results.addAll(mCandidatePreferences);
		return new PreferencesTaskResultImpl(mCandidatePreferences);
	}

	@Override
	public void processSubTaskResults(List<ITaskResult> subTasksResults) {
		//Do nothing
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
	}

}
