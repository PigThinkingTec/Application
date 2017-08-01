package com.pigthinkingtec.framework.dbaccess;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;

/**
 * SQLのバインド変数の値を保持するクラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
public class Mapping {

	/** ログオブジェクト */
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	@SuppressWarnings("rawtypes")
	private ArrayList params;
	private Object lastUpdate;
	
	@SuppressWarnings("rawtypes")
	public Mapping(){
		params = new ArrayList();
	}

	@SuppressWarnings("unchecked")
	public void setArgument(Object obj){
		params.add(obj);
	}

	public Object getArgment(int i) {
		return params.get(i);
	}
	
	public Object getlastUpdate() {
		return lastUpdate;
	}
	
	/**
	 * 設定するパラメータ数を取得する。
	 * 
	 * @return INパラメータ数
	 */
	public int size() {
		return params.size();
	}
	
	/**
	 * PreparedStatementオブジェクトにINパラメータをセットする。
	 * 
	 * @param statement PreparedStatement Object
	 * @throws SQLException
	 */
	protected void setPreparedStatementParameters(PreparedStatement statement) throws SQLException {

		for (int i = 0; i < params.size(); i++) {
			Object param = params.get(i);
			if (param instanceof DBNullObject) {
				DBNullObject obj = (DBNullObject)param;
				statement.setNull(i+1,obj.getSqlType());
			} else if (param instanceof Integer) {
				int value = ((Integer) param).intValue();
				statement.setInt(i + 1, value);
			} else if (param instanceof Short) {
				short sh = ((Short) param).shortValue();
				statement.setShort(i + 1, sh);
			} else if (param instanceof String) {
				String s = (String) param;
				statement.setString(i + 1, s);
			} else if (param instanceof Double) {
				double d = ((Double) param).doubleValue();
				statement.setDouble(i + 1, d);
			} else if (param instanceof Float) {
				float f = ((Float) param).floatValue();
				statement.setFloat(i + 1, f);
			} else if (param instanceof Long) {
				long l = ((Long) param).longValue();
				statement.setLong(i + 1, l);
			} else if (param instanceof Boolean) {
				boolean b = ((Boolean) param).booleanValue();
				statement.setBoolean(i + 1, b);
			} else if (param instanceof Timestamp) { 
				statement.setTimestamp(i + 1, (Timestamp)param);
			} else if (param instanceof java.sql.Date) {
				java.sql.Date d = (java.sql.Date) param;
				statement.setDate(i + 1, d);
			} else if (param instanceof Blob) {	
				statement.setBlob(i + 1, (Blob)param);
			} else if (param instanceof InputStream) {
				statement.setBlob(i + 1, (InputStream)param);
			} else if (param instanceof BigDecimal) {
				statement.setBigDecimal(i + 1, (BigDecimal) param);
			} else if (param == null) {
				statement.setNull(i + 1, Types.NULL);
				log.debug("param = null");
			} else {
				clearParams();
				log.debug("clearParams");
			}
		}
	}
	
	/**
	 * CallableStatement ObjectにINパラメータをセットする。
	 * 
	 * @param statement CallableStatement Object
	 * @param returnIndex ファンクションならば2、プロシージャなら1
	 * @throws SQLException
	 */
	protected void setCallableStatementParameters(CallableStatement statement, int returnIndex) throws SQLException {

		for (int i = 0; i < params.size(); i++) {
			Object param = params.get(i);
			if (param instanceof DBNullObject) {
				DBNullObject obj = (DBNullObject)param;
				statement.setNull(i + returnIndex, obj.getSqlType());
			} else if (param instanceof Integer) {
				int value = ((Integer) param).intValue();
				statement.setInt(i + returnIndex, value);
			} else if (param instanceof Short) {
				short sh = ((Short) param).shortValue();
				statement.setShort(i + returnIndex, sh);
			} else if (param instanceof String) {
				String s = (String) param;
				statement.setString(i + returnIndex, s);
			} else if (param instanceof Double) {
				double d = ((Double) param).doubleValue();
				statement.setDouble(i + returnIndex, d);
			} else if (param instanceof Float) {
				float f = ((Float) param).floatValue();
				statement.setFloat(i + returnIndex, f);
			} else if (param instanceof Long) {
				long l = ((Long) param).longValue();
				statement.setLong(i + returnIndex, l);
			} else if (param instanceof Boolean) {
				boolean b = ((Boolean) param).booleanValue();
				statement.setBoolean(i + returnIndex, b);
			} else if (param instanceof Timestamp) { 
				statement.setTimestamp(i + returnIndex , (Timestamp)param);
			} else if (param instanceof java.sql.Date) {
				java.sql.Date d = (java.sql.Date) param;
				statement.setDate(i + returnIndex, d);
			} else if (param instanceof Blob) {	
				statement.setBlob(i + returnIndex, (Blob)param);
			} else if (param instanceof InputStream) {
				statement.setBlob(i + returnIndex, (InputStream)param);
			} else if (param == null) {
				statement.setNull(i + returnIndex, Types.NULL);
				log.debug("param=null");
			} else {
				clearParams();
				log.debug("clearParams");
			}
		}
	}
	
	private void clearParams() {
		params.clear();
	}
}