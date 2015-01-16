package edu.rits.ma.theory;

public class NashEquilibriumProblem {
	private IPreferenceSet mPreferenceSet;
	private IUtilitiesMap mUtilitiesMap;
	private IPreferenceSet mCandidateResultPrefences;
	
	public NashEquilibriumProblem(IPreferenceSet preferenceSet, IUtilitiesMap utilitiesMap, IPreferenceSet candidateResultPreferences) {
		mPreferenceSet = preferenceSet;
		mUtilitiesMap = utilitiesMap;
		mCandidateResultPrefences = candidateResultPreferences;
	}

	public IPreferenceSet getPreferenceSet() {
		return mPreferenceSet;
	}

	public IUtilitiesMap getUtilitiesMap() {
		return mUtilitiesMap;
	}

	public IPreferenceSet getCandidateResultPrefences() {
		return mCandidateResultPrefences;
	}
	
	public void addResultPreferenceCandidate(IPreference preference) {
		mPreferenceSet.addPreference(preference);
	}
	
	public void removeResultPreferenceCandidate(IPreference preference) {
		mPreferenceSet.removePreference(preference);
	}
}