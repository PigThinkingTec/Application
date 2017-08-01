package com.pigthinkingtec.framework.file;

/**
 *　文字列を「,（カンマ）」区切りで解析するクラス
 *
 * @author yizhou
 * @history
 *
 * 
 */
public class CSVParser extends Parser {

	/**
	 * コンストラクタ
	 * 
	 */
	public CSVParser() {
		super();
		token = new CSVTokenizer();
	}
}



