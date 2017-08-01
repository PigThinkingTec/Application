package com.pigthinkingtec.framework.command;

import com.pigthinkingtec.framework.databean.DataBeanInterface;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.BusinessDialog;
import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessInformation;
import com.pigthinkingtec.framework.databean.message.BusinessWarning;
import com.pigthinkingtec.framework.databean.message.MessageContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.function.CommonFunctionInterface;
import com.pigthinkingtec.framework.log.LogParamOutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Commandクラスの基底
 * 
 * @author  yizhou
 * @history
 * 
 */
public abstract class AbstractCommand implements CommandInterface{
	//ログオブジェクト
	private Log log = LogFactory.getLog(this.getClass().getName());
	//Inputデータ
	protected Order order = null;
	//Outputデータ
	private Report report = null; 
		
	/**
	 * Outputデータを取得する
	 * @return Report
	 */
	public final Report getReport() {
		return report;
	}
	
	/**
	 * Inputデータを設定する
	 * @param order
	 */
	public final void setOrder(Order order) {
		this.order = order;
	}
	
//	/**
//	 * プログラム区分を取得する。
//	 * 
//	 * @return プログラム区分
//	 */
//	public final String getPgmCls() {
//		return order.getPgmCls();
//	}

	/**
	 * MessageContainerのインスタンスを取得するメソッド
	 * @return
	 */
	private MessageContainer getMessageContainer() {
		if(report.getMessageContainer() == null){
			report.setMessageContainer(new MessageContainer());
		}
		return report.getMessageContainer();
	}
	
	/**
	 * Command派生クラスを実行する
	 * @throws SystemException 
	 */
	public final void execute() throws SystemException{
		log.debug("execute start");
	
       	if(getClass() != null){
       		log.info("[Command]=" + getClass().getName() + " start");
       	}
		
		try{
			
			report = new Report();
			beforeInvoke();
			invoke();
			afterInvoke();
			report.setOutputDataBean(order.getInputDataBean());
			
		}catch(Exception e) {
	        //パラメータログ
			if(getDataBean() != null){
		        LogParamOutUtil logParamOutUtil = new LogParamOutUtil("error");
		        logParamOutUtil.invoke(getDataBean(), "[Parameter ]:");
			}
			log.debug("execute end");
			throw new SystemException(e);
			
		}
		
       	if(getClass() != null){
       		log.info("[Command]=" + getClass().getName() + " end");
       	}
		
		log.debug("execute end");
	}

	/**
	 * Function派生クラスを実行する
	 * @param func
	 * @throws SystemException
	 */
	protected final void execFunction(CommonFunctionInterface func) throws SystemException {
		func.setUserData(getUserData());
		//func.setPgmCls(getPgmCls());
		func.execute();
		//コマンドは勝手にFunctionのメッセージを引き継がないこととする。2006/05/29 kshiobara
		//this.addMessageContainer(func.getMessageContainer());
	}
	
	/*(non-javadoc)
	 * Command派生クラスを実行する前に行う処理がある場合は、Overwrideすること 
	 */
	protected void beforeInvoke() throws SystemException{
	}
	
	/*(non-javadoc)
	 * Command派生クラスを実行した後に行う処理がある場合は、Overwrideすること
	 */
	protected void afterInvoke() throws SystemException{
		
	}
	
	/**
	 * 派生クラスがオーバーライドする必要のある主処理を行うメソッド
	 * 
	 */
	protected abstract void invoke() throws SystemException;
	
	
	

	
	/**
	 * Commandクラス内でDialogErrorが発生した場合、状態を保存する
	 * 
	 * @param key
	 * @param error
	 */
	protected final void saveDialog(String key,BusinessDialog dialog){
		getMessageContainer().saveDialog(key,dialog);
	}
	
	protected final void saveDialog(BusinessDialog dialog) {
		saveDialog(null, dialog);
	}
	
	
	/**
	 * Onlineで画面上部に出力するエラーの登録
	 * 
	 * @param error
	 */
	protected final void saveError(BusinessError error) {
		saveError(null, error);
	}
	
	/**
	 * Commandクラス内でApplicationErrorが発生した場合、状態を保存する
	 * 
	 * @param key
	 * @param error
	 */
	protected final void saveError(String key, BusinessError error){
		getMessageContainer().saveError(key, error);
	}
	
	/**
	 * Onlineで画面上部に出力するワーニングの登録
	 * 
	 * @param warning
	 */
	protected final void saveWarning(BusinessWarning warning) {
		saveWarning(null, warning);
	}
	
	/**
	 * Commandクラス内でApplicationWarningが発生した場合、状態を保存する
	 * 
	 * @param key
	 * @param warn
	 */
	protected final void saveWarning(String key, BusinessWarning warn){
		getMessageContainer().saveWarning(key, warn);
	}

	/**
	 * Onlineで画面上部に出力するインフォメーションの登録
	 * 
	 * @param information
	 */
	protected final void saveInformation(BusinessInformation information){
		saveInformation(null, information);
	}
	
	/**
	 * Commandクラス内でApplicationInformationが発生した場合、状態を保存する
	 * 
	 * @param key
	 * @param information
	 */
	protected final void saveInformation(String key, BusinessInformation information){
		getMessageContainer().saveInformation(key, information);
	}
	
	/**
	 * DataBeanを取得する
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends DataBeanInterface> T getDataBean() {
		return (T) order.getInputDataBean();
	}
	
	/**
	 * パラメータデータを取得する
	 * 
	 * @return
	 */
	protected final Object getArgumentData() {
		return order.getArgumentData();
	}

	/**
	 * パラメータデータを設定する
	 * 
	 * @return
	 */
	protected final void setArgumentData(Object argumentData) {
		order.setArgumentData(argumentData);
	}
	
	/**
	 * ユーザ固有情報を取得する。
	 * @return userData
	 */
	@SuppressWarnings("unchecked")
	protected final <T extends UserContainer> T getUserData() {
		return (T) order.getLoginUser();
	}
	
//	/**
//	 * AD認証フラグを取得する。
//	 * @return userData
//	 */
//	protected final String getAdAuth() {
//		return order.getAdAuth();
//	}
	
	/**
	 * ログオブジェクトを取得する。
	 * @return
	 */
	protected final Log getLog() {
		return log;
	}
	
	/**
	 * 現在保持しているMessageContainerに、別のMessageContainerの内容を追加する。
	 * @param msg 追加するMessageContainerクラス
	 */
	protected final void addMessageContainer(MessageContainer msg){
		if(msg == null)return;
		getMessageContainer().add(msg);
	}
        
}