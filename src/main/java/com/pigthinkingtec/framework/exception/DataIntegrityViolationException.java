package com.pigthinkingtec.framework.exception;

/**
 * テーブルで定義されている制約に違反した場合にスローされる例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class DataIntegrityViolationException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでDataIntegrityViolationExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public DataIntegrityViolationException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージとThrowableでDataIntegrityViolationExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public DataIntegrityViolationException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでDataIntegrityViolationExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public DataIntegrityViolationException(Throwable e){
		super(e);
	}
}
