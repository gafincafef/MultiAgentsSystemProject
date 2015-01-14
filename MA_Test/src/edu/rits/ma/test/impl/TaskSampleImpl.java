package edu.rits.ma.test.impl;

import java.util.List;

import edu.rits.ma.common.abstr.ITask;

public class TaskSampleImpl extends AbstractTask {

	private int[] mSubTaskResults = null;
	
	public TaskSampleImpl(int nbSubTasks) {
		super();
		mSubTaskResults = new int[nbSubTasks];
	}
	
	@Override
	public void execute() {
		onInitiated();
		onSucceeded();
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		subTasks.clear();
		int nbSubTasks = mSubTaskResults.length;
		for(int i = 0; i < nbSubTasks; i++) {
			ITask subTask = new ChildTaskSampleImpl(mSubTaskResults, i);
			subTasks.add(subTask);
		}
	}

	@Override
	public Object[] getResults() {
		Object[] results = new Object[mSubTaskResults.length];
		for(int i = 0; i < mSubTaskResults.length; i++) {
			results[i] = mSubTaskResults[i];
		}
		return results;
	}

	@Override
	public long getTimeOutMs() {
		return 10;
	}

}
