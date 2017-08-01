package com.pigthinkingtec.framework.function;

import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.MessageContainer;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 *　共通ファンクションクラスのインターフェイス
 * 
 * @author yizhou
 * @history
 *  
 */
public interface CommonFunctionInterface {

	/**
	 * ユーザ情報の設定
	 * @param user
	 */
	public void setUserData(UserContainer userData);
	
	/**
	 * ジョブタイプの設定
	 * @param jobCls
	 */
	public void setPgmCls(String pgmCls);
	
	/**
	 * 入力パラメータを設定するメソッド
	 * @param inputDataBean
	 */
	public void setInputDataBean(Parameter inputDataBean);
	
	/**
	 * 出力パラメータを取得するメソッド
	 * @return DataBeanInterface
	 */
	public <T extends Parameter> T getOutputDataBean();

	/**
	 * Function派生クラスを実行する
	 * @throws SystemException
	 */
	public void execute() throws SystemException;
	
	/**
	 * メッセージ保持オブジェクトを取得する
	 * @return messageContainer メッセージ保持オブジェクト
	 */
	public MessageContainer getMessageContainer();
}