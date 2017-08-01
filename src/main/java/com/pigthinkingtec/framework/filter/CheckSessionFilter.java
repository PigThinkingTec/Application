package com.pigthinkingtec.framework.filter;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sessionの存在状態を確認するフィルター
 *
 * @author yizhou
 */
public class CheckSessionFilter implements Filter {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CheckSessionFilter.class);
	
	private static String[] THROW_PATHS = null;
	private static String ERROR_PAGE = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String throwPaths = filterConfig.getInitParameter("throwPaths");
	if (throwPaths != null) {
			THROW_PATHS = throwPaths.split(",");
	}
		ERROR_PAGE = filterConfig.getInitParameter("errorPage");
	}

	/**
	 * Sessionの存在チェックを行うフィルターメソッド
	 *
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		String servletPath = ((HttpServletRequest)request).getServletPath();
		
		if (Strings.isNullOrEmpty(servletPath) || servletPath.contains("resources")) {
		} else {
			HttpSession session = ((HttpServletRequest)request).getSession(false);
			if (!isThrowPath(servletPath) && session == null) {
				((HttpServletRequest)request).getRequestDispatcher(ERROR_PAGE).forward(request, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}
	
	/**
	 * ThrowしてよいPathかどうかを判定するメソッド
	 * 
	 * @param servletPath
	 * @return Throw可能な場合はTrue、そうでなければFalse
	 */
	private boolean isThrowPath(String servletPath) {

		if(Strings.isNullOrEmpty(servletPath) || servletPath.contains(".jsp")) {
			return true;
		}

		boolean flg = false;
		if (THROW_PATHS != null) {
			for (String THROW_PATHS1 : THROW_PATHS) {
				if (servletPath.equals(THROW_PATHS1)) {
					flg = true;
					break;
				}
			}
		}
		return flg;
	}
	
}
