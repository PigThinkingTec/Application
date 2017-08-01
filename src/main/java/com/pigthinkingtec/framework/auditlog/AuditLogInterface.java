package com.pigthinkingtec.framework.auditlog;

import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.exception.SystemException;

public interface AuditLogInterface {

	/**
	 * 
	 * @param order	監査Logに出力する用汎用情報
	 * @param serviceName	監査Logに出力するServiceの名前
	 * @param loginfo	監査Logに補足情報を出力する場合使う補足情報
	 * @param messageId	監査Logに出力するメッセージのId
	 * @param args	監査Logに出力するメッセージのパラメータ（必要な場合のみ設定)
	 * @throws SystemException
	 */
	abstract public void writeAuditLog(
			Order order, String serviceName, String loginfo, String messageId, Object... args) 
					throws SystemException;
	
	/**
	 * 
	 * @param computerName
	 * @param jobId
	 * @param programId
	 * @param serviceName
	 * @param messageId
	 * @param userId
	 * @param lang
	 * @param loginfo
	 * @param args	メッセージの入れ替えパラメータ　（0～6）
	 * @throws SystemException
	 */
	abstract public void writeAuditLog(
					String computerName, String jobId, String programId, String serviceName, 
					String messageId, String userId, String lang, String loginfo, Object... args) 
					throws SystemException;
	
}
