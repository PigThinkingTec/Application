package com.pigthinkingtec.framework.auditlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.OnlineOrder;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.message.MessageUtil;

/**
 * 監査ログファイルaudit.logに監査Logを出力する
 * 
 * @author AB-ZHOU
 *
 */
public class FileAuditLogWriter implements AuditLogInterface {

	private final static Logger logger = LoggerFactory.getLogger(FileAuditLogWriter.class);

	/**
	 * 
	 * @param order
	 *            監査Logに出力する用汎用情報
	 * @param serviceName
	 *            監査Logに出力するServiceの名前
	 * @param loginfo
	 *            監査Logに補足情報を出力する場合使う補足情報
	 * @param messageId
	 *            監査Logに出力するメッセージのId
	 * @param args
	 *            監査Logに出力するメッセージのパラメータ（必要な場合のみ設定)
	 * @throws SystemException
	 */
	@Override
	public void writeAuditLog(Order order, String serviceName, String loginfo, String messageId, Object... args)
			throws SystemException {

		String computerName = null;
		String jobId = null;
		String programId = order.getLoginUser().getPgmId();
		String userId = order.getLoginUser().getUserId();
		String lang = order.getLoginUser().getUserLang();

		// ユーザセッションが無い場合、nullを渡すとバッチユーザになってしまうので、明示的に空文字列を渡す
		if (userId == null) {
			userId = "";
			// Batchも、Onlineも使う可能性があるため、Online専用のPropertiesUtilではなく、FwPropertyReaderを使うこと
			// lang =
			// PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
			lang = FwPropertyReader.getProperty(SystemConstant.DEFAULT_LANG_KEY, null);
		}

		// 監査Log Messageを取得する
		String message = MessageUtil.getMessage(order, messageId, lang, args);

		// 監査Log内容を作成
		String auditLog = 
				" { " + 
		        "comupterName = [" + computerName + "], " + 
		        "jobId = [" + jobId + "], " + 
		        "programId = [" + programId + "], " + 
		        "serviceName = [" + serviceName + "], " + 
		        "userId = [" + userId + "], " + 
		        "lang = [" + lang + "], " + 
		        "message = [" + message + "], " + 
		        "loginfo = [" + loginfo + "] " + 
		        "}";

		// 監査Log出力する
		// @see logback.xml中のaudit_log_file設定Sector
		logger.info(auditLog);
	}

	@Override
	public void writeAuditLog(String computerName, String jobId, String programId, String serviceName, String messageId,
			String userId, String lang, String loginfo, Object... args) throws SystemException {

		OnlineOrder order = new OnlineOrder();

		UserContainer user = UserUtil.getUserContainer();
		user.setUserId(userId);
		user.setUserLang(lang);

		order.setLoginUser(user);

		// 監査Log Messageを取得する
		String message = MessageUtil.getMessage(order, messageId, lang, args);

		// 監査Log内容を作成
		String auditLog = 
				" { " + 
				"comupterName = [" + computerName + "], " + 
				"jobId = [" + jobId + "], " + 
				"programId = [" + programId + "], " + 
				"serviceName = [" + serviceName + "], " + 
				"userId = [" + userId + "], " + 
				"lang = [" + lang + "], " + 
				"message = [" + message + "], " + 
				"loginfo = [" + loginfo + "] "
				+ "}";

		// 監査Log出力する
		// @see logback.xml中のaudit_log_file設定Sector
		logger.info(auditLog);
	}
}
