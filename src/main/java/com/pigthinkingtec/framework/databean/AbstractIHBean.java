package com.pigthinkingtec.framework.databean;

import java.util.List;

/**
 * InputHelp用DataBean基底クラス
 * 
 * @author yizhou
 * @history
 *
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractIHBean extends AbstractOnlineDataBean {
	/* 検索結果行(IHResultRow)の配列 */
	protected List<IHResultRow> resultList;
	
	/* 選択行の行番号 */
	protected String selected;
	
	protected String onFocusColumn;
	
	protected String valueField;
	
	/**
	 * @return the valueField
	 */
	public String getValueField() {
		return valueField;
	}

	/**
	 * @param valueField the valueField to set
	 */
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getOnFocusColumn() {
		return onFocusColumn;
	}

	public void setOnFocusColumn(String _onFocusColumn) {
		this.onFocusColumn = _onFocusColumn;
	}
	
	public String getSelected() {
		return selected;
	}

	public void setSelected(String _selected) {
		this.selected = _selected;
	}

	
	public List<IHResultRow> getResultList(){
		return this.resultList;
	}
	
	
	public void setResultList(List<IHResultRow> _resultlist){
		this.resultList = _resultlist;
	}
	
	public IHResultRow getResult(int i){
		if ((resultList == null) || (i > resultList.size())){
			return null;
		}else{
			return resultList.get(i);
		}
	}
}
