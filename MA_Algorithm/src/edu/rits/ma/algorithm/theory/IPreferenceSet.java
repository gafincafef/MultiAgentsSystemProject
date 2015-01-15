package edu.rits.ma.algorithm.theory;

import java.util.List;

public interface IPreferenceSet {
	public void addPreference(IPreference preference);
	public void removePreference(IPreference preference);
	public void clear();
	public void getContainerPreferences(IPreference subPreference, List<IPreference> containerPreferences);
}
