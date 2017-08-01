package com.pigthinkingtec.batch.framework.util.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.FwPropertyReader;

/**
 * batch用framework.propertiesファイル設定によってBatch User情報を取得する
 * 
 * @author AB-ZHOU
 *
 */
public class BatchUserUtil {

	/** ログ出力用Logger */
	private final static Logger logger = LoggerFactory.getLogger(BatchUserUtil.class);

	private static BatchUserInterface batchuser = null;

	/**
	 * batch用framework.propertiesファイル設定によってBatch User情報を取得するクラスを初期化
	 * 
	 * @return Batch User情報を取得するクラスのインスタンス
	 */
	private static BatchUserInterface getBatchUserClass() throws SystemException {

		// batch用framework.propertiesファイルから、batch userの取得方式を取得する
		String batchUserSourceType = FwPropertyReader.getProperty(SystemConstant.BATCH_USER_SOURCE_TYPE, null);

		if (batchUserSourceType == null) {
			logger.error("Can not found key [" + SystemConstant.BATCH_USER_SOURCE_TYPE + "] in "
					+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file. " + "Must to set key["
					+ SystemConstant.BATCH_USER_SOURCE_TYPE + "] either File or Database.");

			throw new SystemException("Can not found key [" + SystemConstant.BATCH_USER_SOURCE_TYPE + "] in "
					+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file. ");

		}

		if (BatchConstants.SOURCE_TYPE_FILE.equals(batchUserSourceType.toUpperCase())) {
			// Property Fileから取得する
			batchuser = new FileBatchUser();
		} else if (BatchConstants.SOURCE_TYPE_DB.equals(batchUserSourceType.toUpperCase())) {
			// Databaseから取得する
			batchuser = new DatabaseBatchUser();
		} else {
			// Defaultとして、Property Fileから取得する
			batchuser = new FileBatchUser();
		}

		return batchuser;
	}

	/**
	 * Batch User情報を取得する
	 * 
	 * @return System_User
	 */
	public static System_User getBatchUser(String batchPgId) throws SystemException {

		if (batchuser == null) {
			batchuser = getBatchUserClass();
		}

		return batchuser.getBatchUser(batchPgId);
	}
}
