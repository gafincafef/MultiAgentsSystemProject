package edu.rits.ma.test.impl;

import java.io.Serializable;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;

public abstract class AbstractTask implements ITask, Serializable {

	private static final long serialVersionUID = 98446188229760633L;
	private int mStatus;
	
	public AbstractTask() {
		mStatus = TASK_STATUS_CREATED;
	}
	
	@Override
	public int getStatus() {
		return mStatus;
	}
	
	@Override
	public void processSubTaskResults(List<ITaskResult> subTasksResults) {
		//Do no thing
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
