package edu.rits.ma.common.abstr;

import java.util.Set;

public interface ITaskResult {
	public Object[] toArray();
	public Set<Object> toSet();
	public String toString();
}
