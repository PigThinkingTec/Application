package com.pigthinkingtec.framework.exception;

/**
 * トランザクションが無効になっている際に発生する例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class InvalidTransactionException extends DatabaseException {

	/**
	 * 指定された詳細メッセージとThrowableでInvalidTransactionExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public InvalidTransactionException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * 指定された詳細メッセージでInvalidTransactionExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public InvalidTransactionException(String msg) {
		super(msg);
	}

	/**
	 * 指定されたThrowableでInvalidTransactionExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public InvalidTransactionException(Throwable e) {
		super(e);
	}

}
