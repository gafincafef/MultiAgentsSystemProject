package edu.rits.ma.test.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rits.ma.algorithm.nashequil.NashEquilibriumAgentBasedSolverImpl;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.NashEquilibriumProblem;
import edu.rits.ma.webapp.util.NashEquilibirumProblemParser;

public class TestSolver {

	private NashEquilibirumProblemParser mParser = new NashEquilibirumProblemParser();
	private String mProblemStr = "{\"utilityMap\":{\"1\":{\"1\":\"3\",\"2\":\"3\"},\"2\":{\"1\":\"6\",\"2\":\"4\"},\"3\":{\"1\":\"4\",\"2\":\"6\"},\"4\":{\"1\":\"2\",\"2\":\"2\"}},\"numberOfAgent\":\"2\",\"preferences\":[{\"id\":\"1\",\"actionMap\":{\"1\":\"1\",\"2\":\"1\"}},{\"id\":\"2\",\"actionMap\":{\"1\":\"1\",\"2\":\"2\"}},{\"id\":\"3\",\"actionMap\":{\"1\":\"2\",\"2\":\"1\"}},{\"id\":\"4\",\"actionMap\":{\"1\":\"2\",\"2\":\"2\"}}]}";
	private NashEquilibriumProblem mProblem = null;
	private NashEquilibriumAgentBasedSolverImpl mSolver = new NashEquilibriumAgentBasedSolverImpl(null);
	
	@Before
	public void setUp() throws Exception {
		mProblem = mParser.parse(mProblemStr);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		testProblemParsing();
		testProblemSolving();
	}
	
	private void testProblemSolving() {
		Set<Integer> agentIdSet = mProblem.getPreferenceSet().getAllAgentIds();
		assertEquals(2, agentIdSet.size());
		
		List<ITask> tasks = new ArrayList<ITask>();
		
		mSolver.convertProblemToTasks(mProblem, agentIdSet, tasks);
		
		assertEquals(2, tasks.size());
		
		ITask task1 = tasks.get(1);
		task1.execute();
		
		ITaskResult taskResult1 = task1.getResult();
		Set<Object> task1CandidatePreferences = taskResult1.toSet();
		
		assertEquals(1, task1CandidatePreferences.size());
		
		IPreference candadiatePreference1 = (IPreference) taskResult1.toArray()[0];
		
		assertNotNull(candadiatePreference1);
		
		assertEquals(1, candadiatePreference1.getActionOfAgent(1).getId());
		assertEquals(2, candadiatePreference1.getActionOfAgent(2).getId());
	
		List<ITask> subTasks1 = new ArrayList<ITask>();
		task1.createSubTasks(subTasks1);
		
		assertEquals(1, subTasks1.size());
		
		List<ITaskResult> subTasksResults1 = new LinkedList<ITaskResult>();
		for(ITask subTask : subTasks1) {
			subTask.execute();
			subTasksResults1.add(subTask.getResult());
		}
		
		taskResult1 = task1.getResult();
		task1CandidatePreferences = taskResult1.toSet();
		
		assertEquals(1, task1CandidatePreferences.size());
		
		candadiatePreference1 = (IPreference) taskResult1.toArray()[0];
		
		assertNotNull(candadiatePreference1);
		
		assertEquals(1, candadiatePreference1.getActionOfAgent(1).getId());
		assertEquals(2, candadiatePreference1.getActionOfAgent(2).getId());
	
		task1.processSubTaskResults(subTasksResults1);
		
	}

	private void testProblemParsing() {
		assertNotNull(mProblem);
	}

}
