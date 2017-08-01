package com.pigthinkingtec.framework.databean.message;

import java.util.HashMap;


/**
 * 業務エラーメッセージの集合
 * 
 * @author  yizhou
 * @history 
 * 
 */
public class BusinessErrors extends BusinessMessages {
	/**
	 * コンストラクタ
	 */
	public BusinessErrors() {
		super();
	}
	
	/**
	 * エラーを追加する
	 * 
	 * @param property キー
	 * @param error エラーオブジェクト
	 */
	protected void add(String property, BusinessError error) {
		super.add(property, error);
	}
	
	/**
	 * エラーの集合を追加する
	 * 
	 * @param berrors エラーの集合
	 */
	protected void add(BusinessErrors berrors) {
		super.add(berrors);
	}
	
	/**
	 * エラーの集合を取得する
	 * 
	 * @return エラーの集合
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getErrors() {
		return super.getMessages();
	}
	
	/**
	 * エラーがあるかチェックする
	 * 
	 * @return エラーが発生している場合はTrue、発生していない場合はFalse
	 */
	public boolean hasError() {
		return super.hasMessage();
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Error;
	}
}
