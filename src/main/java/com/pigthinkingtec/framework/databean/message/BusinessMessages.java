package com.pigthinkingtec.framework.databean.message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * 各BusinessMessages クラスの基底クラス
 * 
 * @author  yizhou
 * @version 
 * @history 
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BusinessMessages implements Iterable<BusinessMessage> {
	private HashMap messages = null;
	
		abstract public MessageType getMessageType();
		
	
	BusinessMessages(){
		messages = new LinkedHashMap();
	}
	
	void add(String property,BusinessMessage message){
		messages.put(property,message);
	}
	
	void add(BusinessMessages bmessages){
		HashMap map = null;
		String key = null;
		
		map = bmessages.getMessages();
		Iterator ir = map.keySet().iterator();
		while(ir.hasNext()){
			key = (String)ir.next();
			messages.put(key,map.get(key));
		}
	}
	
	/**
	 * @return   Returns the errors.
	 */
	public HashMap getMessages(){
		return messages;
	}
	
	/**
	 * エラーが発生している場合はTrue、発生していない場合はFalse
	 * 
	 * @return boolean
	 */
	public boolean hasMessage(){
		return !messages.isEmpty();
	}
	
	/**
	 * 格納されているBusinessMessageオブジェクトのメッセージを取得し、
	 * String[]にして取得するするメソッド
	 * 
	 * @return メッセージ配列
	 */
		@Deprecated
	public String[] getMessageArray() {
		if (messages == null || messages.size() == 0) {
			return null;
		}
		String[] messageArray = new String[messages.size()];
		Iterator iter = messages.keySet().iterator();
		int index = 0;
		while(iter.hasNext()) {
			Object obj = iter.next();
			BusinessMessage message = (BusinessMessage)messages.get(obj);
			messageArray[index] = message.getMessage();
			index++;
		}
		return messageArray;
	}

	@Override
	public Iterator<BusinessMessage> iterator() {
		return messages.values().iterator();
	}
}
