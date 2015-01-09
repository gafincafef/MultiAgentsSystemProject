package edu.rits.ma.common.theory;

import java.util.List;

public interface IPreferenceSet {
	public static final int PRERENCE_SET_OP_RESULT_OK = 0;
	public static final int PRERENCE_SET_OP_RESULT_NG = -1;
	
	public int addPreference(Preference preference);
	public int getAllPrefenreces(List<Preference> outputs);
	public int getPreferencesOfAgents(int[] agentIds, List<Preference> outputs);
}