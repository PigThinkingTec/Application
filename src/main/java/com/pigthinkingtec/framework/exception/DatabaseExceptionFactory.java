package com.pigthinkingtec.framework.exception;

import java.sql.SQLException;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.util.FwPropertyReader;
/**
 * SQLExceptionの種別に応じて適切なオブジェクトを生成する
 * 
 * @author yizhou
 * @history
 *
 * 
 */
public class DatabaseExceptionFactory {
	private final static String message = "Database error occurred ErrorCode=[";
	private static String DATABASE_TYPE = "";
	
	static {
		DATABASE_TYPE = FwPropertyReader.getProperty("database.type",null);
	}
	
	/**
	 * ErrorCodeに対応するExceptionを生成する
	 * 
	 * @param e　SQL発行の際、例外が生じた場合にスローされるSQLException
	 * @return DatabaseException
	 */
	public static DatabaseException createException(SQLException e) {
		int code = e.getErrorCode();
		DatabaseException exception = null;

		if (SystemConstant.DATABASE_TYPE_ORACLE.equals(DATABASE_TYPE)) {
			if ((code >= 900) && (code <= 999)) {
				exception = new BadSQLGrammarException(message + code + "]",e);
			} else if (((code >= 1033) && (code <= 1035)) || (code == 1071)
					|| (code == 1089) || (code == 1092) || (code == 1109)) {
				exception = new DatabaseUnavailableException(message + code + "]",e);
			} else if((code == 1) || (code == 1407) || (code == 1722)){
				exception = new DataIntegrityViolationException(message + code + "]",e);
			} else if((code == 104) || (code == 1013) || (code == 2087) || (code == 60)){
				exception = new DeadlockException(message + code + "]",e);
			} else if ((code == 54) || (code == 30006)) {
				exception = new ResourceBusyException(message + code + "]",e);
			} else {
				exception = new DatabaseException(message + code + "]",e);
			}
			return exception;
		} else if(SystemConstant.DATABASE_TYPE_SQLSERVER.equals(DATABASE_TYPE)) {
			// SQLServer のExceptionマッピングは未実装
			exception = new DatabaseException(message + code + "]",e);
			return exception;
		} else if(SystemConstant.DATABASE_TYPE_MYSQL.equals(DATABASE_TYPE)) {
			// 未実装
			exception = new DatabaseException(message + code + "]",e);
			return exception;
		} else if(SystemConstant.DATABASE_TYPE_POSTGRE.equals(DATABASE_TYPE)) {
			// 未実装
			exception = new DatabaseException(message + code + "]",e);
			return exception;
		} else {
			exception = new DatabaseException(message + code + "]",e);
			return exception;
		}
		

	}
}
