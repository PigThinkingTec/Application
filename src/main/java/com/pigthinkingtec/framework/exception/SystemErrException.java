package com.pigthinkingtec.framework.exception;

/**
 * 当システムで回復可能と考えられる例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class SystemErrException extends SystemException {

	/**
	 * 指定された詳細メッセージとThrowableでCimSystemErrExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public SystemErrException(String msg, Throwable e) {
		super(msg, e);
		
	}

	/**
	 * 指定された詳細メッセージでCimSystemErrExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public SystemErrException(String msg) {
		super(msg);
		
	}

	/**
	 * 指定されたThrowableでCimSystemErrExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public SystemErrException(Throwable e) {
		super(e);
	}

}
