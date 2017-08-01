package com.pigthinkingtec.framework.service;

import com.pigthinkingtec.framework.databean.OnlineOrder;
import com.pigthinkingtec.framework.databean.OnlineReport;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.Report;

/**
 * Online用のService基底クラス
 * 
 * @author yizhou
 * @history 
 * 
 */
public abstract class AbstractOnlineService extends AbstractService{
	//処理結果ステータス
	private String status = null;
	//セッションID
	private String sessionId = null;
	
	/**
	 * Service実行結果のステータスを設定する
	 * @param status
	 */
	protected void setStatus(String status){
		this.status = status; 
	}
	
	/**
	 * Service実行結果のステータスを取得する
	 * @return
	 */
	protected String getStatus(){
		return status;
	}
	
	/**
	 * ユーザ固有のIDを設定する
	 * @param id
	 */
	protected String getSessionId(){
		return this.sessionId;
	}
	
	
	/**
	 * オンライン用の処理結果を取得する。
	 * 
	 */
	public Report getReport() {
		report = new OnlineReport();

		if (getOutputDataBean() == null) {
			setOutputDataBean(getInputDataBean());
		}
		report.setOutputDataBean(getOutputDataBean());
		((OnlineReport)report).setStatus(getStatus());
		report.setMessageContainer(getMessageContainer());
		
		return report;
	}
	
	/**
	 * オンライン用の入力データをセットする。
	 * 
	 */
	public void setOrder(Order order) {
		super.setOrder(order);
		//セッションIDを取得する。
		this.sessionId = ((OnlineOrder)order).getSessionId();
	}
}