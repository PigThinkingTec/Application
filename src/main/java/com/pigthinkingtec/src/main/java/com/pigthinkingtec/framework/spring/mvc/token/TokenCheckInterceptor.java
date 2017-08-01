package com.pigthinkingtec.framework.spring.mvc.token;

import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Token Check 処理をおこなう インターセプター
 *
 * @author yizhou
 */
public class TokenCheckInterceptor implements HandlerInterceptor {

    /**
     * トークンのチェックを行う
     * 
     * @param request
     * @param response
     * @param obj
     * @return
     * @throws Exception 
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        if (!(obj instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod)obj;
        
        if (method.getBeanType().isAnnotationPresent(NotCheckToken.class) ||
                method.getMethodAnnotation(NotCheckToken.class) != null) {
            return true;
        }
        String token = request.getParameter("token");
        
        if (!TokenProcessor.getInstance().isTokenExists(request)) {
        	throw new SessionTimeOutException("session timeout error.");
        }
        
        if (!TokenProcessor.getInstance().isTokenValid(request, token)) {
            throw new TokenErrorException("token check error.");
        }
        return true;
    }

    /**
     * トークンを作成して保存する
     * 
     * @param request
     * @param response
     * @param obj
     * @param model
     * @throws Exception 
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView model) throws Exception {
        
        if (!(obj instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod method = (HandlerMethod)obj;
        
        if (method.getBeanType().isAnnotationPresent(NotSaveToken.class) ||
                method.getMethodAnnotation(NotSaveToken.class) != null) {
            return;
        }
        
        //Request単位ではなく、Session単位でToken情報を管理するために、
        //下記の処理は廃止
//        TokenProcessor.getInstance().resetToken(request);
        TokenProcessor.getInstance().saveToken(request);
        String token = TokenProcessor.getInstance().getCurrentToken(request);
        if ((!Strings.isNullOrEmpty(token)) && (model != null)) {
//        if (!Strings.isNullOrEmpty(token)) {//QA128対応
            model.getModel().put("token", token);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
        
    }
    
}
