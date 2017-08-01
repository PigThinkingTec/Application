package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.taglib.TagFormat;
import com.pigthinkingtec.framework.taglib.TaglibFwFormatManager;
import com.pigthinkingtec.framework.util.UserUtil;

/**
 * JSTL:Out タグを拡張したクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class OutTagImpl extends OutSupport {

	private static final Logger logger = LoggerFactory.getLogger(OutTagImpl.class);

	private String fwformat;
	private String value_;			// stores EL-based property
	private String default_;			// stores EL-based property
	private String escapeXml_;			// stores EL-based property
	private Object value;
	private Boolean escapeXml;
	private String def;

	public OutTagImpl() {
		super();
		init();
	}

	public void release() {
		super.release();
		init();
	}
	
	public void setFwformat(String fwformat) {
		this.fwformat = fwformat;
	}
	
	public void setValue(String value_) {
		this.value_ = value_;
	}

	public void setDefault(String default_) {
		this.default_ = default_;
	}

	public void setEscapeXml(String escapeXml_) {
		this.escapeXml_ = escapeXml_;
	}

	@Override
	public int doStartTag() throws JspException {
		evaluateExpressions();
		return super.doStartTag();
	}
	
	private void init() {
	value_ = default_ = escapeXml_ = null;
		fwformat = "NONE";
	}
   
	private String formatValue(String fwformat, String value) throws JspException {

		//ユーザ情報の取得
		UserContainer user = UserUtil.createUserContainer(pageContext);
		
		TagFormat tf = TaglibFwFormatManager.get(fwformat);
		if (tf == null) {
			throw new JspException("Invalid fwformat = " + fwformat);
		} else {
			try {
				return TaglibFwFormatManager.formatValue(tf, value, user);
			} catch (SystemException ex) {
				logger.error("system error: {}", ex);
				throw new JspException(ex);
			}
		}
	}

	private void evaluateExpressions() throws JspException {
		try {
			value = ExpressionUtil.evalNotNull(
					"out", "value", value_, Object.class, this, pageContext);
			
			if (value instanceof String) {
				value = formatValue(fwformat, (String)value);
			} else {
				value = formatValue(fwformat, value.toString());
			}
			
		} catch (NullAttributeException ex) {
			// explicitly allow 'null' for value
			value = null;
		}
		try {
			def = (String) ExpressionUtil.evalNotNull(
					"out", "default", default_, String.class, this, pageContext);
		} catch (NullAttributeException ex) {
			// explicitly allow 'null' for def
			def = null;
		}
		escapeXml = true;
		Boolean escape = ((Boolean) ExpressionUtil.evalNotNull(
				"out", "escapeXml", escapeXml_, Boolean.class, this, pageContext));
		if (escape != null) {
			escapeXml = escape.booleanValue();
		}
	}

	@Override
	protected String evalDefault() throws JspException {
		return def;
	}

	@Override
	protected boolean evalEscapeXml() throws JspException {
		return escapeXml.booleanValue();
	}

	@Override
	protected Object evalValue() throws JspException {
		return value;
	}
}
