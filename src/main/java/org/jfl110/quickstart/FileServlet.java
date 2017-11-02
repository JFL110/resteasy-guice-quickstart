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

/**
 * HttpServlet that prints the contents of a file to the response.
 *
 * @author JFL110
 */
@SuppressWarnings("serial")
public class FileServlet extends HttpServlet {

	private final Supplier<String> pageContent;

	/**
	 * Creates a servlet that prints the contents of a file to the response. The
	 * file is read once and the result is cached.
	 */
	public static HttpServlet ofCachedFile(String path) {
		return new FileServlet(path, true);
	}

	/**
	 * Creates a servlet that prints the contents of a file to the response. The
	 * file is read on each request.
	 */
	public static HttpServlet ofUncachedFile(String path) {
		return new FileServlet(path, false);
	}

	private FileServlet(String path, boolean cache) {
		this.pageContent = cache ? Suppliers.memoize(() -> readFromContextAsString(path))
				: () -> readFromContextAsString(path);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().print(pageContent.get());
	}

	private String readFromContextAsString(String path) {
		String realPath = getServletContext().getRealPath(path);
		if (realPath == null) {
			throw new RuntimeException("File not found [" + path + "]");
		}
		try {
			return Files.asCharSource(new File(realPath), Charsets.UTF_8).read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}