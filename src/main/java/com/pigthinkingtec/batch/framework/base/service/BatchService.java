package com.pigthinkingtec.batch.framework.base.service;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.ProcessManagementBean;
import com.pigthinkingtec.batch.framework.common.utils.LogUtil;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeKeyUtil;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeUtil;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.auditlog.AuditLogUtil;
import com.pigthinkingtec.framework.databean.AbstractCOMMONTableBean;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessWarning;
import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.service.AbstractService;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.Logging;
import com.pigthinkingtec.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName: BatchService
 * @Description: バッチ用の基底Serviceクラス
 * @author yizhou
 * 
 */
public abstract class BatchService extends AbstractService {

	/** ログオブジェクト */
	private static Log log = LogFactory.getLog(BatchService.class);

	/* 監査ログ出力用 開始時のログメッセージID： 「プログラム{0}を開始しました。（引数：{1} */
	private static String MESSAGEID_AUDITLOG_START = "001001";
	/* 監査ログ出力用 正常終了、警告なしの場合時のログメッセージID(プログラム{0}は正常終了しました。) */
	private static String MESSAGEID_AUDITLOG_END_OK = "001010";
	/* 監査ログ出力用 正常終了、警告ありの場合時のログメッセージID(プログラム{0}は正常終了しましたが、警告が発生しました。) */
	private static String MESSAGEID_AUDITLOG_END_WARNING = "001011";
	/*
	 * 監査ログ出力用 異常終了場合時のログメッセージID(プログラム{0}は異常終了しました。)
	 * (システムエラーとユーザエラーは同じメッセージを使うこと)
	 */
	private static String MESSAGEID_AUDITLOG_END_EXCEPTION = "001012";

	static {

		/* 監査ログ出力用 開始時のログメッセージID： 「プログラム{0}を開始しました。（引数：{1} */
		MESSAGEID_AUDITLOG_START = FwPropertyReader.getProperty(BatchConstants.BATCH_AUDITLOG_START_MESSAGEID_KEY,
				null);

		/* 監査ログ出力用 正常終了、警告なしの場合時のログメッセージID(プログラム{0}は正常終了しました。) */
		MESSAGEID_AUDITLOG_END_OK = FwPropertyReader.getProperty(BatchConstants.BATCH_AUDITLOG_OK_END_MESSAGEID_KEY,
				null);

		/* 監査ログ出力用 正常終了、警告ありの場合時のログメッセージID(プログラム{0}は正常終了しましたが、警告が発生しました。) */
		MESSAGEID_AUDITLOG_END_WARNING = FwPropertyReader
				.getProperty(BatchConstants.BATCH_AUDITLOG_WARNING_END_MESSAGEID_KEY, null);

		/*
		 * 監査ログ出力用 異常終了場合時のログメッセージID(プログラム{0}は異常終了しました。)
		 * (システムエラーとユーザエラーは同じメッセージを使うこと)
		 */
		MESSAGEID_AUDITLOG_END_EXCEPTION = FwPropertyReader
				.getProperty(BatchConstants.BATCH_AUDITLOG_EXCEPTION_END_MESSAGEID_KEY, null);
	}

	private boolean isExcutedCommand = true;

	// 処理結果ステータス
	private String status = null;
	
	// DBからの戻り値Keyを取得する
	protected String RET_OK_KEY = "";
	protected String RET_WARNING_KEY = "";
	protected String RET_USER_ERR_KEY = "";
	protected String RET_EXCEPTION_KEY = "";
	
	// DBからの戻り値を取得する
	protected int RET_OK = 0;
	protected int RET_WARNING = 2;
	protected int RET_USER_ERR = 8;
	protected int RET_EXCEPTION = 9;
	
	/**
	 * Service実行結果のステータスを設定する
	 * 
	 * @param status
	 */
	protected void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Service実行結果のステータスを取得する
	 * 
	 * @return
	 */
	protected String getStatus() {
		return status;
	}

	/**
	 * Service派生クラスにて、業務ロジックを実装する
	 * 
	 * @throws SystemException
	 */
	protected void process() throws SystemException {
		log.info("BatchService start.");

		ProcessManagementBean manager = (ProcessManagementBean) getInputDataBean();
		
		// DBからの戻り値Keyを取得する
		RET_OK_KEY = ReturnCodeKeyUtil.getReturnCodeKey(BatchConstants.BATCH_RETURN_CODE_KEY_OK, manager.getBatchPgId());
		RET_WARNING_KEY = ReturnCodeKeyUtil.getReturnCodeKey(BatchConstants.BATCH_RETURN_CODE_KEY_WARNING, manager.getBatchPgId());
		RET_USER_ERR_KEY = ReturnCodeKeyUtil.getReturnCodeKey(BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, manager.getBatchPgId());
		RET_EXCEPTION_KEY = ReturnCodeKeyUtil.getReturnCodeKey(BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION, manager.getBatchPgId());
		
		// DBからの戻り値を取得する
		RET_OK = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_OK, manager.getBatchPgId());
		RET_WARNING = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_WARNING, manager.getBatchPgId());
		RET_USER_ERR = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, manager.getBatchPgId());
		RET_EXCEPTION = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION, manager.getBatchPgId());

		// 戻り値を初期化する
		manager.setResultCode(RET_OK);
		manager.setResultCodeKey(RET_OK_KEY);

		// バッチの引数を取得する。
		String[] params = (String[]) getArgumentData();

		// 処理開始Log
		LogUtil.start(manager.getBatchPgId(), getUserContainer(), manager.getBatchPgId(), params);

		// 開始監査Logを出力する
		saveStartAuditLog();

		try {
			// 処理開始時に必要な準備処理を行う(必要な場合、各Serviceを実装)
			// もし状態処理管理DBで状態を管理する場合ここで状態管理DBを更新する
			doStartProcess();

			// 業務ロジックを実装する(各Serviceを実装)
			subProcess();

			// 正常終了時終了処理を行う(必要な場合、各Serviceを実装)
			// もし状態処理管理DBで状態を管理する場合ここで状態管理DBを更新する
			doEndProcess();

			// 終了状態を設定
			if (StringUtils.equals(SystemConstant.STATUS_SUCCESS, getStatus())) {
				if (RET_OK_KEY.equals(manager.getResultCodeKey())) {//戻り値Keyを比較
					// 正常終了する場合
					saveOKEndAuditLog();
					
				} else if (RET_WARNING_KEY.equals(manager.getResultCodeKey())) {
					// Warning終了する場合
					saveWarningEndAuditLog();
					
				} else if (RET_USER_ERR_KEY.equals(manager.getResultCodeKey())) {
					// 業務エラー終了する場合
					saveErrEndAuditLog();
				} else {
					// 業務エラーと見なす
					log.warn("The manager.getResultCode() = [" + manager.getResultCode() + "] is invalid !! ");
					saveErrEndAuditLog();
				}

			} else { // SystemConstant.STATUS_FAULTの場合
				// 業務エラー終了する場合
				saveErrEndAuditLog();
			}

			// 終了ログ
			LogUtil.end(manager.getBatchPgId(), getUserContainer(), manager.getBatchPgId());
			log.info("BatchService end.");

		} catch (Exception e) {
			try {
				log.debug("BatchService: process(): Exception occoured.", e);
				// 親クラスでrollback処理を行うため、ここでしないこと
				// rollbackTransaction();

				// 処理戻り値を設定する
				manager.setResultCode(RET_EXCEPTION);

				// 異常終了時、もし状態処理管理DBで状態を管理する場合ここで状態管理DBを更新する
				doEndProcess();

				LogUtil.error(manager.getBatchPgId(), getUserContainer(), manager.getBatchPgId());
				log.fatal("BatchService: process(): Exception occoured.", e);

				// 異常終了の監査Logは、親クラスAbstractService中に出力するため、ここで対応しないこと。
				throw new SystemException(e);

			} catch (Exception e2) {
				log.fatal("BatchService: process(): Exception occoured.", e2);

				throw new SystemException(e2);
			}
		}

	}

	/**
	 * 
	 * @Title: doStartProcess
	 * @Description: 処理開始時処理(各準備、必要なパラメータの設定など)が必要な場合ここで実装。
	 * @throws SystemException
	 */
	protected void doStartProcess() throws SystemException {

	}

	/**
	 * 
	 * @Title: doEndProcess
	 * @Description: 処理終了時点特殊な処理が必要な場合ここで実装。
	 * @throws SystemException
	 */
	protected void doEndProcess() throws SystemException {

	}

	/**
	 *  業務ロジックを実装する
	 */
	protected abstract void subProcess() throws SystemException;

	/**
	 * Outputデータを取得する
	 */
	public Report getReport() {
		return null;
	}

	/**
	 * 開始監査Log出力
	 * 
	 * @throws SystemException
	 */
	@Override
	protected void saveStartAuditLog() throws SystemException {
		try {
			// プログラム{0}を開始しました。（引数：{1}
			saveAuditLog(MESSAGEID_AUDITLOG_START);
			commitTransaction();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new SystemException(e);
		} catch (SystemException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Error終了Logを出力
	 * 
	 * @throws SystemException
	 */
	protected void saveErrEndAuditLog() throws SystemException {
		try {
			if (!isExcutedCommand()) {
				// commandを実行前に既にエラー発生した場合(基本はパラメータチェック系)
				// エラーメッセージを出すために
				saveAuditLog();
				commitTransaction();
			}
			// プログラム{0}は正常終了しましたが、エラーが発生しました。
			// システムエラーとユーザエラーは同じメッセージを使うこと
			saveAuditLog(MESSAGEID_AUDITLOG_END_EXCEPTION);
			commitTransaction();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new SystemException(e);
		} catch (SystemException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Error終了Logを出力
	 * 
	 * @throws SystemException
	 */
	protected void saveWarningEndAuditLog() throws SystemException {
		try {
			if (!isExcutedCommand()) {
				// commandを実行前に既にエラー発生した場合(基本はパラメータチェック系)
				// エラーメッセージを出すために
				saveAuditLog();
				commitTransaction();
			}
			// プログラム{0}は正常終了しましたが、警告が発生しました。
			saveAuditLog(MESSAGEID_AUDITLOG_END_WARNING);
			commitTransaction();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new SystemException(e);
		} catch (SystemException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * 正常終了Logを出力
	 * 
	 * @throws SystemException
	 */
	protected void saveOKEndAuditLog() throws SystemException {
		try {
			// プログラム{0}は正常終了しました。
			saveAuditLog(MESSAGEID_AUDITLOG_END_OK);
			commitTransaction();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new SystemException(e);
		} catch (SystemException ex) {
			log.error(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Logを出力
	 * 
	 * @throws SystemException
	 */
	@Override
	protected void saveAuditLog(String msgId) throws SystemException {

		String computerName = null;
		String jobId = getUserContainer().getJobId();//親クラスAbstractServiceと違いところ
		String programId = getUserContainer().getPgmId();
		String serviceName = this.getClass().getSimpleName();
		String userId = getUserContainer().getUserId();
		String lang = getUserContainer().getUserLang();

		String messageId = msgId;
		String[] messageArgs = new String[7];

		messageArgs[0] = programId;////親クラスAbstractServiceと違いところ
		messageArgs[1] = getPara();// パラメータ取得

		try {
			AuditLogUtil.writeAuditLog(computerName, jobId, programId, serviceName, messageId, userId, lang,
					messageArgs[1], // para設定
					(Object[]) messageArgs);

		} catch (DatabaseException DBException) {
			log.error("saveAuditLog DBException occured. msgId = " + msgId + ", messageArgs[1] = " + messageArgs[1]
					+ ", DBException = [" + DBException.toString() + "]");
			throw new SystemException(DBException.toString(), DBException);

		} catch (ArrayIndexOutOfBoundsException arrayException) {
			// 基本的には発生しないエラー
			log.error("saveAuditLog arrayException occured. msgId = " + msgId + ", messageArgs[1] = " + messageArgs[1]
					+ ", arrayException = [" + arrayException.getMessage() + "]");
			throw new SystemException(arrayException);

		} catch (Exception e) {
			log.error("saveAuditLog exception occured. msgId = " + msgId + ", messageArgs[1] = " + messageArgs[1]
					+ ", exception = [" + e.getMessage() + "]");
			throw new SystemException(e);
		}

	}

	@Override
	protected String getPara() throws SystemException {
		// LogInfoをBeanから取得
		StringBuffer logInfo = new StringBuffer();

		Object obj = order.getArgumentData();
		if (obj instanceof AbstractCOMMONTableBean) {
			// commandを実行後、ArgumentDataの内容は、string[]ではなく、databeanなったため、下記のように取得
			try {
				// フィールドでループ
				for (Field field : order.getArgumentData().getClass().getDeclaredFields()) {
					// privateフィールドも強制的に読み込み可に設定
					field.setAccessible(true);
					// Loggingアノテーションがついたフィールドのみ書き出し
					for (Annotation annot : field.getAnnotations()) {
						if (Logging.class.getName().equals(annot.annotationType().getName())) {
							// 区切り文字の挿入
							if (logInfo.length() != 0) {
								logInfo.append(";");
							}

							// フィールドの値を取得しnullチェック
							Object fieldValue = field.get(order.getArgumentData());
							if (fieldValue != null) {
								logInfo.append(field.getName() + "=" + fieldValue.toString());
							} else {
								logInfo.append(field.getName() + "=null");
							}
						}
					}
				}
			} catch (IllegalArgumentException argException) {
				// 基本的には発生しないエラー
				log.error(argException.getMessage());
				throw new SystemException(argException);
			} catch (IllegalAccessException accessException) {
				// 基本的には発生しないエラー
				log.error(accessException.getMessage());
				throw new SystemException(accessException);
			}
		} else {
			// commandを実行前の状態
			// この時点、String配列の状態
			try {
				String[] paras = (String[]) order.getArgumentData();

				if (paras == null || paras.length == 0) {
					logInfo.append("No parameter is setted.");
					return logInfo.toString();
				}

				logInfo.append("[");

				for (int i = 0; i < paras.length; i++) {

					if (StringUtil.isEmpty(paras[i])) {
						logInfo.append("NULL");
					} else {
						logInfo.append(paras[i]);
					}
					logInfo.append(" ");
				}
				logInfo.append("]");

			} catch (IllegalArgumentException argException) {
				// 基本的には発生しないエラー
				log.error(argException.getMessage());
			}
		}

		return logInfo.toString();
	}

	/**
	 * 監査ログの出力を行うメソッド。 エラーがあればその個数だけログを出力し、 エラーが無ければ正常終了1件のログを出力する。
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected void saveAuditLog() throws SystemException {
		String computerName = null;
		String programId = getUserContainer().getPgmId();
		String jobId = getUserContainer().getJobId();
		String serviceName = this.getClass().getSimpleName();
		String userId = getUserContainer().getUserId();
		String lang = getUserContainer().getUserLang();

		String logInfo = getPara();

		String messageId = "";
		String[] messageArgs = new String[7];

		try {
			// エラーがあるか判断 エラーがある場合
			if (hasBusinessError()) {
				List list = getMessageContainer().getErrorList();
				// エラーの回数分ループ
				for (Object value : list) {
					// キャスト(基本的にはすべてBusinessErrorのはず)
					if (value instanceof BusinessError) {
						BusinessError error = (BusinessError) value;

						// ログ出力済みかどうかを判定
						if (!error.isLogOutputComplete()) {
							// メッセージIDの取得
							messageId = error.getKey();

							// 置換変数の取得
							Object[] errorArgs = error.getValues();
							if (errorArgs != null) {
								for (int i = 0; i < errorArgs.length; i++) {
									messageArgs[i] = errorArgs[i] != null ? errorArgs[i].toString() : "null";
								}
							}

							AuditLogUtil.writeAuditLog(computerName, jobId, programId, serviceName, messageId,
									userId, lang, logInfo, // para設定
									(Object[]) messageArgs);

							// ログ出力済みフラグを立てる
							error.setLogOutputComplete(true);
						}
					}
				}

				// Waringがある場合
			} else if (hasBusinessWarning()) {
				List list = getMessageContainer().getWarningList();
				// エラーの回数分ループ
				for (Object value : list) {

					// キャスト(基本的にはすべてhasBusinessWarningのはず)
					if (value instanceof BusinessWarning) {
						BusinessWarning warning = (BusinessWarning) value;

						// ログ出力済みかどうかを判定
						if (!warning.isLogOutputComplete()) {
							// メッセージIDの取得
							messageId = warning.getKey();

							// 置換変数の取得
							Object[] warningArgs = warning.getValues();
							if (warningArgs != null) {
								for (int i = 0; i < warningArgs.length; i++) {
									messageArgs[i] = warningArgs[i] != null ? warningArgs[i].toString() : "null";
								}
							}

							AuditLogUtil.writeAuditLog(computerName, jobId, programId, serviceName, messageId,
									userId, lang, logInfo, // para設定
									(Object[]) messageArgs);
						}
					}
				}

				// エラーが無い場合
			} else {
				// 正常終了Logは既にServiceレベルで出力しているため、ここで対応しないこと。
			}
		} catch (DatabaseException DBException) {
			log.error("DB Error:監査ログ出力に失敗しました。MessageID：" + messageId + " MessageArgs:" + messageArgs.toString()
					+ " logInfo:" + logInfo.toString());
			throw new SystemException(DBException);
		} catch (ArrayIndexOutOfBoundsException arrayException) {
			// 基本的には発生しないエラー
			log.error(arrayException.getMessage());
			throw new SystemException(arrayException);
		}
	}

	/**
	 * commandを実行したかどうかを示すFlgを取得
	 * 
	 * @return
	 */
	public boolean isExcutedCommand() {
		return isExcutedCommand;
	}

	/**
	 * commandを実行したかどうかを示すFlgを設定する
	 * 
	 * @return
	 */
	public void setExcutedCommand(boolean isExcutedCommand) {
		this.isExcutedCommand = isExcutedCommand;
	}
}
