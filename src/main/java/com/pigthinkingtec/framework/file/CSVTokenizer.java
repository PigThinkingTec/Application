package com.pigthinkingtec.framework.file;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * CSV用のTokenizer
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class CSVTokenizer extends Tokenizer {

	/**
	 * コンストラクター
	 * 
	 */
	public CSVTokenizer() {
		super();
		setSeparater(SystemConstant.COMMA);
	}
}
