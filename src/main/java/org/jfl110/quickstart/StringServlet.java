package org.jfl110.quickstart;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StringServlet extends HttpServlet {

	private final String content;

	public static HttpServlet of(String content) {
		return new StringServlet(content);
	}

	private StringServlet(String content) {
		this.content = content;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print(content);
	}
}