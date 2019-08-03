import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("filter init method");
		String value = filterConfig.getInitParameter("filterParam");
		System.out.println("filter config param: " + value);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("filter doFilter method");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		if (session.getAttribute("userName") == null) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.sendRedirect("../index.html");
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		System.out.println("filter destroy method");
	}

}
