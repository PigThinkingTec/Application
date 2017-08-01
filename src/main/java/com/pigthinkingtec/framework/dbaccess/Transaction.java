package com.pigthinkingtec.framework.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pigthinkingtec.framework.exception.InvalidTransactionException;

/**
 * トランザクションコンテキストクラス
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class Transaction {
	
	//トランザクションの分離レベル
	private int isolationLevel = 0;
	//タイマーを使うかどうか
	private boolean timeoutFlg = false;
	//トランザクションのタイムアウトまでの時間（ミリ秒）
	private long maxLifeTime = 120000;
	//タイムアウトチェックの間隔（ミリ秒）
	private long interval = 1000;
	//タイムアウトチェックタイマー
	private Timer timer;
	//コミットモード
	private boolean autoCommitMode = false;
	//トランザクション開始日時
	private long beginTime;
	//このトランザクションに使用するコネクション
	private Connection connection = null;
	//トランザクションが破棄されているかどうか
	private boolean finalizeFlg = false;
	//セーブポイントを格納するオブジェクト
	private Map<String, Savepoint> savePointMap = new HashMap<String, Savepoint>();
	//ログ
	private Log log = LogFactory.getLog(this.getClass());
	// コネクションの取得元名
	private String connectionSourceName = null;
		
	/**
	 * コンストラクタ
	 * 
	 * @param scope commitするタイミングを決定するscope
	 * @param config トランザクションの設定情報
	 */
	public Transaction(int scope, TransactionConfig config) {
		//start
		log.debug("new Transaction start");
		
		this.isolationLevel  = config.getIsolationLevel();
		this.timeoutFlg = config.isAvailable();
		if (timeoutFlg) {
			this.interval = config.getInterval();
			this.maxLifeTime = config.getLifeTime();
		}
		//トランザクション管理しない場合は、
		//オートコミットをオフにする。
		if (scope == TransactionScope.NON) {
			this.autoCommitMode = true;
		} else {
			this.autoCommitMode = false;
		}
		//end
		log.debug("new Transaction end");
		
	}

	/**
	 * トランザクションを開始する。
	 *
	 * @throws SQLException
	 */
	void begin() throws SQLException {
		//start
		log.debug("begin start");
		
		if (connection == null) {
			connection = ConnectionUtil.getInstance().getNewConnection(this.connectionSourceName);
			connection.setTransactionIsolation(this.isolationLevel);
			connection.setAutoCommit(this.autoCommitMode);
		}
		//タイマー機能を使う場合の処理。
		if (timeoutFlg) {
			beginTime = System.currentTimeMillis();
			timer = new Timer();
			timer.schedule(new TransactionTimer(this),this.interval,this.interval);
		}
		//end
		log.debug("begin end");
	}
	
	/**
	 * トランザクションをクローズする。
	 * 
	 * @throws SQLException
	 */
	void close() throws SQLException {
		//start
		log.debug("close start");
		
		if (connection != null && !connection.isClosed()) {
			if (! connection.getAutoCommit()){
				connection.rollback();
			}
			connection.close();
		}
		connection = null;
		finalize();
		//end
		log.debug("close end");
	}
	
	/**
	 * トランザクションをコミットする。
	 * 
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	void commit() throws SQLException, InvalidTransactionException {
		//start
		log.debug("commit start");
		
		synchronized (this) {
			if (isValid()) {
				if (isActive()) {		
					connection.commit();
				}
			} else {
				//トランザクションが破棄されている。
				throw new InvalidTransactionException("transaction invalid");
			}
		}
		//end
		log.debug("commit end");
	}
	
	/**
	 * トランザクションをロールバックする。
	 * 
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	void rollback() throws SQLException, InvalidTransactionException {
		rollback(null);
	}
	
	/**
	 * このオブジェクトが破棄される際に呼ばれるメソッド
	 * 
	 */
	protected void finalize() {
		//start
		log.debug("finalize start");

		try {
			super.finalize();
			if (connection != null && !connection.isClosed()) {
				if (! connection.getAutoCommit()){
					connection.rollback();
				}
				connection.close();
				connection = null;
			}
			if (timeoutFlg && timer != null) {
				timer.cancel();
				timer = null;
			}
		} catch (Throwable e) {
			log.error("finalize error", e);
		} finally {
			this.finalizeFlg = true;
			//end
			log.debug("finalize end");
		}
	}


	/**
	 * トランザクションに紐づくコネクションを取得する。
	 * 
	 * @return Connection Object
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	public Connection getConnection(String connectionSourceName) throws SQLException, InvalidTransactionException {
		//start
		log.debug("getConnection start");
		this.connectionSourceName = connectionSourceName;
		synchronized (this) {
			if (isValid()) {
				//log.debug("トランザクションは生きている。");
				if (!isActive()) {
					//log.debug("トランザクションはまだ始まっていない。");
					begin();
				} else {
					//log.debug("トランザクションは始まっています。");
				}
			} else {
				//log.debug("トランザクションはしんでいる。");
				throw new InvalidTransactionException("transaction invalid");
			}
			//end
			log.debug("getConnection end");
			
			return connection;
		}
	}
	
	/**
	 * トランザクションが開始しているか判定する。
	 * 
	 * @return トランザクションが開始しているかどうか
	 */
	private boolean isActive() {
		//start
		log.debug("isActive start");
		boolean flg = false;
		if (connection == null) {
			flg = false;
		} else {
			flg = true;
		}
		//end
		log.debug("isActive end");
		
		return flg;
	}
	
	/**
	 * トランザクションがタイムアウトかどうかを判定する。
	 * 
	 * @return　トランザクションがタイムアウトしているかどうか
	 */
	private boolean isTimeout() {
		//start
		log.debug("isTimeout start");
		
		boolean flg = false;
		//タイムアウトかどうかのチェック
		if (beginTime == 0) {
			flg = false;
		} else {
			//現在の時間を取得する。
			long nowTime = System.currentTimeMillis();
			//何ミリ秒経過したか算出
			long diff = nowTime - beginTime;
			if (diff <= maxLifeTime) {
				flg = false;
			} else {
				flg = true;
			}
		}
		//end
		log.debug("isTimeout end");
		
		return flg;
	}
	
	/**
	 * トランザクションが破棄されていないかをチェックするメソッド
	 * 
	 * @return　トランザクションが破棄されているかどうか
	 */
	private boolean isValid() {
		return !finalizeFlg;
	}
	
	
	/**
	 * タイムアウトチェックを行う。トランザクションタイマーから呼ばれる。
	 * タイムアウトの場合はトランザクションを破棄する。
	 *
	 */
	void checkTimeout() {
		//start
		log.debug("checkTimeout start");
		
		if (isTimeout()) {
			try {
				this.finalize();
			} catch (Throwable e) {
				log.error("finalize error", e);
			}
		}
		//end
		log.debug("checkTimeout end");
	}
	
	/**
	 * セーブポイントを生成する。
	 * 
	 * @param name セーブポイント名
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	void setSavePoint(String name) throws SQLException, InvalidTransactionException {
		log.debug("setSavePoint start");
		synchronized (this) {
			if (isValid()) {
				if (isActive()) {		
					//savepointを生成する。
					Savepoint point = connection.setSavepoint(name);
					//savepointを保存する。
					savePointMap.put(name, point);
				}
			} else {
				//トランザクションが破棄されている。
				throw new InvalidTransactionException("transaction invalid");
			}
		}
		log.debug("setSavePoint start");
	}
	
	/**
	 * トランザクションをロールバックする。
	 * 
	 * @param name セーブポイント名
	 * @throws SQLException
	 * @throws InvalidTransactionException
	 */
	void rollback(String name) throws SQLException, InvalidTransactionException {
		//start
		log.debug("rollback start");

		synchronized (this) {
			if (isValid()) {
				if (isActive()) {
					//セーブポイントが指定されている場合は、そのポイントまでロールバックする。
					//されていない場合は、トランザクション開始時点までロールバックする。
					if (name != null) {
						Savepoint point = (Savepoint)savePointMap.get(name);
						connection.rollback(point);
					} else {
						connection.rollback();
					}
				}
			} else {
				//トランザクションが破棄されている。
				throw new InvalidTransactionException("transaction invalid");
			}
		}
		//end
		log.debug("rollback end");
	}
	
}
