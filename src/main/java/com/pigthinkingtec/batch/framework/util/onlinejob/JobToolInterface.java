package com.pigthinkingtec.batch.framework.util.onlinejob;

import com.pigthinkingtec.framework.exception.SystemException;

public interface JobToolInterface {

	abstract public int runJob(JobToolConnectInfo connInfo, String jobPath) throws SystemException;

	abstract public int runJobChain(JobToolConnectInfo connInfo, String jobChainPath) throws SystemException;
}
