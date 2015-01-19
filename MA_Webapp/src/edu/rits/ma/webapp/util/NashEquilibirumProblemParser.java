package edu.rits.ma.webapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.rits.ma.algorithm.impl.IndexBasedPreferenceImpl;
import edu.rits.ma.algorithm.impl.PreferenceSetImpl;
import edu.rits.ma.algorithm.impl.UtilitiesMapImpl;
import edu.rits.ma.theory.Action;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.IPreferenceSet;
import edu.rits.ma.theory.IUtilitiesMap;
import edu.rits.ma.theory.NashEquilibriumProblem;

public class NashEquilibirumProblemParser {
	private IPreferenceSet mPreferenceSet = null;
	private IUtilitiesMap mUtilityMap = null;
	private Map<Integer, IPreference> mPreferenceTmpMap = new HashMap<Integer, IPreference>();

	public NashEquilibirumProblemParser() {
		mPreferenceSet = new PreferenceSetImpl();
		mUtilityMap = new UtilitiesMapImpl();
	}
	
	public NashEquilibirumProblemParser(IPreferenceSet preferenceSet, IUtilitiesMap utilityMap) {
		mPreferenceSet = preferenceSet;
		mUtilityMap = utilityMap;
	}

	public IPreferenceSet getPreferenceSet() {
		return mPreferenceSet;
	}

	public IUtilitiesMap getUtilityMap() {
		return mUtilityMap;
	}

	public NashEquilibriumProblem parse(String jsonString) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject problemJSONObject = (JSONObject) parser.parse(jsonString);

		mPreferenceSet.clear();
		mPreferenceTmpMap.clear();

		//Parse preference set
		parsePreferenceSet(problemJSONObject, mPreferenceSet, mPreferenceTmpMap);

		//Parse utility map with information of preference set
		parseUtilityMap(problemJSONObject, mPreferenceSet, mPreferenceTmpMap, mUtilityMap);
	
		return new NashEquilibriumProblem(mPreferenceSet, mUtilityMap);
	}

	@SuppressWarnings("rawtypes")
	private void parsePreferenceSet(JSONObject jsonObject, IPreferenceSet preferenceSet, Map<Integer, IPreference> preferenceTmpMap) throws Exception {
		JSONArray jPreferences = (JSONArray)jsonObject.get("preferences");
		for(Object oPreference : jPreferences) {
			JSONObject jPreference = (JSONObject) oPreference;
			String sId = (String) jPreference.get("id");
			int preferenceId = Integer.valueOf(sId);

			//TODO Think again to avoid this dependency
			IPreference preference = new IndexBasedPreferenceImpl(preferenceId);
			mPreferenceTmpMap.put(preferenceId, preference);

			JSONObject jActionMap = (JSONObject) jPreference.get("actionMap");

			Set oAgentIdKeySet = jActionMap.keySet();
			for(Object oAgentId : oAgentIdKeySet) {
				String sAgentId = (String) oAgentId;
				String sActionId = (String) jActionMap.get(oAgentId);
				int agentID = Integer.valueOf(sAgentId);
				int actionID = Integer.valueOf(sActionId);
				preference.addAgentAction(agentID, new Action(actionID, sActionId));
			}

			preferenceSet.addPreference(preference);
		}
	}

	@SuppressWarnings("rawtypes")
	private void parseUtilityMap(JSONObject jsonObject, IPreferenceSet preferenceSet, Map<Integer, IPreference> preferenceTmpMap, IUtilitiesMap utilityMap) {
		JSONObject jUtilityMap = (JSONObject) jsonObject.get("utilityMap");
		Set oUtilityPreferenceKeySet = jUtilityMap.keySet();
		for(Object oUtilityPreferenceKey : oUtilityPreferenceKeySet) {
			String sPreferenceKey = (String) oUtilityPreferenceKey;
			int preferenceKey = Integer.valueOf(sPreferenceKey);
			IPreference preference = preferenceTmpMap.get(preferenceKey);

			JSONObject jUtilityMapForPreference = (JSONObject) jUtilityMap.get(oUtilityPreferenceKey);
			Set oAgentIdKeySet = jUtilityMapForPreference.keySet();
			for(Object oAgentId : oAgentIdKeySet) {
				String sAgentId = (String) oAgentId;
				String sUtilityValue = (String) jUtilityMapForPreference.get(oAgentId);
				int agentId = Integer.valueOf(sAgentId);
				int utilityValue = Integer.valueOf(sUtilityValue);
				utilityMap.addPreferenceUtilityForAgent(preference, agentId, utilityValue);
			}
		}
	}
}
