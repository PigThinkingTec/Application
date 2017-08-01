package com.pigthinkingtec.batch.framework.util.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.FwPropertyReader;

/**
 * PropertyファイルからBatch user IDを取得する
 * 
 * @author AB-ZHOU
 *
 */
public class FileBatchUser implements BatchUserInterface {

	private final static Logger logger = LoggerFactory.getLogger(FileBatchUser.class);

	@Override
	public System_User getBatchUser(String batchPgId) throws SystemException {

		System_User batchUser = new System_User();

		// batch用framework.propertiesファイルから、batch userを取得する
		String batchUserId = FwPropertyReader.getProperty(SystemConstant.BATCH_USER_ID_KEY, null);

		if (batchUserId == null) {
			String msg = "Can not found key [" + SystemConstant.BATCH_USER_ID_KEY + "] in "
					+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file. " + "Must to set key["
					+ SystemConstant.BATCH_USER_ID_KEY + "].";
			logger.error(msg);
			throw new SystemException(msg);
		}

		// batch用framework.propertiesファイルから、batch userの言語情報を取得する
		String batchUserLang = FwPropertyReader.getProperty(SystemConstant.BATCH_USER_LANG_KEY, null);

		if (batchUserLang == null) {
			String msg = "Can not found key [" + SystemConstant.BATCH_USER_ID_KEY + "] in "
					+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file. " + "Must to set key["
					+ SystemConstant.BATCH_USER_ID_KEY + "].";
			logger.error(msg);
			throw new SystemException(msg);
		}

		batchUser.setUserID(batchUserId);
		batchUser.setLanguage(batchUserLang);

		return batchUser;
	}
}
