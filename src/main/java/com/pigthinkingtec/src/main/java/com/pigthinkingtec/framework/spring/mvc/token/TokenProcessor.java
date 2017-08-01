package com.pigthinkingtec.framework.spring.mvc.token;

import com.google.common.base.Strings;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * トークンの処理を行うクラス
 *
 * @author yizhou
 */
public final class TokenProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenProcessor.class);
    
    private static final String SESSION_TOKEN_KEY = TokenProcessor.class.getName() + "@token";
    
    private static TokenProcessor instance = new TokenProcessor();
    
    public static TokenProcessor getInstance() {
        return instance;
    }
    
    /**
     * 現在のトークンを取得する
     * 
     * @param request
     * @return 
     */
    public String getCurrentToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String)session.getAttribute(SESSION_TOKEN_KEY);
        } else {
            return null;
        }
    }
    
    /**
     * トークンのチェックを行う
     * 
     * @param request
     * @param requestToken
     * @param reset
     * @return 
     */
    public synchronized boolean isTokenValid(HttpServletRequest request, final String requestToken, boolean reset) {
        
        if (Strings.isNullOrEmpty(requestToken)) {
            return false;
        }
        
        final String sessionToken = getCurrentToken(request);
        if (Strings.isNullOrEmpty(sessionToken)) {
            return false;
        }
        boolean result = requestToken.equals(sessionToken);
        
        if (reset) {
            
        }   
        return result;
    }
    
    /**
     * トークンのチェックを行う
     * 
     * @param request
     * @param requestToken
     * @return 
     */
    public boolean isTokenValid(HttpServletRequest request, final String requestToken) {
        return isTokenValid(request, requestToken, true);
    }

    
    /**
     * トークンの存在するかどうかチェックを行う
     * 
     * @param request
     * @return 
     */
    public boolean isTokenExists(HttpServletRequest request) {
    	final String sessionToken = getCurrentToken(request);
    	
    	return !Strings.isNullOrEmpty(sessionToken);
    }
    
    /**
     * トークンのリセットを行う
     * 
     * @param request 
     */
    public void resetToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(SESSION_TOKEN_KEY);
        }
    }
    
    /**
     * トークンを新しいセッションに保存する
     * 
     * @param request
     * @return 
     */
    public synchronized String saveToken(HttpServletRequest request) {
//        final String value = UUID.randomUUID().toString();
    	String value = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
        	String token = getCurrentToken(request);
        	
        	if (Strings.isNullOrEmpty(token)) {
        		//TOKENが存在しない場合,新規作成
        		value = UUID.randomUUID().toString();
        		session.setAttribute(SESSION_TOKEN_KEY, value);
        	}
            
        } else {
            logger.error("didn't start session");
        }
        
        return value;
    }
}

