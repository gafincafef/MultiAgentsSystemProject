package edu.rits.ma.test.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rits.ma.algorithm.nashequil.FindBestPreferencesTaskImpl;
import edu.rits.ma.algorithm.theory.impl.IndexBasedPreferenceImpl;
import edu.rits.ma.algorithm.theory.impl.PreferenceSetImpl;
import edu.rits.ma.algorithm.theory.impl.UtilitiesMapImpl;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;

public class TestAlgorithmTask {

	private IPreferenceSet mPreferenceSet = new PreferenceSetImpl();
	private IUtilitiesMap mUtilityMap = new UtilitiesMapImpl();
	
	//Test case

	//Preference set
	//Agent 1: Action set = 1, 2
	//Agent 2: Action set = 3, 4, 5
	//Agent 3: Action set = 7
	private int[] mAgent1Actions = new int[] {1, 2};
	private int[] mAgent2Actions = new int[] {3, 4, 5};
	private int[] mAgent3Actions = new int[] {7};

	//Utility map

	//Utility[1][3][7][1] = 4;
	//Utility[1][3][7][2] = 5;
	//Utility[1][3][7][3] = 6;
	//Utility[1][4][7][1] = 10;
	//Utility[1][4][7][2] = 12;
	//Utility[1][4][7][3] = 8;
	//Utility[1][5][7][1] = 10;
	//Utility[1][5][7][2] = 0;
	//Utility[1][5][7][3] = 2;

	//Utility[2][3][7][1] = 4;
	//Utility[2][3][7][2] = 5;
	//Utility[2][3][7][3] = 6;
	//Utility[2][4][7][1] = 10;
	//Utility[2][4][7][2] = 12;
	//Utility[2][4][7][3] = 8;
	//Utility[2][5][7][1] = 9;
	//Utility[2][5][7][2] = 0;
	//Utility[2][5][7][3] = 2;

	private int[][][][] mUtilities = new int[10][10][10][4];

	@Before
	public void setUp() throws Exception {
		mUtilities[1][3][7][1] = 4;
		mUtilities[1][3][7][2] = 5;
		mUtilities[1][3][7][3] = 6;
		mUtilities[1][4][7][1] = 10;
		mUtilities[1][4][7][2] = 12;
		mUtilities[1][4][7][3] = 8;
		mUtilities[1][5][7][1] = 10;
		mUtilities[1][5][7][2] = 90;
		mUtilities[1][5][7][3] = 2;

		mUtilities[2][3][7][1] = 4;
		mUtilities[2][3][7][2] = 5;
		mUtilities[2][3][7][3] = 6;
		mUtilities[2][4][7][1] = 10;
		mUtilities[2][4][7][2] = 12;
		mUtilities[2][4][7][3] = 8;
		mUtilities[2][5][7][1] = 9;
		mUtilities[2][5][7][2] = 0;
		mUtilities[2][5][7][3] = 2;

		for(int actionIndex1 = 0; actionIndex1 < mAgent1Actions.length; actionIndex1++) {
			for(int actionIndex2 = 0; actionIndex2 < mAgent2Actions.length; actionIndex2++) {
				for(int actionIndex3 = 0; actionIndex3 < mAgent3Actions.length; actionIndex3++) {
					int agent1Action = mAgent1Actions[actionIndex1];
					int agent2Action = mAgent2Actions[actionIndex2];
					int agent3Action = mAgent3Actions[actionIndex3];

					IPreference preference = new IndexBasedPreferenceImpl();
					preference.addAgentAction(1, new Action(agent1Action, ""));
					preference.addAgentAction(2, new Action(agent2Action, ""));
					preference.addAgentAction(3, new Action(agent3Action, ""));
					mPreferenceSet.addPreference(preference);

					for(int agentId = 1; agentId <= 3; agentId++)
						mUtilityMap.addPreferenceUtilityForAgent(preference, agentId, mUtilities[agent1Action][agent2Action][agent3Action][agentId]);
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		//testUtilityMap();
		//testHashMap();
		testFindBestPreference();
	}
						
	private void testHashMap() {
		Map<IntWrapper, Integer> map = new HashMap<IntWrapper, Integer>();
		map.put(new IntWrapper(10), 1);
		map.put(new IntWrapper(1), 12);
		map.put(new IntWrapper(20), 3);
		map.put(new IntWrapper(30), 39);
		
		System.out.println("Checking search");
		IntWrapper key = new IntWrapper(20);
		assertEquals(3, (int)map.get(key));
	}
	
	private void testUtilityMap() {
		int agent1Action = 1;
		int agent2Action = 5;
		int agent3Action = 7;
		
		//Test utilities value of preference 
		IPreference preference = new IndexBasedPreferenceImpl();
		preference.addAgentAction(1, new Action(agent1Action, ""));
		preference.addAgentAction(2, new Action(agent2Action, ""));
		preference.addAgentAction(3, new Action(agent3Action, ""));
		
		for(int agentId = 1; agentId <= 3; agentId++) {
			Integer utilityVal = mUtilityMap.getUtilityOfAgent(preference, agentId);
			assertNotNull(utilityVal);
			assertEquals(mUtilities[agent1Action][agent2Action][agent3Action][agentId], (int)utilityVal);
		}
	}
	
	private void testFindBestPreference() {
		int primaryAgentId = 1;
		
		//Agent2Action = 5
		//Agent3Action = 7
		//Expect result for Agent1Action = {1} because utility[1][5][7][1] = 10 > utility[2][5][7][1] = 9
		int agent2Action = 5;
		int agent3Action = 7;
		IPreference subPreference = new IndexBasedPreferenceImpl();
		subPreference.addAgentAction(2, new Action(agent2Action, ""));
		subPreference.addAgentAction(3, new Action(agent3Action, ""));
		
		
		ITask task = new FindBestPreferencesTaskImpl(primaryAgentId, subPreference, mPreferenceSet, mUtilityMap); 
		task.execute();
		
		ITaskResult result = task.getResult();
		Set<Object> resultPreferences = result.toSet();
		
		assertEquals(1, resultPreferences.size());
		
		for(Object oPreference : resultPreferences) {
			IPreference preference = (IPreference) oPreference;
			assertEquals(1, preference.getActionOfAgent(1).getId());
			assertEquals(5, preference.getActionOfAgent(2).getId());
			assertEquals(7, preference.getActionOfAgent(3).getId());
		}
		
		//Now suppose we have to process sub task now
		List<ITask> subTasks = new ArrayList<ITask>();
		task.createSubTasks(subTasks);
		List<ITaskResult> subTasksResults = new LinkedList<ITaskResult>();
		for(ITask subTask : subTasks) {
			subTask.execute();
			subTasksResults.add(subTask.getResult());
		}
		
		//There must be 2 sub tasks for agent 2 and agent 3
		assertEquals(2, subTasks.size());
		
		task.processSubTaskResults(subTasksResults);
		
		result = task.getResult();
		resultPreferences = result.toSet();
		
		//Now the results size still 1 because utility[1][5][7][2] = 90  > utility[1][3][7][2] = 5
		//and 7 is the only choice for agent 3
		assertEquals(1, resultPreferences.size());
	}
	
	private static class IntWrapper {
		int val = 0;
		
		public IntWrapper(int val) {
			this.val = val;
		}
		
		@Override
		public boolean equals(Object object) {
			System.out.println("Check equal");
			if(object instanceof IntWrapper) {
				return ((IntWrapper)object).val == val;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return val % 10;
		}
	}
}
