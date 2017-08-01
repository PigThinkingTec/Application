package com.pigthinkingtec.batch.framework.common.databean;

import java.util.HashMap;
import java.util.Map;

import com.pigthinkingtec.framework.databean.DataBeanInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * DBからBatch Return CodeのKeyを取得するDataBean
 * 
 * @author  AB-ZHOU
 * @version $Revision: 1.0 $ $Date: 2015/11/12 13:59:02 $
 * @history 
 * 
 */
@Getter @Setter
public class DatabaseBatchReturnCodeKeyBean implements DataBeanInterface {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -72541634932379474L;

	private String batchPgId = null;

	private Map<String, String> map = new HashMap<String, String>();
}
