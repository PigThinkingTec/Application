package com.pigthinkingtec.framework.dbaccess;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.pigthinkingtec.framework.util.Copy;

/**
 * TransactionManager生成クラス
 * 
 * @author yizhou
 *
 */
public class TransactionManagerFactory {
	
	/** ログ出力オブジェクト */
	private static final Logger logger = LoggerFactory.getLogger(TransactionManagerFactory.class);
	/** トランザクション設定情報 */
	private static TransactionConfig config = null;
	/** Singletonの場合のTransactionManager */
	private static TransactionManager transactionManager;
	/** Transaction設定情報記載ファイル名 */
	private static final String FILE_NAME = "transaction.xml";
	
	/**
	 * TransactionManagerを取得するメソッド
	 * 
	 * @return TransactionManagerオブジェクト
	 */
	public static synchronized TransactionManager getTransactionManager() {
		//Configをまだ読み込んでいない場合は、読み込みを実施
		if (config == null) {
			loadConfig();
		}
		
		TransactionManager tm = null;
		try {
			if (!config.isSingleton()) {
				//TransactionManagerをSingletonとして扱わない場合は、インスタンスを生成
				tm = (TransactionManager)(Class.forName(config.getTmName()).newInstance());
			} else {
				//Singletonとして扱う場合、まだTransactionManagerが生成されていない場合のみ、インスタンスを生成
				if (transactionManager == null) {
					transactionManager = (TransactionManager)(Class.forName(config.getTmName()).newInstance());
					tm = transactionManager;
				} else {
					tm = transactionManager;
				}
			}
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
			logger.error("class load error", e);
		}
		return tm;
	}
	
	/**
	 * Configファイルの読み込みを行うメソッド
	 * 
	 */
	private synchronized static void loadConfig() {
		//start
		logger.debug("init start");
		
		//XMLファイルのルールの設定
		Digester digester = new Digester();
		digester.addObjectCreate("config",TransactionConfig.class);
		digester.addSetProperties("config/timeout");
		digester.addCallMethod("config/timeout","setTimeout",2);
		digester.addCallParam("config/timeout/lifeTime",0);
		digester.addCallParam("config/timeout/interval",1);
		digester.addCallMethod("config/isolationLevel","setIsolationLevel",0);
		digester.addCallMethod("config/scope","setScope",0);
		digester.addCallMethod("config/tmName","setTmName",0);
		digester.addCallMethod("config/singleton","setSingleton",0);
		URL filePath = TransactionManagerFactory.class.getClassLoader().getResource(FILE_NAME);
		//transaction.xmlから情報を取得しTransactionConfigに設定する。
		try {
			config = (TransactionConfig)digester.parse(filePath.toString());
			TransactionScope.setDefaultScope(config.getScope());
			//log.debug("read transaction.xml end");
		} catch (SAXException e) {
			logger.error("XML NOT ANALYZE",e);
		} catch (IOException e) {
			logger.error("FILE NOT READ",e);
		}
		//end
		logger.debug("init end");
	}
	
	/**
	 * トランザクション設定情報オブジェクトを取得するメソッド
	 * 
	 * @return トランザクション設定情報
	 */
	public synchronized static TransactionConfig getTransactionConfig() {
		//設定情報をコピーして戻す
		return (TransactionConfig)Copy.copy(config);
	}
}
