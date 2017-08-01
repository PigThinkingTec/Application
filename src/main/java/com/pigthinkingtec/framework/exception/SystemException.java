package com.pigthinkingtec.framework.exception;

/**
 * 当システムにおける例外の基本クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public class SystemException extends Exception {

	/**
	 * 指定された詳細メッセージでCimSystemExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public SystemException(String msg){
		super(msg);
	}
	
	/**
	 * 指定された詳細メッセージとThrowableでCimSystemExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public SystemException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでCimSystemExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public SystemException(Throwable e){
		super(e);
	}
}
