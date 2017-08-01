package com.pigthinkingtec.batch.framework.util.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.databean.DatabaseBatchUserBean;
import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.batch.framework.common.utils.DefaultBatchUserUtil;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.ControllerUtil;

/**
 * DB テーブルからBatch User情報を取得する
 * 
 * @author AB-ZHOU
 *
 */
public class DatabaseBatchUser implements BatchUserInterface {

	/** ログ出力用Logger */
	private final static Logger logger = LoggerFactory.getLogger(DatabaseBatchUser.class);

	@Override
	public System_User getBatchUser(String batchPgId) throws SystemException {

		// ユーザデータを設定する
		UserContainer user = DefaultBatchUserUtil.getUser(batchPgId);

		// InputデータBeanを設定する
		DatabaseBatchUserBean inputDataBean = new DatabaseBatchUserBean();
		inputDataBean.setBatchPgId(batchPgId);

		// Serviceを実行して、戻り値を取得する
		Report report = ControllerUtil.runService(SystemConstant.BATCH_USER_DB_ACCESS_CLASS_KEY, user, inputDataBean);

		logger.debug("DatabaseBatchUser: getBatchUser() end");

		// DBから取得したBatch User情報を取得
		System_User batchUser = ((DatabaseBatchUserBean) report.getOutputDataBean()).getUser();

		// 戻り値を設定
		return batchUser;
	}
}
