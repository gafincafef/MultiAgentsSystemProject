package edu.rits.ma.jade.behaviour;

import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.gateway.GatewayAgent;

import java.util.ArrayList;
import java.util.List;

import edu.rits.ma.common.ITask;
import edu.rits.ma.jade.dataobject.IResponse;

public class TaskProcessorBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8974716117508990142L;
	
	private static final int RUN_TASK_PHASE = 0;
	private static final int WAIT_SUB_TASK_PHASE = 1;
	private static final int STOP_PHASE = 2;
	
	private List<ITask> mTasks = null;
	private List<AgentController> mSecondaryAgentControllers = null;
	private GatewayAgent mCallbackGateWayAgent;
	private int mNumberOfRemainingSubTasks = 0;

	private int mActionPhase = RUN_TASK_PHASE;

	@SuppressWarnings("unchecked")
	public TaskProcessorBehaviour(Object command, List<AgentController> secondaryAgentControllers, GatewayAgent callbackGatewayAgent) {
		super();
		mTasks = (List<ITask>) command;
		mSecondaryAgentControllers = secondaryAgentControllers;
		mCallbackGateWayAgent = callbackGatewayAgent;
	}

	@Override
	public void action() {
		try {
			if(mActionPhase == RUN_TASK_PHASE) {
				//Run assigned tasks
				runTasks();
				mActionPhase = WAIT_SUB_TASK_PHASE;
			}
			if(mActionPhase == WAIT_SUB_TASK_PHASE) {
				//Wait for secondary agents report results of sub tasks
				waitForSubTasksOnSecondaryAgents();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mActionPhase = STOP_PHASE;
		}
	}

	@Override
	public int onEnd() {
		//Command released
		mCallbackGateWayAgent.releaseCommand(mTasks);
		return super.onEnd();
	}

	public boolean done() {
		//Done if there is no sub tasks remaining
		return noSubTasks();
	}

	private void runTasks() throws StaleProxyException {
		for(ITask task : mTasks) {
			//Firstly execute the task assigned to primary agent
			task.execute();

			if(task.getStatus() == ITask.TASK_STATUS_SUCCEEDED) {
				List<ITask> subTasks = new ArrayList<ITask>();

				//Each task spawns a list of sub task after finished
				task.createSubTasks(subTasks);

				for(int i = 0; i < subTasks.size(); i++) {
					int secondaryAgentIndex = i;//TODO Tweak this logic. There may be different between sub task id and secondary agent id 
					assignSubTaskForSecondaryAgent(mSecondaryAgentControllers.get(secondaryAgentIndex), subTasks.get(i));
				}
			}
		}
	}

	private void waitForSubTasksOnSecondaryAgents() throws StaleProxyException {
		//Each secondary agent is expected to return number of sub task it completed, i.e. ideally 1
		for(AgentController ac : mSecondaryAgentControllers) {
			IResponse responseFromSecondaryAgent = ac.getO2AInterface(IResponse.class);

			if(responseFromSecondaryAgent != null) {
				onSubTaskCompleted();
			}
		}
	}

	private void assignSubTaskForSecondaryAgent(AgentController agentController, ITask subTask) throws StaleProxyException {
		agentController.putO2AObject(subTask, AgentController.ASYNC);
		onSubTaskCreated();
	}

	private void onSubTaskCreated() {
		mNumberOfRemainingSubTasks++;
	}

	private void onSubTaskCompleted() {
		mNumberOfRemainingSubTasks--;
	}

	private boolean noSubTasks() {
		return mNumberOfRemainingSubTasks <= 0;
	}
}
