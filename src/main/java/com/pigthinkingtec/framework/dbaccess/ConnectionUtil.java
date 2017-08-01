package com.pigthinkingtec.framework.dbaccess;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xml.sax.SAXException;

/**
 * コネクションを生成するシングルトンクラス
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class ConnectionUtil {
	/* 設定ファイル名 */
	private static final String FILE_NAME = "connection.xml";
	/* このクラスのインスタンス */
	private static ConnectionUtil instance = null;
	/* コネクションの設定オブジェクト */
	private ConnectionConfig config = null;
	/* データソース */
	private DataSource ds = null;
	/* ログ */
	private Log log = LogFactory.getLog(this.getClass());
	/* コネクション供給クラス */
	private ConnectionProvider connectionProvider = null;
	
	
	/**
	 * コンストラクタ
	 *
	 */
	private ConnectionUtil() {
		init();
	}
	
	/**
	 * 初期化する
	 *
	 */
	private synchronized void init() {
		//start
		log.debug("init start");
		
		//connection.xmlから情報を取得しConnectionConfigに設定する。
		Digester digester = new Digester();
		digester.addObjectCreate("config", ConnectionConfig.class);
		digester.addCallMethod("config/type", "setType", 0);
		digester.addCallMethod("config/driver", "setDriver", 0);
		digester.addCallMethod("config/jdbc/uri", "setUri", 0);
		digester.addCallMethod("config/jdbc/userid", "setUserid", 0);
		digester.addCallMethod("config/jdbc/password", "setPassword", 0);
		digester.addCallMethod("config/jndi", "setJndi", 0);
		digester.addCallMethod("config/pool/maxactive", "setMaxActive", 0);
		digester.addCallMethod("config/connectionProvider", "setConnectionProvider", 0);
		try {
			//クラスパスからファイルのパスを取得する。
			//URL filePath = Loader.getResource(FILE_NAME);
			URL filePath = this.getClass().getClassLoader().getResource(FILE_NAME);
			//log.debug("read connection.xml start");
			config = (ConnectionConfig)digester.parse(filePath.toString());

			//log.debug("read connection.xml end");
		} catch (SAXException e) {
			log.fatal("XML NOT ANALYZE",e);
		} catch (IOException e) {
			log.fatal("FILE NOT READ",e);
		}
		
		try {
			if(config.getDriver() != null && config.getDriver().length() != 0){
				//ドライバーのロード
				Class.forName(config.getDriver());
			}
		} catch (ClassNotFoundException e) {
			log.fatal("driver not found", e);
		}
		
		//end
		log.debug("init end");
	}
	

	
	/**
	 * 新しくコネクションを作成する。
	 * 
	 * @return　Connection Object
	 * @throws SQLException
	 */
	public synchronized Connection getNewConnection(String connectionSourceName) throws SQLException{
		//start
		log.debug("getNewConnection start");
		
		Connection conn = null;
		
		if (config.getType() == 1) {
			//JDBCの場合
			conn = getDriverConnection();
		} else if(config.getType() == 2) {
			//データソースから取得する場合
			conn = getJNDIConnection();
		} else if(config.getType() == 3) {
			//common-dbcpを利用する場合
			conn = getPoolsConnection();
		} else if(config.getType() == 4) {
			conn = getConnectionByOther(connectionSourceName);
		}
		log.debug("getNewConnection end");
		
		return conn;
	}
	
	/**
	 * DataSourceManager経由で生成されたDataSourceからコネクションオブジェクトを生成するメソッド
	 * 
	 * @return connectionオブジェクト
	 * @throws SQLException 
	 */
	private Connection getConnectionByOther(String connectionSourceName)  throws SQLException {
		//start
		log.debug("getConnectionByOther start");
		
		if (connectionProvider == null) {
			try {
				connectionProvider = (ConnectionProvider)Class.forName(config.getConnectionProvider()).newInstance();
			} catch (Throwable e) {
				log.error("couldn't create connectionProvider ", e);
			}
		}
		
		Connection conn = connectionProvider.getConnection(connectionSourceName);
		//end
		log.debug("getConnectionByOther end");
		
		return conn;
	}
	
	/**
	 * commons-dbcpを利用しコネクションを取得する。
	 * 
	 * @return Connection Object
	 * @throws SQLException
	 */
	private Connection getPoolsConnection() throws SQLException {
		//start
		log.debug("getPoolsConnection start");
		
		Connection conn = null;
		//まだデータソースを生成していない場合
		if (ds == null) {
			getDataSourceByDBCP();
		}
		//データソースからコネクションを取得
		conn = ds.getConnection();
		
		//end
		log.debug("getPoolsConnection end");
		
		return conn;
	}
	
	/**
	 * JDBCによるコネクションの取得
	 * 
	 * @return Connection Object
	 * @throws SQLException
	 */
	public Connection getDriverConnection() throws SQLException {
		//start
		log.debug("getDriverConnection start");
		
		Connection conn = null;
		/*
		try {
			Class.forName(config.getDriver());
		} catch (ClassNotFoundException e) {
			log.fatal("driver not found", e);
		}
		*/
		
		conn = DriverManager.getConnection(config.getUri(), config.getUserid(), config.getPassword());
		
		//end
		log.debug("getDriverConnection end");
		
		return conn;
	}
	

	/**
	 * JNDIによるデータソースからコネクションを取得する。
	 * 
	 * @return Connection Object
	 * @throws SQLException
	 */
	private Connection getJNDIConnection() throws SQLException {
		//start
		log.debug("getJNDIConnection start");
		
		if (ds == null) {
			getDataSourceByJNDI();
		}
		Connection conn = ds.getConnection();
		//end
		log.debug("getJNDIConnection end");
		
		return conn;
	}
	
	/**
	 * JNDI経由でデータソースを取得する。
	 * 
	 * @return DataSource　object
	 */
	private DataSource getDataSourceByJNDI() {
		//start
		log.debug("getDataSourceByJNDI start");
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource)ic.lookup(config.getJndi());
		} catch (NamingException e){
			ds = null;
		}
		//end
		log.debug("getDataSourceByJNDI end");
		return ds;
	}
	
	/**
	 * DBCP経由でデータソースを取得する。
	 * 
	 * @return DataSource　object
	 */
	private DataSource getDataSourceByDBCP() {
		log.debug("getDataSourceByDBCP start");
		/*
		try {
			//ドライバーのロード
			Class.forName(config.getDriver());
		} catch (ClassNotFoundException e) {
			log.fatal("driver not found", e);
		}
		*/
		//ObjectPoolの生成
		ObjectPool pool = createObjectPool();
		//コネクション生成用にConnectionFactorｙを生成する。
		ConnectionFactory conFactory = new DriverManagerConnectionFactory(config.getUri(),config.getUserid(),config.getPassword());
		new PoolableConnectionFactory(conFactory, pool, null, null, false, true);
		//データソースを生成する。
		ds = new PoolingDataSource(pool);
		log.debug("getDataSourceByDBCP end");
		return ds;
	}
	
	

	
	/**
	 * オブジェクトプールを生成する。
	 * 
	 * @return ObjectPool Object
	 */
	private ObjectPool createObjectPool() {
		log.debug("createObjectPool start");
		GenericObjectPool pool = null;
		pool = new GenericObjectPool();
		//プールから取得できる最大のオブジェクト数を設定
		pool.setMaxActive(1);
		//プール内のオブジェクトがなくなった際の動作を設定
		pool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
		
		log.debug("createObjectPool end");
		return pool;
	}
	
	/**
	 * このクラスのインスタンスを取得する。
	 * 
	 * @return this class instance
	 */
	public static ConnectionUtil getInstance() {
		if (instance == null) {
			instance = new ConnectionUtil();
		}
		return instance;
	}
	
	public ConnectionConfig getConfig(){
		return config;
	}
}
