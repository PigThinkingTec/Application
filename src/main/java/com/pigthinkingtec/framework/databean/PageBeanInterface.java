package com.pigthinkingtec.framework.databean;

import java.util.ArrayList;
import java.util.List;


/**
 * Page Bean Interface
 * 
 * @author yizhou
 * @history
 * 
 */
@Deprecated
public interface PageBeanInterface {
	/** method defined for interface */
	@SuppressWarnings("rawtypes")
	public List getSearchList();
	@SuppressWarnings("rawtypes")
	public List getResultList();

//	public List getActionList();

	public void setRowCount(Integer integer);

	public PageSelectorBean getPage(int intPage);

	public Integer getPageSelector();

	public void changePage();
	@SuppressWarnings("rawtypes")
	public void setSearchList(List searchList);
	@SuppressWarnings("rawtypes")
	public ArrayList getPageList();
}
