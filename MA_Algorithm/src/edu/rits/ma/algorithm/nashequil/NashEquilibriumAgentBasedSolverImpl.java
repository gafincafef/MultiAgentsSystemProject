package edu.rits.ma.algorithm.nashequil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.IAgentGateway;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.theory.INashEquilibriumSolver;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.NashEquilibriumProblem;

public class NashEquilibriumAgentBasedSolverImpl implements INashEquilibriumSolver {

	private IAgentGateway mAgentGateway = null;
	private List<ITask> mTasks = null;
	
	public NashEquilibriumAgentBasedSolverImpl(IAgentGateway agentGateway) {
		mAgentGateway = agentGateway;
	}
	
	@Override
	public void solve(NashEquilibriumProblem problem) {
		mTasks = new ArrayList<ITask>();
		
		Set<Integer> agentIdSet = problem.getPreferenceSet().getAllAgentIds();
		int numberOfAgents = agentIdSet.size();
		mAgentGateway.prepareAgents(numberOfAgents);
		
		convertProblemToTasks(problem, agentIdSet, mTasks);
		mAgentGateway.runTasksOnAgents(mTasks);
	}
	
	private void convertProblemToTasks(NashEquilibriumProblem problem, Set<Integer> agentIdSet, List<ITask> tasks) {
		Integer[] agentIds = agentIdSet.toArray(new Integer[agentIdSet.size()]);
		
		if(agentIds == null || agentIds.length == 0) {
			return;
		}
		
		int primaryAgentId = agentIds[0];
		
		Set<Integer> secondaryAgentIdSet = new HashSet<Integer>();
		secondaryAgentIdSet.addAll(agentIdSet);
		secondaryAgentIdSet.remove(primaryAgentId);
		
		List<IPreference> allSubPreferences = new ArrayList<IPreference>();
		IPreferenceSet preferenceSet = problem.getPreferenceSet();
		preferenceSet.getSubPreferencesForAgents(secondaryAgentIdSet, allSubPreferences);
		
		for(IPreference subPreference : allSubPreferences) {
			ITask task = new FindBestPreferencesTaskImpl(primaryAgentId, subPreference, preferenceSet, problem.getUtilitiesMap());
			tasks.add(task);
		}
	}

	@Override
	public void getResults(List<IPreference> resultPreferences) {
		for(ITask task : mTasks) {
			Object[] taskResults = task.getResult().toArray();
			for(Object oResult : taskResults) {
				resultPreferences.add((IPreference) oResult);
			}
		}
	}
}
