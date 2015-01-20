package edu.rits.ma.algorithm.theory.impl;

import edu.rits.ma.theory.IPreference;

public class IndexBasedPreferenceImpl extends PreferenceImpl {
	private int mIndex;

	public IndexBasedPreferenceImpl() {
		mIndex  = -1;
	}

	public IndexBasedPreferenceImpl(int id) {
		mIndex = id;
	}

	@Override
	public int getIndex() {
		return mIndex;
	}

	@Override
	public boolean isIndexed() {
		return mIndex > 0;
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof IndexBasedPreferenceImpl && mIndex > 0 && ((IndexBasedPreferenceImpl)other).mIndex >0) {
			return mIndex == ((IndexBasedPreferenceImpl)other).mIndex;
		}
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		return mIndex > 0 ? mIndex : super.hashCode();
	}

	@Override
	protected IPreference newInstance() {
		return new IndexBasedPreferenceImpl();
	}
}
