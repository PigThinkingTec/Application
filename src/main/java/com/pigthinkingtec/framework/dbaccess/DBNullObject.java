package com.pigthinkingtec.framework.dbaccess;
/**
 * NULLをあらわすオブジェクトクラス
 * 
 * @author smiyazawa@abema.com
 * @version $Revision: 1.1 $ $Date: 2009/11/13 06:58:21 $
 * @history
 *
 * 
 */
public class DBNullObject {
	
	//SQL型
	private int sqlType = 0;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlType
	 */
	public DBNullObject(int sqlType) {
		this.sqlType = sqlType;
	}
	
	/**
	 * SQL型を取得する。
	 * 
	 * @return　int
	 */
	public int getSqlType() {
		return sqlType;
	}
}
