package com.pigthinkingtec.framework.databean.message;

import java.util.HashMap;


/**
 * 業務メッセージの集合
 * 
 * @author   yizhou
 * @history
 * 
 */
public class BusinessInformations extends BusinessMessages {
	/**
	 * コンストラクタ
	 */
	public BusinessInformations(){
		super();
	}
	
	/**
	 * informationを追加する
	 * 
	 * @param property キー
	 * @param error エラーオブジェクト
	 */
	protected void add(String property,BusinessError info){
		super.add(property,info);
	}
	
	/**
	 * informationの集合を追加する
	 * 
	 * @param berrors エラーの集合
	 */
	protected void add(BusinessInformations binfos){
		super.add(binfos);
	}
	
	/**
	 * informationの集合を取得する
	 * 
	 * @return エラーの集合
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getInfos(){
		return super.getMessages();
	}
	
	/**
	 * informationが発生している場合はTrue、発生していない場合はFalse
	 * 
	 * @return boolean
	 */
	public boolean hasInfo(){
		return super.hasMessage();
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Infomation;
	}
}
