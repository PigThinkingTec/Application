package com.pigthinkingtec.framework.spring.database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.pigthinkingtec.framework.dbaccess.Transaction;
import com.pigthinkingtec.framework.dbaccess.TransactionConfig;
import com.pigthinkingtec.framework.dbaccess.TransactionManager;
import com.pigthinkingtec.framework.dbaccess.TransactionManagerFactory;
import com.pigthinkingtec.framework.dbaccess.TransactionScope;
import com.pigthinkingtec.framework.exception.InvalidTransactionException;

/**
 * SpringのTransactionManagerをラッピングしたTransactionManagerクラス
 * 
 * @author yizhou
 *
 */
@SuppressWarnings("serial")
public class SpringTransactionManager extends TransactionManager implements Serializable {
	
	/** Springのトランザクションマネージャオブジェクト */
	@Autowired
	PlatformTransactionManager transactionManager;
	
	/** SavePoint用の情報を格納するMapオブジェクト */
	private Map<String, Object> savePointMap = new HashMap<String, Object>();
	
	private static final ThreadLocal<TransactionStatus> thLocal = new ThreadLocal<TransactionStatus>();
	
	/**
	 * デフォルトコンストラクタ
	 */
	public SpringTransactionManager() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/**
	 * トランザクションオブジェクトを取得するメソッド
	 */
	@Override
	public Transaction getCurrentTransaction() {
		TransactionConfig config = TransactionManagerFactory.getTransactionConfig();
		Transaction tx = new Transaction(TransactionScope.GLOBAL, config);
		return tx;
	}

	/**
	 * トランザクションを開始するメソッド
	 */
	@Override
	public void assosiateTransaction() {
		assosiateTransaction(0);
	}

	/**
	 * トランザクションを開始するメソッド
	 */
	@Override
	public void assosiateTransaction(int scope) {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		if (status == null || status.isCompleted()) {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			status = transactionManager.getTransaction(def);
			thLocal.set(status);
		}
	}

	/**
	 * トランザクションをコミットするメソッド
	 */
	@Override
	public void commitTransaction() throws SQLException,
			InvalidTransactionException {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		if (status != null && !status.isCompleted()) {
			transactionManager.commit(status);
		}
	}

	/**
	 * トランザクションをロールバックするメソッド
	 */
	@Override
	public void rollbackTransaction() throws SQLException,
			InvalidTransactionException {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		if (status != null && !status.isCompleted()) {
			transactionManager.rollback(status);
		}
	}

	/**
	 * トランザクションを終了するメソッド
	 */
	@Override
	public void endTransaction() throws SQLException {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		if (status != null && !status.isCompleted()) {
			transactionManager.rollback(status);
		}
		thLocal.set(null);
	}

	/**
	 * トランザクションをセーブポイントまでロールバックするメソッド
	 */
	@Override
	public void rollbackTransaction(String name) throws SQLException,
			InvalidTransactionException {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		if (status != null && !status.isCompleted()) {
			Object savepoint = savePointMap.get(name);
			if (savepoint != null) {
				status.rollbackToSavepoint(savepoint);
				transactionManager.rollback(status);
			}
		}
	}

	/**
	 * セーブポイントを設定するメソッド
	 */
	@Override
	public void setSavePoint(String name) throws SQLException,
			InvalidTransactionException {
		TransactionStatus status = (TransactionStatus)thLocal.get();
		Object savepoint = status.createSavepoint();
		savePointMap.put(name, savepoint);
	}
}
