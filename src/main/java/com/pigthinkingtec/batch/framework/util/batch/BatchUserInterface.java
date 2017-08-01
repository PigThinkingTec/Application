package com.pigthinkingtec.batch.framework.util.batch;

import com.pigthinkingtec.batch.framework.common.databean.System_User;
import com.pigthinkingtec.framework.exception.SystemException;

public interface BatchUserInterface {

	abstract public System_User getBatchUser(String batchPgId) throws SystemException;
}
