package com.pigthinkingtec.framework.dbaccess;

import lombok.Getter;
import lombok.Setter;

/**
 * コネクションに関する設定情報を保持するクラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@Getter @Setter
public class ConnectionConfig {
	/* 接続タイプ */
	private int type = 0;
	/* Driver Class Name */
	private String driver;
	/* URI */
	private String uri;
	/* 接続ID */
	private String userid;
	/* 接続パスワード */
	private String password;
	/* JNDI名 */
	private String jndi;
	/* 最大コネクション数 */
	private int maxActive = 1;
	/* connectionProvider Class 名 */
	private String connectionProvider;
	
	/**
	 * 接続タイプを設定する。
	 * 
	 * @param type The type to set.
	 */
	public void setType(String type) {
		if (type.equals("JDBC")) {
			this.type = 1;
		} else if (type.equals("JNDI")) {
			this.type = 2;
		} else if (type.equals("DBCP")) {
			this.type = 3;
		} else if (type.equals("OTHER")) {
			this.type = 4;
		} else {
			this.type = 1;
		}
	}
	
	/**
	 * DBCP時の最大コネクション数を設定する。
	 *  
	 * @param maxActive The maxActive to set.
	 */
	public void setMaxActive(String maxActive) {
		this.maxActive = Integer.parseInt(maxActive);
	}
}
