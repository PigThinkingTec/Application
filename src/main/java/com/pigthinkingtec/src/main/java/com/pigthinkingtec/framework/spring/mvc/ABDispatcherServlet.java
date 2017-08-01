package com.pigthinkingtec.framework.spring.mvc;

import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.taglib.TaglibFwFormatManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * SpringMVCのDispatcherServletを継承したクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class ABDispatcherServlet extends DispatcherServlet {

	private static final Logger logger = LoggerFactory.getLogger(ABDispatcherServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config); 
		try {
			//TaglibFwFormatManagerの初期化処理を呼び出す。
			TaglibFwFormatManager.init();
		} catch (SystemException ex) {
			logger.error("initialize error", ex);
			throw new ServletException("initialize error", ex);
		}
	}
	
	
}
