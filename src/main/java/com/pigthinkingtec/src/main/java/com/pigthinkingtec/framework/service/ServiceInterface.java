package com.pigthinkingtec.framework.service;

import com.pigthinkingtec.framework.databean.Order;
import com.pigthinkingtec.framework.databean.Report;
import com.pigthinkingtec.framework.exception.SystemException;

/**
 * Serviceクラスのインターフェース
 * 
 * @author  yizhou
 * @version 
 * @history 
 * 
 */
public interface ServiceInterface {
	
	abstract public void execute() throws SystemException;
	abstract public void setOrder(Order order);
	abstract public Report getReport();
	
}
