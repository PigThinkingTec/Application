package com.pigthinkingtec.framework.dbaccess;

import java.sql.Connection;

/**
 * コネクション供給クラスのインターフェース
 * 
 * @author yizhou
 *
 */
public interface ConnectionProvider {
	
	public Connection getConnection(String connectionSourceName);

}
