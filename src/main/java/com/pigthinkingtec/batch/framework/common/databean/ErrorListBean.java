package com.pigthinkingtec.batch.framework.common.databean;


import com.pigthinkingtec.framework.databean.AbstractDataBean;

/**
 * @ClassName: ErrorListBean
 * @Description: エラーリストデータビーン
 * @author yizhou
 * @history
 */
public class ErrorListBean extends AbstractDataBean {

	/**
	  * @Fields serialVersionUID : 1L
	  */
	private static final long serialVersionUID = 1L;

	/**
	  * @Fields gyouBango : 行番号
	  */
	private String gyoBango = null;

	/**
	  * @Fields dataShoriKubun : データ処理区分
	  */
	private String dataShoriKubun = null;

	/**
	  * @Fields messageCode : メッセージコード
	  */
	private String messageCode = null;
	
	/**
	  * @Fields errorMessage : エラーメッセージ
	  */
	private String errorMessage = null;

	/**
	 * getter method
	 * @return the gyoBango
	 */
	
	public String getGyoBango() {
		return gyoBango;
	}

	/**
	 * setter method
	 * @param gyouBango the gyouBango to set
	 */
	public void setGyoBango(String gyoBango) {
		this.gyoBango = gyoBango;
	}

	/**
	 * getter method
	 * @return the dataShoriKubun
	 */
	
	public String getDataShoriKubun() {
		return dataShoriKubun;
	}

	/**
	 * setter method
	 * @param dataShoriKubun the dataShoriKubun to set
	 */
	public void setDataShoriKubun(String dataShoriKubun) {
		this.dataShoriKubun = dataShoriKubun;
	}

	/**
	 * getter method
	 * @return the messageCode
	 */
	
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * setter method
	 * @param messageCode the messageCode to set
	 */
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/**
	 * getter method
	 * @return the errorMessage
	 */
	
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * setter method
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * データ処理区分（画面表示用）
	 */
	public String getDispDataShoriKubun() {
		//TODO:
//		return CommonConstants.DATA_SYORI_KUBUN_MAP.get(this.dataShoriKubun);
		return "";
	}

}