package com.pigthinkingtec.framework.spring.mvc.tags.form;

import java.io.IOException;

import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;
import com.pigthinkingtec.framework.util.message.MessageUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DialogMessage表示用のカスタムタグ
 *
 * @author yizhou
 */
public class DialogButtonTagImpl extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5624119387411279048L;

	private static final Logger logger = LoggerFactory.getLogger(DialogButtonTagImpl.class);

	/** ダイアログのタイトルID **/
	private String titleId = null;
	
	/** メッセージの可変項目 **/
	private String args0 = null;
	private String args1 = null;
	private String args2 = null;
	private String args3 = null;
	private String args4 = null;
	private String args5 = null;
	private String args6 = null;

	/** ダイアログのメッセージID **/
	private String messageId = null;
	/** ダイアログタイプ  1:error 2:warning 3:message  **/
	private String type = null;
	/** ダイアログパターン 1:ボタン1つ(OK) 2:ボタン2つ(はい(Y)、いいえ(N)) 3:ボタン3つ(はい(Y)、いいえ(N)、キャンセル)  **/	
	private String pattern = null;
	/** form1オブジェクト名 **/
	private String form1 = null;
	/** form2オブジェクト名 **/
	private String form2 = null;
	/** 遷移先action1(ボタンごとに設定可) **/
	private String action1 = null;
	/** 遷移先action2(ボタンごとに設定可) **/
	private String action2 = null;
	/** CSS **/
	private String cssClass = null;
	/** id **/
	private String id = null;
	/** ボタン文字列ID **/
	private String value = null;
	/** ダイアログの横幅 **/
	private String width = null;
	/** ダイアログの高さ　**/
	private String height = null;
	
	// OKボタンのテキストID
	public static String OK_BUTTON_TEXT_ID;
	// YESボタンのテキストID	
	public static String YES_BUTTON_TEXT_ID;
	// NOボタンのテキストID
	public static String NO_BUTTON_TEXT_ID;
	// キャンセルボタンのテキストID
	public static String CANCEL_BUTTON_TEXT_ID;
	
	static {
		OK_BUTTON_TEXT_ID = PropertiesUtil.getProperty("dialogbutton.textid.ok");
		YES_BUTTON_TEXT_ID = PropertiesUtil.getProperty("dialogbutton.textid.yes");
		NO_BUTTON_TEXT_ID = PropertiesUtil.getProperty("dialogbutton.textid.no");
		CANCEL_BUTTON_TEXT_ID = PropertiesUtil.getProperty("dialogbutton.textid.cancel");
	}

	// ダイアログボックスのデフォルトの横幅
	public static final String DEFAULT_WIDTH = "350";
	// ダイアログボックスのデフォルトの高さ
	public static final String DEFAULT_HEIGHT = "250";
	
	// ユーザセッション情報
	private UserContainer user = null;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getTitleId() {
		return titleId;
	}
	
	public String getArgs0() {
		return args0;
	}

	public String getArgs1() {
		return args1;
	}

	public String getArgs2() {
		return args2;
	}

	public String getArgs3() {
		return args3;
	}

	public String getArgs4() {
		return args4;
	}

	public String getArgs5() {
		return args5;
	}

	public String getArgs6() {
		return args6;
	}
	public void setTitle(String titleId) {
		this.titleId = titleId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public String getForm1() {
		return form1;
	}

	public void setForm1(String form1) {
		this.form1 = form1;
	}
	
	public String getForm2() {
		return form2;
	}

	public void setForm2(String form2) {
		this.form2 = form2;
	}
	
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	
	public void setArgs0(String args0) {
		this.args0 = args0;
	}
	
	public void setArgs1(String args1) {
		this.args1 = args1;
	}
	
	public void setArgs2(String args2) {
		this.args2 = args2;
	}
	
	public void setArgs3(String args3) {
		this.args3 = args3;
	}
	
	public void setArgs4(String args4) {
		this.args4 = args4;
	}
	
	public void setArgs5(String args5) {
		this.args5 = args5;
	}
	
	public void setArgs6(String args6) {
		this.args6 = args6;
	}
	
	public String getAction1() {
		return action1;
	}
	
	public void setAction1(String action1) {
		this.action1 = action1;
	}
	
	public String getAction2() {
		return action2;
	}
	
	public void setAction2(String action2) {
		this.action2 = action2;
	} 
	
	public String getWidth() {
		return width;
	}
	
	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getHeight() {
		return height;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	@Override
	public int doStartTag() throws JspException {

		user = UserUtil.createUserContainer(pageContext);

		StringBuilder sb = new StringBuilder();

		sb.append(getScript());
		sb.append("<button ");

		// CSSの指定があるかを確認する
		if(getCssClass() != null) {
			sb.append("class=\"");
			sb.append(getCssClass());
			sb.append("\" ");
		} else {
			sb.append("class=\"button\" ");
		}
		
		// idの指定があれば追加
		if(getId() != null) {
			sb.append("id=\"");
			sb.append(getId());
			sb.append("\" ");
		} 
		
		// onClickイベントの描画
		sb.append("onclick=\"showDialog();\" name=\"dialigButton\" type=\"button\" value=\"Submit\">");
		// ボタン文字列の描画
		sb.append(getLabelText(value));
		sb.append("</button>");
		sb.append("<div id=\"show_dialog\"></div>");
			
		try {	
			JspWriter writer = pageContext.getOut();
			writer.println(sb.toString());
		} catch(IOException e) {
			logger.error(e.getMessage());
			throw new JspException(e.getMessage());
		}
		
		return SKIP_BODY;
	}

	private String getLabelText(String id) throws JspException {
	
		//JavaScript　Escape, 改行コー<br>はEscapeしないこと
		String res = null;
		try {
			res = LabelUtil.getLabel(user, id, user.getUserLang());
		} catch (SystemException e) {
			logger.error(e.toString());
			throw new JspException(e);
		}
		return StringUtil.escapeJavaScript(res, true);
	}
	
	private String getMessageText(String id) throws JspException {

		//JavaScript　Escape, 改行コー<br>はEscapeしないこと
		String res = null;
		
		String[] values = new String[7]; 
		values[0] = this.args0;
		values[1] = this.args1;
		values[2] = this.args2;
		values[3] = this.args3;
		values[4] = this.args4;
		values[5] = this.args5;
		values[6] = this.args6;
		
		try {
			res = StringUtil.escapeJavaScript(MessageUtil.getMessage(user, id, user.getUserLang(),(Object[]) values), true).replaceAll("\\\\r\\\\n", "<br>");;
		} catch (SystemException e) {
			logger.error(e.toString());
			throw new JspException(e);
		}
		if(res != null) {
			return res;
		} 
		return null;
	}
	
	private String getScript() throws JspException {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" <script type=\"text/javascript\"> ");
		sb.append("function showDialog() { ");
		String message = "";
		if("1".equals(getType())) {
			message += "<span class='ui-icon ui-icon-alert'></span>" + getMessageText(getMessageId());
		} else if("2".equals(getType())) {
			message += "<span class='ui-icon ui-icon-notice'></span>" + getMessageText(getMessageId());
		} else if("3".equals(getType())) {
			message += "<span class='ui-icon ui-icon-info'></span>" + getMessageText(getMessageId());
		}

		// ダイアログボックスの横幅が設定されていなければデフォルト値をセットする。
		if(getWidth() == null || "".equals(getWidth())) {
			setWidth(DEFAULT_WIDTH);	
		}
		// ダイアログボックスの高さが設定されていなければデフォルト値をセットする。
		if(getHeight() == null || "".equals(getHeight())) {
			setHeight(DEFAULT_HEIGHT);
		}		
		
		sb.append(" $(\"#show_dialog\").html(\"" + message + "\"); ");
		sb.append("$(\"#show_dialog\").dialog({");
		sb.append(" width : " + width + ",");
		sb.append(" height : " + height + ",");
		sb.append(" modal : true,");
		sb.append(" title : '" + getLabelText(getTitleId()) + "',");
		sb.append(" buttons : { '");
		if("1".equals(getPattern())) {
			sb.append(getLabelText(OK_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\"); } ");
			sb.append(" }});");
		} else if ("2".equals(getPattern())) {
			sb.append(getLabelText(YES_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\"); ");
			sb.append(" $(this).customSubmit('" + form1 + "','" + action1 + "',null);}, '");
			sb.append(getLabelText(NO_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\"); } ");
			sb.append(" }});");
		} else if("3".equals(getPattern())) {
			sb.append(getLabelText(YES_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\"); ");;
			sb.append(" $(this).customSubmit('" + form1 + "','" + action1 + "',null);}, '");
			sb.append(getLabelText(NO_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\");  ");
			sb.append(" $(this).customSubmit('" + form2 + "','" + action2 + "',null);}, '");
			sb.append(getLabelText(CANCEL_BUTTON_TEXT_ID));
			sb.append("' : function() { $(this).dialog(\"close\"); } ");
			sb.append(" }});");
		}
		sb.append(" } ");
		sb.append(" </script>");
		
		return sb.toString();
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {
	}
	
}
