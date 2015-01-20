package edu.rits.ma.test.main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rits.ma.algorithm.theory.impl.IndexBasedPreferenceImpl;
import edu.rits.ma.algorithm.theory.impl.PreferenceSetImpl;
import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;

public class TestPreferenceSet {

	private IPreferenceSet mPreferenceSet = new PreferenceSetImpl();

	//Test case of preference set
	//Agent 1: Action set = 1, 2
	//Agent 2: Action set = 3, 4, 5
	//Agent 3: Action set = 7
	private int[] mAgent1Actions = new int[] {1, 2};
	private int[] mAgent2Actions = new int[] {3, 4, 5};
	private int[] mAgent3Actions = new int[] {7};

	@Before
	public void setUp() throws Exception {
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
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		testAgentSet();
		testNumberOfPreferences();
		testSubPreference();
	}


	private void testAgentSet() {
		Set<Integer> agentIdSet = mPreferenceSet.getAllAgentIds();
		assertEquals(3, agentIdSet.size());
		assertTrue(agentIdSet.contains(1));
		assertTrue(agentIdSet.contains(2));
		assertTrue(agentIdSet.contains(3));
	}

	private void testNumberOfPreferences() {
		List<IPreference> preferences = new ArrayList<IPreference>();
		mPreferenceSet.getAllPreference(preferences);
		assertEquals(mAgent1Actions.length * mAgent2Actions.length * mAgent3Actions.length, preferences.size());
	}

	private void testSubPreference() {
		Set<Integer> subAgentIds = new HashSet<Integer>();
		subAgentIds.add(1);
		subAgentIds.add(2);
		
		Set<Integer> agentIds = new HashSet<Integer>();
		agentIds.add(1);
		agentIds.add(2);
		agentIds.add(3);
		
		List<IPreference> subPreferences = new ArrayList<IPreference>();
		mPreferenceSet.getSubPreferencesForAgents(subAgentIds, subPreferences);
	
		assertEquals(mAgent1Actions.length * mAgent2Actions.length, subPreferences.size());
	
		for(IPreference subPreference : subPreferences) {
			assertEquals(subAgentIds.size(), subPreference.getAllAgentIds().size());
			
			List<IPreference> containterPreferences = new ArrayList<IPreference>();
			mPreferenceSet.getContainerPreferences(subPreference, containterPreferences);
			assertEquals(mAgent3Actions.length, containterPreferences.size());
		}
	}
}
