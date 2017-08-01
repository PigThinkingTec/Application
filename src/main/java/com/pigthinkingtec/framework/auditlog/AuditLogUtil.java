package com.pigthinkingtec.framework.auditlog;

import java.sql.SQLException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.batch.framework.util.batch.BatchUserUtil;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.FwPropertyReader;

public class AuditLogUtil {

	// インスタンス化を防ぐコンストラクタ
	private AuditLogUtil() {
	}

	/* ログ出力用オブジェクト */
	private static final Logger logger = LoggerFactory.getLogger(AuditLogUtil.class);

	private static final int ERRORCODE_INSERTLOG = 99999;
	private static final String ERRORMESSAGE = "例外の形式が不適切です。例外メッセージ：";

	/*
	 * 監査ログ出力用 異常終了場合時のログメッセージID(プログラム{0}は異常終了しました。) システムエラーとユーザエラーは同じメッセージを使うこと
	 */
	private static String MESSAGEID_AUDITLOG_END_EXCEPTION = "";

	static {
		/*
		 * 監査ログ出力用 異常終了場合時のログメッセージID(プログラム{0}は異常終了しました。)
		 * システムエラーとユーザエラーは同じメッセージを使うこと
		 */
		MESSAGEID_AUDITLOG_END_EXCEPTION = FwPropertyReader.getProperty(SystemConstant.MESSAGEID_AUDITLOG_EXCEPTION_KEY,
				null);
	}

	// 監査Log出力用Writer
	private static AuditLogInterface auditLogWriter = null;

	/**
	 * 異常情報を監査Logに出力する
	 * 
	 * @param e
	 * @param order
	 * @throws DatabaseException
	 */
	public static void insertLogFromException(Throwable e, Order order) throws DatabaseException {

		// SQLExceptionがラップされている場合、それを取り出す
		Throwable tempE = e;
		// getCause()でたどっていき、tempEがSQLExceptionかnullならループを終了する
		while (!((tempE instanceof SQLException) || tempE == null)) {
			tempE = tempE.getCause();
		}

		if (order.isBatch()) {
			// Online画面ではなく、バッチから起動する場合

			// -------------------------------------------
			// 99999エラー内容を出力する
			// -------------------------------------------
			// SQLExceptionが見つかった場合のみ処理
			if (tempE instanceof SQLException) {
				SQLException sqlE = (SQLException) tempE;
				// エラーコードを取得
				int errorCode = sqlE.getErrorCode();

				// エラーコードが99999の場合のみ、ログを出力する
				if (errorCode == ERRORCODE_INSERTLOG) {
					String[] ExceptionMessage = sqlE.getMessage().split(",");

					// ログ情報に過不足がある場合、エラー
					if (ExceptionMessage.length < 5 || ExceptionMessage.length > 12) {
						logger.error(ERRORMESSAGE + sqlE.getMessage());
						throw new DatabaseException(ERRORMESSAGE + sqlE.getMessage(), e);
					}

					// 省略された置換変数はnullで埋める
					String[] logInfo = Arrays.copyOf(ExceptionMessage, 12);

					// Msg用パラメータ配列を作成
					Object[] args = new String[] { 
							logInfo[5], // messageArgs[0]
							logInfo[6], // messageArgs[1]
							logInfo[7], // messageArgs[2]
							logInfo[8], // messageArgs[3]
							logInfo[9], // messageArgs[4]
							logInfo[10], // messageArgs[5]
							logInfo[11] // messageArgs[6]
					};

					// system_contanstからUserIDを取得
					System_User batchUser = null;
					try {
						batchUser = BatchUserUtil.getBatchUser(logInfo[1]);
					} catch (SystemException e1) {
						throw new DatabaseException("", e1);
					}

					// 監査Logを出力する
					AuditLogUtil.writeAuditLog(
							logInfo[0], // computerName
							logInfo[2], // jobId
							logInfo[1], // programId
							logInfo[3], // serviceName
							logInfo[4], // messageId
							batchUser.getUserID(), // userId
							batchUser.getLanguage(), // lang
							null, // loginfo
							args // Msg parameter
					);

				}
			}

			// -------------------------------------------
			// 異常終了監査Logを出力する
			// -------------------------------------------
			String comupterName = null;
			String jobId = order.getLoginUser().getJobId();
			String programId = order.getLoginUser().getPgmId();
			String serviceName = programId;
			String userId = order.getLoginUser().getUserId();
			String lang = order.getLoginUser().getUserLang();

			// プログラム{0}は異常終了しました。
			String messageId = MESSAGEID_AUDITLOG_END_EXCEPTION;
			String[] messageArgs = new String[7];

			messageArgs[0] = programId;

			// 監査Logを出力する
			AuditLogUtil.writeAuditLog(comupterName, jobId, programId, serviceName, messageId, userId, lang, "" // loginfo
					, (Object[]) messageArgs);
		} else {
			// SQLExceptionが見つかった場合のみ処理
			if (tempE instanceof SQLException) {
				SQLException sqlE = (SQLException) tempE;
				// エラーコードを取得
				int errorCode = sqlE.getErrorCode();

				// エラーコードが99999の場合のみ、ログを出力する
				if (errorCode == ERRORCODE_INSERTLOG) {
					String[] ExceptionMessage = sqlE.getMessage().split(",");

					// ログ情報に過不足がある場合、エラー
					if (ExceptionMessage.length < 5 || ExceptionMessage.length > 12) {
						logger.error(ERRORMESSAGE + sqlE.getMessage());
						throw new DatabaseException(ERRORMESSAGE + sqlE.getMessage(), e);
					}

					// 省略された置換変数はnullで埋める
					String[] logInfo = Arrays.copyOf(ExceptionMessage, 12);

					// Msg用パラメータ配列を作成
					Object[] args = new String[] { 
							logInfo[5], // messageArgs[0]
							logInfo[6], // messageArgs[1]
							logInfo[7], // messageArgs[2]
							logInfo[8], // messageArgs[3]
							logInfo[9], // messageArgs[4]
							logInfo[10], // messageArgs[5]
							logInfo[11] // messageArgs[6]
					};

					// 監査Logを出力する
					AuditLogUtil.writeAuditLog(
							logInfo[0], // computerName
							null, // jobId
							logInfo[1], // programId
							logInfo[3], // serviceName
							logInfo[4], // messageId
							null, // userId
							null, // lang
							null, // loginfo
							args // Msg parameter
					);

				}
			}
		}
	}

	/**
	 * framework.propertiesファイル設定によって監査ログを出力するクラスを初期化
	 * 
	 * @return 監査ログを出力するクラスのインスタンス
	 */
	private static AuditLogInterface getAuditLogWriter() throws SystemException {

		// framework.propertiesファイルから、監査ログの出力タイプを取得する
		// Batchも、Onlineも使う可能性があるため、Online専用のPropertiesUtilではなく、FwPropertyReaderを使うこと
		// String
		// auditLogType=PropertiesUtil.getProperty(SystemConstant.DEFAULT_AUDITLOG_TYPE_KEY);
		String auditLogType = FwPropertyReader.getProperty(SystemConstant.DEFAULT_AUDITLOG_TYPE_KEY, null);

		if (auditLogType == null) {
			throw new SystemException("The key[" + SystemConstant.DEFAULT_AUDITLOG_TYPE_KEY + "] is empty. "
					+ "Please set it in framework.properties file.");
		}

		if (SystemConstant.DATA_ACCESS_TYPE_FILE.equals(auditLogType.toUpperCase())) {
			// 監査ログを監査ファイルに出力する
			auditLogWriter = new FileAuditLogWriter();
		} else if (SystemConstant.DATA_ACCESS_TYPE_DB.equals(auditLogType.toUpperCase())) {
			// 監査ログを監査DBに出力する
			auditLogWriter = new DatabaseAuditLogWriter();
		} else {
			// Defaultとして、監査ログを監査ファイルに出力する
			auditLogWriter = new FileAuditLogWriter();
		}

		return auditLogWriter;
	}

	/**
	 * 監査Logを出力する
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
	public static void writeAuditLog(Order order, String serviceName, String loginfo, String messageId, Object... args)
			throws SystemException {
		// framework.propertiesの設定によって、
		// 監査Log出力するクラスを初期化する
		if (auditLogWriter == null) {
			auditLogWriter = getAuditLogWriter();
		}

		// 監査Log出力する
		auditLogWriter.writeAuditLog(order, serviceName, loginfo, messageId, args);
	}

	/**
	 * 監査Logを出力する
	 * 
	 * @param computerName
	 * @param jobId
	 * @param programId
	 * @param serviceName
	 * @param messageId
	 * @param userId
	 * @param lang
	 * @param loginfo
	 * @param args
	 * @throws SystemException
	 */
	public static void writeAuditLog(String computerName, String jobId, String programId, String serviceName,
			String messageId, String userId, String lang, String loginfo, Object... args) throws DatabaseException {
		// framework.propertiesの設定によって、
		// 監査Log出力するクラスを初期化する
		if (auditLogWriter == null) {
			try {
				auditLogWriter = getAuditLogWriter();
			} catch (SystemException e) {
				String err = "AuditLogUtil.getAuditLogWriter() failed. ";
				logger.error(err + e.toString());
				throw new DatabaseException(err, e);
			}
		}

		// 監査Log出力する
		try {
			auditLogWriter.writeAuditLog(computerName, jobId, programId, serviceName, messageId, userId, lang, loginfo,
					args);
		} catch (SystemException e) {
			String err = "writeAuditLog() failed. computerName = [" + computerName + "], " + "jobId = [" + jobId + "], "
					+ "programId = [" + programId + "], " + "serviceName = [" + serviceName + "], " + "messageId = ["
					+ messageId + "], " + "userId = [" + userId + "], " + "lang = [" + lang + "], " + "loginfo = ["
					+ loginfo + "] ";
			logger.error(err);
			throw new DatabaseException(err, e);
		}
	}
}
