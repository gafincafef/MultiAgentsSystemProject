package edu.rits.ma.test.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.rits.ma.common.abstr.IAgentGateway;
import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.jade.agent.TaskProcessorPrimaryAgentImpl;
import edu.rits.ma.jade.gateway.JadeAgentGatewayImpl;
import edu.rits.ma.jade.util.LogUtil;
import edu.rits.ma.test.impl.NashEquilibirumProblemParser;
import edu.rits.ma.test.impl.TaskSampleImpl;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;
import edu.rits.ma.theory.NashEquilibriumProblem;

public class Test {
	public static void main(String[] args) {
		try {
			//testCreateProblemFromJSONString2();
			testJade();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void testListRemoveWhileTraverse(List<Integer> list, int valueToRemove) {
		for(int e : list) {
			System.out.println("Next element " + e);
			if(e == valueToRemove) {
				list.remove(e);
			}
		}
	}
	
	private static void testJade() {
		String host = "localhost";
		String port = "1099";
		String primaryAgentClassName = TaskProcessorPrimaryAgentImpl.class.getName();
		
		IAgentGateway agentGateway = new JadeAgentGatewayImpl(host, port, primaryAgentClassName);
		int nbAgents = 4;
		agentGateway.prepareAgents(nbAgents);
		
		List<ITask> tasks = new ArrayList<ITask>();
		tasks.add(new TaskSampleImpl(nbAgents - 1));
		tasks.add(new TaskSampleImpl(nbAgents - 1));
		
		agentGateway.runTasksOnAgents(tasks);
		
		Object[] results = tasks.get(0).getResult().toArray();
		if(results != null) {
			for(int i = 0; i < results.length; i++) {
				int rv = (int) results[i];
				LogUtil.logInfo(Test.class, "Result " + i + " " + rv);
			}
		}
	}
	
	private static NashEquilibriumProblem testCreateProblemFromJSONString() {
		String jsonStringifiedProblem = "{\"utilityMap\":{\"1\":{\"1\":\"19\",\"2\":\"12\",\"3\":\"23\"},\"2\":{\"1\":\"5\",\"2\":\"3\",\"3\":\"5\"}},\"numberOfAgent\":\"3\",\"preferences\":[{\"id\":\"1\",\"actionMap\":{\"1\":\"3\",\"2\":\"5\",\"3\":\"5\"}},{\"id\":\"2\",\"actionMap\":{\"1\":\"4\",\"2\":\"4\",\"3\":\"5\"}}]}";
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStringifiedProblem);
			
			testParseUtitlityMap((JSONObject) jsonObject.get("utilityMap"));
			
			String numberOfAgentString = (String) jsonObject.get("numberOfAgent");
			System.out.println("Parsed number of agent:" + numberOfAgentString);
			
			JSONArray preferences = (JSONArray)jsonObject.get("preferences");
			System.out.println("Number of preferences:" + preferences.size());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void testParseUtitlityMap(JSONObject utilityMapJson) {
		for(Object preferenceKeyString : utilityMapJson.keySet()) {
			System.out.println("Preference key string " + preferenceKeyString);
			JSONObject utilMapForPreference = (JSONObject) utilityMapJson.get(preferenceKeyString);
			for(Object agentIdString : utilMapForPreference.keySet()) {
				String utilValueString = (String) utilMapForPreference.get(agentIdString);
				System.out.println("Agent " + agentIdString + " utility " + utilValueString);
			}
		}
	}
	

	private static void testCreateProblemFromJSONString2() throws Exception {
		String jsonStringifiedProblem = "{\"utilityMap\":{\"1\":{\"1\":\"19\",\"2\":\"12\",\"3\":\"23\"},\"2\":{\"1\":\"5\",\"2\":\"3\",\"3\":\"5\"}},\"numberOfAgent\":\"3\",\"preferences\":[{\"id\":\"1\",\"actionMap\":{\"1\":\"3\",\"2\":\"5\",\"3\":\"5\"}},{\"id\":\"2\",\"actionMap\":{\"1\":\"4\",\"2\":\"4\",\"3\":\"5\"}}]}";
		NashEquilibirumProblemParser parser = new NashEquilibirumProblemParser();
		parser.parse(jsonStringifiedProblem);
		
		//Test agent id
		IPreferenceSet preferenceSet = parser.getPreferenceSet();
		Set<Integer> agentIds = preferenceSet.getAllAgentIds();
		for(int agentId : agentIds) {
			System.out.println("Agent id " + agentId);
		}
		
		//Test preferences
		List<IPreference> preferences = new ArrayList<IPreference>();
		preferenceSet.getAllPreference(preferences);
		
		System.out.println("Number of preferences " + preferences.size());
	
		//Test utility map
		IUtilitiesMap utilityMap = parser.getUtilityMap();
		for(IPreference preference : preferences) {
			for(int agentId : agentIds) {
				System.out.println("Utility value of agent " + agentId + " = " +utilityMap.getUtilityOfAgent(preference, agentId));
			}
		}
	}
}
