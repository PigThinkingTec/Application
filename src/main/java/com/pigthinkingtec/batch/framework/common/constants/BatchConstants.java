package com.pigthinkingtec.batch.framework.common.constants;

public class BatchConstants {

	public static final String BATCH_ID_NAME_PROPERTY_FILE_NAME = "batchProgramId";

	/*****************************************************/
	/* 処理戻り値 (画面と統一するために再定義する) */
	/*****************************************************/
	/** バッチ処理正常終了した場合の戻り値 */
	public static final int RET_OK = 0;
	/** バッチ処理警告終了した場合の戻り値 */
	public static final int RET_WARNING = 2;
	/** バッチ処理ユーザエラー（業務エラー）で終了した戻り値 */
	public static final int RET_USER_ERR = 8;
	/** バッチ処理異常終了した場合の戻り値 */
	public static final int RET_SYSERR = 9;

	/** バッチ処理正常終了した場合の戻り値 */
	public static final String RET_KEY_OK = String.valueOf(RET_OK);
	/** バッチ処理警告終了した場合の戻り値 */
	public static final String RET_KEY_WARNING = String.valueOf(RET_WARNING);
	/** バッチ処理ユーザエラー（業務エラー）で終了した戻り値 */
	public static final String RET_KEY_USER_ERR = String.valueOf(RET_USER_ERR);
	/** バッチ処理異常終了した場合の戻り値 */
	public static final String RET_KEY_SYSERR = String.valueOf(RET_SYSERR);

	/*****************************************************/
	/* BATCH_RESULT STATUS */
	/*****************************************************/
	/** プロシージャの戻り値（定数ID）（正常）を常数テーブルから取得するKEY */
	public static final String BATCH_RETURN_CODE_KEY_OK = "RETUNCODE_S";
	/** プロシージャの戻り値（定数ID）（警告）を常数テーブルから取得するKEY */
	public static final String BATCH_RETURN_CODE_KEY_WARNING = "RETUNCODE_W";
	/** プロシージャの戻り値（定数ID）（業務エラー）を常数テーブルから取得するKEY */
	public static final String BATCH_RETURN_CODE_KEY_BUSINESS_ERROR = "RETUNCODE_E";
	/** プロシージャの戻り値（定数ID）（異常）を常数テーブルから取得するKEY */
	public static final String BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION = "RETUNCODE_A";

	/*****************************************************/
	/* BATCH 共通設定 */
	/*****************************************************/
	public static final String DEFAULT_LANG_KEY = "default.lang";

	public static final String SOURCE_TYPE_FILE = "FILE";
	public static final String SOURCE_TYPE_DB = "DATABASE";

	/*****************************************************/
	/* BATCH_USER INFO */
	/*****************************************************/
	public static final String BATCH_USER_KEY = "BATCH_USERID";
	public static final String DEFAULT_BATCH_USER_ID = "batch_user";
	public static final String DEFAULT_BATCH_USER_LANG = "JP";

	/*****************************************************/
	/* BATCH RETURN CODE INFO */
	/*****************************************************/
	public static final String BATCH_RETURN_CODE_SOURCE_TYPE = "batch.return.code.source.type";

	public static final String BATCH_RETURN_CODE_SUCCESS_KEY = "batch.return.code.SUCCESS";
	public static final String BATCH_RETURN_CODE_WARNING_KEY = "batch.return.code.WARNING";
	public static final String BATCH_RETURN_CODE_USRERROR_KEY = "batch.return.code.USRERROR";
	public static final String BATCH_RETURN_CODE_EXCEPTION_KEY = "batch.return.code.EXCEPTION";

	public static final String BATCH_RETURN_CODE_DB_ACCESS_CLASS_KEY = "batch.return.code.database.access.class";

	/*****************************************************/
	/* BATCH RETURN CODE KEY INFO */
	/*****************************************************/
	public static final String BATCH_RETURN_CODE_KEY_SOURCE_TYPE = "batch.return.code.key.source.type";

	public static final String BATCH_RETURN_CODE_KEY_SUCCESS_KEY = "batch.return.code.key.SUCCESS";
	public static final String BATCH_RETURN_CODE_KEY_WARNING_KEY = "batch.return.code.key.WARNING";
	public static final String BATCH_RETURN_CODE_KEY_USRERROR_KEY = "batch.return.code.key.USRERROR";
	public static final String BATCH_RETURN_CODE_KEY_EXCEPTION_KEY = "batch.return.code.key.EXCEPTION";

	public static final String BATCH_RETURN_CODE_KEY_DB_ACCESS_CLASS_KEY = "batch.return.code.key.database.access.class";

	/*****************************************************/
	/* 監査Log用Message Key */
	/*****************************************************/
	public static final String BATCH_AUDITLOG_START_MESSAGEID_KEY = "audit.log.start.messageid";
	public static final String BATCH_AUDITLOG_OK_END_MESSAGEID_KEY = "audit.log.ok-end.messageid";
	public static final String BATCH_AUDITLOG_WARNING_END_MESSAGEID_KEY = "audit.log.warning-end.messageid";
	public static final String BATCH_AUDITLOG_EXCEPTION_END_MESSAGEID_KEY = "audit.log.exception-end.messageid";
}
