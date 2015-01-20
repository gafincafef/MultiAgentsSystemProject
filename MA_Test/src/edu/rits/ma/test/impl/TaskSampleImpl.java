package edu.rits.ma.test.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;

public class TaskSampleImpl extends AbstractTask implements Serializable{

	private static final long serialVersionUID = -818353857965426427L;
	
	private int[] mSubTaskResults = null;
	
	public TaskSampleImpl(int nbSubTasks) {
		super();
		mSubTaskResults = new int[nbSubTasks];
		for(int i = 0; i < nbSubTasks; i++) {
			mSubTaskResults[i] = i * 2 + 1;
		}
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
	public ITaskResult getResult() {
		return new TaskResult();
	}
	
	private class TaskResult implements ITaskResult {

		@Override
		public Object[] toArray() {
			Object[] obs = new Object[mSubTaskResults.length];
			for(int i = 0; i < obs.length; i++) {
				obs[i] = mSubTaskResults[i];
			}
			return obs;
		}

		@Override
		public Set<Object> toSet() {
			Set<Object> set = new HashSet<Object>();
			for(int i = 0; i < mSubTaskResults.length; i++) {
				set.add(mSubTaskResults[i]);
			}
			return set;
		}
		
	}

}
