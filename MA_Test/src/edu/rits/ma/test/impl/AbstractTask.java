package edu.rits.ma.test.impl;

import edu.rits.ma.common.ITask;

public abstract class AbstractTask implements ITask {

	private int mStatus;
	
	public AbstractTask() {
		mStatus = TASK_STATUS_CREATED;
	}
	
	@Override
	public int getStatus() {
		return mStatus;
	}
	
	protected void onInitiated() {
		mStatus = TASK_STATUS_INITIATED;
	}
	
	protected void onSucceeded() {
		mStatus = TASK_STATUS_SUCCEEDED;
	}
	
	protected void onFailed() {
		mStatus = TASK_STATUS_FAILED;
	}

}