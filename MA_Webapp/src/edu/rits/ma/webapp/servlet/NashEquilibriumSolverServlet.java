package edu.rits.ma.webapp.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

public class NashEquilibriumSolverServlet extends HttpServlet {

	private static final long serialVersionUID = 5003473793288915706L;
	private static final String POST_PARAM_NAME = "problem";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String problemInJSONString = getPostString(req);
		Logger.getLogger(getServletName()).log(Level.INFO, problemInJSONString);
		
		//Simply response the mock result. TODO Parse results from task
		int[] results = {1, 2};
		responseResults(resp, results);
	}

	private String getPostString(HttpServletRequest req) throws IOException {
		return req.getParameter(POST_PARAM_NAME);
	}
	
	@SuppressWarnings("unchecked")
	private void responseResults(HttpServletResponse response, int[] resultIds) throws IOException {
		JSONArray jResults = new JSONArray();
		for(int resultId : resultIds) {
			jResults.add(resultId);
		}
		String strResults = jResults.toJSONString();
		Logger.getLogger(getServletName()).log(Level.INFO, "Response JSON string:" + strResults);
		
		response.setHeader("Content-Type", "application/json");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(strResults.getBytes());
	}
}