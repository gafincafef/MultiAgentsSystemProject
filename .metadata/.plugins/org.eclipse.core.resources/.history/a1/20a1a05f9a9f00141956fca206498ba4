package edu.rits.ma.webapp.servlet;
import javax.servlet.*;
import java.io.IOException;

public class MyGenericServlet extends GenericServlet {
	
	private static final long serialVersionUID = 1L;
	private String corpName = "abc";

	//read the field corpName value when initialized
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		corpName = servletConfig.getInitParameter("corpName");
	}

	//Simply return a string "It works"
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		String res = "<html><body>" + corpName + ", it works</body></html>";
		response.getWriter().write(res);
	}
}

