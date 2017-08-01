package com.pigthinkingtec.framework.service;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 * ビジネスロジックが不要な場合に使用するServiceクラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
public class OnlineDummyService extends AbstractOnlineService {

	/** 
	 * サービスクラスの実処理メソッド
	 * このクラスでは特に処理はせずにSUCCESSステータスを返す。
	 * 
	 * @throws SystemException
	 */
	protected void process() throws SystemException {
		getLog().debug("process start");
		setStatus(SystemConstant.STATUS_SUCCESS);
		getLog().debug("process end");

	}

}
