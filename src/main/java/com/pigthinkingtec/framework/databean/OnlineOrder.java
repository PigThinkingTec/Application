package com.pigthinkingtec.framework.databean;

/**
 * オンライン用のInputデータ格納クラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
public class OnlineOrder extends Order {
	//セッションID
	private String sessionId = null;

	/**
	 * コンストラクタ
	 *
	 */
	public OnlineOrder() {
		super();
	}

	/**
	 * セッションIDを取得する
	 * @return String
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * セッションIDを設定する
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
