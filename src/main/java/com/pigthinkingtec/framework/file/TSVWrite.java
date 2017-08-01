package com.pigthinkingtec.framework.file;

/**
 * TSVファイルを出力する
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TSVWrite extends FileWrite {
	private TSVLiner liner = null;

	/**
	 * コンストラクター
	 * 
	 */
	public TSVWrite() {
		super();
		liner = new TSVLiner();
	}

	protected String makeLine(String[] line, int linecount) {
		return liner.format(line);
	}

}
