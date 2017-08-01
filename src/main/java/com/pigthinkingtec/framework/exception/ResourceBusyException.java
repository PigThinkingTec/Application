package com.pigthinkingtec.framework.exception;

/**
 * 「ORA-00054: リソース・ビジー、NOWAITが指定されていました。」
 * 「ORA-30006: リソース・ビジー; WAITタイムアウトの期限に達しました。」
 * に対応する例外クラス
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings("serial")
public class ResourceBusyException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでResourceBusyExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public ResourceBusyException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージとThrowableでResourceBusyExceptionを構成する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public ResourceBusyException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでResourceBusyExceptionを構成する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public ResourceBusyException(Throwable e){
		super(e);
	}
}
