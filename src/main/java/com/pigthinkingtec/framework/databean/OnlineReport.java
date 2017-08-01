package com.pigthinkingtec.framework.databean;

/**
 * オンライン用のOutputデータ格納クラス
 * 
 * @author  yizhou
 * @version 
 * @history 
 * 
 */
public class OnlineReport extends Report {
	//処理結果のステータス
	private String status;

	/**
	 * 処理結果を取得する。
	 * @return String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 処理結果をセットする。
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
