package com.pigthinkingtec.framework.file;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ファイルデータ保持クラス 
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class ParseResult {
	private Log log = LogFactory.getLog("ParseResult");

	private ArrayList result = null;
	@SuppressWarnings("unused")
	private ArrayList colName = null;

	private int colCount = 0;

	public ParseResult() {
		result = new ArrayList();
		colName = new ArrayList();
	}

	public void setCount(int count) {
		log.debug("------------------------[" + count + "/" + colCount + "]");
		colCount = count;
	}
	
	public int getColCount(){
		return colCount;
	}

	@SuppressWarnings("unchecked")
	public boolean add(Object o) {
		return result.add(o);
	}

	public int getRowCount() {
		if(result.size() == 0 || colCount == 0) return 0;
		else return (result.size()) / colCount;
	}

	public Object getObject(int row, int col) {
		return result.get((row * colCount) + col);
	}
	
	@SuppressWarnings("unused")
	private void dump(){
		System.out.println("----------------------------------------");
		for(int i = 0 ; i < result.size(); i++){
			System.out.print(result.get(i));
		}
		System.out.println("----------------------------------------");
	}
}