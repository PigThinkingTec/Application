package com.pigthinkingtec.framework.file;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * TAB用のTokenizer
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TABTokenizer extends Tokenizer {
	public TABTokenizer(){
		super();
		setSeparater(SystemConstant.TAB);
	}
}
