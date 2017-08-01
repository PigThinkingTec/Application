package com.pigthinkingtec.framework.exception;

/**
 * DBアクセス時に発生する例外の基本クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class DatabaseException extends SystemErrException {

	/**
	　* 指定された詳細メッセージでDatabaseExceptionを構築する
	　* 
	　* @param msg 当例外にセットするメッセージ
	　*/
	public DatabaseException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージとThrowableでDatabaseExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public DatabaseException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでDatabaseExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public DatabaseException(Throwable e){
		super(e);
	}
}
