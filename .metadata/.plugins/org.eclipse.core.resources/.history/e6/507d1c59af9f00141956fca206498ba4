package edu.rits.ma.webapp.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NashEquilibriumSolverServlet extends HttpServlet {

	private static final long serialVersionUID = 5003473793288915706L;
	private static final String POST_PARAM_NAME = "problem";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String problemInJSONString = getPostString(req);
		ServletOutputStream responseStream = resp.getOutputStream();
		Logger.getLogger(getServletName()).log(Level.INFO, problemInJSONString);
		
		//Simply rewrite the string
		resp.setHeader("Content-Type", "text/plain");
		responseStream.write(problemInJSONString.getBytes());
	}

	private String getPostString(HttpServletRequest req) throws IOException {
		return req.getParameter(POST_PARAM_NAME);
	}
}
