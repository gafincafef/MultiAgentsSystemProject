package edu.rits.ma.theory;

import java.util.List;

public interface INashEquilibriumSolver {
	public void solve(NashEquilibriumProblem problem);
	public void getResults(List<IPreference> resultPreferences);
}
