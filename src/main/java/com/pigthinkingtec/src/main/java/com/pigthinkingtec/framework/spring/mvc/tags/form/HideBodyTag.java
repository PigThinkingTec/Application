package com.pigthinkingtec.framework.spring.mvc.tags.form;

import java.io.IOException;
import java.io.BufferedReader;
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
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

public class HideBodyTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1539422583541796818L;

	private Logger logger = LoggerFactory.getLogger(HideBodyTag.class);

	private Tag parentTag = null;

	private boolean defaultshowflg = true;
	
	public Tag getParentTag() {
		return parentTag;
	}

	public void setParentTag(Tag parentTag) {
		this.parentTag = parentTag;
	}

	public boolean getDefaultshowflg() {
		return defaultshowflg;
	}

	public void setDefaultshowflg(boolean defaultshowflg) {
		this.defaultshowflg = defaultshowflg;
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public void doInitBody() throws JspException {}
	
	//「表示」「非表示」のラベルID
	public static final String HIDE_LABELID = "04100010";
	public static final String SHOW_LABELID = "04100009";
	
	public int doAfterBody() throws JspException {
		
		UserContainer user = UserUtil.createUserContainer(pageContext);
		//ラベル情報の取得
		String hideString;
		String showString;
		
		try {					
			user.setPgmId("HideBodyTag");
			
			//ユーザと繋がる言語情報を取得する
			String lang = user.getUserLang();

			if (lang == null ) {
				lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
				//万が一Propertiesの設定がもれてしまった場合
				if (lang == null ) {
					logger.error("The lang info is not set .");
					throw new JspException("The lang info is not set .");
				}
			}

			hideString =  LabelUtil.getLabel(user, HIDE_LABELID, lang);
			showString =  LabelUtil.getLabel(user, SHOW_LABELID, lang);
			
			//JavaScript　Escape, 改行コー<br>はEscapeしないこと
			hideString = StringUtil.escapeJavaScript(hideString, true);
			showString = StringUtil.escapeJavaScript(showString, true);
			
				
		} catch (SystemException e) {
			logger.error(e.getMessage());
			throw new JspException(e);
		} 
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<script type='text/javascript'>");
		buffer.append("var showstring = '" + showString + "';");
		buffer.append("var hidestring = '" + hideString + "';");
		if(getDefaultshowflg()) {
			buffer.append("var defaultshowflg = true;");
		} else {
			buffer.append("var defaultshowflg = false;");
		}
		buffer.append("</script>");

		
		buffer.append("<span id=\"checklabel\" class=\"hidebody\">hide</span>");
		buffer.append("<div id=\"hidden_box\">");
		try {
			BufferedReader reader = new BufferedReader(bodyContent.getReader());
			JspWriter writer = bodyContent.getEnclosingWriter();
			String str = null;

			while ((str = reader.readLine()) != null) {
				buffer.append(str);
			}
			buffer.append("</div>");
			writer.println(buffer.toString());
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new JspException(e.getMessage());
		}
		
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
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {}

}