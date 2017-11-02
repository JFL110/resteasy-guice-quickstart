package org.jfl110.quickstart;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Supplier;

/**
 * A HttpServlet that just writes a string to the response.
 *
 * @author JFL110
 */
@SuppressWarnings("serial")
public class StringServlet extends HttpServlet {

	private final Supplier<String> content;

	/**
	 * Creates a StringServlet from a String supplier. The supplier will be
	 * invoked on every request.
	 */
	public static HttpServlet of(Supplier<String> content) {
		return new StringServlet(content);
	}

	/**
	 * Creates a HttpServlet that writes the given String to the response.
	 */
	public static HttpServlet of(String content) {
		return new StringServlet(() -> content);
	}

	private StringServlet(Supplier<String> content) {
		this.content = content;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print(content.get());
	}
}