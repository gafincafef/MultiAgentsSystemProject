package edu.rits.ma.algorithm.nashequil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.theory.IPreference;

public class PreferencesTaskResultImpl implements ITaskResult {

	private Set<Object> mResults = new HashSet<Object>();
	
	public PreferencesTaskResultImpl(List<IPreference> resultPreferences) {
		mResults.addAll(resultPreferences);
	}
	
	@Override
	public Object[] toArray() {
		return mResults.toArray(new Object[mResults.size()]);
	}

	@Override
	public String toString() {
		return "Preference List";
	}

	@Override
	public Set<Object> toSet() {
		return mResults;
	}
}
