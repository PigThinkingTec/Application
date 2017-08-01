package com.pigthinkingtec.framework.databean.message;


/**
 * 業務メッセージ保持クラス
 * 
 * @author   yizhou
 * @history 
 * 
 */
public class BusinessInformation extends BusinessMessage{

	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 */
	public BusinessInformation(String key){
		super(key,null);
	}
	
	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 * @param value
	 */
	public BusinessInformation(String key,Object value){
		super(key,new Object[]{value});
	}
	
	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 * @param value
	 * @param value1
	 */
	public BusinessInformation(String key,Object value,Object value1){
		super(key,new Object[]{value,value1});
	}
	
	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 * @param value
	 * @param value1
	 * @param value2
	 */
	public BusinessInformation(String key,Object value,Object value1,Object value2){
		super(key,new Object[]{value,value1,value2});
	}
	
	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 * @param value
	 * @param value1
	 * @param value2
	 * @param value3
	 */
	public BusinessInformation(String key,Object value,Object value1,Object value2,Object value3){
		super(key,new Object[]{value,value1,value2,value3});
	}
	
	/**
	 * メッセージを元にBusinessInformationを生成する
	 * 
	 * @param key
	 * @param value
	 */
	public BusinessInformation(String key,Object[] value){
		super(key,value);
	}
	
	/**
	 * メッセージのキーを取得する
	 * 
	 * @return   Returns the key.
	 */
	public String getKey(){
		return super.getKey();
	}
	
	/**
	 * 値を取得する
	 * 
	 * @return   Returns the values.
	 */
	public Object[] getValues(){
		return super.getValues();
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Infomation;
	}
}
