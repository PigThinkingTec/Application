package com.pigthinkingtec.batch.framework.common.utils;

import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.util.StringUtil;

/**
 * @ClassName: LogUtil
 * @Description: アプリログ出力
 * @author yizhou
 * @history
 */
public class LogUtil {

	/** ログオブジェクト */
	private static Log log = LogFactory.getLog(LogUtil.class);

	private static final String SEP = "	";
	private static final String LOG_LEVEL_ERROR = "[ERROR]";
	private static final String LOG_LEVEL_INFO = "[INFORMATION]";

	/**
	 *
	 * @Title: start
	 * @Description: 処理開始のログ
	 * @param batchPgId
	 *            バッチプログラムID
	 * @param userId
	 *            ユーザID
	 * @param eventName
	 *            イベント名
	 */
	public static void start(String batchPgId, UserContainer user, String eventName, String... params) {
		// ログメッセージ
		String logMessage = createInfoLogMessage(batchPgId, // バッチプログラムID
				user.getUserId(), // ユーザID
				user.getUserLang(), // ユーザLang
				eventName, // イベント名
				"");

		log.info(createParaStr(logMessage, params));
	}

	/**
	 *
	 * @Title: end
	 * @Description: 処理終了のログ
	 * @param batchPgId
	 *            バッチプログラムID
	 * @param userId
	 *            ユーザID
	 * @param eventName
	 *            イベント名
	 */
	public static void end(String batchPgId, UserContainer user, String eventName) {
		// ログメッセージ
		String logMessage = createInfoLogMessage(batchPgId, // バッチプログラムID
				user.getUserId(), // ユーザID
				user.getUserLang(), // ユーザLang
				eventName, // イベント名
				"");
		log.info(logMessage);
	}

	/**
	 *
	 * @Title: error
	 * @Description: エラーログ
	 * @param batchPgId
	 *            バッチプログラムID
	 * @param userId
	 *            ユーザID
	 * @param eventName
	 *            イベント名
	 */
	public static void error(String batchPgId, UserContainer user, String messageCode, String... paras) {
		// ログメッセージ
		String logMessage = createErrorLogMessage(batchPgId, // バッチプログラムID
				user.getUserId(), // ユーザID
				user.getUserLang(), // ユーザLang
				messageCode, // メッセージコード
				paras // メッセージパラメータ
		);
		log.error(logMessage);
	}

	/**
	 *
	 * @Title: createErrorLogMessage
	 * @Description: ERRORログメッセージを作成
	 * @param batchPgId
	 *            バッチプログラムID
	 * @param userId
	 *            ユーザID
	 * @param messageCode
	 *            メッセージコード
	 * @param messageParams
	 *            メッセージパラメータ
	 * @return ログメッセージ
	 */
	private static String createErrorLogMessage(String batchPgId, String userId, String userLang, String messageCode,
			String... paras) {

		String sysDate = DateFormatUtils.format(new Date(), "yyyy/MM/dd");
		String sysTime = DateFormatUtils.format(new Date(), "HH:mm:ss");

		return StringUtil.build(sysDate, SEP, sysTime, SEP, batchPgId, SEP, LOG_LEVEL_ERROR, SEP, userId, SEP, SEP,
				messageCode);
	}

	/**
	 *
	 * @Title: createInfoLogMessage
	 * @Description: INFOログメッセージを作成
	 * @param batchPgId
	 *            バッチプログラムID
	 * @param userId
	 *            ユーザID @param eventName イベント名
	 * @param messageCode
	 *            メッセージコード
	 * @param messageParams
	 *            メッセージパラメータ
	 * @return ログメッセージ
	 */
	private static String createInfoLogMessage(String batchPgId, String userId, String userLang, String eventName,
			String messageCode, String... paras) {

		String sysDate = DateFormatUtils.format(new Date(), "yyyy/MM/dd");
		String sysTime = DateFormatUtils.format(new Date(), "HH:mm:ss");

		return StringUtil.build(sysDate, SEP, sysTime, SEP, batchPgId, SEP, LOG_LEVEL_INFO, SEP, userId, SEP, SEP,
				messageCode, SEP, eventName);
	}

	private static String createParaStr(String logMessage, String... params) {
		for (int i = 0; i < params.length; i++) {
			if (i == 0) {
				logMessage = StringUtil.build(logMessage, SEP);
				logMessage = StringUtil.build(logMessage, "P=");
			}
			logMessage = StringUtil.build(logMessage, params[i]);
			if (i < params.length - 1) {
				logMessage = StringUtil.build(logMessage, ",");
			}
		}

		return logMessage;
	}
}
