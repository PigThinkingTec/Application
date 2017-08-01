package com.pigthinkingtec.batch.framework.common.utils;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.DatabaseBatchReturnCodeBean;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.DatabaseException;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.ControllerUtil;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.StringUtil;

public class ReturnCodeUtil {
	/** ログ出力用Logger */
	private final static Logger logger = LoggerFactory.getLogger(ReturnCodeUtil.class);

	private static Map<String, Integer> returnCodeMap = null;

	/**
	 * Storedに使う戻り値のMapを取得する
	 * 
	 * @return Storedに使う戻り値のKey－Value Map
	 */
	public static Map<String, Integer> getReturnCodeMap(String batchPgId) throws SystemException {
		if (returnCodeMap == null) {

			// batch用framework.propertiesファイルから、batch return codeの取得方式を取得する
			String batchReturncodeSourceType = FwPropertyReader
					.getProperty(BatchConstants.BATCH_RETURN_CODE_SOURCE_TYPE, null);

			if (StringUtil.isEmpty(batchReturncodeSourceType)) {
				String msg = "key [" + BatchConstants.BATCH_RETURN_CODE_SOURCE_TYPE + "] is Empty. "
						+ "Must to set key[" + BatchConstants.BATCH_RETURN_CODE_SOURCE_TYPE + "] in "
						+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file";
				logger.error(msg);
				throw new SystemException(msg);
			}

			if (BatchConstants.SOURCE_TYPE_FILE.equals(batchReturncodeSourceType.toUpperCase())) {
				// Property Fileから取得する
				returnCodeMap = getReturnCodeMapFromFile();
			} else if (BatchConstants.SOURCE_TYPE_DB.equals(batchReturncodeSourceType.toUpperCase())) {
				// Databaseから取得する
				returnCodeMap = getReturnCodeMapFromDatabase(batchPgId);
			} else {
				// Defaultとして、Property Fileから取得する
				returnCodeMap = getReturnCodeMapFromFile();
			}
		}
		return returnCodeMap;
	}

	/**
	 * Storedに使う戻り値Key<br>
	 * (例：正常の場合のKey：0,<br>
	 * Warningの場合のKey：2,<br>
	 * 業務エラーの場合のKey：8,<br>
	 * システムエラーの場合のKey：9)<br>
	 * によって戻り値を取得する
	 * 
	 * @param key
	 *            戻り値のKey
	 * @return Storedに使う戻り値
	 * @throws SystemException
	 */
	public static int getRetCodeByRetKey(String retKey, String batchPgId) throws SystemException {
		Map<String, Integer> retCodeMap = new HashMap<String, Integer>();

		try {
			// 戻り値を取得する(Default: OK:0 Waring:2 BusinessError:8 Exception:9)
			retCodeMap = ReturnCodeUtil.getReturnCodeMap(batchPgId);
		} catch (DatabaseException e) {
			throw new SystemException("ReturnCodeUtil.getReturnCode: DatabaseException = " + e.toString(), e);
		} catch (Exception e) {
			throw new SystemException("ReturnCodeUtil.getReturnCode: Exception = " + e.toString(), e);
		}

		int ret = 0;
		if (retCodeMap.containsKey(retKey)) {
			ret = retCodeMap.get(String.valueOf(retKey));
		} else {
			throw new SystemException("There is no setting for Key [ " + retKey + " ] in System_ReturnCode table.");
		}

		return ret;
	}

	/**
	 * Storedに使う戻り値Key<br>
	 * ( 正常：RETUNCODE_S、<br>
	 * Warning:RETUNCODE_W、<br>
	 * 業務エラー:RETUNCODE_E、<br>
	 * システムエラー:RETUNCODE_A)<br>
	 * によって戻り値を取得する
	 * 
	 * @param key
	 *            戻り値のKey()
	 * @return Storedに使う戻り値
	 * @throws SystemException
	 */
	public static int getRetCode(String key, String batchPgId) throws SystemException {

		Map<String, Integer> retCodeMap = new HashMap<String, Integer>();
		Map<String, String> retCodeKeyMap = new HashMap<String, String>();

		try {
			// 戻り値を取得する(Default: OK:0 Waring:2 BusinessError:8 Exception:9)
			retCodeMap = ReturnCodeUtil.getReturnCodeMap(batchPgId);
		} catch (DatabaseException e) {
			throw new SystemException("ReturnCodeUtil.getReturnCode: DatabaseException = " + e.toString(), e);
		} catch (Exception e) {
			throw new SystemException("ReturnCodeUtil.getReturnCode: Exception = " + e.toString(), e);
		}

		try {
			// 戻り値のKey(Default: OK:0 Waring:2 BusinessError:8 Exception:9)を取得する
			retCodeKeyMap = ReturnCodeKeyUtil.getReturnCodeKeyMap(batchPgId);
		} catch (DatabaseException e) {
			throw new SystemException("ReturnCodeKeyUtil.getReturnCodeKey: DatabaseException = " + e.toString(), e);
		} catch (Exception e) {
			throw new SystemException("ReturnCodeKeyUtil.getReturnCodeKey: Exception = " + e.toString(), e);
		}

		int ret = 0;
		if (retCodeMap.containsKey(String.valueOf(retCodeKeyMap.get(key)))) {
			ret = retCodeMap.get(String.valueOf(retCodeKeyMap.get(key)));
		} else {
			throw new SystemException(
					"There is no setting for Key [ " + retCodeKeyMap.get(key) + " ] in System_ReturnCode table.");
		}

		return ret;
	}

	/**
	 * batch用framework.propertiesファイルから、batch return codeを取得する
	 * 
	 * @return
	 */
	private static Map<String, Integer> getReturnCodeMapFromFile() throws SystemException {
		Map<String, Integer> map = new HashMap<String, Integer>();

		int ret = 0;

		// 正常終了の戻り値を取得
		ret = getReturnCodeMapFromFile(BatchConstants.BATCH_RETURN_CODE_SUCCESS_KEY);
		map.put(String.valueOf(BatchConstants.RET_KEY_OK), ret);

		// Warning終了の戻り値を取得
		ret = getReturnCodeMapFromFile(BatchConstants.BATCH_RETURN_CODE_WARNING_KEY);
		map.put(String.valueOf(BatchConstants.RET_KEY_WARNING), ret);

		// UserError終了の戻り値を取得
		ret = getReturnCodeMapFromFile(BatchConstants.BATCH_RETURN_CODE_USRERROR_KEY);
		map.put(String.valueOf(BatchConstants.RET_KEY_USER_ERR), ret);

		// 異常終了の戻り値を取得
		ret = getReturnCodeMapFromFile(BatchConstants.BATCH_RETURN_CODE_EXCEPTION_KEY);
		map.put(String.valueOf(BatchConstants.RET_KEY_SYSERR), ret);

		return map;
	}

	/**
	 * batch用framework.propertiesファイルから、batch return codeのを取得する
	 * 
	 * @return
	 */
	private static int getReturnCodeMapFromFile(String key) throws SystemException {

		int ret = -1;

		String rtnCode = FwPropertyReader.getProperty(key, null);

		if (StringUtil.isEmpty(rtnCode)) {
			// Fileから取得できない場合

			logger.warn("The key[" + key + "] is empty. Please check " + SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME
					+ " file.");

			switch (key) {
			case BatchConstants.BATCH_RETURN_CODE_SUCCESS_KEY:
				ret = BatchConstants.RET_OK;
				break;
			case BatchConstants.BATCH_RETURN_CODE_WARNING_KEY:
				ret = BatchConstants.RET_WARNING;
				break;
			case BatchConstants.BATCH_RETURN_CODE_USRERROR_KEY:
				ret = BatchConstants.RET_USER_ERR;
				break;
			case BatchConstants.BATCH_RETURN_CODE_EXCEPTION_KEY:
				ret = BatchConstants.RET_SYSERR;
				break;
			default:
				String msg = "The key [" + key + "] is not correct.";

				logger.error(msg);
				throw new SystemException(msg);
			}
		} else {
			// 取得できた場合
			try {
				ret = Integer.valueOf(rtnCode);
			} catch (NumberFormatException e) {
				logger.error(e.toString());
				throw new SystemException(e);
			}
		}

		return ret;
	}

	/**
	 * Databaseから、batch return codeを取得する
	 * 
	 * @return
	 */
	private static Map<String, Integer> getReturnCodeMapFromDatabase(String batchPgId) throws SystemException {
		Map<String, Integer> map = new HashMap<String, Integer>();

		// ユーザデータを設定する
		UserContainer user = DefaultBatchUserUtil.getUser(batchPgId);

		// InputデータBeanを設定する
		DatabaseBatchReturnCodeBean inputDataBean = new DatabaseBatchReturnCodeBean();
		inputDataBean.setBatchPgId(batchPgId);

		// batch用framework.propertiesファイルから、DBからbatch return codeを取得するクラス名を取得する
		// クラスを起動し、戻り値を取得する
		Report report = ControllerUtil.runService(BatchConstants.BATCH_RETURN_CODE_DB_ACCESS_CLASS_KEY, user,
				inputDataBean);

		logger.debug("ReturnCodeUtil: getReturnCodeMapFromDatabase() end");

		// DBから取得したBatch return code情報を取得
		map = ((DatabaseBatchReturnCodeBean) report.getOutputDataBean()).getMap();

		// 戻り値を設定
		return map;
	}
}
