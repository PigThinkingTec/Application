package com.pigthinkingtec.framework;

/**
 * 定数保持クラス
 * 
yizhou
 * @version $Revision: 1.72 $ $Date: 2013/01/07 06:30:42 $
 * @version $Revision: 1.73 $ $Date: 2015/04/30 01:52:10 $
 */
public class SystemConstant {
	/** ユーザオブジェクトキー */
	public static final String USER_CONTAINER = "com.pigthinkingtec.framework.business.UserContainer";
	/** 改行コード */
	public static final String LINE_FEED = "\r\n";
	/** リクエスト・レスポンスのエンコード */
	public static final String ENCODE = "UTF-8";
	/** imagesディレクトリパス */
	public static final String IMAGE_PATH = "screen/images/";
	/** ContentType */
	public static final String CONTENT_TYPE = "application/octet-stream";
	/** カンマ */
	public static final char COMMA = ',';
	/** カンマ */
	public static final String COMMA_STR = ",";
	/** タブ */
	public static final char TAB = '\t';
	/** ダブルクォーテーション */
	public static final char DOUBLE_QUOTE = '"';
	/** バックスラッシュ */
	public static final char BACKSLASH = '\\';
	/** 改行コード */
	public static final char ENTER = '\n';
	/** ワイルドカード */
	public static final String WILD_CARD = "*";
	/** 空白 */
	public static final String SPACE = " ";
	/** ブランク */
	public static final String BLANK = "";
	/** 小数点 */
	public static final String DECIMAL_POINT = ".";
	/** プラス記号 */
	public static final String PLUS = "+";
	/** マイナス記号 */
	public static final String MINUS = "-";
	/** スラッシュ */
	public static final String SLASH = "/";
	/** ゼロ(IFゼロ埋めで利用) */
	public static final String ZERO = "0";
	/** エスケープ文字 */
	public static final String ESCAPE_CHAR = "!";
	/** エスケープオプション文字列 */
	public static final String SQL_ESCAPE = " {ESCAPE '" + ESCAPE_CHAR + "'} ";
	/** select for update wait文で使用する共通の待ち時間（秒) */
	public static final int WAIT_SECONDS = 3600;
	/** select for update wait文の再試行回数 */
	public static final int RETRY_COUNT = 3;

	// --------------------------------------------------------Logic Hndling Key
	/** 1ページの表示明細数 */
	public static final int PAGE_SIZE = 20;
	/** フラグON */
	public static final String FLAG_ON = "1";
	/** フラグOFF */
	public static final String FLAG_OFF = "0";
	/** Forward Status(成功) */
	public static final String STATUS_SUCCESS = "success";
	/** Forward Status(失敗) */
	public static final String STATUS_FAULT = "business_error";
	/** Forward Status(権限エラー) */
	public static final String STATUS_AUTHERROR = "auth_error";
	/** １つのRequestNoに対する通常メッセージ最大登録件数 */
	public static final int MAX_NORMAL_MESSAGE = 99975;
	/** １つのRequestNoに対する処理結果メッセージ最大登録件数 */
	public static final int MAX_RESULT_MESSAGE = 20;

	// --------------------------------------------------------
	/** ラベル、メッセージなどのデフォルト表示言語を取得するKey */
	public static final String DEFAULT_LANG_KEY = "default.lang";
	/** ラベル用プロパティファイルKey */
	public static final String DEFAULT_LABEL_TYPE_KEY = "label.source.type";
	public static final String DB_GET_LABLE_CLASS_KEY = "label.database.access.class";
	/** メッセージ用プロパティファイルKey */
	public static final String DEFAULT_MESSAGE_TYPE_KEY = "message.source.type";
	public static final String DB_GET_MESSAGE_CLASS_KEY = "message.database.access.class";
	/** ラベル・メッセージ用プロパティファイルKey */
	public static final String DATA_ACCESS_TYPE_FILE = "FILE";
	public static final String DATA_ACCESS_TYPE_DB = "DATABASE";
	public static final String DEFAULT_MESSAGE_CACHETIME_KEY = "default.database.cacheTime";

	// --------------------------------------------------------
	/** セッション用プロパティファイルKey */
	public static final String SESSION_MANAGER_CREATE_KEY = "sessionManager.create.service";
	public static final String SESSION_MANAGER_DESTROY_KEY = "sessionManager.destroy.service";

	// --------------------------------------------------------
	/** 監査Log用プロパティファイルKey */
	public static final String MESSAGEID_AUDITLOG_SERVICE_START_KEY = "audit.log.service.start.messageid";
	public static final String MESSAGEID_AUDITLOG_KEY = "audit.log.messageid";
	public static final String MESSAGEID_AUDITLOG_EXCEPTION_KEY = "audit.log.exception.messageid";
	public static final String MESSAGEID_AUTHORITYERROR_KEY = "authority.error.log.messageid";

	public static final String DEFAULT_AUDITLOG_TYPE_KEY = "audit.log.output.type";
	public static final String AUDITLOG_WRITE_DB_CLASS_KEY = "audit.log.database.output.class";

	// @see DB_GET_MESSAGE_CLASS_KEY
	public static final String DB_GET_MESSAGE_COMMAND_CLASS_KEY = "message.database.access.command.class";

	// --------------------------------------------------------
	/** Validation error Message id を取得するKey */
	public static final String MESSAGEID_VALIDATION_ERROR_KEY = "validation.error.messageid";

	// --------------------------------------------------------

	/** 一回最大取得件数を取得するKey */
	public static final String DEFAULT_MAX_ROW_COUNT_KEY = "default.max.row.count";

	/** 権限チェックで権限が無い場合の戻り値 */
	public static final int AUTHORITYLEVEL_NOAUTHORITY = 0;
	/** 権限チェックで参照のみ権限の場合の戻り値 */
	public static final int AUTHORITYLEVEL_READONLY = 1;
	/** 権限チェックで全権限がある場合の戻り値 */
	public static final int AUTHORITYLEVEL_FULLAUTHORITY = 2;

	// --------------------------------------------------------
	/** Validationメッセージ用プロパティファイルKey */
	public static final String DB_GET_VALIDATION_MESSAGE_CLASS_KEY = "validation.message.database.access.class";

	// --------------------------------------------------------
	// 言語キー
	public static final String LANG_JP = "JP";
	public static final String LANG_EN = "EN";
	public static final String LANG_TW = "TW";
	public static final String LANG_CN = "CN";

	// javascript用言語キー
	public static final String JS_LANGKEY_JP = "ja";
	public static final String JS_LANGKEY_EN = "en-GB";
	public static final String JS_LANGKEY_CN = "zh-CN";

	// 日付用フォーマット定数 日本
	public static final String DATEFORMAT_YYYYMMDD = "YYYY/MM/DD";
	// 日付用フォーマット定数 DDMMYYYY
	public static final String DATEFORMAT_DDMMYYYY = "DD/MM/YYYY";
	// 日付用フォーマット定数 MMDDYYYY
	public static final String DATEFORMAT_MMDDYYYY = "MM/DD/YYYY";

	// 日付用フォーマット定数 (0-23)時分秒＋ミリ秒
	public static final String DATEFORMAT_23HMMSS = "HH:mm:ss.SSS";
	// 日付用フォーマット定数 (1-12)時分秒＋ミリ秒
	public static final String DATEFORMAT_12HMMSS = "hh:mm:ss.SSS";

	// トークンキー
	public static final String TOKEN_KEY = "com.pigthinkingtec.framework.spring.mvc.token.TokenProcessor@token";

	// Batch,Online用Propertiesファイル名（ファイルは別で、ファイル名は同じである）
	public static final String FRAMEWORK_PROPERTY_FILE_NAME = "framework";

	// Propertyファイルから、Batch用User情報を取得するKey
	public static final String BATCH_USER_SOURCE_TYPE = "batch.user.source.type";
	public static final String BATCH_USER_ID_KEY = "batch.user.id";
	public static final String BATCH_USER_LANG_KEY = "batch.user.lang";
	public static final String BATCH_USER_DB_ACCESS_CLASS_KEY = "batch.user.database.access.class";

	// Propertyファイルから、ファイルからMessage内容を取得するメッセージファイルのBasenameを取得するKey
	public static final String AUDIT_LOG_MESSAGE_FILE_BASE_NAME_KEY = "message.source.file.basename";
	
	// ユーザ言語情報とISO Language情報変換Mapping関係Key
	public static final String I18N_USERLANGUAGE_ISOLANGUAGE_MAPPING_KEY = "i18n.user_lang.iso_lang.mapping";
	
	// --------------------------------------------------------
	//接続するDatabase種別
	public static final String DATABASE_TYPE_ORACLE = "ORACLE";
	public static final String DATABASE_TYPE_SQLSERVER = "SQLServer";
	public static final String DATABASE_TYPE_MYSQL = "MySQL";
	public static final String DATABASE_TYPE_POSTGRE = "PostgreSQL";
	
	// --------------------------------------------------------
	/** ジョブツール名前のキー */
	public static final String JOB_TOOL_NAME_KEY = "job.tool.name";
	
	/** ジョブツール名前:SOS-JOBSCHEDULER */
	public static final String JOB_TOOL_NAME_SOS_JOBSCHEDULER = "SOS-JOBSCHEDULER";//JobScheduler
	/** ジョブツール名前:JOBCENTER */
	public static final String JOB_TOOL_NAME_JOBCENTER = "JOBCENTER";//JobCenter

	/** ジョブツールがインストールされているサーバ名またはIPアドレス */
	public static final String JOB_TOOL_SERVER = "job.tool.server";
	
	/** ジョブツールのポート番号 */
	public static final String JOB_TOOL_PORT = "job.tool.port";
	
	/** ジョブツールと接続用ユーザ名 */
	public static final String JOB_TOOL_USER = "job.tool.user";
	
	/** ジョブツールと接続用パースワード */
	public static final String JOB_TOOL_PASSWORD = "job.tool.password";
	
	/** ジョブツールと接続用webservice名 */
	public static final String JOB_TOOL_WEBSERVICE = "job.tool.webserice";
}