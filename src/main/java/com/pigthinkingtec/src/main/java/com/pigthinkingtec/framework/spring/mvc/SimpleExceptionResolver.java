package com.pigthinkingtec.framework.spring.mvc;

import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessErrors;
import com.pigthinkingtec.framework.databean.message.MessageType;
import com.pigthinkingtec.framework.spring.mvc.security.UnauthorizedException;
import com.pigthinkingtec.framework.spring.mvc.token.SessionTimeOutException;
import com.pigthinkingtec.framework.spring.mvc.token.TokenErrorException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exception Resolver
 *
 * @author yizhou
 */
public class SimpleExceptionResolver implements HandlerExceptionResolver {
    
    private final static Logger logger = LoggerFactory.getLogger(SimpleExceptionResolver.class);
    
    private final static String PAGE_ERROR_TOKEN = PropertiesUtil.getProperty("page.error.token");
    
    private final static String PAGE_ERROR_SYSTEM = PropertiesUtil.getProperty("page.error.system");
    
    private final static String PAGE_ERROR_SESSION_TIMEOUT = PropertiesUtil.getProperty("page.error.session.timeout");

    @Override
    @SuppressWarnings("unchecked")
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, 
            Object o, Exception exception) {
        
        ModelAndView model = new ModelAndView();
        if (exception instanceof TokenErrorException) {
        	logger.info("トークンエラーが発生しました");
            model.addObject("message", "トークンエラーが発生しました");
            model.addObject("exception", exception);
            model.setViewName(PAGE_ERROR_TOKEN);
        } else if (exception instanceof UnauthorizedException) {
        	logger.info("権限エラーが発生しました");
            model = (ModelAndView)request.getSession(false).getAttribute("lastView");
            BusinessError error = new BusinessError("function.authorize.error");
			BusinessErrors errors = new BusinessErrors();
			errors.getMessages().put(error.hashCode(), error);
			model.addObject(MessageType.Error.toString(), errors);
        } else if (exception instanceof SessionTimeOutException) {
        	logger.info("セッションタイムアウトエラーが発生しました");
            model.addObject("message", "セッションタイムアウトエラーが発生しました");
            model.addObject("exception", exception);
            model.setViewName(PAGE_ERROR_SESSION_TIMEOUT);
        } else {
        	logger.error(exception.toString(), exception);
            model.addObject("message", "システムエラーが発生しました");
            model.addObject("exception", exception);
            model.setViewName(PAGE_ERROR_SYSTEM);
        }
        return model;
    }
    
}
