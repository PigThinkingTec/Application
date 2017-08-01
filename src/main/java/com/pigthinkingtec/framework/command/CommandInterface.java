package com.pigthinkingtec.framework.command;

import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 * コマンドクラスのインターフェース
 * 
 * @author yizhou
 * @history 
 * 
 */
public interface CommandInterface {

	/**
	 * コマンドクラスの実際の処理が行われるメソッド
	 * 
	 * @throws SystemException
	 */
	public void execute() throws SystemException;
	
	/**
	 * Inputデータを設定する
	 * 
	 * @param order
	 */
	public void setOrder(Order order);
	
	/**
	 * Outputデータを取得する
	 * 
	 * @return Report
	 */
	public Report getReport();
}
