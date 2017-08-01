package com.pigthinkingtec.batch.framework.common.utils;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.util.FwPropertyReader;

public class DefaultBatchUserUtil {

	public static UserContainer getUser(String batchPgId) {

		UserContainer user = new UserContainer();

		// batch用framework.propertiesファイルから、default言語を取得する
		String defaultLang = FwPropertyReader.getProperty(BatchConstants.DEFAULT_LANG_KEY, null);

		if (defaultLang == null) {
			user.setUserLang(BatchConstants.DEFAULT_BATCH_USER_LANG);
		} else {
			user.setUserLang(defaultLang);
		}
		user.setUserId(BatchConstants.DEFAULT_BATCH_USER_ID);

		// PGIDを設定する
		user.setPgmId(batchPgId);
		// Job IDを設定する
		user.setJobId(batchPgId);
		return user;
	}

}
