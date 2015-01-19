package edu.rits.ma.theory;

public class NashEquilibriumProblem {
	private IPreferenceSet mPreferenceSet;
	private IUtilitiesMap mUtilitiesMap;
	
	public NashEquilibriumProblem(IPreferenceSet preferenceSet, IUtilitiesMap utilitiesMap) {
		mPreferenceSet = preferenceSet;
		mUtilitiesMap = utilitiesMap;
	}

	public IPreferenceSet getPreferenceSet() {
		return mPreferenceSet;
	}

	public IUtilitiesMap getUtilitiesMap() {
		return mUtilitiesMap;
	}
}
