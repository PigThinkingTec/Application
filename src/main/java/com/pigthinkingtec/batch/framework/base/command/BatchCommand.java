package com.pigthinkingtec.batch.framework.base.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.ProcessManagementBean;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeKeyUtil;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeUtil;
import com.pigthinkingtec.framework.command.AbstractCommand;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 * @ClassName: BatchService
 * @Description: バッチ用の基底Commandクラス
 * @author yizhou
 * 
 */
public abstract class BatchCommand extends AbstractCommand {

	/** ログオブジェクト */
	private static Log log = LogFactory.getLog(BatchCommand.class);
	
	@Override
	protected void invoke() throws SystemException {		
		// 起動引数のチェック
		if (!checkParameter()) {
			int RET_USER_ERR = ReturnCodeUtil.getRetCode(
												BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, 
												getUserData().getPgmId());
			String RET_USER_ERR_KEY = ReturnCodeKeyUtil.getReturnCodeKey(
												BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, 
												getUserData().getPgmId());
			
			ProcessManagementBean bean = getDataBean();
			
			//業務エラーとみなす
			bean.setResultCode(RET_USER_ERR);
			bean.setResultCodeKey(RET_USER_ERR_KEY);
			return;
		}
		
		//必要な参数を共通参数クラスに設定する
		setBeanInfo();
		
		// batch起動
		subInvoke();

	}

	protected abstract boolean checkParameter() throws SystemException;
	
	protected abstract void setBeanInfo() throws SystemException;
	
	protected abstract void subInvoke() throws SystemException;

	/**
	 * バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)を元に
	 * 戻り値に変換して設定する
	 * @param retCodeKey	バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)
	 * @throws SystemException
	 * 
	 * @remark 下記理由でこのメソッドをこれから使わないようにする。代わりにsetRetInfoByRetCodeKey()を使ってください。<br>
	 * １：戻り値Keyは、基本はStringタイプであり、intタイプは不適当であること。<br>
	 * ２：メソッド名には、戻り値Keyの表記はなくて、適当ではないこと<br>
	 */
	@Deprecated
	protected void setMsgByRetValue(int retCodeKey) throws SystemException {
		
		log.debug("The parameter retCodeKey = [" + retCodeKey + "]");
		
		setRetInfoByRetCodeKey(retCodeKey);
		
	}
	
	/**
	 * バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)を元に
	 * 戻り値に変換して設定する
	 * @param retCodeKey	バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)
	 * @throws SystemException
	 */
	protected void setRetInfoByRetCodeKey(String retCodeKey) throws SystemException {
		
		log.debug("The parameter retCodeKey = [" + retCodeKey + "]");
		
		//DBからの戻り値を取得する
		final int RET_OK = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_OK, getUserData().getPgmId());
		final int RET_WARNING = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_WARNING, getUserData().getPgmId());
		final int RET_USER_ERR = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, getUserData().getPgmId());
		final int RET_EXCEPTION = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION, getUserData().getPgmId());
		
		//Storedの戻り値はReturnKeyであるため、このReturnKeyとつながる戻り値を取得する
		int ret = ReturnCodeUtil.getRetCodeByRetKey(retCodeKey, getUserData().getPgmId());
		
		ProcessManagementBean bean = getDataBean();
		
		//戻り値を設定する
		bean.setResultCode(ret);
		bean.setResultCodeKey(retCodeKey);
		
		if (ret == RET_EXCEPTION) {
			//異常発生時、この正常ルートを到着しないため、ありえないが、異常終了させる
			throw new SystemException("Stored return value is Exception: " + ret);
		} 
		
		if ((ret != RET_OK) && 
				(ret != RET_WARNING) &&
				(ret != RET_USER_ERR) &&
				(ret != RET_EXCEPTION) ){
			bean.setResultCode(RET_USER_ERR);//業務エラーとみなす
			log.warn("The parameter retCodeKey = [" + retCodeKey + "] is invalid.");
		}
	
	}
	
	/**
	 * バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)を元に
	 * 戻り値に変換して設定する
	 * @param retCodeKey	バッチStoredProcedure実行結果(戻り値ではなく、戻り値のKey)
	 * @throws SystemException
	 */
	protected void setRetInfoByRetCodeKey(int retCodeKey) throws SystemException {
		
		log.debug("The parameter retCodeKey = [" + retCodeKey + "]");
		
		setRetInfoByRetCodeKey(String.valueOf(retCodeKey));

	}
}
