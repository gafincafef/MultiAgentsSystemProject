package edu.rits.ma.jade.concurrency;

import edu.rits.ma.jade.dataobject.CondVar;

public interface ICondVarAcquirer {
	void acquire(CondVar condVar);
}