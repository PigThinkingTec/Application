package com.pigthinkingtec.framework.databean;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * ページセレクターの内容を保持するクラス
 * 
 * @author yizhou
 * @history 
 * 
 */
@Deprecated
@SuppressWarnings("serial")
public class PageSelectorBean implements Serializable {

	public PageSelectorBean() {
		super();
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param label
	 * @param value
	 * @param from
	 * @param to
	 */
	public PageSelectorBean(String label, Integer value, Integer from, Integer to) {
		this.label = label;
		this.value = value;
		this.from = from;
		this.to = to;
	}
	
	/* セレクトボックスに表示するラベル */
	private String label = null;
	
	/* セレクトボックスの値 */
	private Integer value = null;
	
	/* 開始番号 */
	private Integer from = null;
	
	/* 終了番号 */
	private Integer to = null;
	
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getTo() {
		return to;
	}
	public void setTo(Integer to) {
		this.to = to;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	/**
	 * セレクトボックスに保持するデータを生成する
	 * 
	 * @param rowCount
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ArrayList createPageList(int rowCount, int pageSize) {
		
		ArrayList pageList = new ArrayList();
		// データ件数が0件の場合は空リストを作成する
		if (rowCount == 0) {
			pageList.add(new PageSelectorBean("0 - 0 of 0" ,new Integer(0), new Integer(0), new Integer(0)));
			return pageList;
		}
		
		int pageCount = 0;
		if (rowCount != 0 && rowCount % pageSize == 0) {
			pageCount = rowCount / pageSize;
		} else {
			pageCount = rowCount / pageSize + 1;
		}
		
		for (int i = 0; i < pageCount; i++){
			StringBuffer label = new StringBuffer();
			Integer from = new Integer((pageSize * i) + 1);
			label.append(from);
			label.append(" - ");
			Integer to = null;
			
			//最終ページかどうか
			if ((pageCount - 1) != i) {
				to = new Integer(pageSize * (i + 1));
			} else {
				to = new Integer(rowCount);
			}
			label.append(to);
			label.append(" of ");
			label.append(String.valueOf(rowCount));
			pageList.add(new PageSelectorBean(label.toString() ,new Integer(i), from, to));
		}
		
		return pageList;
	}
}