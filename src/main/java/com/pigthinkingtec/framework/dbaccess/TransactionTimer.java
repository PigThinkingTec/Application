package com.pigthinkingtec.framework.dbaccess;

import java.util.TimerTask;

/**
 * トランザクションがタイムアウトしているかどうかをチェックするタイマータスク。
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TransactionTimer extends TimerTask {

	private Transaction trx;
	
	/**
	 * コンストラクタ
	 * 
	 * @param trx
	 */
	public TransactionTimer(Transaction trx) {
		this.trx = trx;
	}
	
	/**
	 * タイマーから呼ばれる実行メソッド。
	 * 
	 */
	public void run() {
		trx.checkTimeout();
	}

}
