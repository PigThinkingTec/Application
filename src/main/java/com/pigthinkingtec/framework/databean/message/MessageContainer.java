package com.pigthinkingtec.framework.databean.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * メッセージ情報保持クラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class MessageContainer {
	private List messages;
	private List rstMessages;
	private BusinessInformations informations;
	private BusinessWarnings warnings;
	private BusinessErrors errors;
	private BusinessDialogs dialogs;
	/*共通ファンクション専用エラー格納リスト*/
	private List funcErrors;
	/*共通ファンクション専用ワーニング格納リスト*/
	private List funcWarnings;
	/*共通ファンクション専用インフォメーション格納リスト*/
	private List funcInformations;
	/*共通ファンクション専用ダイアログ格納リスト*/
	private List funcDialogs;
	/*共通ファンクション専用,行あたりのメッセージ格納用Map*/
	private TreeMap lineMsgMap;
	
	private int msgCount;
	private int rstMsgCount;
	//ログオブジェクト
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	public MessageContainer() {
		messages = new ArrayList();
		rstMessages = new ArrayList();
		warnings = new BusinessWarnings();
		dialogs = new BusinessDialogs();
		errors = new BusinessErrors();
		informations = new BusinessInformations();
		msgCount = 0;
		rstMsgCount = 0;
		funcErrors = new ArrayList();
		funcWarnings = new ArrayList();
		funcInformations = new ArrayList();
		funcDialogs = new ArrayList();
		lineMsgMap = new TreeMap();
	}
	
	/**
	 * 
	 * @return int
	 */
	public int getMsgCount() {
		return msgCount;
	}
	
	private void putMessageToLineMsgMap(int linenum, BusinessMessage mesg){
		if (linenum >= 1){
			Integer lineNumber = new Integer(linenum);
			if (lineMsgMap.containsKey(lineNumber)){
				((List)lineMsgMap.get(lineNumber)).add(mesg);
			}else{
				ArrayList msglist = new ArrayList();
				msglist.add(mesg);
				lineMsgMap.put(lineNumber,msglist);
			}
		}
	}
	
	

	/**
	 * BusinessErrorをエラーリストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * 
	 * @param key   エラーのキー情報
	 * @param error　エラーオブジェクト
	 * @param linenum メッセージを保管したい行番号(1以上の整数)
	 */	
	public void addError(String key, BusinessError error, int linenum){
		errors.add(key,error);
		funcErrors.add(error);
		
		//行番号対応
		putMessageToLineMsgMap(linenum,error);
		
		
		if (msgCount < SystemConstant.MAX_NORMAL_MESSAGE) {
			messages.add(error);
		}
		msgCount++;
	}
	
	/**
	 * BusinessErrorをエラーリストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * 
	 * @param key   エラーのキー情報
	 * @param error　エラーオブジェクト
	 */
	public void addError(String key , BusinessError error) {
		addError(key, error, 0);
	}

	/**
	 * BusinessWarningを警告リストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * 
	 * @param key   警告のキー情報
	 * @param warn　 警告オブジェクト
	 * @param linenum メッセージを保管したい行番号(1以上の整数)
	 */
	public void addWarning(String key , BusinessWarning warn , int linenum) {
		warnings.add(key,warn);
		funcWarnings.add(warn);
		
		//行番号対応
		putMessageToLineMsgMap(linenum,warn);
		if (msgCount < SystemConstant.MAX_NORMAL_MESSAGE) {
			messages.add(warn);
		}
		msgCount++;
	}	
	
	/**
	 * BusinessWarningを警告リストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * 
	 * @param key   警告のキー情報
	 * @param warn　 警告オブジェクト
	 */
	public void addWarning(String key , BusinessWarning warn) {
		addWarning(key,warn,0);
	}

	/**
	 * BusinessInformationを情報リストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * 
	 * @param key   情報のキー情報
	 * @param information　 情報オブジェクト
	 * @param linenum メッセージを保管したい行番号(1以上の整数)
	 */
	public void addInformation(String key,BusinessInformation information,int linenum) {
		informations.add(key,information);
		funcInformations.add(information);
		
		//行番号対応
		putMessageToLineMsgMap(linenum,information);
		
		if (msgCount < SystemConstant.MAX_NORMAL_MESSAGE) {
			messages.add(information);
		}
		msgCount++;
	}
	
	/**
	 * BusinessInformationを情報リストに追加します。
	 * 同時にMessageListにもBusinessMessageを追加します。
	 * @param key
	 * @param information
	 */
	public void addInformation(String key, BusinessInformation information){
		addInformation(key,information,0);
	}
	
	/**
	 * 処理結果メッセージのリストにBusinessMessageを追加する
	 * 
	 * @param information　 情報オブジェクト
	 */
	public void addEndMessage(BusinessInformation information) {
		if (rstMsgCount < SystemConstant.MAX_RESULT_MESSAGE) {
			rstMessages.add(information);
		}
		rstMsgCount++;
	}


	/**
	 * エラーリストを取得
	 * 
	 * @return errors エラーリストオブジェクト
	 */
	public BusinessErrors getErrors() {
		return errors;
	}
	
	/**
	 * 警告リストを取得
	 * 
	 * @return warnings 警告リストオブジェクト
	 */
	public BusinessWarnings getWarnings() {
		return warnings;
	}

	/**
	 * 情報リストを取得
	 * 
	 * @return informations 情報リストオブジェクト
	 */
	public BusinessInformations getInformations() {
		return informations;
	}

	/**
	 * ダイアログリストを取得
	 * 
	 * @return informations 情報リストオブジェクト
	 */
	public BusinessDialogs getDialogs() {
		return dialogs;
	}

	/**
	 * メッセージリストを取得
	 * 
	 * @return message メッセージリスト
	 */
	public List getMessageList() {
		return messages;
	}
	
	/**
	 * エラーメッセージリストを取得
	 * @return
	 */
	public List getErrorList(){
		return funcErrors;
	}

	/**
	 * ダイアログメッセージリストを取得
	 * @return
	 */
	public List getDialogList(){
		return funcDialogs;
	}

	/**
	 * ワーニングメッセージリストを取得
	 * @return
	 */
	public List getWarningList(){
		return funcWarnings;
	}
	
	/**
	 * インフォメーションメッセージリストを取得
	 * @return
	 */
	public List getInformationList(){
		return funcInformations;
	}
	
	/**
	 * 行番号ごとのメッセージを格納したSortedMapを取得する。
	 * 取り出したMapは順序保障されており、逐次処理するためにはentrySet().iterator()メソッドにより
	 * イテレーターを取得する
	 * @return
	 */
	public SortedMap getLineMessageMap(){
		return lineMsgMap;
	}
	
	/**
	 * 処理結果メッセージリストを取得
	 * 
	 * @return rstMessages 処理結果メッセージリスト
	 */
	public List getRstMessageList() {
		return rstMessages;
	}

	/**
	 * 指定した行番号に対してセットされているメッセージリストを取得。
	 * セットされていない場合は長さ０のListを返す。
	 * @param lineno
	 * @return
	 */
	public List getMessagesByLineNo(int lineno){
		Integer lineNumber = new Integer(lineno);
		if (lineMsgMap.containsKey(lineNumber)){
			return (List)lineMsgMap.get(lineNumber);
		}else{
			return new ArrayList();
		}
	}
	/**
	 * メッセージ情報を保持しているかを返す
	 * @return true:メッセージ情報を保持している<br>false:メッセージ情報を保持していない
	 */
	public boolean hasInfo() {
		return informations.hasInfo();
	}
	
	/**
	 * 警告情報を保持しているかを返す
	 * @return true:警告情報を保持している<br>false:警告情報を保持していない
	 */
	public boolean hasWarning() {
		return warnings.hasWarning();
	}

	/**
	 * エラー情報を保持しているかを返す
	 * @return true:エラー情報を保持している<br>false:エラー情報を保持していない
	 */
	public boolean hasError() {
		return errors.hasError();
	}

	/**
	 * Information,Warning,Error,Dialog MessageListにメッセージを追加する
	 * @param msg
	 */
	public void add(MessageContainer msg) {
		this.informations.add(msg.getInformations());
		this.errors.add(msg.getErrors());
		this.warnings.add(msg.getWarnings());
		this.dialogs.add(msg.getDialogs());
		for (Iterator ite = msg.getMessageList().iterator(); ite.hasNext();) {
			if (msgCount < SystemConstant.MAX_NORMAL_MESSAGE) {
				this.messages.add(ite.next());
				msgCount++;
			} else {
				break;
			}
		}
		for (Iterator ite = msg.getRstMessageList().iterator(); ite.hasNext();) {
			if (rstMsgCount < SystemConstant.MAX_RESULT_MESSAGE) {
				this.rstMessages.add(ite.next());
				rstMsgCount++;
			} else {
				break;
			}
		}
		this.funcErrors.addAll(msg.getErrorList());
		this.funcWarnings.addAll(msg.getWarningList());
		this.funcInformations.addAll(msg.getInformationList());
		this.funcDialogs.addAll(msg.getDialogList());
		for(Iterator iter = msg.lineMsgMap.entrySet().iterator(); iter.hasNext();){
			Map.Entry mapping = (Map.Entry)iter.next();
			if (this.lineMsgMap.containsKey(mapping.getKey())){
				List orig = (List)this.lineMsgMap.get(mapping.getKey());
				List additional = (List)mapping.getValue();
				orig.addAll(additional);
				this.lineMsgMap.put(mapping.getKey(),orig);
			}else{
				this.lineMsgMap.put(mapping.getKey(),mapping.getValue());
			}
			
		}
	}
	
	/**
	 * messagesにメッセージListをセット
	 * @param messagelist
	 */
	public void setMessages(List messagelist){
		this.messages = messagelist;
	}
	/**
	 * funcErrorsにエラーメッセージListをセット
	 * @param errorlist
	 */
	public void setErrors(List errorlist){
		this.funcErrors = errorlist;
	}
	
	/**
	 * funcWarningsにワーニングメッセージListをセット
	 * @param warninglist
	 */
	public void setWarnings(List warnlist){
		this.funcWarnings = warnlist;
	}
	
	/**
	 * funcInformationsにインフォメーションメッセージListをセット
	 * @param infomationlist
	 */
	public void setInformations(List infolist){
		this.funcInformations = infolist;
	}

	/**
	 * funcDialogsにダイアログメッセージListをセット
	 * @param funcDialogs
	 */
	public void setDialogs(List funcDialogs){
		this.funcDialogs = funcDialogs;
	}

	/**
	 * ApplicationErrorを保存する
	 * @param key
	 * @param error
	 */
	public void saveError(String key, BusinessError error) {
		log.debug("saveError start");
		if(key == null) {
			key = "error" + String.valueOf(error.hashCode());
		}
		addError(key,error);
		log.debug("saveError end");
	}
	
	/**
	 * ApplicationErrorを保存する
	 * @param key
	 * @param error
	 */
	public void saveDialog(String key, BusinessDialog dialog) {
		log.debug("saveDialog start");
		if(key == null) {
			key = "dialog" + String.valueOf(dialog.hashCode());
		}
		addDialog(key,dialog);
		log.debug("saveDialog end");
	}

	/**
	 * ApplicationWarningを保存する
	 * @param key
	 * @param warn
	 */
	public void saveWarning(String key, BusinessWarning warn) {
		log.debug("saveWarning start");
		
		if(key == null) {
			key = "warning" + String.valueOf(warn.hashCode());
		}
		addWarning(key, warn);
		log.debug("saveWarning end");
	}
	
	/**
	 * BusinessDialogをリストに追加します。
	 * BusinessDialogは複数選択することが出来ません。
	 * 
	 * @param key   Dialogのキー情報
	 * @param warn　 Dialogオブジェクト
	 */
	public void addDialog(String key , BusinessDialog dialog) {
		// ダイアログメッセージは原則1つしか登録しない
		if(funcDialogs.size() == 0) {
			dialogs.add(key,dialog);
			funcDialogs.add(dialog);
			messages.add(dialog);
		}
	}

	/**
	 * ApplicationInformationを保存する
	 * @param key
	 * @param information
	 */
	public void saveInformation(String key, BusinessInformation information) {
		log.debug("saveInformation start");
		if(key == null) {
			key = "info" + String.valueOf(information.hashCode());
		}
		addInformation(key, information);
		log.debug("saveInformation end");
	}
}