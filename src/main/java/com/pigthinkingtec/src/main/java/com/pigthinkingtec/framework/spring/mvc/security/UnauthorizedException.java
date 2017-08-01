package com.pigthinkingtec.framework.spring.mvc.security;

import com.pigthinkingtec.framework.exception.SystemErrException;

/**
 * 機能の実行権限がない場合に発生する例外クラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class UnauthorizedException extends SystemErrException {
	
	public UnauthorizedException(String msg) {
		super(msg);
	}

}
