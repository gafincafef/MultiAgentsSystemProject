package edu.rits.ma.test.impl;

import java.io.Serializable;
import java.util.List;

import edu.rits.ma.common.abstr.ITask;
import edu.rits.ma.common.abstr.ITaskResult;
import edu.rits.ma.jade.util.LogUtil;

public class ChildTaskSampleImpl extends AbstractTask implements Serializable {

	private static final long serialVersionUID = -9074618601502495931L;
	
	private int mChildTaskId;
	private int[] mResultStorage = null;
	
	public ChildTaskSampleImpl(int[] resultStorage, int childTaskId) {
		super();
		mChildTaskId = childTaskId;
		mResultStorage = resultStorage;
		for(int i = 0; i < resultStorage.length; i++) {
			LogUtil.logInfo(this, mResultStorage + "Index " + i + " value " + mResultStorage[i]);
		}
	}
	
	@Override
	public void execute() {
		onInitiated();
		try {
			for(int i = 0; i < mResultStorage.length; i++) {
				LogUtil.logInfo(this, mResultStorage + "Index " + i + " value " + mResultStorage[i]);
			}
			LogUtil.logInfo(this, "On " + mResultStorage + " child task executing " + mChildTaskId);
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
	public ITaskResult getResult() {
		return null;
	}

}
