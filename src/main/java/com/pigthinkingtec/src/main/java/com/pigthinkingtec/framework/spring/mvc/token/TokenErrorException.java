package com.pigthinkingtec.framework.spring.mvc.token;

import com.pigthinkingtec.framework.exception.SystemException;

/**
 * Token Check Errorが発生したことを通知する例外クラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class TokenErrorException extends SystemException {

	public TokenErrorException(String msg) {
		super(msg);
	}
	
}
