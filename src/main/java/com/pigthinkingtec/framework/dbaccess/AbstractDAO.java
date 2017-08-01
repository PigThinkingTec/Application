package com.pigthinkingtec.framework.dbaccess;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.AbstractCOMMONTableBean;
import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.DatabaseExceptionFactory;
import com.pigthinkingtec.framework.exception.InvalidTransactionException;
import com.pigthinkingtec.framework.exception.ResourceBusyException;
import com.pigthinkingtec.framework.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * データアクセスオブジェクトの基底クラス
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
abstract public class AbstractDAO {

	/* ログ出力用オブジェクト */  
	private Log log = LogFactory.getLog(this.getClass().getName());

	/* 更新登録ユーザID */
	protected String userId = null;

	/* 更新登録ジョブID */
	protected String pgmId = null;

	/* 接続方法 */
	private int type;

	/* NOWAITをそのままにするかどうかのフラグ */
	private String nowaitFlg = SystemConstant.FLAG_OFF;

	/* FETCH SIZE */
	private static final int FETCH_SIZE = 1000;

	private long startTime = 0;
	private long endTime = 0;
	private long costTime = 0;
	private BigDecimal time = null;

	private ArrayList sqlLogList = null;
	
	private String connectionSourceName = null;

	/**
	 * DAOクラスのコンストラクタ
	 * 
	 * @param userId 更新登録ユーザID
	 * @param pgmId 更新登録ジョブID
	 */
	public AbstractDAO(String userId, String pgmId) {
		this.userId = userId;
		this.pgmId = pgmId;
		this.type = ConnectionUtil.getInstance().getConfig().getType();
		this.sqlLogList = new ArrayList();
	}

	/**
	 * 条件ありのUPDATE/INSERT文(複数)を発行する
	 * 
	 * @param sql　実行するSQL
	 * @param mappings SQLにセットするパラメータ配列
	 * @return 更新件数結果配列
	 * @throws DatabaseException
	 */
	protected final int[] updates(String sql, Mapping[] mappings)
			throws DatabaseException {
		//start
		log.debug("updates start");
		startTime = System.currentTimeMillis();

		int[] count = null;
		PreparedStatement stm = null;
		List countList = new ArrayList();

		if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
			sql = sql.replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);

		try {
			stm = createPreparedStatement(sql);
			stm.setFetchSize(FETCH_SIZE);
			for (int i = 0; i < mappings.length; i++) {
				
				if (i != 0 && i % 15000 == 0) {
					countList.add(stm.executeBatch());
					stm.clearBatch();
				}
				mappings[i].setPreparedStatementParameters(stm);
				if (getLog().isDebugEnabled()){
					String sqlLog = getSqlLog(sql, mappings[i]);
					getLog().debug(sqlLog);
					sqlLogList.add(sqlLog);
				}
				stm.addBatch();
			}
			countList.add(stm.executeBatch());
			if (countList.size() == 1) {
				count = (int[])countList.get(0);
			} else {
				count = new int[mappings.length];
				for (int i = 0; i < countList.size(); i++) {
					for (int j = 0; j < ((int[])countList.get(i)).length; j++ ) {
						count[i * 15000 + j] = ((int[])countList.get(i))[j];
					}
				}
			}
			endTime = System.currentTimeMillis();
			costTime = endTime - startTime;
			time = BigDecimal.valueOf(costTime).divide(BigDecimal.valueOf(1000), new MathContext(3));
		
		} catch (SQLException e) {
			for (int i = 0; i < mappings.length; i++) {
				getLog().error(getSqlLog(sql, mappings[i]));
			}
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					throw DatabaseExceptionFactory.createException(e);
				}
			}
		}
		//end
		log.debug("updates end");

		//		if(!isLogSQL(sql)){
		//			// check the insert-SQL needs or not
		//			boolean sqlLogSkipPGM = false;
		//			
		//			if (sqlLogSkipPGM){
		//				saveSQLIntoDB(_type, "", count.length ,_result, time);
		//			} else {
		//				for (int i = 0; i < mappings.length; i++) {
		//					saveSQLIntoDB(_type, getSqlLog(sql, mappings[i]), count.length ,_result, time);
		//				}
		//			}
		//		}
		return count;
	}

	/**
	 * SQLをまとめて実行する。
	 * 
	 * @param sqls 実行するSQLの配列
	 * @return 更新結果件数配列
	 * @throws DatabaseException
	 */
	protected final int[] updates(String[] sqls) throws DatabaseException {
		log.debug("updates start");
		int [] count = null;
		if (sqls != null && sqls.length != 0) {
			Statement stmt = null;
			try {
				stmt = createStatement();
				stmt.setFetchSize(FETCH_SIZE);
				for (int i = 0; i < sqls.length; i++) {
					if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
						sqls[i] = sqls[i].replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
					}
					if (getLog().isDebugEnabled()){
						String sqlLog = getSqlLog(sqls[i]);
						getLog().debug(sqlLog);
						sqlLogList.add(sqlLog);
					}
					stmt.addBatch(sqls[i]);
				}
				count = stmt.executeBatch();
			} catch (SQLException e) {
				for (int i = 0; i < sqls.length; i++) {
					getLog().error(getSqlLog(sqls[i]));
				}
				throw DatabaseExceptionFactory.createException(e);
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException e) {
					throw DatabaseExceptionFactory.createException(e);
				}	
			}
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);
		log.debug("updates end");
		return count;
	}

	/**
	 * 条件ありのUPDATE/INSERT文を発行する
	 * 
	 * @param sql　実行するSQL
	 * @param mapping SQLにセットするパラメータ
	 * @return 更新結果件数
	 * @throws DatabaseException
	 */
	protected final int update(String sql, Mapping mapping)
			throws DatabaseException {
		log.debug("update start");
		startTime = System.currentTimeMillis();

		int count = 0;
		PreparedStatement stmt = null;

		if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
			sql = sql.replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);

		try {
			stmt = createPreparedStatement(sql);
			stmt.setFetchSize(FETCH_SIZE);
			mapping.setPreparedStatementParameters(stmt);

			System.out.println(getSqlLog(sql, mapping));
			
			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql, mapping);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			count = stmt.executeUpdate();
			endTime = System.currentTimeMillis();
			costTime = endTime - startTime;
			time = BigDecimal.valueOf(costTime).divide(BigDecimal.valueOf(1000), new MathContext(3));
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql, mapping));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("update end");
		//		if(!isLogSQL(sql)){
		//			saveSQLIntoDB(_type, getSqlLog(sql, mapping), count,_result, time);
		//		}
		return count;
	}

	/**
	 * 条件ありのSELECT文を発行する
	 * 
	 * @param sql 実行するSQL
	 * @param mapping SQLにセットするパラメータ
	 * @return 検索結果
	 * @throws DatabaseException
	 */
	protected final QueryResult select(String sql, Mapping mapping)
			throws DatabaseException {
		//start
		log.debug("select start");
		startTime = System.currentTimeMillis();

		QueryResult result = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
			sql = sql.replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);

		try {
			stm = createPreparedStatement(sql);
			stm.setFetchSize(FETCH_SIZE);
			mapping.setPreparedStatementParameters(stm);

			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql, mapping);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			rs = stm.executeQuery();
			result = convertResult(rs);
			endTime = System.currentTimeMillis();
			costTime = endTime - startTime;
			time = BigDecimal.valueOf(costTime).divide(BigDecimal.valueOf(1000), new MathContext(3));
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql, mapping));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		//end
		log.debug("select end");
		//		if(!isLogSQL(sql)){
		//			saveSQLIntoDB(_type, getSqlLog(sql, mapping), 0,_result, time);
		//		}
		return result;
	}

	/**
	 * 条件なしのUPDATE/INSERT文を発行する
	 * 
	 * @param sql 実行するSQL
	 * @return 更新結果件数
	 * @throws DatabaseException
	 */
	protected final int update(String sql) throws DatabaseException {
		log.debug("update start");
		startTime = System.currentTimeMillis();

		int count = 0;
		Statement stm = null;

		if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
			sql = sql.replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);

		try {
			stm = createStatement();
			stm.setFetchSize(FETCH_SIZE);

			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			count = stm.executeUpdate(sql);
			endTime = System.currentTimeMillis();
			costTime = endTime - startTime;
			time = BigDecimal.valueOf(costTime).divide(BigDecimal.valueOf(1000), new MathContext(3));
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("update end");
		//		if(!isLogSQL(sql)){
		//			saveSQLIntoDB(_type, sql, count, _result, time);
		//		}
		return count;
	}

	/**
	 * 条件なしのSELECT文を発行する
	 * 
	 * @param sql 実行するSQL
	 * @return 検索結果
	 * @throws DatabaseException
	 */
	protected final QueryResult select(String sql) throws DatabaseException {
		log.debug("select start");
		startTime = System.currentTimeMillis();

		QueryResult result = null;
		Statement stm = null;
		ResultSet rs = null;

		if (type != 2 && nowaitFlg.equals(SystemConstant.FLAG_OFF)) {
			sql = sql.replaceAll(" NOWAIT", " WAIT " + String.valueOf(SystemConstant.WAIT_SECONDS));
		}
		setNowaitFlg(SystemConstant.FLAG_OFF);

		try {
			stm = createStatement();
			stm.setFetchSize(FETCH_SIZE);

			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			rs = stm.executeQuery(sql);
			result = convertResult(rs);
			endTime = System.currentTimeMillis();
			costTime = endTime - startTime;
			time = BigDecimal.valueOf(costTime).divide(BigDecimal.valueOf(1000), new MathContext(3));
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("select end");
		//		if(!isLogSQL(sql)){
		//			saveSQLIntoDB(_type, sql, 0, _result, time);
		//		}
		return result;
	}

	/**
	 * プロシージャを呼び出す。
	 * このメソッドを使用する場合はsqlパラメータは、
	 * {EXEC プロシージャ名(?, ?)}
	 * の形にしOUTパラメータは全てVARCHAR2であつかう。
	 * 
	 * @param sql 実行するプロシージャ
	 * @param mapping プロシージャに渡すINパラメータ
	 * @param returnCount アウトパラメータ数
	 * @return プロシージャのリターン値
	 * @throws DatabaseException
	 */
	//public Object[] callProcedure(String sql, Mapping mapping, int returnCount)
	protected final QueryResult callProcedure(String sql, Mapping mapping, int returnCount)
			throws DatabaseException {

		log.debug("callProcedure start");
		QueryResult result = new QueryResult(returnCount);
		//Object[] returnValues = new Object[returnCount];
		CallableStatement cStmt = null;
		try {
			//CallableStatementオブジェクトを生成する。
			cStmt = createCallableStatement(sql);
			//INパラメータを設定する。
			mapping.setCallableStatementParameters(cStmt, 1);
			//OUTパラメータを設定する。
			for (int i = mapping.size(); i < mapping.size() + returnCount; i++) {
				//全てVARCHARで設定
				cStmt.registerOutParameter(i + 1, Types.VARCHAR);
			}

			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql, mapping);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			//プロシージャの実行
			cStmt.execute();
			//OUTパラメータを取得する。
			for (int i = 0; i < returnCount; i++) {
				result.addColumnName(String.valueOf(i));
				result.add(cStmt.getObject(mapping.size() + 1 + i));
				//returnValues[i] = cStmt.getObject(mapping.size() + 1 + i);
			}
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql, mapping));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (cStmt != null) {
					cStmt.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("callProcedure end");
		//return returnValues;
		return result;
	}

	/**
	 * プロシージャを呼び出す。
	 * このメソッドを使用する場合はsqlパラメータは、
	 * ★{? = call  プロシージャ名(?, ?)}★
	 * の形にしReturn値は、パラメータreturnValueTypeによって指定する。
	 * 
	 * @param sql 実行するプロシージャ
	 * @param mapping プロシージャに渡すINパラメータ
	 * @param returnValueType 戻り値タイプ
	 * @return プロシージャのリターン値
	 * @throws DatabaseException
	 */
	protected final QueryResult callProcedureWithReturnValue(String sql, Mapping mapping, int returnValueType)
			throws DatabaseException {

		log.debug("callProcedureWithReturnValue start");
		QueryResult result = new QueryResult(1);
		//Object[] returnValues = new Object[returnCount];
		CallableStatement cStmt = null;
		try {
			//CallableStatementオブジェクトを生成する。
			cStmt = createCallableStatement(sql);
			
			//Returnパラメータを設定する。
			cStmt.registerOutParameter(1, returnValueType);
			
			//INパラメータを設定する。
			mapping.setCallableStatementParameters(cStmt, 2);


			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql, mapping);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			//プロシージャの実行
			cStmt.execute();
			//OUTパラメータを取得する。
			result.addColumnName(String.valueOf(0));
			result.add(cStmt.getObject(1));
				
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql, mapping));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (cStmt != null) {
					cStmt.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("callProcedureWithReturnValue end");
		return result;
	}
	
	/**
	 * プロシージャを呼び出す。
	 * このメソッドを使用する場合はsqlパラメータは、
	 * {call sp_name( ?,? )}の形にしてください。
	 * 
	 * @param sql 実行するプロシージャ
	 * @param mapping プロシージャに渡すINパラメータ
	 * @return プロシージャのリターン値（データセット）
	 * @throws DatabaseException
	 */
	protected final QueryResult[] callProcedure(String sql, Mapping mapping)
			throws DatabaseException {

		log.debug("callProcedure() start");

		List<QueryResult> list = new ArrayList<QueryResult>();
		CallableStatement cStmt = null;
		
		try {
			//CallableStatementオブジェクトを生成する。
			cStmt = createCallableStatement(sql);
			
			//INパラメータを設定する。
			if (mapping == null){
				//引数がない場合、何もしないこと
			} else {
				//Functionの場合：２	Procedureの場合：１
				mapping.setCallableStatementParameters(cStmt,1);
			}
			
			if (getLog().isDebugEnabled()){
				String sqlLog = getSqlLog(sql, mapping);
				getLog().debug(sqlLog);
				sqlLogList.add(sqlLog);
			}

			//プロシージャの実行
			boolean bRes = cStmt.execute();

			//複数結果Setを戻る場合も対応できるようにする
			while(true){
				if (bRes){
					//データセットを取得する
					ResultSet rs = cStmt.getResultSet();
					//データセットの値を変換する
					
					QueryResult qRes = convertResult(rs);
					//データセットを戻り値に設定する
					list.add(qRes);

				}else {
					int updateCount = cStmt.getUpdateCount();
					if (updateCount == -1) {
						break;
					}
					// Do something with update count ...
				}
				
				//複数データセットがある場合、次のデータセットを取得する
				bRes = cStmt.getMoreResults();
			}
		} catch (SQLException e) {
			getLog().error(getSqlLog(sql, mapping));
			throw DatabaseExceptionFactory.createException(e);
		} finally {
			try {
				if (cStmt != null) {
					cStmt.close();
				}
			} catch (SQLException e) {
				throw DatabaseExceptionFactory.createException(e);
			}
		}
		log.debug("callProcedure end");
		return list.toArray(new QueryResult[0]);
	}
	
	/**
	 * ResultSetオブジェクトをQueryResultオブジェクトに変換する
	 * 
	 * @param rs ResultSet Object
	 * @return 検索結果
	 * @throws DatabaseException
	 */
	private QueryResult convertResult(ResultSet rs) throws DatabaseException {

		log.debug("convertResult start");

		QueryResult results = null;
		Object obj = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			results = new QueryResult(columnCount);

			// add field values to ResultSet object
			while (rs.next()) {
				for (int i = 0; i < columnCount; i++){
					obj = rs.getObject(i + 1);
					if (obj instanceof byte[]) {
						results.add(rs.getBlob(i + 1));
					} else {
						results.add(obj);
					}
				}
			}
			// add column names to ResultSet object
			for (int i = 0; i < columnCount; i++) {
				results.addColumnName(rsmd.getColumnName(i + 1));
			}
		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}
		log.debug("convertResult end");
		return results;
	}

	/**
	 * statementオブジェクトを作成する
	 * 
	 * @return Statement Object
	 * @throws DatabaseException
	 */
	private Statement createStatement() throws DatabaseException {

		log.debug("createStatement start");

		Statement stmt = null;
		try {
			//return con.createStatement();

			stmt = getConnection().createStatement();
		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}

		log.debug("createStatement end");

		return stmt;
	}

	/**
	 * PreparedStatementオブジェクトを作成する
	 * 
	 * @param sql 実行するSQL
	 * @return PreParedStatement Object
	 * @throws DatabaseException
	 */
	private PreparedStatement createPreparedStatement(String sql)
			throws DatabaseException {

		log.debug("createPreparedStatement start");

		PreparedStatement pStmt = null; 
		try {
			//return con.prepareStatement(sql);
			pStmt = getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}

		log.debug("createPreparedStatement end");

		return pStmt;
	}

	/**
	 * CallableStatementオブジェクトを作成する。
	 * 
	 * @param sql 実行するSQL
	 * @return CallableStatement Object
	 * @throws DatabaseException
	 */
	private CallableStatement createCallableStatement(String sql)
			throws DatabaseException {


		log.debug("createCallableStatement start");

		CallableStatement cStmt = null;
		try {
			cStmt = getConnection().prepareCall(sql);

		} catch (SQLException e) {
			throw DatabaseExceptionFactory.createException(e);
		}

		log.debug("createCallableStatement end");

		return cStmt;
	}

	/**
	 * コネクションを取得する。
	 *
	 * @return Connection Object
	 * @throws SQLException
	 * @throws InvalidTransactionException 
	 */
	private Connection getConnection()
			throws SQLException, InvalidTransactionException {

		log.debug("getConnection start");

		//トランザクションマネージャから現在のスレッドに紐づくコネクションを取得する。
		TransactionManager tm = TransactionManagerFactory.getTransactionManager();
		tm.assosiateTransaction(TransactionManagerFactory.getTransactionConfig().getScope());
		Connection conn = tm.getCurrentTransaction().getConnection(connectionSourceName);

		log.debug("getConnection end");

		return conn;
	}

	/**
	 * Like句でワイルドカード文字(%,_)を文字列として検索できるように変換するメソッド
	 * @param 文字列
	 * @return 変換後文字列
	 */
	protected String replaceWildCard(String str) {
		if(str == null) {
			return null;
		}
		//エスケープ文字の変換
		str = str.replaceAll(SystemConstant.ESCAPE_CHAR, SystemConstant.ESCAPE_CHAR + SystemConstant.ESCAPE_CHAR);
		//ワイルドカード文字の変換
		str = str.replaceAll("%", SystemConstant.ESCAPE_CHAR + "%");
		str = str.replaceAll("_", SystemConstant.ESCAPE_CHAR + "_");
		return str;
	}

	/**
	 * DBNULLオブジェクトを取得する。
	 * 
	 * @param sqlType テーブルの型
	 * @return NULLを表すオブジェクト
	 */
	protected DBNullObject getNullObject(int sqlType) {
		return new DBNullObject(sqlType);
	}

	/**
	 * ログオブジェクトを取得する。
	 * 
	 * @return　ログ出力用オブジェクト
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * プログラムIDを取得する
	 * 
	 * @return Returns the pgmId.
	 */
	protected String getPgmId() {
		return pgmId;
	}

	/**
	 * ユーザIDを取得する
	 * 
	 * @return Returns the userId.
	 */
	protected String getUserId() {
		return userId;
	}

	/**
	 * valueをチェックしnullまたはblank（全角も含まれる）だった場合にはDBNullObjectをリターンするメソッド
	 * 
	 * @param value チェック対象オブジェクト
	 * @param type java.sql.Typesクラスで定義されている定数
	 * @return valueがnullまたはblankの場合はNullObjectを返す。それ以外の場合はvalueを返す。
	 */
	protected Object getValue(Object value, int type) {
		Object returnValue = value;
		if (StringUtil.isBlank(value)) {
			returnValue = getNullObject(type);
		}
		return returnValue;
	}

	/**
	 * valueをチェックし空だった場合にはDBNullObjectをリターンするメソッド
	 * 
	 * @param value チェック対象オブジェクト
	 * @param type java.sql.Typesクラスで定義されている定数
	 * @return valueが空（nullないし""）の場合はNullObjectを返す。それ以外の場合はvalueを返す。
	 */
	protected Object getValue2(Object value, int type) {
		Object returnValue = value;
		if (StringUtil.isEmpty(value)) {
			returnValue = getNullObject(type);
		}
		return returnValue;
	}

	/**
	 *  valueをチェックしnullまたはblankだった場合にはSystemConstant.FLAG_OFFを返す
	 *  
	 *  @param value チェック対象オブジェクト
	 *  @return nullでない場合はチェック対象を返し、nullの場合はSystemConstant.FLAG_OFFを返す
	 */
	protected Object getFlgValue(Object value) {
		Object returnValue = value;
		if (StringUtil.isBlank(value)) {
			returnValue = SystemConstant.FLAG_OFF;
		}
		return returnValue;
	}

	/**
	 * 引数から該当するDateObjectを生成する
	 * 
	 * @param dateStr
	 * @return 引数から生成されたDateObject<BR>引数がNull/Blankの場合NullObjectを返す
	 */
	protected Object getDate(String dateStr) throws DatabaseException {
		Object returnValue = null;
		Date returnDate = null;
		if (StringUtil.isBlank(dateStr)) {
			returnValue = getNullObject(Types.DATE);
		} else {
			try {
				SimpleDateFormat sdf = null;
				if (dateStr.length() == 8) {
					sdf = new SimpleDateFormat("yyyyMMdd");
					returnDate = sdf.parse(dateStr);
				} else if (dateStr.length() == 15) {
					sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
					returnDate = sdf.parse(dateStr);
				} else if (dateStr.length() == 18) {
					sdf = new SimpleDateFormat("yyyyMMdd HHmmssSSS");
					returnDate = sdf.parse(dateStr);
				} else {
					throw new DatabaseException("Parse Error");			
				}

				returnValue = new Timestamp(returnDate.getTime());

			} catch (ParseException e) {
				getLog().error("Parse Error", e);
				throw new DatabaseException(e);
			}
		}
		return returnValue;		
	}

	/**
	 * パラメータ「?」を含むのSQL文に<br>
	 * 値を埋め込んで返す。
	 *
	 * @param SQL文,埋め込む値のマッピングクラス
	 * @return String
	 */
	private static String getSqlLog(String sql, Mapping mapping) {

		if (mapping == null || mapping.size() == 0) {
			return new String("SQL=" + sql);
		}

		StringBuffer stBuf = new StringBuffer(sql);
		stBuf.insert(0, "SQL=");

		//?のインデックス
		int index = 0;

		//SQL文中の?をmappingで書き換える
		for(int i = 0; i < mapping.size(); i++)
		{
			index = stBuf.indexOf("?");

			//本番環境でindexが-1になりStringIndexOutOfBoundsExceptionが発生したための対応
			if (index == -1){
				break;
			}

			Object param = mapping.getArgment(i);
			if(param instanceof DBNullObject)
			{
				stBuf.replace(index, index + 1, "null");
			} else
				if(param instanceof Integer)
				{
					int value = ((Integer)param).intValue();
					stBuf.replace(index, index + 1, String.valueOf(value));
				} else
					if(param instanceof Short)
					{
						short sh = ((Short)param).shortValue();
						stBuf.replace(index, index + 1, String.valueOf(sh));
					} else
						if(param instanceof String)
						{
							String s = (String)param;
							stBuf.replace(index, index + 1, "'" + s + "'");
						} else
							if(param instanceof Double)
							{
								double d = ((Double)param).doubleValue();
								stBuf.replace(index, index + 1, String.valueOf(d));
							} else
								if(param instanceof Float)
								{
									float f = ((Float)param).floatValue();
									stBuf.replace(index, index + 1, String.valueOf(f));
								} else
									if(param instanceof Long)
									{
										long l = ((Long)param).longValue();
										stBuf.replace(index, index + 1, String.valueOf(l));
									} else
										if(param instanceof Boolean)
										{
											boolean b = ((Boolean)param).booleanValue();
											stBuf.replace(index, index + 1, String.valueOf(b));
										} else
											if(param instanceof BigDecimal)
											{
												stBuf.replace(index, index + 1, String.valueOf(param));
											} else
												if(param instanceof Date)
												{
													Date d = (Date)param;
													System.out.println(param);
													System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(d));
													//stBuf.replace(index, index + 1, "TO_DATE('" + new SimpleDateFormat("yyyyMMddHHmmss").format(d) + "','yyyymmddhh24miss')");
													stBuf.replace(index, index + 1, "CONVERT(VARCHAR,'" + new SimpleDateFormat("yyyyMMddHHmmss").format(d) + "',120)");
												}
		}

		return stBuf.toString();

	}

	private String getSqlLog(String sql) {
		return getSqlLog(sql, null);
	}

	protected void setNowaitFlg(String flg) {
		this.nowaitFlg = flg;
	}

	//private void saveSQLIntoDB(String type, String SQL, int count, String result, BigDecimal time) throws DatabaseException{
	//		//ログレベルINFO又はワークテーブルフラグtrueの場合のみ
	//		if(SystemConstant.LOG_LV != 1 || wtableFlg){
	//			return;
	//		}
	//		TiLogDAO tiLogDAO = new TiLogDAO(userId, SystemConstant.PGM_CLS_ONLINE, pgmId);
	//		
	//		StringBuffer msgDetail = new StringBuffer();
	//		msgDetail.append("画面ID=" +pgmId+ ",区分=" +type+ "," +SQL+ ",処理件数=" +count+ ",処理結果=" +result+ ",処理時間=" +time+ "s");
	//
	//		//init Databean
	//		TiLog tiLog = new TiLog();
	//		tiLog.setLogLv(SystemConstant.INFO_LV);
	//		tiLog.setLogMsg(null);
	//		tiLog.setMsgId(null);
	//		if("0".equals(userId)){
	//			userId = "";
	//		}
	//		tiLog.setMsgDetailed(getByteLengthStr(msgDetail.toString(),4000));
	//		tiLog.setRegUserId(StringUtil.isBlank(userId)? SystemConstant.DUMMY_USER_ID : userId);
	//		tiLog.setRegPgmCls(SystemConstant.PGM_CLS_ONLINE);
	//		tiLog.setRegPgmId(StringUtil.isBlank(pgmId)? SystemConstant.DUMMY_PGM_ID : pgmId);
	//		tiLog.setUpdUserId(StringUtil.isBlank(userId)? SystemConstant.DUMMY_USER_ID : userId);
	//		tiLog.setUpdPgmCls(SystemConstant.PGM_CLS_ONLINE);
	//		tiLog.setUpdPgmId(StringUtil.isBlank(pgmId)? SystemConstant.DUMMY_PGM_ID : pgmId);
	//
	//		//save DB
	//		try {
	//			tiLogDAO.add(tiLog);
	//		} catch (DatabaseException e) {
	//			// Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//	}

	//	private boolean isLogSQL(String SQL){
	//		boolean flg = false;
	//		if(SQL.length()!= SQL.replace("TI_Log", "").length() 
	//		 ||SQL.length()!= SQL.replace("SEQ_I_SEQ_NO0023", "").length()  ){
	//			flg = true;
	//		}
	//		return flg;
	//	}

	/**
	 * Get left-substring by specifying byteLength 
	 * 
	 * @param str
	 * @param byteLength
	 * @return tmpStr
	 */
	private static String getByteLengthStr(String str, int byteLength){
		String resultStr = "" ;
		String tmpStr = "";

		if (str==null || StringUtil.isBlank(str)){
			resultStr = str;
		}else if (str.getBytes().length <= byteLength){
			resultStr = str;
		}else {
			int totalBytes = 0;
			for (int i=0 ; i<str.length(); i++){
				tmpStr = new String(str.substring(i, i+1));
				totalBytes += tmpStr.getBytes().length ;

				if (totalBytes > byteLength){
					break;
				}

				resultStr += tmpStr;
			}
		}

		return resultStr;
	}

	public ArrayList getSqlLogList(){
		return sqlLogList;
	}

	/**
	 * 指定したSQLおよびMappingオブジェクトを使用して検索した結果をもとに<BR>
	 * EntityオブジェクトのListを返すメソッド
	 * 
	 * @param sql 実行するSQL
	 * @param mapping Mappingオブジェクト
	 * @return EntityオブジェクトのList
	 * @throws DatabaseException
	 */
	protected List find(String sql, Mapping mapping) throws DatabaseException {

		QueryResult qr = null;
		if (mapping == null) {
			qr = select(sql);
		} else {
			qr = select(sql, mapping);
		}

		List <AbstractCOMMONTableBean> resultList = new ArrayList<AbstractCOMMONTableBean>();
		for(int i = 0; i < qr.getRowCount(); i++) {
			resultList.add(createObject(qr, i));
		}

		return resultList;
	}

	/**
	 * 指定したSQLを使用して検索した結果をもとに<BR>
	 * EntityオブジェクトのListを返すメソッド
	 * 
	 * @param sql 実行するSQL
	 * @return EntityオブジェクトのList
	 * @throws DatabaseException
	 */
	protected List find(String sql) throws DatabaseException {
		return find(sql, null);
	}

	/**
	 * 指定したSQL、Mappingオブジェクトを使用して、行ロックを行い<BR>
	 * 同時に行ロックしたEntityオブジェクトのListを取得するメソッド
	 * 
	 * @param sql 実行するSQL
	 * @param mapping Mappingオブジェクト
	 * @return ロックしたEntityオブジェクトののList ロック取得に失敗した場合は、0件Listを戻す
	 * @throws DatabaseException
	 */
	protected List findLockRecords(String sql, Mapping mapping) throws DatabaseException {

		List resultList = null;
		try {
			resultList = find(sql, mapping);
		} catch (ResourceBusyException rbe) {
			resultList = new ArrayList();
		}
		return resultList;
	}

	/**
	 * 指定したSQL、Mappingオブジェクトを使用して、行ロックを行うメソッド<BR>
	 * 
	 * @param sql 実行するSQL
	 * @param mapping Mappingオブジェクト
	 * @return ロックしたデータ件数 ロック取得に失敗した場合は0を戻す
	 * @throws DatabaseException
	 */
	protected int lockRecords(String sql, Mapping mapping) throws DatabaseException {

		int i = 0;
		try {
			i = update(sql, mapping);
		} catch (ResourceBusyException rbe) {
			i = 0;
		}
		return i;
	}

	/**
	 * SQLサーバ用の日付フォーマットで現在時刻を返す
	 * @author tamizuma
	 */

	protected String getDateTimeSQLServerFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * データ挿入時の共通l項目をMappingクラスにセットする
	 * 
	 * @param Mappingオブジェクト
	 * @return Mappingオブジェクト
	 * @author tamizuma
	 */

	protected Mapping setCommonInsertColumnValues(Mapping mapping) {
		// IT(更新日付をセット)
		mapping.setArgument(getDateTimeSQLServerFormat());
		// IU(ユーザIDをセット) 
		mapping.setArgument(userId);
		// IP(プログラムIDをセット)
		mapping.setArgument(pgmId);
		// UT(更新日付をセット)
		mapping.setArgument(getDateTimeSQLServerFormat());
		// UU(ユーザIDをセット)
		mapping.setArgument(userId);
		// UP(プログラムIDをセット)
		mapping.setArgument(pgmId);

		return mapping;
	}

	/**
	 * データ更新時の共通l項目をMappingクラスにセットする
	 * 
	 * @param Mappingオブジェクト
	 * @return Mappingオブジェクト
	 * @author tamizuma
	 */
	protected Mapping setCommonUpdateColumnValues(Mapping mapping) {
		// UT(更新日付をセット)
		mapping.setArgument(getDateTimeSQLServerFormat());
		// UU(ユーザIDをセット)
		mapping.setArgument(userId);
		// UP(プログラムIDをセット)
		mapping.setArgument(pgmId);

		return mapping;
	}

	/**
	 * 検索結果から、行番号のデータを取得し、Entityオブジェクトを生成するAbstractメソッド
	 * 
	 * @param qr 検索結果オブジェクト
	 * @param index 行番号
	 * @return Entityオブジェクト
	 * @throws DatabaseException
	 */
	abstract protected <T extends AbstractCOMMONTableBean> T createObject(QueryResult qr, int index) throws DatabaseException;

	/**
	 * Connectionの取得先を設定する
	 * 
	 * @param connectionSourceName を設定する connectionSourceName
	 */
	public void setConnectionSourceName(String connectionSourceName) {
		this.connectionSourceName = connectionSourceName;
	}
	
}