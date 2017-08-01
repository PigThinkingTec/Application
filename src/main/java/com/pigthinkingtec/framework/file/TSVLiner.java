package com.pigthinkingtec.framework.file;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * 配列をTAB区切りの文字列に変更する 
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class TSVLiner extends Liner {

	/**
	 * コンストラクター
	 * 
	 */
	public TSVLiner() {
		super();
		setSeparater(SystemConstant.TAB);
	}

	/**
	 * TSV形式にフォーマットする
	 * 
	 * @param param
	 * @return String
	 */
	public String format(Object[] param) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < param.length; i++) {
			if (i != 0) {
				result.append(SystemConstant.TAB);
			}
			result.append(enquote((String) param[i]));
		}
		return result.toString();
	}

}
