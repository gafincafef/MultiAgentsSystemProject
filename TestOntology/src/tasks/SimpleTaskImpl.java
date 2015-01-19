package tasks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTaskImpl implements ITask, Serializable {

	private static final long serialVersionUID = -5543755710209030768L;

	private Map<Integer, Integer> mMap = new HashMap<Integer, Integer>();
	
	public SimpleTaskImpl() {
		mMap.put(1, 2);
		mMap.put(2, 4);
		mMap.put(3, 6);
	}
	
	@Override
	public void execute() {
		mMap.put(5, 10);
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public Object[] getResults() {
		return new Object[] {mMap};
	}

	@Override
	public void createSubTasks(List<ITask> subTasks) {
		
	}

}
