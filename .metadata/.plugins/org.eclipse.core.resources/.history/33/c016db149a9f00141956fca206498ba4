package edu.rits.ma.webapp.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyHttpServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String HTTP_RESPONSE_PREFIX = "<html><body>";
	private static final String HTTP_RESPONSE_POSTFIX = "</body></html>";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = req.getParameterNames();
		
		//Check for the second parameter
		boolean hasMoreThanTwoParam = paramNames.hasMoreElements();
		if (hasMoreThanTwoParam) {
			String secondParamName = paramNames.nextElement();
			String secondParamValue = req.getParameterValues(secondParamName)[0];
			StringBuilder responseStr = new StringBuilder();
			responseStr.append(HTTP_RESPONSE_PREFIX);
			responseStr.append(secondParamValue);			
			responseStr.append(HTTP_RESPONSE_POSTFIX);
			resp.getWriter().write(responseStr.toString());
		}
		else {			
			String fileName = "../webapps/servlet-examples/images/tomcat.gif";
			FileInputStream inputStream = new FileInputStream(fileName);
			ByteArrayOutputStream streamReader = new ByteArrayOutputStream();
			
			int read;
			while((read = inputStream.read()) != -1) {
				streamReader.write(read);
			}
			
			inputStream.close();
			
			byte[] imageBytes = streamReader.toByteArray();
			
			streamReader.close();
			
			resp.setHeader("Content-Type", "image/gif");
			
			//Using outputStream to write binary data, printWriter to write pure text data
			ServletOutputStream outputStream = resp.getOutputStream();
			outputStream.write(imageBytes);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*TODO
		 * Return to the client exactly the request header
		 */
		ServletInputStream inputStream = req.getInputStream();
		
		/*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int read;
		while((read = inputStream.read()) != 1) {
			outputStream.write(read);
		}
		byte[] reqData = outputStream.toByteArray();*/
		
		byte[] reqData = new byte[1024];
		int size = inputStream.read(reqData);
		ServletOutputStream responseStream = resp.getOutputStream();
		resp.setHeader("Content-Type", "text/plain");
		
		
		responseStream.write(reqData, 0, size);
	}
}
