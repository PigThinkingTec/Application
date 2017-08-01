package com.pigthinkingtec.framework.databean.message;


/**
 * ダイヤログメッセージ保持クラス
 * 
 * @author   yizhou
 * @history 
 * 
 */
public class BusinessDialog extends BusinessMessage {
	
    /**
     * メッセージを元にBusinessBialogを生成する
     * @param url
     * @param key
     */
    public BusinessDialog(String url,String key) {
    	super(key,new Object[]{url});
    }
    
    /**
     * メッセージを元にBusinessDialogを生成する
     * 
     * @param key
     * @param url
     * @param value1
     */
    public BusinessDialog(String url,String key, String value1) {
    	super(key, new Object[]{value1,url});
    }
    
    /**
     * メッセージを元にBusinessDialogを生成する
     * 
     * @param key
     * @param url
     * @param value1
     * @param value2
     */
    public BusinessDialog(String url,String key, String value1,String value2) {
    	super(key, new Object[]{value1, value2,url});
    }

    /**
     * メッセージを元にBusinessDialogを生成する
     * 
     * @param key
     * @param url
     * @param value1
     * @param value2
     * @param value3
     */
    public BusinessDialog(String url,String key, String value1,String value2,String value3) {
    	super(key, new Object[]{value1,value2,value3,url});
    }
    
    /**
     * メッセージのキーを取得する
     * 
	 * @return   Returns the key.
	 */
    public String getKey() {
		return super.getKey();
	}
    
    /**
     * 値を取得する
     * 
	 * @return   Returns the values.
	 */
    public Object[] getValues() {
		return super.getValues();
	}

    @Override
    public MessageType getMessageType() {
        return MessageType.Error;
    }
}
