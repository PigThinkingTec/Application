package com.pigthinkingtec.framework.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.framework.exception.InvalidTransactionException;

/**
 * トランザクションを管理するシングルトンクラス。
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TransactionManager {
	
	//当クラスのインスタンス
	@SuppressWarnings("unused")
	private static TransactionManager instance;
	//トランザクションコンテキスト格納領域
	@SuppressWarnings("rawtypes")
	protected ThreadLocal trxThreadLocal = new ThreadLocal();
	//トランザクションタイムアウトのチェックを行うかどうか。
	@SuppressWarnings("unused")
	private static boolean TIMEOUT_CHECK = false;
	//トランザクションタイムアウト時間
	private static long DEFAULT_TIMEOUT = 120000;
	//トランザクション分離レベル
	private static int DEFAULT_ISOLATIONLEBEL = Connection.TRANSACTION_READ_COMMITTED;
	//トランザクションコンフィグ
	private TransactionConfig conf;
	//コンフィグパス
	@SuppressWarnings("unused")
	private static final String FILE_NAME = "transaction.xml";
	//ログファイル
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * コンストラクタ
	 *
	 */
	public TransactionManager() {
		conf = TransactionManagerFactory.getTransactionConfig();
		TIMEOUT_CHECK = conf.isAvailable();
		DEFAULT_TIMEOUT = conf.getLifeTime();
		DEFAULT_ISOLATIONLEBEL = conf.getIsolationLevel();
		TransactionScope.setDefaultScope(conf.getScope());
	}
	
	/**
	 * トランザクションのタイムアウト時間
	 * 
	 */
	@SuppressWarnings("unused")
	private long getDefaultTaimeout() {
		return DEFAULT_TIMEOUT;
	}
	
	/**
	 * トランザクションの分離レベルを取得する。
	 * 
	 * @return isolation lebel
	 */
	@SuppressWarnings("unused")
	private int getDefaultIsolateionLebel() {
		return DEFAULT_ISOLATIONLEBEL;
	}
	
	/**
	 * スレッドに関連づけられたトランザクションコンテキストを取得します。
	 * 
	 * @return Transaction Object
	 */
	public Transaction getCurrentTransaction() {
		//start
		log.debug("getCurrentTransaction start");
		if(log.isDebugEnabled()){
			log.debug(trxThreadLocal.toString());
		}
		
		Transaction trx = (Transaction)trxThreadLocal.get();
				
		//end
		log.debug("getCurrentTransaction end");
		
		return trx;
	}
	/**
	 * トランザクションをスレッドに関連づける。
	 *
	 */
	public void assosiateTransaction() {
		assosiateTransaction(TransactionScope.getDefaultScope());
	}
	
	/**
	 * トランザクションをスレッドに関連づける。
	 * 
	 * @param scope トランザクションスコープ
	 */
	@SuppressWarnings("unchecked")
	public void assosiateTransaction(int scope) {
		//start
		log.debug("assosiateTransaction start");
		
		Transaction trx = null;
		trx = getCurrentTransaction();
		if (trx == null) {
			//まだトランザクションが生成されていない場合には生成する。
			trx = new Transaction(scope,conf);
			trxThreadLocal.set(trx);
		}
		//end
		log.debug("assosiateTransaction end");
	}
	
	/**
	 * トランザクションをコミットする
	 * 
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	public void commitTransaction() throws SQLException, InvalidTransactionException {
		//start
		log.debug("commitTransaction start");
		
		Transaction trx = (Transaction)trxThreadLocal.get();
		if (trx != null) {
			trx.commit();
		}
		//end
		log.debug("commitTransaction end");
	}
	
	/**
	 * トランザクションをロールバックする。
	 * 
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	public void rollbackTransaction() throws SQLException, InvalidTransactionException {
		rollbackTransaction(null);
	}
	
	/**
	 * トランザクションを終了する。
	 * 
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public void endTransaction() throws SQLException {
		//start
		log.debug("endTransaction start");
		
		Transaction trx = (Transaction)trxThreadLocal.get();
		//スレッドとの関連をはずす。
		trxThreadLocal.set(null);
		if (trx != null) {
			trx.close();
		}
		//end
		log.debug("endTransaction end");
	}
	
	/**
	 * 
	 * オブジェクトが破棄される際に呼ばれる処理。
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}
	
	/**
	 * トランザクションをロールバックする。
	 * 
	 * @param name セーブポイント名
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	public void rollbackTransaction(String name)
		throws SQLException, InvalidTransactionException {
		//start
		log.debug("rollbackTransaction start");
		
		Transaction trx = (Transaction)trxThreadLocal.get();
		if (trx != null) {
			trx.rollback(name);
		}
		//end
		log.debug("rollbackTransaction end");
	}
	
	/**
	 * トランザクションにセーブポイントを設定する。
	 * 
	 * @param name セーブポイント名
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	public void setSavePoint(String name)
		throws SQLException, InvalidTransactionException {
		log.debug("setSavePoint start");
		
		Transaction trx = (Transaction)trxThreadLocal.get();
		if (trx != null) {
			//セーブポイントを設定する。
			trx.setSavePoint(name);
		}
		log.debug("setSavePoint end");
	}

	/**
	 * トランザクションマネージャを取得する。
	 * 
	 * @return TransactionManager Object
	 */
	@Deprecated
	public static TransactionManager getInstance() {
		//インスタンスが生成されていなければ生成する。
		return TransactionManagerFactory.getTransactionManager();
	}
}
