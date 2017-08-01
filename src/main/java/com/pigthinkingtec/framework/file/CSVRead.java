package com.pigthinkingtec.framework.file;

/**
 * CSVデータ(1行)をカンマで分割する
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class CSVRead extends FileRead {
	CSVTokenizer tkn = null;

	/**
	 * コンストラクター
	 * 
	 */
	public CSVRead() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pigthinkingtec.framework.util.file.FileRead#setArgument(com.pigthinkingtec.framework.util.file.ParseResult,
	 *      java.lang.String)
	 */
	protected void setArgument(ParseResult rst, String line, int linecount) {
		getLog().info("setArgument Start");
		
		if (tkn == null) {
			tkn = new CSVTokenizer();
		}
		tkn.setNewLine(line);
		while (tkn.hasMoreElements()) {
			rst.add(tkn.nextToken());
		}
		rst.setCount(tkn.getCount());
		
		getLog().info("setArgument Start");
	}
}