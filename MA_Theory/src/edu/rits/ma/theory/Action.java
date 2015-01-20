package edu.rits.ma.theory;

public class Action {
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
		System.out.println("Compare " + getId() + " " + ((Action)other).getId() + " " + (getId()==((Action)other).getId()));
		boolean eq = (getId()==((Action)other).getId());
		return eq;
	}
	
}
