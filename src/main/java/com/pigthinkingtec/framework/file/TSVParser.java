package com.pigthinkingtec.framework.file;

/**
 * 文字列を「\t（タブ）」区切りで解析するクラス
 *
 * @author yizhou
 * @history
 *
 * 
 */
public class TSVParser extends Parser {

	/**
	 * コンストラクタ
	 */
	public TSVParser() {
		super();
		token = new TABTokenizer();
	}	
}
