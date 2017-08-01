package com.pigthinkingtec.framework.spring.mvc.util;

import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

/**
 * PropertiesUtilクラスを初期化するinitializerクラス
 *
 * @author yizhou
 */
@Getter
@Setter
public class PropertiesUtilBean {
	
	private Properties frameworkProperties;
	
	public void init() {
		PropertiesUtil.frameworkProperties = this.frameworkProperties;
	}
	
}
