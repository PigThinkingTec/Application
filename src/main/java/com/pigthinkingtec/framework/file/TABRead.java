package com.pigthinkingtec.framework.file;

/**
 * TABデータ(1行)をカンマで分割する
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TABRead extends FileRead {
	TABTokenizer tkn = null;

	public TABRead() {
		super();
	}

	/*
	 *  (non-Javadoc)
	 * @see com.pigthinkingtec.framework.util.file.FileRead#setArgument(com.pigthinkingtec.framework.util.file.ParseResult, java.lang.String, int)
	 */
	protected void setArgument(ParseResult rst, String line, int linecount) {
		if (tkn == null) {
			tkn = new TABTokenizer();
		}
		tkn.setNewLine(line);
		while (tkn.hasMoreElements()) {
			rst.add(tkn.nextToken());
		}
		rst.setCount(tkn.countTokens());

		System.out.println(rst.getObject(0, 0));
		System.out.println(rst.getObject(0, 1));

	}
}
