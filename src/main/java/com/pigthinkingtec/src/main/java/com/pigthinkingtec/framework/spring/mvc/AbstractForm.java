package com.pigthinkingtec.framework.spring.mvc;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * SpringWebMVCのFormにおいて基底とするクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
@Getter @Setter
public abstract class AbstractForm implements Serializable {
    
    private Integer selectedIndex;
	
	private String maskPattern;
	
	private boolean maskFlg = false;
	
	private String screenPattern;
	
	private String screenName;
    
    /**
     * Formの項目をリセットするメソッド
     * 
     */
    public void reset() {
        
    }
    
}
