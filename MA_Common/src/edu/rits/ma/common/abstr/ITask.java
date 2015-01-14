package edu.rits.ma.common.abstr;

import java.util.List;

public interface ITask {
	public static final int TASK_STATUS_CREATED = -1;
	public static final int TASK_STATUS_INITIATED = 0;
	public static final int TASK_STATUS_FINISHED = 1;
	public static final int TASK_STATUS_SUCCEEDED = 2;
	public static final int TASK_STATUS_FAILED = 3;
	
	long getTimeOutMs();
	
	void execute();
	
	int getStatus();
	
	Object[] getResults();
	
	void createSubTasks(List<ITask> subTasks);
}