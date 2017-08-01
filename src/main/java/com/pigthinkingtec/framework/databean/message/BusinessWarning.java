package com.pigthinkingtec.framework.databean.message;


/**
 * ワーニングメッセージクラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
public class BusinessWarning extends BusinessMessage {

	/**
	 * メッセージを元にBusinessWarningを生成する
	 * 
	 * @param key メッセージ
	 */
	public BusinessWarning(String key) {
		super(key, null);
	}
	
	/**
	 * メッセージを元にBusinessWarningを生成する
	 * @param key   メッセージ
	 * @param value バインドする変数
	 */
	public BusinessWarning(String key, Object value) {
		super(key, new Object[]{value});
	}
	
	/**
	 * メッセージを元にBusinessWarningを生成する
	 * @param key
	 * @param value
	 * @param value1
	 */
	public BusinessWarning(String key, Object value, Object value1) {
		super(key, new Object[]{value, value1});
	}
	
	/**
	 * メッセージを元にBusinessWarningを生成する
	 * @param key
	 * @param value
	 * @param value1
	 * @param value2
	 */
	public BusinessWarning(String key, Object value, Object value1, Object value2) {
		super(key, new Object[]{value, value1, value2});
	}
	
	/**
	 * メッセージを元にBusinessWarningを生成する
	 * @param key
	 * @param value
	 * @param value1
	 * @param value2
	 * @param value3
	 */
	public BusinessWarning(String key, Object value, Object value1, Object value2, Object value3) {
		super(key, new Object[]{value, value1, value2, value3});
	}
	
	/**
	 * メッセージを元にBusinessWarningを生成する
	 * @param key
	 * @param value
	 */
	public BusinessWarning(String key, Object[] value) {
		super(key, value);
	}
	
	/**
	 * @return   Returns the key.
	 */
	public String getKey() {
		return super.getKey();
	}
	/**
	 * @return   Returns the values.
	 */
	public Object[] getValues() {
		return super.getValues();
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Warning;
	}
}
