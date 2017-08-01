package com.pigthinkingtec.framework.databean.message;

import java.util.HashMap;


/**
 * 警告情報リストを保持するクラス
 * 
 * @author   yizhou
 * @history 
 * 
 */
public class BusinessWarnings extends BusinessMessages {
	public BusinessWarnings(){
		super();
	}
	
	/**
	 * 警告情報リストにBussinessWarningを追加する
	 * @param property
	 * @param warn
	 */
	protected void add(String property,BusinessWarning warn){
		super.add(property,warn);
	}
	
	/**
	 * 警告情報リストにBusinessWarningsオブジェクトを追加する
	 * @param bWarns
	 */
	protected void add(BusinessWarnings bWarns){
		super.add(bWarns);
	}
	
	/**
	 * @return   Returns the Warnings.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getWarns(){
		return super.getMessages();
	}
	
	/**
	 * 警告が発生している場合はTrue、発生していない場合はFalse
	 * 
	 * @return boolean
	 */
	public boolean hasWarning(){
		return super.hasMessage();
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Warning;
	}
}
