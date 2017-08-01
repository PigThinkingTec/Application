package com.pigthinkingtec.framework.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.auditlog.AuditLogUtil;
import com.pigthinkingtec.framework.command.CommandInterface;
import com.pigthinkingtec.framework.databean.AbstractOnlineDataBean;
import com.pigthinkingtec.framework.databean.DataBeanInterface;
import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessInformation;
import com.pigthinkingtec.framework.databean.message.BusinessWarning;
import com.pigthinkingtec.framework.databean.message.MessageContainer;
import com.pigthinkingtec.framework.dbaccess.TransactionManager;
import com.pigthinkingtec.framework.dbaccess.TransactionManagerFactory;
import com.pigthinkingtec.framework.dbaccess.TransactionScope;
import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.DatabaseExceptionFactory;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.exception.InvalidTransactionException;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.Logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service基底クラス
 * 
 * @author yizhou
 * @history
 * 
 */
public abstract class AbstractService implements ServiceInterface {
	/* ログ */
	Log log = LogFactory.getLog(this.getClass().getName());
	/* 出力データ */
	private DataBeanInterface outputDataBean = null;
	/* アプリケーションエラーの集合 */
	private MessageContainer messageContainer = null;
	/* 次のコマンドをスキップするかどうかのサイン */
	private boolean isSkip = false;
	/* 入力データ */
	protected Order order = null;
	/* 出力データ */
	protected Report report = null;
	/* 指定されているセーブポイント */
	private String savepoint = null;
	/* TransactionManager */
	protected TransactionManager transactionManager = null;

	/* 監査ログ出力用 サービスを開始する時に使う :サービス{0}は開始しました。 */
	private static String MESSAGEID_AUDITLOG_SERVICE_START = "";
	/* 監査ログ出力用 正常終了時のログメッセージID */
	private static String MESSAGEID_AUDITLOG = "";
	/* 監査ログ出力用 権限エラー時のログメッセージID */
	private static String MESSAGEID_AUTHORITYERROR = "";

	static {
		/* 監査ログ出力用 サービスを開始する時に使う :サービス{0}は開始しました。 */
		MESSAGEID_AUDITLOG_SERVICE_START = FwPropertyReader.getProperty(
				SystemConstant.MESSAGEID_AUDITLOG_SERVICE_START_KEY, null);

		/* 監査ログ出力用 正常終了時のログメッセージID */
		MESSAGEID_AUDITLOG = FwPropertyReader.getProperty(
				SystemConstant.MESSAGEID_AUDITLOG_KEY, null);

		/* 監査ログ出力用 権限エラー時のログメッセージID */
		MESSAGEID_AUTHORITYERROR = FwPropertyReader.getProperty(
				SystemConstant.MESSAGEID_AUTHORITYERROR_KEY, null);
	}

	/**
	 * Service派生クラスにて、業務ロジックを実装する
	 * 
	 * @throws SystemException
	 */
	protected abstract void process() throws SystemException;

	/**
	 * Outputデータを取得する
	 */
	public abstract Report getReport();

	/**
	 * Inputデータをセットする。
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return this.order;
	}

	/**
	 * 該当Serviceの開始、終了、及び途中情報（Info、Warning,UserError）は監査Logに出力するかFlagを設定する
	 * ※基本は必要な場合、Overrideの形を使うこと defaultは出力すること
	 */
	protected void changeAuditLogOutputFlg() {
		getOrder().setAuditLogOutputFlg(true);
	}

	/**
	 * Commandを実行する Commandでエラーが発生した場合は、次のコマンドの実行をSkipする
	 * 
	 * @param command
	 *            実行するコマンド
	 * @exception SystemException
	 */
	protected void execCommand(CommandInterface command)
			throws SystemException {
		execCommand(command, null);
	}

	/**
	 * 入力データを取得する。
	 * 
	 * @return Returns the inputDataBean.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends DataBeanInterface> T getInputDataBean() {
		return (T) order.getInputDataBean();
	}

	protected void setInputDataBean(DataBeanInterface inputdata) {
		order.setInputDataBean(inputdata);
	}

	/**
	 * 出力データを取得する。
	 * 
	 * @return Returns the outputDataBean.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends DataBeanInterface> T getOutputDataBean() {
		return (T) this.outputDataBean;
	}

	/**
	 * 出力データをセットする。
	 * 
	 * @param outputDataBean
	 *            The outputDataBean to set.
	 */
	protected void setOutputDataBean(DataBeanInterface outputData) {
		this.outputDataBean = outputData;
	}

	/**
	 * パラメータデータを取得する。
	 * 
	 * @return Returns the argumentData.
	 */
	protected Object getArgumentData() {
		return order.getArgumentData();
	}

	/**
	 * パラメータデータを取得する。
	 * 
	 * @return Returns the argumentData.
	 */
	protected void setArgumentData(Object argumentData) {
		order.setArgumentData(argumentData);
	}

	/**
	 * ユーザデータを取得する。
	 * 
	 * @return Returns the userData.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends UserContainer> T getUserContainer() {
		return (T) order.getLoginUser();
	}

	/**
	 * Message集合を取得する
	 * 
	 * @return Returns the businessErrors.
	 */
	protected MessageContainer getMessageContainer() {
		return messageContainer;
	}

	/**
	 * Message集合をセットする
	 * 
	 * @param message
	 */
	protected void setMessageContainer(MessageContainer message) {
		this.messageContainer = message;
	}

	/**
	 * Command Component内でApplicationErrorが発生しているかどうか確認する
	 * エラーが発生していたらTrue、発生していなければFalse
	 * 
	 * @return 実行結果
	 */
	protected boolean hasBusinessError() {
		log.debug("hasBsuinessError start");
		if (messageContainer == null) {
			return false;
		}
		log.debug("hasBsuinessError end");
		return messageContainer.hasError();
	}

	/**
	 * Command Component内でApplicationWarningが発生しているかどうか確認する
	 * 警告が発生していたらTrue、発生していなければFalse
	 * 
	 * @return 実行結果
	 */
	protected boolean hasBusinessWarning() {
		log.debug("hasBusinessWarning start");
		if (messageContainer == null) {
			return false;
		}
		log.debug("hasBusinessWarning end");
		return messageContainer.hasWarning();
	}

	/**
	 * サービスの処理を実行する
	 * 
	 * @exception SystemException
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pigthinkingtec.framework.service.ServiceInterface#execute()
	 */
	public void execute() throws SystemException {
		log.debug("execute start");

		if (getClass() != null) {
			log.info("[Service]=" + getClass().getName() + " start");
		}

		// 監査Logを出力するかを設定する
		// Defaultは出力すること
		changeAuditLogOutputFlg();

		// トランザクションスコープの取得
		int scope = order.getTransactionScope();
		try {
			// トランザクションを開始する。NONの場合はここでは開始しない。
			if (scope == TransactionScope.GLOBAL
					|| scope == TransactionScope.LOCAL) {
				startTransaction();
			}

			if (!order.isBatch()) {
				// Batchである場合は、BatchService側で別途開始Logを出力しているため
				// Batch出ない場合のみ、監査Logにサービスは開始するLogを出力する
				// 注意：startTransaction()の後ろに実装することを前提
				saveStartAuditLog();
			}

			// 実際の処理ロジック
			process();

			// コマンドまたぎのトランザクションの場合はトランザクションをコミットする。
			if (scope == TransactionScope.GLOBAL) {
				// 監査ログの出力
				saveAuditLog();

				// ビジネスエラーが発生した場合のロールバック処理はexecCommand内で行っているので、
				// ここでは何も考えずに一律commitしてしまう。
				commitTransaction();
				/*
				 * if (!hasBusinessError()) { //ビジネスエラーが発生していない場合
				 * commitTrandaction(); } else { //ビジネスエラーが発生している場合
				 * rollbackTrandaction(); }
				 */
			} else if (scope == TransactionScope.NON) {
				// 監査ログの出力
				saveAuditLog();
				commitTransaction();
			}

			if (getClass() != null) {
				log.info("[Service]=" + getClass().getName() + " end");
			}

		} catch (Throwable e) {
			// どんな場合でもロールバックする。
			try {
				// セーブポイントが設定されていなかったり、トランザクションスコープがGLOBAL以外であれば、
				// 通常のロールバック
				if (this.savepoint == null || scope != TransactionScope.GLOBAL) {
					rollbackTransaction();
				} else {
					rollbackToSavepoint(this.savepoint);
				}
				AuditLogUtil.insertLogFromException(e, order);
				commitTransaction();
			} catch (SQLException ex) {
				getLog().error("service execute error: ", e);
				getLog().error("service execute exception process error: ", ex);
				throw DatabaseExceptionFactory.createException(ex);
			}
			// 例外の種類がSQLExceptionの場合はDatabase関連の例外を作成。
			// それ以外の例外はシステムエラーとする。
			if (e instanceof SQLException) {
				throw DatabaseExceptionFactory
						.createException((SQLException) e);
			} else if (e instanceof SystemException) {
				throw (SystemException) e;
			} else {
				throw new SystemException(e);
			}
		} finally {
			// 最終的にトランザクションを終了する。
			try {
				// NONの場合、トランザクションを閉じようとするとエラーになるため、閉じる処理を行わない
				if (scope == TransactionScope.GLOBAL
						|| scope == TransactionScope.LOCAL) {
					endTransaction();
				}
				log.debug("execute end");
			} catch (SQLException exc) {
				throw DatabaseExceptionFactory.createException(exc);
			}

		}
	}

	/**
	 * トランザクションを開始する。
	 * 
	 * @throws SQLException
	 *
	 */
	protected void startTransaction() throws SQLException {
		transactionManager = TransactionManagerFactory.getTransactionManager();
		transactionManager.endTransaction();
		transactionManager.assosiateTransaction(order.getTransactionScope());
	}

	/**
	 * トランザクションを終了する。
	 * 
	 * @throws SQLException
	 */
	protected void endTransaction() throws SQLException {
		transactionManager.endTransaction();
	}

	/**
	 * トランザクションをコミットする
	 * 
	 * @throws SQLException
	 *             ,InvalidTransactionException
	 */
	protected void commitTransaction() throws SQLException,
			InvalidTransactionException {
		transactionManager.commitTransaction();
	}

	/**
	 * トランザクションをロールバックする
	 * 
	 * @throws SQLException
	 *             ,InvalidTransactionException
	 */
	protected void rollbackTransaction() throws SQLException,
			InvalidTransactionException {
		transactionManager.rollbackTransaction();
	}

	/**
	 * ログオブジェクトを取得するメソッド
	 * 
	 * @return
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * 次のコマンドをスキップするか判定する。
	 * 
	 * @return
	 */
	protected boolean isSkip() {
		return isSkip;
	}

	/**
	 * スキップフラグを設定する。
	 * 
	 * @param isSkip
	 * @uml.property name="isSkip"
	 */
	protected void setSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}

	/**
	 * トランザクションにセーブポイントを設定する。
	 * 
	 * @param name
	 *            セーブポイント名
	 * @throws SystemException
	 */
	protected void setSavePoint(String name) throws SystemException {
		try {
			transactionManager.setSavePoint(name);
		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}
	}

	/**
	 * セーブポイントまでトランザクションをロールバックさせる。
	 * 
	 * @param name
	 *            セーブポイント名
	 * @throws SystemException
	 */
	protected void rollbackToSavepoint(String name) throws SystemException {
		try {
			transactionManager.rollbackTransaction(name);
		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}
	}

	/**
	 * コマンドを実行する。エラーが発生しセーブポイントが設定されている場合には、 セーブポイントまでロールバックする。
	 * 
	 * @param command
	 *            実行コマンド
	 * @param name
	 *            セーブポイント
	 * @throws SystemException
	 */
	protected void execCommand(CommandInterface command, String name)
			throws SystemException {
		log.debug("execCommand start");
		int scope = order.getTransactionScope();
		MessageContainer message;

		// セーブポイントを設定
		this.savepoint = name;
		try {
			// コマンド内のトランザクションの場合はトランザクションを開始する。
			// Localの場合もService開始時に行うようにする。
			// if (scope == TransactionScope.LOCAL) {
			// startTransaction();
			// }
			if (!isSkip) {
				// コマンドクラスに入力データを渡す。
				command.setOrder(order);
				// コマンドの処理を実行する。
				command.execute();
				// コマンドの処理結果を取得する。
				message = command.getReport().getMessageContainer();
				addMessage(message);
				if (message != null && message.hasError()) {
					// エラーが発生している場合の処理
					// アプリケーションエラーが発生した場合はセーブポイントまでロールバックする。
					if (name == null || scope != TransactionScope.GLOBAL) {
						rollbackTransaction();
						// localの場合、監査ログを出力する
						if (scope == TransactionScope.LOCAL) {
							// 監査ログの出力
							saveAuditLog();
							commitTransaction();
						}
					} else {
						rollbackToSavepoint(name);
						// localの場合、監査ログを出力する
						if (scope == TransactionScope.LOCAL) {
							// 監査ログの出力
							saveAuditLog();
							commitTransaction();
						}
					}
					// skipの設定
					isSkip = true;
				} else {
					// エラーが発生していない場合の処理。
					// コマンド内のトランザクションの場合はトランザクションをコミットする。
					if (scope == TransactionScope.LOCAL) {
						// 監査ログの出力
						saveAuditLog();

						commitTransaction();
						// Localの場合もServiceの終了時に行うようにする。
						// endTransaction();
					}
				}
			}
			log.debug("execCommand end");
		} catch (SQLException e) {
			// コマンド内でSQLExceptionが発生した場合は変換する。
			// 例外発生時のロールバック処理はexecuteメソッド内で行う。
			log.debug("", e);
			// コマンドの処理結果を取得する。
			message = command.getReport().getMessageContainer();
			addMessage(message);
			throw DatabaseExceptionFactory.createException(e);
		} catch (SystemException e) {
			// コマンドの処理結果を取得する。
			message = command.getReport().getMessageContainer();
			addMessage(message);
			throw e;
		}
	}

	private void addMessage(MessageContainer message) {
		if (message != null) {
			if (getMessageContainer() == null) {
				messageContainer = new MessageContainer();
			}
			getMessageContainer().add(message);
		}
	}

	/**
	 * 権限エラーを保存するメソッド
	 * 
	 * @throws SystemException
	 */
	protected void saveAuthorityError() throws SystemException {
		BusinessError error = new BusinessError(MESSAGEID_AUTHORITYERROR,
				getUserContainer().getUserId(), getUserContainer().getPgmId());
		MessageContainer message = new MessageContainer();
		message.saveError(null, error);
		addMessage(message);

		try {
			// LocalかつCommandが実行されない場合、ログが取れないのでここで明示的にログ出力
			saveAuditLog();
			commitTransaction();
		} catch (SQLException e) {
			log.error(e.getMessage());
			throw new SystemException(e);
		} catch (SystemException ex) {
			throw ex;
		}
	}

	protected void saveBusinessError(BusinessError error) {
		MessageContainer message = new MessageContainer();
		message.saveError(null, error);
		addMessage(message);
	}

	protected void saveBusinessWarning(BusinessWarning warning) {
		MessageContainer message = new MessageContainer();
		message.saveWarning(null, warning);
		addMessage(message);
	}

	protected void saveBusinessInfo(BusinessInformation info) {
		MessageContainer message = new MessageContainer();
		message.saveInformation(null, info);
		addMessage(message);
	}

	/**
	 * 監査ログの出力を行うメソッド。 エラーがあればその個数だけログを出力し、 エラーが無ければ正常終了1件のログを出力する。
	 */
	@SuppressWarnings("rawtypes")
	protected void saveAuditLog() throws SystemException {

		if (!this.order.isAuditLogOutput()) {
			// 監査Logは出力しない場合、何もしないこと
			return;
		}

		String serviceName = this.getClass().getSimpleName();

		// LogInfoをBeanから取得
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
									messageArgs[i] = errorArgs[i] != null ? errorArgs[i]
											.toString() : "null";
								}
							}

							// 監査Logを出力する
							AuditLogUtil.writeAuditLog(order, serviceName,
									logInfo, messageId, (Object[]) messageArgs);

							// ログ出力済みフラグを立てる
							error.setLogOutputComplete(true);
						}
					}
				}
				// エラーが無い場合
			} else {
				messageId = MESSAGEID_AUDITLOG; // サービス{0}は正常終了しました。
				messageArgs[0] = serviceName;

				// 監査Logを出力する
				AuditLogUtil.writeAuditLog(order, serviceName, logInfo,
						messageId, (Object[]) messageArgs);
			}
		} catch (DatabaseException DBException) {
			log.error("DB Error:監査ログ出力に失敗しました。MessageID：" + messageId
					+ " MessageArgs:" + messageArgs.toString() + " logInfo:"
					+ logInfo);
			throw new SystemException(DBException);
		} catch (ArrayIndexOutOfBoundsException arrayException) {
			// 基本的には発生しないエラー
			log.error(arrayException.getMessage());
			throw new SystemException(arrayException);
		}
	}

	/**
	 * 開始監査Log出力
	 * 
	 * @throws SystemException
	 */
	protected void saveStartAuditLog() throws SystemException {

		if (!this.order.isAuditLogOutput()) {
			// 監査Logは出力しない場合、何もしないこと
			return;
		}

		try {
			// サービス{0}は開始しました。
			saveAuditLog(MESSAGEID_AUDITLOG_SERVICE_START);
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
	protected void saveAuditLog(String msgId) throws SystemException {

		if (!this.order.isAuditLogOutput()) {
			// 監査Logは出力しない場合、何もしないこと
			return;
		}

		String computerName = null;
		String jobId = null;
		String programId = getUserContainer().getPgmId();
		String serviceName = this.getClass().getSimpleName();
		String userId = getUserContainer().getUserId();
		String lang = getUserContainer().getUserLang();

		String messageId = msgId;
		String[] messageArgs = new String[7];

		messageArgs[0] = this.getClass().getSimpleName();
		;
		messageArgs[1] = getPara();// パラメータ取得

		try {
			AuditLogUtil.writeAuditLog(computerName, jobId, programId,
					serviceName, messageId, userId, lang, messageArgs[1], // para設定
					(Object[]) messageArgs);

		} catch (DatabaseException DBException) {
			log.error("saveAuditLog DBException occured. msgId = " + msgId
					+ ", messageArgs[1] = " + messageArgs[1]
					+ ", DBException = [" + DBException.toString() + "]");
			throw new SystemException(DBException.toString(), DBException);

		} catch (ArrayIndexOutOfBoundsException arrayException) {
			// 基本的には発生しないエラー
			log.error("saveAuditLog arrayException occured. msgId = " + msgId
					+ ", messageArgs[1] = " + messageArgs[1]
					+ ", arrayException = [" + arrayException.getMessage()
					+ "]");
			throw new SystemException(arrayException);

		} catch (Exception e) {
			log.error("saveAuditLog exception occured. msgId = " + msgId
					+ ", messageArgs[1] = " + messageArgs[1]
					+ ", exception = [" + e.getMessage() + "]");
			throw new SystemException(e);
		}

	}

	/**
	 * 監査ログ出力内容取得
	 * Loggingアノテーションが付いたフィールドの名称/値を連結した文字列を作成
	 * [フィールド名=値; ･･･ ;フィールド名=値]
	 * @return
	 * @throws SystemException
	 */
	protected String getPara() throws SystemException {
		// LogInfoをBeanから取得
		StringBuffer logInfo = new StringBuffer();
		try {
			// フィールドでループ
			Class<?> clazz = order.getInputDataBean().getClass();

			// クラス内のフィールドを参照し、Loggingアノテーションがあれば監査ログ出力文字列に追加する
			// beanのsuperクラスを順にループ処理し、基底クラス(AbstractOnlineDataBean)になったら終了
			// インターフェースを実装しているクラスの場合はObjectクラスになったら終了
			while (!clazz.equals(AbstractOnlineDataBean.class)
					&& !clazz.equals(Object.class)) {

				for (Field field : clazz.getDeclaredFields()) {
					// privateフィールドも強制的に読み込み可に設定
					field.setAccessible(true);
					// Loggingアノテーションがついたフィールドのみ書き出し
					for (Annotation annot : field.getAnnotations()) {
						if (Logging.class.getName().equals(
								annot.annotationType().getName())) {
							// 区切り文字の挿入
							if (logInfo.length() != 0) {
								logInfo.append(";");
							}

							// フィールドの値を取得しnullチェック
							Object fieldValue = field.get(order
									.getInputDataBean());
							if (fieldValue != null) {
								logInfo.append(field.getName() + "="
										+ fieldValue.toString());
							} else {
								logInfo.append(field.getName() + "=null");
							}
						}
					}
				}
				clazz = clazz.getSuperclass();
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

		return logInfo.toString();
	}
}
