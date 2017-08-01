package com.pigthinkingtec.framework.spring.mvc.inputhelp;

import com.pigthinkingtec.framework.databean.IHResultRow;
import com.pigthinkingtec.framework.spring.mvc.AbstractForm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InputHelp用Form基底クラス
 *
 * @author yizhou
 * @history
 *
 *
 */
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
@Data
public abstract class AbstractIHForm extends AbstractForm {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory
			.getLogger(AbstractIHForm.class);

	// ボタン名
	protected String buttonName;

	/* 検索結果行(IHResultRow)の配列 */
	@SuppressWarnings("rawtypes")
	protected List resultList;

	/* 選択行の行番号 */
	protected String selected;

	protected String onFocusColumn;

	protected String valueField;

	protected String parentFormName;

	/* 検索結果のカラムと親画面のコントロール名のマッピング */
	protected Map<String, String> ctrl_mapping = new LinkedHashMap<String, String>();

	/**
	 * @return the valueField
	 */
	public String getValueField() {
		return valueField;
	}

	/**
	 * @param valueField
	 *        the valueField to set
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

	public void setParentFormName(String value) {
		this.parentFormName = value;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String _selected) {
		this.selected = _selected;
	}

	@SuppressWarnings("rawtypes")
	public List getResultList() {
		return this.resultList;
	}

	@SuppressWarnings("rawtypes")
	public void setResultList(List _resultlist) {
		this.resultList = _resultlist;
	}

	public IHResultRow getResult(int i) {
		if ((resultList == null) || (i > resultList.size())) {
			return null;
		} else {
			return (IHResultRow) resultList.get(i);
		}
	}

	public Map<String, String> getCtrlMapping() {
		return this.ctrl_mapping;
	}

	public void setCol1(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col1", value);
	}

	public void setCol2(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col2", value);
	}

	public void setCol3(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col3", value);
	}

	public void setCol4(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col4", value);
	}

	public void setCol5(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col5", value);
	}

	public void setCol6(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col6", value);
	}

	public void setCol7(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col7", value);
	}

	public void setCol8(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col8", value);
	}

	public void setCol9(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col9", value);
	}

	public void setCol10(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col10", value);
	}

	public void setCol11(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col11", value);
	}

	public void setCol12(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col12", value);
	}

	public void setCol13(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col13", value);
	}

	public void setCol14(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col14", value);
	}

	public void setCol15(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col15", value);
	}

	public void setCol16(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col16", value);
	}

	public void setCol17(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col17", value);
	}

	public void setCol18(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col18", value);
	}

	public void setCol19(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col19", value);
	}

	public void setCol20(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col20", value);
	}

	public void setCol21(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col21", value);
	}

	public void setCol22(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col22", value);
	}

	public void setCol23(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col23", value);
	}

	public void setCol24(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col24", value);
	}

	public void setCol25(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col25", value);
	}

	public void setCol26(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col26", value);
	}

	public void setCol27(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col27", value);
	}

	public void setCol28(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col28", value);
	}

	public void setCol29(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col29", value);
	}

	public void setCol30(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col30", value);
	}

	public void setCol31(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col31", value);
	}

	public void setCol32(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col32", value);
	}

	public void setCol33(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col33", value);
	}

	public void setCol34(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col34", value);
	}

	public void setCol35(String value) {
		if (this.ctrl_mapping.containsKey(value)) {
			this.ctrl_mapping.remove(value);
		}
		this.ctrl_mapping.put("col35", value);
	}
}
