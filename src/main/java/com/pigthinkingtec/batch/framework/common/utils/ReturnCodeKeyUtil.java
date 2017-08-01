package com.pigthinkingtec.batch.framework.common.utils;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.batch.framework.common.constants.BatchConstants;
import com.pigthinkingtec.batch.framework.common.databean.DatabaseBatchReturnCodeKeyBean;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.ControllerUtil;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.StringUtil;

public class ReturnCodeKeyUtil {

	/** ログ出力用Logger */
	private final static Logger logger = LoggerFactory.getLogger(ReturnCodeKeyUtil.class);

	private static Map<String, String> returnCodeKeyMap = null;

	/**
	 * 下記４つ戻り値Keyの定数定義から、戻り値Keyを取得する<br>
	 * パラメータの設定可能値<br>
	 * ( 	正常：RETUNCODE_S、<br>
	 * 		Warning:RETUNCODE_W、<br>
	 * 		業務エラー:RETUNCODE_E、<br>
	 * 		システムエラー:RETUNCODE_A)<br>
	 * @param retCodeKey 戻り値Keyの定数定義    <br>
	 * @param batchPgId  バッチプログラムID
	 * @return 
	 * @throws SystemException
	 */
	public static String getReturnCodeKey(String retCodeKey, String batchPgId) throws SystemException {
		
		if (StringUtil.isEmpty(retCodeKey)) {
			String msg = "The parameter: retCodeKey [" + retCodeKey + "] is Empty! Cannot get returnCodeKey!!! ";
			logger.error(msg);
			return "";
		}
		
		retCodeKey = retCodeKey.trim();
		
		if (StringUtil.isEmpty(retCodeKey)) {
			String msg = "The parameter: retCodeKey.trim() [" + retCodeKey + "] is Empty! Cannot get returnCodeKey!!! ";
			logger.error(msg);
			return "";
		}
		
		if (! isOkReturnCodeKey(retCodeKey) ) {
			String msg = "The parameter: retCodeKey.trim() [" + retCodeKey + "] is invaild value! Cannot get returnCodeKey!!! ";
			logger.error(msg);
			return "";
		}
		
		// KeyMapを初期化
		getReturnCodeKeyMap(batchPgId);
		

		// 戻り値Keyを戻す
		return returnCodeKeyMap.get(retCodeKey);
	}
	
	// 戻り値Keyの定数定義をチェックする
	private static boolean isOkReturnCodeKey(String retCodeKey) {
		
		switch(retCodeKey){
			case BatchConstants.BATCH_RETURN_CODE_KEY_OK:
			case BatchConstants.BATCH_RETURN_CODE_KEY_WARNING:
			case BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR:
			case BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION:
				return true;
			default:
				return false;
		}
	}
	
	
	/**
	 * Storedに使う戻り値のKey情報のMapを取得する
	 * 
	 * @return this class instance
	 */
	public static Map<String, String> getReturnCodeKeyMap(String batchPgId) throws SystemException {
		if (returnCodeKeyMap == null) {

			// batch用framework.propertiesファイルから、batch return code keyの取得方式を取得する
			String batchReturncodeKeySourceType = FwPropertyReader
					.getProperty(BatchConstants.BATCH_RETURN_CODE_KEY_SOURCE_TYPE, null);

			if (StringUtil.isEmpty(batchReturncodeKeySourceType)) {
				String msg = "key [" + BatchConstants.BATCH_RETURN_CODE_KEY_SOURCE_TYPE + "] is Empty. "
						+ "Must to set key[" + BatchConstants.BATCH_RETURN_CODE_KEY_SOURCE_TYPE + "] in "
						+ SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME + ".properties file";
				logger.error(msg);
				throw new SystemException(msg);
			}

			if (BatchConstants.SOURCE_TYPE_FILE.equals(batchReturncodeKeySourceType.toUpperCase())) {
				// Property Fileから取得する
				returnCodeKeyMap = getReturnCodeKeyMapFromFile();
			} else if (BatchConstants.SOURCE_TYPE_DB.equals(batchReturncodeKeySourceType.toUpperCase())) {
				// Databaseから取得する
				returnCodeKeyMap = getReturnCodeKeyMapFromDatabase(batchPgId);
			} else {
				// Defaultとして、Property Fileから取得する
				returnCodeKeyMap = getReturnCodeKeyMapFromFile();
			}
		}
		return returnCodeKeyMap;
	}

	/**
	 * batch用framework.propertiesファイルから、batch return code Keyを取得する
	 * 
	 * @return
	 */
	private static Map<String, String> getReturnCodeKeyMapFromFile() throws SystemException {
		Map<String, String> map = new HashMap<String, String>();

		String ret = "0";

		// 正常終了の戻り値のKeyを取得
		ret = getReturnCodeKeyFromFile(BatchConstants.BATCH_RETURN_CODE_KEY_SUCCESS_KEY);
		map.put(BatchConstants.BATCH_RETURN_CODE_KEY_OK, ret);

		// Warning終了の戻り値のKeyを取得
		ret = getReturnCodeKeyFromFile(BatchConstants.BATCH_RETURN_CODE_KEY_WARNING_KEY);
		map.put(BatchConstants.BATCH_RETURN_CODE_KEY_WARNING, ret);

		// UserError終了の戻り値のKeyを取得
		ret = getReturnCodeKeyFromFile(BatchConstants.BATCH_RETURN_CODE_KEY_USRERROR_KEY);
		map.put(BatchConstants.BATCH_RETURN_CODE_KEY_BUSINESS_ERROR, ret);

		// 異常終了の戻り値のKeyを取得
		ret = getReturnCodeKeyFromFile(BatchConstants.BATCH_RETURN_CODE_KEY_EXCEPTION_KEY);
		map.put(BatchConstants.BATCH_RETURN_CODE_KEY_FATAL_EXCEPTION, ret);

		return map;
	}

	/**
	 * batch用framework.propertiesファイルから、batch return codeのKeyを取得する
	 * 
	 * @param 下記４つ種類値しか許せないこと
	 * batch.return.code.key.SUCCESS
	 * batch.return.code.key.WARNING
	 * batch.return.code.key.USRERROR
	 * batch.return.code.key.EXCEPTION
	 * 
	 * @return
	 */
	private static String getReturnCodeKeyFromFile(String key) throws SystemException {

		String ret = "";

		String rtnKey = FwPropertyReader.getProperty(key, null);

		if (StringUtil.isEmpty(rtnKey)) {
			// Fileから取得できない場合
			logger.warn("The key[" + key + "] is empty. Please check " + SystemConstant.FRAMEWORK_PROPERTY_FILE_NAME
					+ " file.");
			
			// Default値を使う
			ret = getDefaultReturnCodeKey(key);
			
		} else {
			// 前後Spaceを外す
			ret = rtnKey.trim();
			
			if (StringUtil.isEmpty(ret)) {
				// Default値を使う
				ret = getDefaultReturnCodeKey(key);
			}
		}

		return ret;
	}
	
	
	/*Keyと対応した戻り値Keyのデフォルト値
	 * 
	 * @param 下記４つ種類値しか許せないこと
	 * batch.return.code.key.SUCCESS
	 * batch.return.code.key.WARNING
	 * batch.return.code.key.USRERROR
	 * batch.return.code.key.EXCEPTION
	 */
	private static String getDefaultReturnCodeKey(String key) throws SystemException{
		switch (key) {
		case BatchConstants.BATCH_RETURN_CODE_KEY_SUCCESS_KEY:
			return BatchConstants.RET_KEY_OK;

		case BatchConstants.BATCH_RETURN_CODE_KEY_WARNING_KEY:
			return BatchConstants.RET_KEY_WARNING;

		case BatchConstants.BATCH_RETURN_CODE_KEY_USRERROR_KEY:
			return BatchConstants.RET_KEY_USER_ERR;
			
		case BatchConstants.BATCH_RETURN_CODE_KEY_EXCEPTION_KEY:
			return BatchConstants.RET_KEY_SYSERR;
			
		default:
			String msg = "The key [" + key + "] is not correct.";

			logger.error(msg);
			throw new SystemException(msg);
		}
	}

	/**
	 * Databaseから、batch return codeのKeyを取得する
	 * 
	 * @return
	 */
	private static Map<String, String> getReturnCodeKeyMapFromDatabase(String batchPgId) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();

		// ユーザデータを設定する
		UserContainer user = DefaultBatchUserUtil.getUser(batchPgId);

		// InputデータBeanを設定する
		DatabaseBatchReturnCodeKeyBean inputDataBean = new DatabaseBatchReturnCodeKeyBean();
		inputDataBean.setBatchPgId(batchPgId);

		// batch用framework.propertiesファイルから、DBからbatch return
		// codeのKeyを取得するクラス名を取得する
		// クラスを起動して、戻り値を取得する
		Report report = ControllerUtil.runService(BatchConstants.BATCH_RETURN_CODE_KEY_DB_ACCESS_CLASS_KEY, 
												  user,
												  inputDataBean);

		logger.debug("ReturnCodeKeyUtil: getReturnCodeKeyMapFromDatabase() end");

		// DBから取得したBatch return code情報を取得
		map = ((DatabaseBatchReturnCodeKeyBean) report.getOutputDataBean()).getMap();

		// 戻り値を設定
		return map;
	}
}
