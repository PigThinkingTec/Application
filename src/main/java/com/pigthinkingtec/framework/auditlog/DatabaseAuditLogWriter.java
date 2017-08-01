package com.pigthinkingtec.framework.auditlog;

import java.util.ArrayList;
import java.util.List;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.OnlineOrder;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.ControllerUtil;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.UserUtil;

/**
 * データベースの監査ログテーブルに監査Logを出力する
 * 
 * @author AB-ZHOU
 *
 */
public class DatabaseAuditLogWriter implements AuditLogInterface {

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

		// ビジネスロジック層に渡すInputデータオブジェクトに各情報を設定する
		OnlineOrder inputOrder = new OnlineOrder();

		String userId = order.getLoginUser().getUserId();
		String lang = order.getLoginUser().getUserLang();

		// ユーザセッションが無い場合、nullを渡すとバッチユーザになってしまうので、明示的に空文字列を渡す
		if (userId == null) {
			userId = "";
			// Batchも、Onlineも使う可能性があるため、Online専用のPropertiesUtilではなく、FwPropertyReaderを使うこと
			// lang =
			// PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
			lang = FwPropertyReader.getProperty(SystemConstant.DEFAULT_LANG_KEY, null);

			order.getLoginUser().setUserLang(lang);
			order.getLoginUser().setUserId(userId);
		}

		// User情報を設定する
		inputOrder.setLoginUser(order.getLoginUser());

		// 監査Logを出力するために必要な情報を設定する
		AuditLogBean inputDataBean = new AuditLogBean();

		inputDataBean.setComupterName(null);
		inputDataBean.setJobId(null);
		inputDataBean.setServiceName(serviceName);
		inputDataBean.setLoginfo(loginfo);
		inputDataBean.setMessageId(messageId);
		if (args == null || args.length == 0) {
			inputDataBean.setArgs(new ArrayList<String>());
		} else {
			List<String> argsList = new ArrayList<String>();
			for (int i = 0; i < args.length; i++) {
				argsList.add((String) args[i]);
			}
			inputDataBean.setArgs(argsList);
		}
		inputOrder.setInputDataBean(inputDataBean);

		// 監査LogをDBに書き出すCommandを起動する
		ControllerUtil.runCommand(
				SystemConstant.AUDITLOG_WRITE_DB_CLASS_KEY, // 監査LogをDBに出力するCommandクラスを取得するKey
				inputOrder);//// 監査LogをDBに出力するServiceクラスに使うIputデータ

	}

	@Override
	public void writeAuditLog(String computerName, String jobId, String programId, String serviceName, String messageId,
			String userId, String lang, String loginfo, Object... args) throws SystemException {

		OnlineOrder order = new OnlineOrder();

		UserContainer user = UserUtil.getUserContainer();
		user.setUserId(userId);
		user.setUserLang(lang);
		user.setJobId(jobId);
		user.setPgmId(programId);

		order.setLoginUser(user);

		// 監査Logを出力する
		writeAuditLog(order, serviceName, loginfo, messageId, args);

	}

}
