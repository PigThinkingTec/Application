package com.pigthinkingtec.framework.databean.message;

import com.pigthinkingtec.framework.util.MessageUtil;

/**
 * 業務メッセージ保持クラス
 * 
 * @author   yizhou
 * @history 
 * 
 */
@SuppressWarnings("deprecation")
public abstract class BusinessMessage {
    private String key = null;
    private Object values[] = null;
    private long createdTime;
    //protected boolean resource = true;
    
    /* 監査ログで使用　ログ出力済みかどうかを表す　*/
    private boolean logOutputComplete = false;
    
    public abstract MessageType getMessageType();
    
    /**
     * メッセージを元にBusinessMessageを生成する
     * 
     * @param key
     * @param value
     */
    BusinessMessage(String key){
    	this(key,null);
    }
    
    BusinessMessage(String key,Object value){
    	this(key,new Object[]{value});
    }
    
    BusinessMessage(String key,Object value,Object value1){
    	this(key,new Object[]{value,value1});
    }
    
    BusinessMessage(String key,Object value,Object value1,Object value2){
    	this(key,new Object[]{value,value1,value2});
    }
    
    BusinessMessage(String key,Object value,Object value1,Object value2,Object value3){
    	this(key,new Object[]{value,value1,value2,value3});
    }
    
    BusinessMessage(String key,Object value,Object value1,Object value2,Object value3, Object value4){
    	this(key,new Object[]{value,value1,value2,value3,value4});
    }
    
    BusinessMessage(String key,Object[] value){
    	this.key = key;
    	this.values = value;
    	this.createdTime = System.currentTimeMillis();
    }
    

    /**
     * メッセージのキーを取得する
     * 
     * @return String
     */
    public String getKey(){
		return key;
	}

    
    /**
     * 値を取得する
     * 
     * @return Object[]
     */
    public Object[] getValues(){
		return values;
	}
    
    /**
     * 作成時刻（ミリ秒）を返す
     * @return long
     */
    public long getCreatedTime(){
    	return createdTime;
    }
    
    /**
     * MessageUtilを使用しメッセージを形成し取得する。
     * 
     * @return Message
     */
    @Deprecated
    public String getMessage() {
    	return MessageUtil.getInstance().getMessage(this.getKey(), this.getValues());
    }

    /**
     * 同一のキー、値をもつBusinessErrorオブジェクトに変換する
     * @return
     */
    public BusinessError toBusinessError(){
    	return new BusinessError(this.getKey(),this.getValues());
    }
    
    /**
     * 同一のキー、値をもつBusinessWarningオブジェクトに変換する
     * @return
     */
    public BusinessWarning toBusinessWarning(){
    	return new BusinessWarning(this.getKey(),this.getValues());
    }
    
    /**
     * 同一のキー、値をもつBusinessInformationオブジェクトに変換する
     * @return
     */
    public BusinessInformation toBusinessInformation(){
    	return new BusinessInformation(this.getKey(),this.getValues());
    }

    /**
     * ログ出力済みかどうかを取得する
     * @return
     */
	public boolean isLogOutputComplete() {
		return logOutputComplete;
	}

	/**
	 * ログ出力済み状態をセットする
	 * @param logOutputComplete
	 */
	public void setLogOutputComplete(boolean logOutputComplete) {
		this.logOutputComplete = logOutputComplete;
	}

 
    
}
