package edu.rits.ma.test.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;

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
	public List<Object> getResults() {
		List<Object> resultList = new ArrayList<Object>();
		for(int i = 0; i < mSubTaskResults.length; i++) {
			resultList.add(mSubTaskResults[i]);
		}
		return resultList;
		
	}

}
