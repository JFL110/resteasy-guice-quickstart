package org.jfl110.quickstart;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.io.Files;

@SuppressWarnings("serial")
public class FileServlet extends HttpServlet {

	private final Supplier<String> pageContent;

	public static HttpServlet ofCachedFile(String path) {
		return new FileServlet(path, true);
	}

	public static HttpServlet ofUncachedFile(String path) {
		return new FileServlet(path, false);
	}

	private FileServlet(String path, boolean cache) {
		this.pageContent = cache ? Suppliers.memoize(() -> readFromContextAsString(path)) : () -> readFromContextAsString(path);
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print(pageContent.get());
	}

	private String readFromContextAsString(String path) {
		try {
			return Files.asCharSource(new File(getServletContext().getRealPath(path)), Charsets.UTF_8).read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}