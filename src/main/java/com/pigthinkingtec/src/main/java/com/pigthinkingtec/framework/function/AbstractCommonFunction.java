package com.pigthinkingtec.framework.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessInformation;
import com.pigthinkingtec.framework.databean.message.BusinessMessage;
import com.pigthinkingtec.framework.databean.message.BusinessWarning;
import com.pigthinkingtec.framework.databean.message.MessageContainer;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 *　共通ファンクションの抽象クラス
 * 
 * @author yizhou
 * @history
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractCommonFunction implements CommonFunctionInterface {
	/* ログ */
	private Log log = LogFactory.getLog(this.getClass().getName());
	/* ユーザ情報 */
	private UserContainer userData = null;
	/* ジョブタイプ */
	private String pgmCls = null;
	//入力パラメータ
	private Parameter inputDataBean = null;
	//出力パラメータ
	private Parameter outputDataBean = null;
	//メッセージコンテナ（ファンクションが独自に溜め込むためのコンテナ）
	private MessageContainer messages = null;
	/* 処理結果ステータス */
	private FunctionStatus functionStatus;
	/**
	 * AbstractCommonFunctionクラスのコンストラクタ
	 */
	public AbstractCommonFunction() {
		//functionStatus = FunctionStatus.NORMAL;
	}
	
	protected void resetStatus(){
		functionStatus = FunctionStatus.NORMAL;
		messages = new MessageContainer();
		
	}
	
	public void setUserData(UserContainer userData) {
		this.userData = userData;
	}
	/**
	 * ユーザ情報の取得
	 * @return
	 */
	protected <T extends UserContainer> T getUserData() {
		return (T) userData;
	}

	public void setPgmCls(String pgmCls) {
		this.pgmCls = pgmCls;
	}

	protected final String getPgmCls() {
		return pgmCls;
	}

	public final void setInputDataBean(Parameter inputDataBean) {
		this.inputDataBean = inputDataBean;
	}

	protected final <T extends Parameter> T getInputDataBean() {
		return (T)inputDataBean;
	}

	protected final void setOutputDataBean(Parameter outputDataBean) {
		this.outputDataBean = outputDataBean;
	}

	public final <T extends Parameter> T getOutputDataBean() {
		return (T)outputDataBean;
	}

	/**
	 * メッセージ一覧を、 IDとbinds[]のMapにして返す。（下位互換メソッド）
	 * 
	 * @return errorMessageList
	 */
	public Map getErrorMessageList() {
		Map errorMessageMap = new HashMap();
		
		for(Iterator iter = messages.getMessageList().iterator(); iter.hasNext();){
			BusinessMessage mesg = (BusinessMessage)iter.next();
			if (mesg.getValues() == null){
				errorMessageMap.put(mesg.getKey(), null);
			}else {
				String[] binds = new String[mesg.getValues().length];
				for(int i = 0; i < mesg.getValues().length; i++){
					binds[i] = mesg.getValues()[i].toString();
				}
				errorMessageMap.put(mesg.getKey(),binds );
			}
		}
		
		return errorMessageMap;
		
	}
	/*(non-javadoc)
	 * Function派生クラスを実行する前に行う処理がある場合は、Overwrideすること 
	 */
	protected void beforeInvoke(){
	}
	/*(non-javadoc)
	 * Function派生クラスを実行した後に行う処理がある場合は、Overwrideすること
	 */
	protected void afterInvoke(){
	}
	/**
	 * 主処理を行うメソッド
	 */
	protected abstract void invoke() throws SystemException;
	
	public final void execute() throws SystemException{
		getLog().debug("execute start");
		resetStatus();
		beforeInvoke();
		invoke();
		afterInvoke();
		getLog().debug("execute end");
	}

	/**
	 * FunctionからサブFunctionを実行する
	 * @param func
	 * @throws SystemException
	 */
	protected final void execFunction(CommonFunctionInterface func) throws SystemException {
		func.setUserData(userData);
		func.setPgmCls(pgmCls);
		func.execute();
		//メッセージコンテナは引き継がない kshiobara 2006/05/30
		//this.addMessageContainer(func.getMessageContainer());
	}

	/* (non-Javadoc)
	 * @see jp.co.shimano.gspi.framework.application.CommonFunctionInterface#getMessageContainer(jp.co.shimano.gspi.framework.business.MessageContainer)
	 */
	
	public final MessageContainer getMessageContainer() {
		if(messages == null){
			messages = new MessageContainer();
		}
		return messages;
	}
	
	/**
	 * Functionで出力されたメッセージすべてを取得する
	 * @return
	 */
	public List getMessages(){
		return messages.getMessageList();
	}
	/**
	 * Functionで出力されたエラーメッセージ群を取得する
	 * @return
	 */
	public List getErrors(){
		return messages.getErrorList();
	}
	
	/**
	 * Functionで出力されたワーニングメッセージ群を取得する
	 * @return
	 */
	public List getWarnings(){
		return messages.getWarningList();
	}
	
	/**
	 * Functionで出力されたインフォメーションメッセージ群を取得する
	 * @return
	 */
	public List getInformations(){
		return messages.getInformationList();
	}
	
	/**
	 * 行番号ごとのメッセージを格納したSortedMapを取得する。
	 * 取り出したMapは順序保障されており、逐次処理するためにはentrySet().iterator()メソッドにより
	 * イテレーターを取得する.
	 * キーは行番号で、Integer型で格納される。値はBusinessMessage型を格納したList型が保管される。
	 * <BR>
	 * (例)<BR>
	 * <PRE>
	 * 		for(Iterator iter=func.getLineMessageMap().entrySet().iterator();iter.hasNext();){
	 *		Map.Entry entry = (Map.Entry)iter.next();
	 *		Integer lineNumber = (Integer)entry.getKey();
	 *		List mesglist = (List)entry.getValue();
	 *		System.out.println("****" + lineNumber.toString() + "行目*****");
	 *		for(Iterator iter2=mesglist.iterator();iter2.hasNext();){
	 *			BusinessMessage mesg = (BusinessMessage)iter2.next();
	 *			System.out.println(mesg.getMessage());
	 *		}
	 *	}
	 *</PRE>
	 * @return
	 */
	public SortedMap getLineMessageMap(){
		return messages.getLineMessageMap();
	}
	
	/**
	 * Functionで出力されたメッセージのうち、指定した行番号に対応するリストを取得する。
	 * @param lineno
	 * @return
	 */
	public List getMessagesByLineNo(int lineno){
		return messages.getMessagesByLineNo(lineno);
	}
	
	/**
	 * メッセージIDを指定して、該当のエラーメッセージ群を取得する。
	 * 該当のメッセージがない場合、nullを返す
	 * @param mesgId
	 * @return
	 */
	public List getErrorsById(String mesgId){
		List errs = new ArrayList();
		for(Iterator iter = messages.getErrorList().iterator(); iter.hasNext(); ){
			BusinessError error = (BusinessError)iter.next();
			if (error.getKey().equals(mesgId)){
				errs.add(error);
			}
		}
		
		if (errs.size() == 0){
			return null;
		}else{
			return errs;
		}
		
	}
	
	/**
	 * メッセージIDを指定して、該当のエラーメッセージを取得する。
	 * メッセージが複数存在した場合、最初にセットされたもののみが返る。
	 * 該当のメッセージがない場合はnullが返る。
	 * @param mesgId
	 * @return
	 */
	public BusinessError getErrorById(String mesgId){
		List errs = getErrorsById(mesgId);
		if (errs == null || errs.size() == 0){
			return null;
		}else{
			return (BusinessError)errs.get(0);
		}
		
	}
	
	/**
	 * メッセージIDを指定して、該当のワーニングメッセージ群を取得する。
	 * 該当のメッセージがない場合、nullを返す
	 * @param mesgId
	 * @return
	 */
	public List getWarningsById(String mesgId){
		List warns = new ArrayList();
		for(Iterator iter = messages.getWarningList().iterator(); iter.hasNext(); ){
			BusinessWarning warn = (BusinessWarning)iter.next();
			if (warn.getKey().equals(mesgId)){
				warns.add(warn);
			}
		}
		
		if (warns.size() == 0){
			return null;
		}else{
			return warns;
		}
		
	}
	
	
	/**
	 * メッセージIDを指定して、該当のワーニングメッセージを取得する。
	 * メッセージが複数存在した場合、最初にセットされたもののみが返る。
	 * 該当のメッセージがない場合はnullが返る。
	 * @param mesgId
	 * @return
	 */
	public BusinessWarning getWarningById(String mesgId){
		List warns = getWarningsById(mesgId);
		if (warns == null || warns.size() == 0){
			return null;
		}else{
			return (BusinessWarning)warns.get(0);
		}
		
	}
	
	/**
	 * メッセージIDを指定して、該当のインフォメッセージ群を取得する。
	 * 該当のメッセージがない場合、nullを返す
	 * @param mesgId
	 * @return
	 */
	public List getInformationsById(String mesgId){
		List infos = new ArrayList();
		for(Iterator iter = messages.getInformationList().iterator(); iter.hasNext(); ){
			BusinessInformation info = (BusinessInformation)iter.next();
			if (info.getKey().equals(mesgId)){
				infos.add(info);
			}
		}
		
		if (infos.size() == 0){
			return null;
		}else{
			return infos;
		}
		
	}
	
	/**
	 * メッセージIDを指定して、該当のインフォメッセージを取得する。
	 * メッセージが複数存在した場合、最初にセットされたもののみが返る。
	 * 該当のメッセージがない場合はnullが返る。
	 * @param mesgId
	 * @return
	 */
	public BusinessInformation getInformationById(String mesgId){
		List infos = getInformationsById(mesgId);
		if (infos == null || infos.size() == 0){
			return null;
		}else{
			return (BusinessInformation)infos.get(0);
		}
		
	}
	
	/**
	 * @return Returns the functionStatus.
	 */
	public FunctionStatus getFunctionStatus() {
		return functionStatus;
	}
	/**
	 * @param functionStatus The functionStatus to set.
	 */
	public void setFunctionStatus(FunctionStatus functionStatus) {
		this.functionStatus = functionStatus;
	}

	/**
	 * 現在保持しているMessageContainerに、別のMessageContainerの内容を追加する。
	 * @param msg 追加するMessageContainerクラス
	 */
	@SuppressWarnings("unused")
	private void addMessageContainer(MessageContainer msg) {
		if(msg == null)return;
		getMessageContainer().add(msg);
	}
	
	/**
	 * 現在保持しいているエラーメッセージListに対して、新たにエラーメッセージListを追加する。
	 * @param errorlist
	 */
	protected void addErrors(List errorlist){
		if (errorlist == null || errorlist.size() == 0){
			return;
		}else{
			getMessageContainer().getErrorList().addAll(errorlist);
			getMessageContainer().getMessageList().addAll(errorlist);
		}
	}
	/**
	 * 現在保持しいているワーニングメッセージListに対して、新たにワーニングメッセージListを追加する。
	 * @param errorlist
	 */	
	protected void addWarnings(List warnlist){
		if (warnlist == null || warnlist.size() ==0){
			return;
		}else{
			getMessageContainer().getWarningList().addAll(warnlist);
			getMessageContainer().getMessageList().addAll(warnlist);
		}
	}
	/**
	 * 現在保持しいているインフォメーションメッセージListに対して、新たにインフォメーションメッセージListを追加する。
	 * @param errorlist
	 */	
	protected void addInformatins(List infolist){
		if (infolist == null || infolist.size() == 0){
			return;
		}else{
			getMessageContainer().getInformationList().addAll(infolist);
			getMessageContainer().getMessageList().addAll(infolist);
		}
	}
	
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 */
	protected void putErrorMessageList(String msgId) {
		setErrorMessage(msgId);
	}
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 * @param bind0
	 */
	protected void putErrorMessageList(String msgId,String bind0) {
		setErrorMessage(msgId,bind0);
	}
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 */
	protected void putErrorMessageList(String msgId,String bind0,String bind1) {
		setErrorMessage(msgId,bind0,bind1);
	}
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 */
	protected void putErrorMessageList(String msgId,String bind0,String bind1,String bind2) {
		setErrorMessage(msgId,bind0,bind1,bind2);
	}	
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 */
	protected void putErrorMessageList(String msgId,String bind0,String bind1,String bind2,String bind3) {
		setErrorMessage(msgId,bind0,bind1,bind2,bind3);
	}	
	/**
	 * エラーメッセージのセット:変更前メソッドの互換ラッパ
	 * @param msgId
	 * @param binds
	 */
	protected void putErrorMessageList(String msgId,String[] binds) {
		setErrorMessage(msgId,binds);
	}	
	
	
	/**
	 * エラーメッセージのセット(バインドなしの場合)
	 * @param msgId
	 * @param bind
	 */
	protected void setErrorMessage(String msgId) {
		BusinessError mesg = new BusinessError(msgId);
		messages.addError(null,mesg);
	}
	
	/**
	 * 行番号指定してエラーメッセージのセット(バインドなしの場合)
	 * @param msgId
	 * @param lineno
	 * 
	 */
	protected void setErrorMessage(String msgId,int lineno) {
		BusinessError mesg = new BusinessError(msgId);
		messages.addError(null,mesg,lineno);
	}
	
	/**
	 * エラーメッセージのセット
	 * @param msgId
	 * @param bind
	 */
	protected void setErrorMessage(String msgId, String bind) {
		BusinessError mesg = new BusinessError(msgId,bind);
		messages.addError(null,mesg);
	}
	
	/**
	 * 行番号指定してエラーメッセージのセット
	 * @param msgId
	 * @param bind
	 * @param lineno
	 */
	protected void setErrorMessage(String msgId, String bind, int lineno) {
		BusinessError mesg = new BusinessError(msgId,bind);
		messages.addError(null,mesg,lineno);
	}
	/**
	 * エラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1);
		messages.addError(null,mesg);
	}
	
	/**
	 * 行番号指定してエラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param lineno
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1, int lineno) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1);
		messages.addError(null,mesg,lineno);
	}
	/**
	 * エラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1, String bind2) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1,bind2);
		messages.addError(null,mesg);
	}
	
	/**
	 * 行番号指定してエラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param lineno
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1, String bind2, int lineno) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1,bind2);
		messages.addError(null,mesg,lineno);
	}
	/**
	 * エラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1, String bind2, String bind3) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1,bind2,bind3);
		messages.addError(null,mesg);
	}
	/**
	 * 行番号指定してエラーメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 * @param lineno
	 */
	protected void setErrorMessage(String msgId, String bind0, String bind1, String bind2, String bind3, int lineno) {
		BusinessError mesg = new BusinessError(msgId,bind0,bind1,bind2,bind3);
		messages.addError(null,mesg,lineno);
	}
	/**
	 * エラーメッセージのセット
	 * @param msgId
	 * @param binds
	 */
	protected void setErrorMessage(String msgId, String[] binds) {
		BusinessError mesg = new BusinessError(msgId,binds);
		messages.addError(null,mesg);
	}
	/**
	 * 行番号指定してエラーメッセージのセット
	 * @param msgId
	 * @param binds
	 * @param lineno
	 */
	protected void setErrorMessage(String msgId, String[] binds, int lineno) {
		BusinessError mesg = new BusinessError(msgId,binds);
		messages.addError(null,mesg,lineno);
	}
	
	/**
	 * ワーニングメッセージのセット(バインドなしの場合)
	 * @param msgId
	 */
	protected void setWarningMessage(String msgId) {
		BusinessWarning mesg = new BusinessWarning(msgId);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット(バインドなしの場合)
	 * @param msgId
	 */
	protected void setWarningMessage(String msgId, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId);
		messages.addWarning(null,mesg,lineno);
	}
	/**
	 * ワーニングメッセージのセット
	 * @param msgId
	 * @param bind
	 */
	protected void setWarningMessage(String msgId, String bind) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット
	 * @param msgId
	 * @param bind
	 * @param lineno
	 */
	protected void setWarningMessage(String msgId, String bind, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind);
		messages.addWarning(null,mesg,lineno);
	}
	/**
	 * ワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param lineno
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1);
		messages.addWarning(null,mesg,lineno);
	}
	/**
	 * ワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1, String bind2) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1,bind2);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param lineno
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1, String bind2, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1,bind2);
		messages.addWarning(null,mesg,lineno);
	}
	/**
	 * ワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1, String bind2, String bind3) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1,bind2,bind3);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 * @param lineno
	 */
	protected void setWarningMessage(String msgId, String bind0, String bind1, String bind2, String bind3, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId,bind0,bind1,bind2,bind3);
		messages.addWarning(null,mesg,lineno);
	}
	/**
	 * ワーニングメッセージのセット
	 * @param msgId
	 * @param binds
	 */
	protected void setWarningMessage(String msgId, String[] binds) {
		BusinessWarning mesg = new BusinessWarning(msgId,binds);
		messages.addWarning(null,mesg);
	}
	/**
	 * 行番号指定してワーニングメッセージのセット
	 * @param msgId
	 * @param binds
	 * @param lineno
	 */
	protected void setWarningMessage(String msgId, String[] binds, int lineno) {
		BusinessWarning mesg = new BusinessWarning(msgId,binds);
		messages.addWarning(null,mesg,lineno);
	}
	
	/**
	 * インフォメッセージのセット(バインドなしの場合)
	 * @param msgId
	 * 
	 */
	protected void setInformationMessage(String msgId) {
		BusinessInformation mesg = new BusinessInformation(msgId);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット(バインドなしの場合)
	 * @param msgId
	 * @param lineno
	 */
	protected void setInformationMessage(String msgId, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * インフォメッセージのセット
	 * @param msgId
	 * @param bind
	 */
	protected void setInformationMessage(String msgId, String bind) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット
	 * @param msgId
	 * @param bind
	 * @param lineno
	 */
	protected void setInformationMessage(String msgId, String bind, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * インフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param lineno
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * インフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1, String bind2) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1,bind2);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param lineno
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1, String bind2, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1,bind2);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * インフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1, String bind2, String bind3) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1,bind2,bind3);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット
	 * @param msgId
	 * @param bind0
	 * @param bind1
	 * @param bind2
	 * @param bind3
	 * @param lineno
	 */
	protected void setInformationMessage(String msgId, String bind0, String bind1, String bind2, String bind3, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId,bind0,bind1,bind2,bind3);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * インフォメッセージのセット
	 * @param msgId
	 * @param binds
	 */
	protected void setInformationMessage(String msgId, String[] binds) {
		BusinessInformation mesg = new BusinessInformation(msgId,binds);
		messages.addInformation(null,mesg);
	}
	/**
	 * 行番号指定してインフォメッセージのセット
	 * @param msgId
	 * @param binds
	 */
	protected void setInformationMessage(String msgId, String[] binds, int lineno) {
		BusinessInformation mesg = new BusinessInformation(msgId,binds);
		messages.addInformation(null,mesg,lineno);
	}
	/**
	 * ログオブジェクトの取得
	 * @return
	 */
	protected Log getLog() {
		return log;
	}
}