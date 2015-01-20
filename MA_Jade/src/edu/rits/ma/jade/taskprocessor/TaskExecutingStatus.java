package edu.rits.ma.jade.taskprocessor;

public class TaskExecutingStatus {
	private int mNextTaskId = 0;
	private int mNumberOfSubTasksToWait = 0;
	private int mNumberOfSecondaryAgentsNotReady = 0;
	private int mNumberOfTasks = 0;
	
	public TaskExecutingStatus(int numberOfTasks) {
		mNumberOfTasks = numberOfTasks;
	}
	
	public int getPreviousTaskIndex() {
		return mNextTaskId - 1;
	}
	
	public int getNextTaskIndex() {
		return mNextTaskId;
	}
	
	public void incrementNextTaskIndex() {
		mNextTaskId++;
	}
	
	public boolean allTasksDone() {
		return mNextTaskId >= mNumberOfTasks;
	}
	
	public boolean allSubTasksDone() {
		return mNumberOfSubTasksToWait <= 0;
	}
	
	public void setNumberOfSubTasksToWait(int numberOfSubTasksToWait) {
		mNumberOfSubTasksToWait = numberOfSubTasksToWait;
	}
	
	public void decrementNumberOfSubTasksToWait() {
		mNumberOfSubTasksToWait--;
	}

	public boolean allSecondaryAgentReady() {
		return mNumberOfSecondaryAgentsNotReady <= 0;
	}

	public void setNumberOfSecondaryAgentsNotReady(int mNumberOfSecondaryAgentsNotReady) {
		this.mNumberOfSecondaryAgentsNotReady = mNumberOfSecondaryAgentsNotReady;
	}
	
	public void decrementNumberOfSecondaryAgentsNotReady() {
		this.mNumberOfSecondaryAgentsNotReady--;
	}
}
