package com.pigthinkingtec.framework.file;

/**
 * CVSファイルを出力する
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class CSVWrite extends FileWrite {
	private CSVLiner liner = null;

	/**
	 * コンストラクター
	 * 
	 */
	public CSVWrite() {
		super();
		liner = new CSVLiner();
	}

	protected String makeLine(String[] line, int linecount) {
		return liner.format(line);
	}

}
