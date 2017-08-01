package com.pigthinkingtec.framework.exception;

/**
 * デッドロックが発生した場合にスローされる例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class DeadlockException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでDeadlockExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public DeadlockException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージとThrowableでDeadlockExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public DeadlockException(String msg , Throwable e){
		super(msg,e);
	}

	/**
	 * 指定されたThrowableでDeadlockExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public DeadlockException(Throwable e){
		super(e);
	}
}
