package com.pigthinkingtec.framework.dbaccess;

/**
 * トランザクションスコープを持っているクラス。
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TransactionScope {
	//トランザクションを設定しない。
	public static final int NON = 0;
	//コマンド内でトランザクション管理を行う。
	public static final int LOCAL = 1;
	//複数コマンドをまたいでトランザクション管理を行う。
	public static final int GLOBAL = 2;
	//デフォルトのトランザクションスコープ
	private static int DEFAULT = 2;
	
	/**
	 * 
	 * @return int
	 */
	public static int getDefaultScope() {
		return DEFAULT;
	}
	
	/**
	 * 
	 * @param defaltScope
	 */
	static void setDefaultScope(int defaltScope) {
		DEFAULT = defaltScope;
	}
	
}
