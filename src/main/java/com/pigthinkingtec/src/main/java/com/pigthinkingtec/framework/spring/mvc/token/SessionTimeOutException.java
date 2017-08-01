package com.pigthinkingtec.framework.spring.mvc.token;

import com.pigthinkingtec.framework.exception.SystemException;

/**
 * Session Timeout Errorが発生したことを通知する例外クラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class SessionTimeOutException extends SystemException {

    public SessionTimeOutException(String msg) {
        super(msg);
    }
    
}