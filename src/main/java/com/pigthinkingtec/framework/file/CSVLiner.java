package com.pigthinkingtec.framework.file;

import java.util.ArrayList;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * 配列をカンマ区切りの文字列に変更する 
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class CSVLiner extends Liner {

	/**
	 * コンストラクター
	 * 
	 */
	public CSVLiner() {
		super();
		setSeparater(SystemConstant.COMMA);
	}

	/**
	 * CSV形式にフォーマットする
	 * 
	 * @param param
	 * @return String
	 */
	public String format(Object[] param) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < param.length; i++) {
			if (i != 0) {
				result.append(SystemConstant.COMMA);
			}
			result.append(enquote(String.valueOf(param[i])));
		}
		return result.toString();
	}
	
	/**
	 * 引数情報をCSV形式("では括らない)にフォーマットする
	 * 
	 * @param param
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String format(ArrayList param) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < param.size(); i++) {
			if (i != 0) {
				result.append(SystemConstant.COMMA);
			}
			result.append(param.get(i));
		}
		return result.toString();
	}

}
