package com.pigthinkingtec.batch.framework.common.databean;

import java.util.HashMap;
import java.util.Map;

import com.pigthinkingtec.framework.databean.DataBeanInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * DBからBatch Return Codeを取得するDataBean
 * 
 * @author  AB-ZHOU
 * @version $Revision: 1.0 $ $Date: 2015/11/12 14:01:02 $
 * @history 
 * 
 */
@Getter @Setter
public class DatabaseBatchReturnCodeBean implements DataBeanInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 432163449859143657L;
	
	private String batchPgId = null;

	private Map<String, Integer> map = new HashMap<String, Integer>();
}
