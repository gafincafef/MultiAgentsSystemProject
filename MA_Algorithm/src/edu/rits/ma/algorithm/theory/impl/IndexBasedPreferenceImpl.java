package edu.rits.ma.algorithm.theory.impl;

import java.io.Serializable;

import edu.rits.ma.theory.IPreference;

public class IndexBasedPreferenceImpl extends PreferenceImpl implements Serializable {

	private static final long serialVersionUID = -5058945633704891446L;
	
	private int mId;
	
	public IndexBasedPreferenceImpl() {
		mId  = -1;
	}
	
	public IndexBasedPreferenceImpl(int id) {
		mId = id;
	}
	
	@Override
	public int getIndex() {
		return mId;
	}

	@Override
	public boolean isIndexed() {
		return mId > 0;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof IPreference)) {
			return false;
		}
		if(other instanceof IndexBasedPreferenceImpl && mId > 0 && ((IndexBasedPreferenceImpl)other).mId >0) {
			return mId == ((IndexBasedPreferenceImpl)other).mId;
		}
		
		return super.equals(other);
	}
	
	@Override
	protected IPreference newInstance() {
		return new IndexBasedPreferenceImpl();
	}
}
