package edu.rits.ma.theory;

import java.io.Serializable;

public class Action implements Serializable {
	private static final long serialVersionUID = 2694159632739375339L;
	
	private int mId;
	private String mName;
	
	public Action(int id, String name) {
		mId = id;
		mName = name;
	}
	
	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Action)) {
			return false;
		}
		return (getId()==((Action)other).getId());
	}
	
}
