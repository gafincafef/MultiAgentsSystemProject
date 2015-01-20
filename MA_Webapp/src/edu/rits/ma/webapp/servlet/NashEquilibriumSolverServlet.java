package edu.rits.ma.webapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import edu.rits.ma.algorithm.nashequil.NashEquilibriumAgentBasedSolverImpl;
import edu.rits.ma.common.abstr.IAgentGateway;
import edu.rits.ma.jade.agent.TaskProcessorPrimaryAgentImpl;
import edu.rits.ma.jade.gateway.JadeAgentGatewayImpl;
import edu.rits.ma.theory.INashEquilibriumSolver;
import edu.rits.ma.theory.IPreference;
import edu.rits.ma.theory.NashEquilibriumProblem;
import edu.rits.ma.webapp.util.NashEquilibirumProblemParser;

public class NashEquilibriumSolverServlet extends HttpServlet {

	private static final long serialVersionUID = 5003473793288915706L;
	private static final String POST_PARAM_NAME = "problem";
	
	private NashEquilibirumProblemParser mProblemParser = null;
	private INashEquilibriumSolver mProblemSolver = null;
	private IAgentGateway mAgentGateway = null;
	
	@Override
	public void init() throws ServletException {
		mProblemParser = new NashEquilibirumProblemParser();
		mAgentGateway = new JadeAgentGatewayImpl("localhost", "1099", TaskProcessorPrimaryAgentImpl.class.getName());
		mProblemSolver = new NashEquilibriumAgentBasedSolverImpl(mAgentGateway);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String problemInJSONString = getPostString(req);
		Logger.getLogger(getServletName()).log(Level.INFO, problemInJSONString);
		
		List<Integer> resultPreferenceIndexes = new ArrayList<Integer>();
		parseAndSolveProblem(problemInJSONString, resultPreferenceIndexes);
		
		responseResults(resp, resultPreferenceIndexes);
	}
	
	private void parseAndSolveProblem(String problemInJSONString, List<Integer> resultPreferenceIndexes) {
		resultPreferenceIndexes.clear();
		try {
			NashEquilibriumProblem problem = mProblemParser.parse(problemInJSONString);
			if(problem != null && mProblemSolver != null) {
				mProblemSolver.solve(problem);
				List<IPreference> resultPreferences = new ArrayList<IPreference>();
				mProblemSolver.getResults(resultPreferences);
				for(IPreference preference : resultPreferences) {
					if(preference.isIndexed()) {
						resultPreferenceIndexes.add(preference.getIndex());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultPreferenceIndexes.clear();
		}
	}

	private String getPostString(HttpServletRequest req) throws IOException {
		return req.getParameter(POST_PARAM_NAME);
	}
	
	@SuppressWarnings("unchecked")
	private void responseResults(HttpServletResponse response, List<Integer> resultIndexes) throws IOException {
		JSONArray jResults = new JSONArray();
		for(int resultId : resultIndexes) {
			jResults.add(resultId);
		}
		String strResults = jResults.toJSONString();
		Logger.getLogger(getServletName()).log(Level.INFO, "Response JSON string:" + strResults);
		
		response.setHeader("Content-Type", "application/json");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(strResults.getBytes());
	}
}
