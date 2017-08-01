package com.pigthinkingtec.framework.spring.mvc.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.UserUtil;


public class LoadFormatTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1579851288683049489L;

	private Logger logger = LoggerFactory.getLogger(LoadFormatTag.class);

	private Tag parentTag = null;
	
	public Tag getParentTag() {
		return parentTag;
	}

	public void setParentTag(Tag parentTag) {
		this.parentTag = parentTag;
	}
	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public void doInitBody() throws JspException {}
	
	public int doAfterBody() throws JspException {
		return SKIP_BODY;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag tag) {
		this.parentTag = tag;
	}

	public Tag getParent() {
		return parentTag;
	}

	public int doStartTag() throws JspException {

		UserContainer user = UserUtil.createUserContainer(pageContext);
		//言語情報の取得
		String lang = user.getUserLang();
		if (lang == null) {
			lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
		}
		//フォーマット情報の取得
		String dateFormat = user.getDateFormat();
		if (dateFormat == null) {
			dateFormat = SystemConstant.DATEFORMAT_YYYYMMDD;
		}
		
		//JavaScript用言語情報の判断
		String jsLang = null;
		if (SystemConstant.LANG_JP.equals(lang)) {
			jsLang = SystemConstant.JS_LANGKEY_JP;
		} else if (SystemConstant.LANG_CN.equals(lang)) {
			jsLang = SystemConstant.JS_LANGKEY_CN;
		} else {
			jsLang = SystemConstant.JS_LANGKEY_EN;
		}
		
		//日付フォーマット判断
		String jsDateFormat = "yy/mm/dd";
		String jsYearTwoDigitsFormat = "y/mm/dd";
		if (SystemConstant.DATEFORMAT_YYYYMMDD.equals(dateFormat)){
			jsDateFormat = "yy/mm/dd";
			jsYearTwoDigitsFormat = "y/mm/dd";
		} else if (SystemConstant.DATEFORMAT_DDMMYYYY.equals(dateFormat)) {
			jsDateFormat = "dd/mm/yy";
			jsYearTwoDigitsFormat = "dd/mm/y";	
		} else if (SystemConstant.DATEFORMAT_MMDDYYYY.equals(dateFormat)) {
			jsDateFormat = "mm/dd/yy";
			jsYearTwoDigitsFormat = "mm/dd/y";	
		}
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<script type='text/javascript'>");
		buffer.append("var jsLang = '" + jsLang + "';");
		buffer.append("var jsDateFormat = '" + jsDateFormat + "';");
		buffer.append("var jsYearTwoDigitsFormat = '" + jsYearTwoDigitsFormat + "';");
		buffer.append("</script>");
		
		// 書き出し
		try {	
			JspWriter writer = pageContext.getOut();
			writer.println(buffer.toString());
		} catch(IOException e) {
			logger.error(e.getMessage());
			throw new JspException(e.getMessage());
		}
		
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {}

}