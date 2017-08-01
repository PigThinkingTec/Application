package com.pigthinkingtec.batch.framework.common.databean;

import com.pigthinkingtec.framework.databean.DataBeanInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * DBからBatch Userを取得するDataBean
 * 
 * @author  AB-ZHOU
 * @version $Revision: 1.0 $ $Date: 2015/10/30 17:40:02 $
 * @history 
 * 
 */
@Getter @Setter
public class DatabaseBatchUserBean implements DataBeanInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private System_User user = null;
	private String batchPgId = null;
}
