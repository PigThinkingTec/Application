package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.OptionsTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのOptionsTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class OptionsTagImpl extends OptionsTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OptionsTagImpl.class);

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		return super.writeTagContent(tagWriter);
	}
	
	/**
	 * writeTagContentの前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
		
	}
	
}
