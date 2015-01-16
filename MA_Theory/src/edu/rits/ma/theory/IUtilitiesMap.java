package edu.rits.ma.theory;

public interface IUtilitiesMap {
	public void addPreferenceUtilityForAgent(IPreference preference, int agentId, int utilityValue);
	public Integer getUtilityOfAgent(IPreference preference, int agentId);
}
