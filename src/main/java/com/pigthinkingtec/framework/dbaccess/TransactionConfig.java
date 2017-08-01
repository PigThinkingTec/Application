package com.pigthinkingtec.framework.dbaccess;

import java.io.Serializable;
import java.sql.Connection;

import lombok.Getter;
import lombok.Setter;

/**
 * トランザクションに関する設定情報
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings("serial")
@Getter @Setter
public class TransactionConfig implements Serializable {
	/* タイムアウト機能を使用するかどうか */
	private boolean available = true;
	/* トランザクション有効期間(ミリ秒) */
	private long lifeTime = 0;
	/* トランザクション確認インターバル(ミリ秒) */
	private long interval = 0;
	/*　トランザクション分離レベル　*/
	private int isolationLevel;
	/* トランザクションのデフォルトスコープ */
	private int scope = 0;
	/* トランザクションマネージャクラス名 */
	private String tmName = null;
	/* トランザクションマネージャをシングルトンにするか */
	private Boolean singleton = Boolean.FALSE;
	
	/**
	 * タイムアウト機能を使用するかどうかを判定する。
	 * 
	 * @return Returns the available.
	 */
	public boolean isAvailable() {
		return available;
	}
	
	/**
	 * タイムアウト機能を使用するかどうかを設定する。
	 * 
	 * @param available The availabel to set.
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * トランザクションの確認インターバルを取得する。
	 * 
	 * @return Returns the interval.
	 */
	public long getInterval() {
		return interval;
	}
	
	/**
	 * トランザクションの確認インターバルを設定する。
	 * 
	 * @param interval The interval to set.
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	/**
	 * トランザクションの有効期間を取得する。
	 * 
	 * @return Returns the lifetime.
	 */
	public long getLifeTime() {
		return lifeTime;
	}
	
	/**
	 * トランザクションの有効期間を設定する。
	 * 
	 * @param lifeTime The lifeTime to set.
	 */
	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}
	
	/**
	 * トランザクションの分離レベルを取得する。
	 * 
	 * @return Returns the isolationLevel. 
	 */
	public int getIsolationLevel() {
		return isolationLevel;
	}
	
	/**
	 * トランザクションの分離レベルを設定する。
	 * 
	 * @param isolationLevel The isolationLevel to set.
	 */
	public void setIsolationLevel(String isolationLevel) {
		if (isolationLevel.equals("READ_COMMITED")) {
			this.isolationLevel = Connection.TRANSACTION_READ_COMMITTED;
		} else if (isolationLevel.equals("SERIALIZABLE")) {
			this.isolationLevel = Connection.TRANSACTION_SERIALIZABLE;
		}
	}
	
	/**
	 * トランザクションのタイムアウトに関する情報を設定する。
	 * 
	 * @param lifeTime The lifiTime to set.
	 * @param interval The interval to set.
	 */
	public void setTimeout(String lifeTime, String interval) {
		setLifeTime(Long.parseLong(lifeTime));
		setInterval(Long.parseLong(interval));
	}
	
	/**
	 * トランザクションのデフォルトスコープを取得する。
	 * 
	 * @return Returns the scope.
	 */
	public int getScope() {
		return scope;
	}
	
	/**
	 * トランザクションのデフォルトスコープを設定する。
	 * 
	 * @param scope The scope to set.
	 */
	public void setScope(String scope) {
		if (scope.equals("GLOBAL")) {
			this.scope = TransactionScope.GLOBAL;
		} else if(scope.equals("LOCAL")) {
			this.scope = TransactionScope.LOCAL;
		} else if(scope.equals("NON")) {
			this.scope = TransactionScope.NON;
		} else {
			this.scope = TransactionScope.GLOBAL;
		}
	}

	/**
	 * TransactionManagerをSingletonとして扱うかどうか
	 * trueの場合は、Singletonとして扱う
	 * 
	 * @return Singletonとして扱う場合はtrue、扱わない場合はfalse
	 */
	public boolean isSingleton() {
		return this.singleton.booleanValue();
	}
	
	/**
	 * TransactionManagerをSingeltonとして扱うかどうかを設定する
	 * "true"の場合は、Singletonとして扱う
	 * 
	 * @param value
	 */
	public void setSingleton(String value) {
		if ("true".equals(value)) {
			this.singleton = Boolean.TRUE;
		} else {
			this.singleton = Boolean.FALSE;
		}
	}
}
