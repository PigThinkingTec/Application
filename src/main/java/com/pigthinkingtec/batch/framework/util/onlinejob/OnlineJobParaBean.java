package com.pigthinkingtec.batch.framework.util.onlinejob;

import com.pigthinkingtec.framework.databean.AbstractOnlineDataBean;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zhou
 */
@Getter @Setter
public class OnlineJobParaBean extends AbstractOnlineDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String paraName;
	private String paraValue;

}

