package com.pigthinkingtec.framework.auditlog;

import java.util.ArrayList;
import java.util.List;

import com.pigthinkingtec.framework.databean.DataBeanInterface;

import lombok.Getter;
import lombok.Setter;

/**
 * 監査Logを出力するDataBean
 * 
 * @author  AB-ZHOU
 * @version $Revision: 1.0 $ $Date: 2015/11/26 19:19:00 $
 * @history 
 * 
 */
@Getter @Setter
public class AuditLogBean implements DataBeanInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6212566022225780163L;
	
	private String comupterName = null;
	private String jobId = null;
	private String messageId = null;
	private String serviceName = null;
	private String loginfo = null;
	private List<String> args = new ArrayList<String>();	
}
