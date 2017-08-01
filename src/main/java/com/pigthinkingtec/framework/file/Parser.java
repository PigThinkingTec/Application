package com.pigthinkingtec.framework.file;

/**
 * 文字列を指定されたTokenで解析するクラス
 *
 * @author yizhou
 * @history
 *
 * 
 */
public abstract class Parser {
	
	protected Tokenizer token = null;

	/**
	 * 
	 * 
	 * @param line
	 * @return String[]
	 */
	public String[] parseStringArray(String line) {
		String[] strings = null;
		
		token.setNewLine(line);
		int count = token.countTokens();
		strings = new String[count];
		int index = 0;
		while (token.hasMoreElements()) {
			strings[index] = token.nextToken();
			index++;
		}
		return strings;
	}
}
