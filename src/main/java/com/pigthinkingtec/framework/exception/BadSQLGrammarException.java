package com.pigthinkingtec.framework.exception;

/**
 * 誤った構文のSQLを発行した場合にスローされる例外クラス
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings("serial")
public class BadSQLGrammarException extends DatabaseException {

	/**
	 * 指定された詳細メッセージでBadSQLGrammarExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 */
	public BadSQLGrammarException(String msg){
		super(msg);
	}

	/**
	 * 指定された詳細メッセージとThrowableでBadSQLGrammarExceptionを構築する
	 * 
	 * @param msg 当例外にセットするメッセージ
	 * @param e 当例外にセットするexception object
	 */
	public BadSQLGrammarException(String msg , Throwable e){
		super(msg,e);
	}
	
	/**
	 * 指定されたThrowableでBadSQLGrammarExceptionを構築する
	 * 
	 * @param e 当例外にセットするexception object
	 */
	public BadSQLGrammarException(Throwable e){
		super(e);
	}
}
