package com.pigthinkingtec.batch.framework.util.onlinejob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.exception.SystemException;

/**
 * SOS-JobScheduler経由してジョブ、ジョブチェーンを起動するクラス
 * @author AB-ZHOU
 *
 */
public class JobCenterTool implements JobToolInterface{

	/** ログ出力用Logger */
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(JobCenterTool.class);

	@Override
	public int runJob(JobToolConnectInfo info, String jobid) throws SystemException {
		// TODO 
		return 0;
	}


	@Override
	public int runJobChain(JobToolConnectInfo info, String jobchainid) throws SystemException {
		// TODO 
		return 0;
	}

}
