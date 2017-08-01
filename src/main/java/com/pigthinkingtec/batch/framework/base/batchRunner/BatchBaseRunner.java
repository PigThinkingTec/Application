package com.pigthinkingtec.batch.framework.base.batchRunner;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.ProcessManagementBean;
import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.batch.framework.common.utils.ReturnCodeUtil;
import com.pigthinkingtec.batch.framework.util.batch.BatchUserUtil;
import com.pigthinkingtec.framework.databean.OnlineOrder;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.dbaccess.TransactionScope;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.service.ServiceInterface;
import com.pigthinkingtec.framework.util.DateUtil;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.StringUtil;

public class BatchBaseRunner {

	/** ログ出力用Logger */
	private final static Logger logger = LoggerFactory.getLogger(BatchBaseRunner.class);

	// /** バッチ起動クラスに必要な最低限パラメータ数
	// * 1個目：起動対象バッチプログラムID
	// * 2個目：起動対象バッチのJobID
	// * 3個目以降：対象バッチ自身に必要なパラメータ*/
	/** バッチ起動クラスに必要な最低限パラメータ数 */
	public final static int MIN_PARAMETER_COUNT = 2;

	// バッチプログラムID
	private String batchPgId = "";

	// ユーザID
	private String userId = "";
	
	// Job ID
	private String jobId = "";

	/**
	 * 
	 * @Title: getParams
	 * @Description: バッチの引数を取得する 必要なの引数を除く
	 * @param args
	 * @return
	 */
	private String[] getParams(String[] args) {
		List<String> paramList = new ArrayList<String>();
		if (args.length > MIN_PARAMETER_COUNT) {
			int argsSize = args.length;
			for (int argsCnt = MIN_PARAMETER_COUNT; argsCnt < argsSize; argsCnt++) {
				String tmp = args[argsCnt];
				paramList.add(tmp);
			}
		}
		String[] ret = paramList.toArray(new String[paramList.size()]);
		return ret;
	}

	/**
	 * サービスを起動する
	 * 
	 * @throws SystemException
	 */
	public int executeService(String[] args) {

		logger.info("BatchBaseRunner: executeService() start");

		int al = args.length;

		if (al < BatchBaseRunner.MIN_PARAMETER_COUNT) {
			// 最低でも起動対象バッチのプログラムIDが必要なので、無い場合エラーにする
			logger.error("BatchBaseRunner.MIN_PARAMETER_COUNT [" + BatchBaseRunner.MIN_PARAMETER_COUNT + "is not enough.Please check parameters.");
			System.exit(BatchConstants.RET_SYSERR);
		}

		if (StringUtil.isEmpty(args[0])) {
			logger.error("args[0] [" + args[0] + "is empty. Please check parameter args[0].");
			System.exit(BatchConstants.RET_SYSERR);
		}
		
		if (StringUtil.isEmpty(args[1])) { 
			logger.error("args[1] [" + args[1] + "is empty. Please check parameter args[1].");
			System.exit(BatchConstants.RET_SYSERR);
		}
		
		// 起動対象バッチPG IDを設定する
		setBatchPgId(args[0]);
		// 起動対象バッチのJob IDを設定する
		setJobId(args[1]);

		// 処理管理クラス
		ProcessManagementBean manager = new ProcessManagementBean();
		// 処理開始時間を処理管理クラスに設定する。
		manager.setShoriKaishiDate(DateUtil.getNowMilSec());
		// バッチプログラムIDを設定する
		manager.setBatchPgId(this.getBatchPgId());
		logger.debug("batchPgId: " + batchPgId);

		try {
			// batchプログラムIDから、対応するサービスを取得する
			String serviceClass = FwPropertyReader.getProperty(BatchConstants.BATCH_ID_NAME_PROPERTY_FILE_NAME,
					batchPgId, null);

			// ビジネスロジック層に渡すInputデータオブジェクト
			OnlineOrder order = new OnlineOrder();

			// ビジネスロジック層に渡す情報の詰め込み
			// ユーザデータを設定する
			UserContainer user = initUser(this.getBatchPgId());
			user.setPgmId(this.getBatchPgId());//Batch　PGIDを設定する
			user.setJobId(this.getJobId());//JobIDも設定する

			order.setLoginUser(user);

			// トランザクションを設定する
			order.setTransactionScope(TransactionScope.LOCAL);

			// 処理管理クラスを設定する
			order.setInputDataBean(manager);

			// バッチの引数を設定する
			order.setArgumentData(getParams(args));

			// バッチから起動するFlgを立てる
			order.setBatchFlg(true);

			setUserId(order.getLoginUser().getUserId());

			logger.debug("ServiceClass: " + serviceClass);

			// ビジネスロジックServiceクラスを取得する
			ServiceInterface service = (ServiceInterface) Class.forName(serviceClass).newInstance();

			// ビジネスロジック層にInputデータを渡す
			service.setOrder(order);

			// 処理を実行する。
			service.execute();

			logger.info("BatchBaseRunner: executeService() end. resultCode = [" +  manager.getResultCode() + "].");

			return manager.getResultCode();

		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			// 異常終了
			int RET_EXCEPTION = BatchConstants.RET_SYSERR;
			try {
				RET_EXCEPTION = ReturnCodeUtil.getRetCode(BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION,
						this.getBatchPgId());
			} catch (Exception e) {
				logger.error("resultCode = [" + RET_EXCEPTION + "]." + ex.toString(), ex);
				return BatchConstants.RET_SYSERR;
			}

			logger.error("resultCode = [" + RET_EXCEPTION + "]." );
			return RET_EXCEPTION;
		}
	}

	private UserContainer initUser(String batchPgId) throws SystemException {
		UserContainer user = new UserContainer();

		// system_contanstからUserIDを取得
		System_User batchUser = BatchUserUtil.getBatchUser(batchPgId);

		if (batchUser != null) {
			user.setUserId(batchUser.getUserID());
			user.setUserLang(batchUser.getLanguage());
		} else {
			// Batch_User IDが未設定する場合、エラーで終了する
			logger.warn("Can not get batch_user info. The default user_id [" + BatchConstants.DEFAULT_BATCH_USER_ID
					+ "] and the default lang [" + BatchConstants.DEFAULT_BATCH_USER_LANG + "] will be used.");

			user.setUserId(BatchConstants.DEFAULT_BATCH_USER_ID);
			user.setUserLang(BatchConstants.DEFAULT_BATCH_USER_LANG);
		}

		return user;
	}

	/**
	 * getter method
	 * 
	 * @return the userId
	 */

	public String getUserId() {
		return userId;
	}

	/**
	 * setter method
	 * 
	 * @param userId
	 *            the userId to set
	 */

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * getter method
	 * 
	 * @return batchPgId
	 */

	public String getBatchPgId() {
		return batchPgId;
	}

	/**
	 * setter method
	 * 
	 * @param batchPgId
	 */
	public void setBatchPgId(String batchPgId) {
		this.batchPgId = batchPgId;
	}

	/**
	 * getter method
	 * 
	 * @return jobId
	 */

	public String getJobId() {
		return jobId;
	}

	/**
	 * setter method
	 * 
	 * @param jobId
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	
}
