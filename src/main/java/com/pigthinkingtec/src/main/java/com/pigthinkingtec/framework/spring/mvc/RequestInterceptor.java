package com.pigthinkingtec.framework.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlleでの処理に対して、共通の前処理/後処理をＡＯＰするクラス
 * 
 * @author yizhou
 */
public class RequestInterceptor implements HandlerInterceptor {
	
	private final static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
	
	private static String[] EXCEPT_PATHS_PREFIX = null;
	
	public RequestInterceptor(){
	}
	
	public RequestInterceptor(String exceptPaths){
		EXCEPT_PATHS_PREFIX = exceptPaths.split(",");
	}
	
	/**
	 * Controllerの共通前後処理
	 * 
	 * @param hsr
	 * @param hsr1
	 * @param o
	 * @return
	 * @throws Exception 
	 */
	@Override
	public boolean preHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o) throws Exception {
		DateTime startTime = new DateTime();
		
		/*
		//以下は、リクエストパラメータを全て出力する場合の処理
		Enumeration enu = hsr.getParameterNames();
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (;enu.hasMoreElements();) {
			String key = (String)enu.nextElement();
			sb.append(key);
			sb.append("='");
			String[] values = hsr.getParameterValues(key);
			sb.append(values[0]);
			for (int i = 1; i < values.length; i++) {
				sb.append(",");
				sb.append(values[i]);
			}
			sb.append("' ");
		}
		sb.append("}");
		*/
		//logger.info("■■■ url='{}{}' parameters: {} 処理を開始します。", hsr.getContextPath(), hsr.getServletPath(), sb.toString());
		logger.info("■■■ url='{}{}' 処理を開始します。", hsr.getContextPath(), hsr.getServletPath());
		hsr.setAttribute("RequestInterceptor.startTime", startTime);
		
		/* pgmIdはコントローラで各自設定のため、コメントアウト　2015/6/2　三浦
		//pgmIdをUserContainerに設定
		HttpSession session = hsr.getSession(false);
		if (session != null) {
			UserContainer user = (UserContainer)session.getAttribute(SystemConstant.USER_CONTAINER);
			if (user != null) {
				String pgmId = getPgmId(hsr);
				user.setPgmId(pgmId);
			}
		}
		*/
		return true;
	}

	/**
	 * Controllerの共通後処理
	 * 
	 * @param hsr
	 * @param hsr1
	 * @param o
	 * @param mav
	 * @throws Exception 
	 */
	@Override
	public void postHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, ModelAndView mav) throws Exception {
		DateTime endTime = new DateTime();
		DateTime startTime = (DateTime)hsr.getAttribute("RequestInterceptor.startTime");
		Duration duration = new Duration(startTime, endTime);
		double time_ms = duration.getMillis();
		HttpSession session = hsr.getSession(false);
		if (session != null) {
			if (mav != null && isSavePath(mav.getViewName())) {
				session.setAttribute("lastView", mav);
			}
		}
		logger.info("■■■ url='{}{}' 処理結果=OK 処理時間={}s", hsr.getContextPath(), hsr.getServletPath(), time_ms/1000);
	}

	/**
	 * 
	 * @param hsr
	 * @param hsr1
	 * @param o
	 * @param excption
	 * @throws Exception 
	 */
	@Override
	public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excption) throws Exception {
		
	}
	
	/**
	 *PgmIdを取得するメソッド
	 * 
	 * @param request
	 * @return 
	 */
	protected String getPgmId(HttpServletRequest request) {
		
		String[] servletPaths = request.getServletPath().split("/");
		String pgmId = null;
		int length = servletPaths.length;

		switch (length) {
		case 0:
			pgmId = "";
			break;
		case 1:
			pgmId = servletPaths[0];
			break;
		case 2:
			pgmId = servletPaths[1];
			break;
		case 3:
			pgmId = servletPaths[2];
			break;
		default:
			pgmId = servletPaths[3];
		}
		
		return pgmId;
	}
	
	/**
	 * 前画面の情報（ModelAndView）を保存する必要があるかどうかを判定する
	 * @param path
	 * @return
	 */
	private boolean isSavePath(String path){
		
		if (EXCEPT_PATHS_PREFIX == null){
			return true;
		}
		
		for (String expceptPath : EXCEPT_PATHS_PREFIX) {
			if (path.startsWith(expceptPath)) {
				return false;
			}
		}
		
		return true;
	}
}
