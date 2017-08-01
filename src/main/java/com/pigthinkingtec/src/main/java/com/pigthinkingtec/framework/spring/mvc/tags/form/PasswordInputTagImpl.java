package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.PasswordInputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 * SpringのPasswordInputTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class PasswordInputTagImpl extends PasswordInputTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PasswordInputTagImpl.class);
	
	protected boolean required;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		return super.writeTagContent(tagWriter);
	}
	
	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * writeTagContent の前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
		if(!required){
			setCssClass("text");
			setCssErrorClass("text_error");
		} else {
			setCssClass("require");
			setCssErrorClass("require_error");
		}
	}
		
	@Override
	public void doFinally() {
		// tomcatで実行する場合は初期化処理を実施。
		if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
			super.doFinally();
			this.required = false;
			setCssClass(null);
			setCssErrorClass(null);
		}
	}
}