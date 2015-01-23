/*
 * Provide interface for tasks. Be used in others module.
 * Implementations should be found only in algorithm module
 */
package edu.rits.ma.common.abstr;

import java.util.List;

public interface ITask {
	public static final int TASK_STATUS_CREATED = -1;
	public static final int TASK_STATUS_INITIATED = 0;
	public static final int TASK_STATUS_FINISHED = 1;
	public static final int TASK_STATUS_SUCCEEDED = 2;
	public static final int TASK_STATUS_FAILED = 3;
	
	void execute();
	
	int getStatus();
	
	ITaskResult getResult();
	
	void processSubTaskResults(List<ITaskResult> subTasksResults);
	
	void createSubTasks(List<ITask> subTasks);
}
