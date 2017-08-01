package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.OptionTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのOptionTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class OptionTagImpl extends OptionTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OptionTagImpl.class);

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		return super.writeTagContent(tagWriter);
	}
	
	/**
	 * writeTagContentの前処理メソッド
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
	}
}
