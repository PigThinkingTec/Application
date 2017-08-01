package com.pigthinkingtec.batch.framework.base.service;

import com.pigthinkingtec.batch.framework.common.databean.ProcessManagementBean;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeUtil;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.AbstractOnlineDataBean;
import com.pigthinkingtec.framework.exception.SystemException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName: BatchService
 * @Description: バッチ用の基底Serviceクラス(画面側実装されたCommandを呼び出す場合専用)
 * @author yizhou
 * @Remarks 画面側実装したCommandを呼び出す場合専用
 */
public abstract class BatchOnlineService extends BatchService{
	
	/** ログオブジェクト */
	private static Log log = LogFactory.getLog(BatchOnlineService.class);
	
	private boolean isCallStoredProcedureFlg = false;
	
	private ProcessManagementBean manager = null;
	
	/**
	 *  業務ロジックを実装する
	 */
	protected void subProcess() throws SystemException{

		log.info("BatchOnlineService subProcess() start.");
	
		manager = (ProcessManagementBean) getInputDataBean();
		
		// バッチの引数を取得する。
		String[] params = (String[]) getArgumentData();	
		
		//manager Beanのバックアップを取得して、かつ、
		//バッチモードの場合、Online画面のパラメータを廃止し、バッチパラメータから取得値を入れ替える
		AbstractOnlineDataBean bean = convertPara2DataBean(params);
		
		//データを入れ替える
		setInputDataBean(bean);
		
		//業務ロジックを実行
		log.info("BatchOnlineService subProcessForOnline() before.");
		subProcessForOnline();
		log.info("BatchOnlineService subProcessForOnline() after.");
		
		//戻り値設定
		if (! isCallStoredProcedureFlg()) {
			//StroedProcedureを呼び出さない場合
			
			if (! isExcutedCommand()) {
				// Commandを実行したことがない場合
				//　　　⇒Commandを実行する前に、エラー発生した場合（例：パラメータチェック、ファイル読み書きなど）
				//   ⇒あるいは、Commandを実行しない場合、
				// この時点は各Appのservice側で設定した戻り値とStatusをそのまま使うため、ここで何もしない。
				log.info("BatchOnlineService subProcess() has ended before command running.");
				
			} else {
				// Commandを実行したことがある場合
				//画面と共有するCommandで設定したメッセージ情報から、戻り値に変換する
				if ( getStatus() == null || StringUtils.equals(SystemConstant.STATUS_SUCCESS,getStatus())) {
					if (hasBusinessError()) { //getStatus() == nullの場合、ここで到着可能性があり
						//失敗ステータスを設定
						setStatus(SystemConstant.STATUS_FAULT);			
						//Batch用戻り値を設定する（Businessエラーがあるとみなし）
						manager.setResultCode(RET_USER_ERR);
						manager.setResultCodeKey(RET_USER_ERR_KEY);//親クラスで監査ログをお出力にこの設定使う
						
					} else if (hasBusinessWarning()) {
						//成功ステータスを設定
						setStatus(SystemConstant.STATUS_SUCCESS);
						
						///Batch用戻り値を設定する（Warning終了）
						manager.setResultCode(RET_WARNING);
						manager.setResultCodeKey(RET_WARNING_KEY);//親クラスで監査ログをお出力にこの設定使う
						
					} else {
						//成功ステータスを設定
						setStatus(SystemConstant.STATUS_SUCCESS);
						
						///Batch用戻り値を設定する（正常終了）
						manager.setResultCode(RET_OK);
						manager.setResultCodeKey(RET_OK_KEY);//親クラスで監査ログをお出力にこの設定使う
					}
				} else {
					//失敗ステータスを設定
					setStatus(SystemConstant.STATUS_FAULT);			
					
					//Batch用戻り値を設定する（Businessエラーがあるとみなし）
					manager.setResultCode(RET_USER_ERR);
					manager.setResultCodeKey(RET_USER_ERR_KEY);//親クラスで監査ログをお出力にこの設定使う
				}
			}

		} else {
			//StoredProcedureを呼び出す場合
			//画面Commandで設定されたStoredProcedureの実行戻り値(戻り値Key)を取得して、
			//管理Bean(manager)に設定する
			setRetValue(manager, bean.getResultCodeKey());
		}
		

		log.info("BatchOnlineService subProcess() end.");
	}

	
	protected abstract void subProcessForOnline() throws SystemException;
	
	/**
	 * Batch起動引数から画面処理Command（Batchと共有）に使うInputDataBeanに変換する
	 * @param params
	 * @return
	 */
	protected abstract AbstractOnlineDataBean convertPara2DataBean(String[] params);
	
	
	protected void setRetValue(ProcessManagementBean manager, String retCodeKey) throws SystemException {
		
		//Storedの戻り値は、ReturnKeyであるため、このReturnKeyとつながる戻り値を取得する
		int ret = ReturnCodeUtil.getRetCodeByRetKey(retCodeKey, manager.getBatchPgId());
		
		//纏めて戻り値Keyを設定する
		manager.setResultCode(ret);
		manager.setResultCodeKey(retCodeKey);//親クラスで監査ログをお出力にこの設定使う
		
		if (ret == RET_EXCEPTION) {
			//異常発生時、この正常ルートを到着しないため、ありえないが、異常終了させる
			throw new SystemException("Stored return value is Exception: " + ret);
			
		} 
		
		if ((ret != RET_OK) && 
				(ret != RET_WARNING) &&
				(ret != RET_USER_ERR) &&
				(ret != RET_EXCEPTION) ){
			log.warn("The parameter retCodeKey = [" + retCodeKey + "] is invalid.");
		}

	}

	/**
	 * 実行結果を保存するBeanを取得
	 * @return
	 */
	public ProcessManagementBean getProcessManagementBean() {
		return manager;
	}
	
	/**
	 * 画面と共有するBatchは、StoredProcedureを呼び出すかどうかを示すFLG。呼び出す場合、True
	 * @return
	 */
	public boolean isCallStoredProcedureFlg() {
		return isCallStoredProcedureFlg;
	}


	/**
	 * 画面と共有するBatchは、StoredProcedureを呼び出すかどうかを示すFLG。呼び出す場合、Trueに設定、その以外の場合、Falseにする
	 * @param isCallStoredProcedureFlg
	 */
	public void setCallStoredProcedureFlg(boolean isCallStoredProcedureFlg) {
		this.isCallStoredProcedureFlg = isCallStoredProcedureFlg;
	}	
}
