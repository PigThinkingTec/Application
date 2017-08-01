package com.pigthinkingtec.framework.exception;

/**
 * DBに接続できない、あるいはログインできない場合にスローされる例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class DatabaseUnavailableException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでDatabaseUnavailableExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public DatabaseUnavailableException(String msg){
		super(msg);
	}
	
	/**
	 * 指定された詳細メッセージとThrowableでDatabaseUnavailableExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public DatabaseUnavailableException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでDatabaseUnavailableExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public DatabaseUnavailableException(Throwable e){
		super(e);
	}
}
