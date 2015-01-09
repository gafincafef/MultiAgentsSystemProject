package edu.rits.ma.jade.concurrency;

import edu.rits.ma.jade.dataobject.CondVar;

public class CondVarManager implements ICondVarReleaser, ICondVarAcquirer {

	@Override
	public void acquire(CondVar condVar) {
		synchronized(condVar) {
			while(condVar.isReleasable()) {
				try {
					condVar.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void release(CondVar condVar) {
		synchronized(condVar) {
			condVar.setReleaseThreshold(condVar.getReleaseThreshold() - 1);
			if(condVar.isReleasable()) {
				condVar.notifyAll();
			}
		}
	}

}
