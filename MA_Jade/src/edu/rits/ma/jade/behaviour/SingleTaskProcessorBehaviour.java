package edu.rits.ma.jade.behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import edu.rits.ma.common.ITask;
import edu.rits.ma.jade.dataobject.IResponse;
import edu.rits.ma.jade.dataobject.ResponseImpl;

public class SingleTaskProcessorBehaviour extends Behaviour {

	private static final int NB_TASK_COMPLETED = 1;
	/**
	 * 
	 */
	private static final long serialVersionUID = -949869179196957814L;
	private boolean mTaskExecuted = false;
	
	public SingleTaskProcessorBehaviour(Agent agent) {
		super();
		setAgent(agent);
	}
	
	@Override
	public void action() {
		ITask task = getAssignedTask();
		if(task != null) {
			task.execute();
			onTaskCompleted();
			responseToPrimaryAgent();
		}
		else {
			block();
		}
	}
	
	@Override
	public boolean done() {
		return isTaskExecuted();
	}

	private ITask getAssignedTask() {
		return (ITask) getAgent().getO2AObject();
	}
	
	private void responseToPrimaryAgent() {
		IResponse response = new ResponseImpl();
		response.setResponseValue(NB_TASK_COMPLETED);
		getAgent().registerO2AInterface(IResponse.class, response);
	}
	
	private void onTaskCompleted() {
		mTaskExecuted = true;
	}
	
	private boolean isTaskExecuted() {
		return mTaskExecuted;
	}
}
