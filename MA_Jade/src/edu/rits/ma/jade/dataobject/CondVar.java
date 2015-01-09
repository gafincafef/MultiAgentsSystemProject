package edu.rits.ma.jade.dataobject;

public class CondVar {
	private int mReleaseThreshold = 0;

	public boolean isReleasable() {
		return mReleaseThreshold <= 0;
	}

	public void setReleaseThreshold(int threshold) {
		this.mReleaseThreshold = threshold;
	}
	
	public int getReleaseThreshold() {
		return mReleaseThreshold;
	}
}