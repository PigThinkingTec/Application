package com.pigthinkingtec.framework.databean;

import com.pigthinkingtec.framework.databean.message.MessageContainer;

/**
 * CommandからActionへの結果を保持するクラス
 * 
 * @author   yizhou
 * @history
 * 
 */
public class Report {
	private DataBeanInterface outputDataBean = null;
	private MessageContainer messageContainer = null;
	/**
	 * Outputデータを取得する
	 * @return DataBeanInterface
	 */
	public final DataBeanInterface getOutputDataBean() {
		return outputDataBean;
	}
	/**
	 * Outputデータを設定する
	 * @param outputDataBean
	 */
	public final void setOutputDataBean(DataBeanInterface outputDataBean) {
		this.outputDataBean = outputDataBean;
	}
	/**
	 * メッセージを取得する
	 * @return
	 */
	public final MessageContainer getMessageContainer() {
		return messageContainer;
	}
	/**
	 * メッセージを設定する
	 * @param messageContainer
	 */
	public final void setMessageContainer(MessageContainer messageContainer) {
		this.messageContainer = messageContainer;
	}
}