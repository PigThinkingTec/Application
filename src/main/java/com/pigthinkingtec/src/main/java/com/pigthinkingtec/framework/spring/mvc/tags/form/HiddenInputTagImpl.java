/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.HiddenInputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのHiddenInputTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class HiddenInputTagImpl extends HiddenInputTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HiddenInputTagImpl.class);
	
	private String maskPatterns = null;
	
	private Boolean escapeMaskFlg = false;
	
	public String getMaskPatterns() {
		return maskPatterns;
	}
	
	public void setMaskPatterns(String maskPatterns) {
		this.maskPatterns = maskPatterns;
	}

	public Boolean isEscapeMaskFlg() {
		return escapeMaskFlg;
	}
	
	public void setEscapeMaskFlg(Boolean escapeMaskFlg) {
		this.escapeMaskFlg = escapeMaskFlg;
	}
	
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
		FormTagImpl formTag = TagUtil.getFormTag(this);
		String formClassName = formTag.getModelAttribute();
		//マスクパターンに応じたdisable設定
		if (maskPatterns != null) {
			String maskPattern = TagUtil.getMaskPattern(formClassName, pageContext);
			if (maskPattern != null) {
				String[] maskPatternArray = maskPatterns.split(",");
				for (int i = 0; i < maskPatternArray.length; i++) {
					if (maskPattern.equals(maskPatternArray[i])) {
						return EVAL_BODY_INCLUDE;
					}
				}
			}
		}
		
		//マスクフラグに応じたdisable設定
		Boolean maskFlg =  TagUtil.getMaskFlg(formClassName, pageContext);
		if (maskFlg == true && escapeMaskFlg == false) {
			return EVAL_BODY_INCLUDE;
		}
    	
        return super.writeTagContent(tagWriter); //To change body of generated methods, choose Tools | Templates.
    }
    
}
