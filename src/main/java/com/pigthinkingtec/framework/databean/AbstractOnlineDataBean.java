package com.pigthinkingtec.framework.databean;

import lombok.Getter;
import lombok.Setter;


/**
 * Online用DataBean基底クラス
 * 
 * @author yizhou
 * 
 */
@SuppressWarnings("serial")
@Getter
@Setter
public abstract class AbstractOnlineDataBean extends AbstractDataBean {
    private Integer selectedIndex;
	private String maskPattern;
	private int resultCode;
	private String resultCodeKey;
}
