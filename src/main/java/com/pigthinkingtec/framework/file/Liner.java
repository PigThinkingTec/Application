package com.pigthinkingtec.framework.file;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.util.StringUtil;

/**
 * 行データ編集SuperClass
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class Liner {
	private char separater = ' ';

	
	/**
	 * データをDouble Quotationで括る
	 * 
	 * @param line
	 * @return
	 */
	String enquote(String line) {

		if (line == null || line.length() == 0) {
			return StringUtil.EMPTY;
		}
		if (line.indexOf(SystemConstant.DOUBLE_QUOTE) < 0
				&& line.indexOf(separater) < 0) {
			return SystemConstant.DOUBLE_QUOTE + line
					+ SystemConstant.DOUBLE_QUOTE;
		}

		StringBuffer buf = new StringBuffer(line.length() + 2);
		buf.append(SystemConstant.DOUBLE_QUOTE);
		
		int start = 0;
		int end = 0;

		while ((end = line.indexOf(SystemConstant.DOUBLE_QUOTE, start)) != -1) {
			
			buf.append(line.substring(start, end));
			buf.append("\"\"");

			start = end + "\"\"".length() - 1;
		}
		buf.append(line.substring(start));
		buf.append(SystemConstant.DOUBLE_QUOTE);

		return buf.toString();
	}

	/**
	 * 区切り文字を設定する
	 * 
	 * @param separate
	 */
	protected void setSeparater(char separate) {
		this.separater = separate;
	}
}
