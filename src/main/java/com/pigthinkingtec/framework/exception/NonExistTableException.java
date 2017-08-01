package com.pigthinkingtec.framework.exception;

/**
 * 存在しないテーブルにアクセスしようとした場合にスローされる例外クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class NonExistTableException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでNonExistTableExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public NonExistTableException(String msg){
		super(msg);
	}
	
	/**
	 * 指定された詳細メッセージとThrowableでNonExistTableExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public NonExistTableException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでNonExistTableExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public NonExistTableException(Throwable e){
		super(e);
	}
}
