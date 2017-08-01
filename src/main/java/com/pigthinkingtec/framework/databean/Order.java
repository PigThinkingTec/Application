package com.pigthinkingtec.framework.databean;


/**
 * ActionからCommandへのInputを纏めるクラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
public class Order {
	private DataBeanInterface inputDataBean = null;
	private UserContainer loginUser = null;
	private int transactionScope = 0;
	private Object argumentData = null;
	
	//バッチから起動するかどうかを示すFlg
	private boolean isBatch = false;
		
	//監査LogにLogを出力する必要であるかを制御するFlg
	private boolean isAuditLogOutput = true;
	
	/**
	 * バッチから起動するかどうかを示すFlgを取得
	 * @return
	 */
	public boolean isBatch() {
		return isBatch;
	}
	
	/**
	 * バッチから起動するかどうかを示すFlgを設定する
	 * @param isBatch
	 */
	public void setBatchFlg(boolean isBatch) {
		this.isBatch = isBatch;
	}
	
	/**
	 * 監査LogにLogを出力する必要であるかを制御するFlgを取得
	 * @return
	 */
	public boolean isAuditLogOutput() {
		return isAuditLogOutput;
	}
	
	/**
	 * 監査LogにLogを出力する必要であるかを制御するFlgを設定する
	 * @param isBatch
	 */
	public void setAuditLogOutputFlg(boolean isAuditLogOutput) {
		this.isAuditLogOutput = isAuditLogOutput;
	}
	
	/**
	 * Inputデータを取得する
	 * @return DataBeanInterface
	 */
	public DataBeanInterface getInputDataBean() {
		return inputDataBean;
	}
	
	/**
	 * Inputデータを設定する
	 * @param inputDataBean
	 */
	public void setInputDataBean(DataBeanInterface inputDataBean) {
		this.inputDataBean = inputDataBean;
	}
	
	/**
	 * ログオン情報を取得する
	 * @return UserContainer
	 */
	public UserContainer getLoginUser() {
		return loginUser;
	}
	
	/**
	 * ログオン情報を設定する
	 * @param loginUser
	 */
	public void setLoginUser(UserContainer loginUser) {
		this.loginUser = loginUser;
	}
	
	/**
	 * トランザクションスコープを取得する
	 * @return int
	 */
	public int getTransactionScope() {
		return transactionScope;
	}
	
	/**
	 * トランザクションスコープを設定する
	 * @param transactionScope
	 */
	public void setTransactionScope(int transactionScope) {
		this.transactionScope = transactionScope;
	}

	/**
	 * Parameterデータを取得する
	 * @return argumentData
	 */
	public Object getArgumentData() {
		return argumentData;
	}

	/**
	 * Parameterデータを設定する
	 * @param argumentData
	 */
	public void setArgumentData(Object argumentData) {
		this.argumentData = argumentData;
	}
}
