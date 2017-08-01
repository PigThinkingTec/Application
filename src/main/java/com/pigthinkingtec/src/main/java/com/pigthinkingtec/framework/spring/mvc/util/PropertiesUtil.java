package com.pigthinkingtec.framework.spring.mvc.util;

import java.util.Properties;

import com.pigthinkingtec.framework.util.StringUtil;

/**
 * Propertieyファイルの内容を保持するクラス
 *
 * @author yizhou
 */
public class PropertiesUtil {
	
	protected static Properties frameworkProperties;
	
	public static String getProperty(String key) {
		
		if (frameworkProperties == null) {
			return "";
		}
		
		String ret = frameworkProperties.getProperty(key);
		
		if (StringUtil.isEmpty(ret) || StringUtil.isBlank(ret)) {
			return "";
		} else {
			return ret.trim();
		}
	}
	
}
