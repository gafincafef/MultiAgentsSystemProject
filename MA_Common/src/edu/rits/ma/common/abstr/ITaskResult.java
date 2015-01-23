/*
 * Provide interface for task 's execution result. Be used in others module.
 * Implementations should be found only in algorithm module
 */
package edu.rits.ma.common.abstr;

import java.util.Set;

public interface ITaskResult {
	public Object[] toArray();
	public Set<Object> toSet();
	public String toString();
}
