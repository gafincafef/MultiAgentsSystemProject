package edu.rits.ma.test.impl;

import java.util.List;

import edu.rits.ma.common.ITask;

public class ChildTaskSampleImpl extends AbstractTask {

	private int mChildTaskId;
	private int[] mResultStorage = null;
	
	public ChildTaskSampleImpl(int[] resultStorage, int childTaskId) {
		super();
		mChildTaskId = childTaskId;
		mResultStorage = resultStorage;
	}
	
	@Override
	public void execute() {
		onInitiated();
		try {
			mResultStorage[mChildTaskId] = mChildTaskId;
			onSucceeded();
		}
		catch(Exception e) {
			e.printStackTrace();
			onFailed();
		}
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		return;
	}

	@Override
	public Object[] getResults() {
		return null;
	}

	@Override
	public long getTimeOutMs() {
		return 1000;
	}

}