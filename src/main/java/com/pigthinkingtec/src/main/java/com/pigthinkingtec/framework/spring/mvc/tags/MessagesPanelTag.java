package com.pigthinkingtec.framework.spring.mvc.tags;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.BusinessErrors;
import com.pigthinkingtec.framework.databean.message.BusinessInformations;
import com.pigthinkingtec.framework.databean.message.BusinessMessage;
import com.pigthinkingtec.framework.databean.message.BusinessMessages;
import com.pigthinkingtec.framework.databean.message.BusinessWarnings;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.message.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * メッセージを画面上に表示させるタグクラス
 * 
 * @author yizhou
 */
public class MessagesPanelTag extends RequestContextAwareTag {

	private final static Logger logger = LoggerFactory.getLogger(MessagesPanelTag.class);

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * default panel class name.
	 */
	protected static final String DEFAULT_PANEL_CLASS_NAME = "alert";

	/**
	 * default panel type class prefix.
	 */
	protected static final String DEFAULT_PANEL_TYPE_CLASS_PREFIX = "alert-";

	/**
	 * default panel element.
	 */
	protected static final String DEFAULT_PANEL_ELEMENT = "div";

	/**
	 * default outer element.
	 */
	protected static final String DEFAULT_OUTER_ELEMENT = "ul";

	/**
	 * default inner element.
	 */
	protected static final String DEFAULT_INNER_ELEMENT = "li";

	/**
	 * program id for dao.
	 */
	protected static final String NO_EXIST_USER_SESSION_MESSAGE = "*Since the user session does not exist, you can not display a message.";

	/**
	 * messages attribute name.
	 */
	private String messagesAttributeName = "resultMessage";

	/**
	 * panel class name.
	 */
	private String panelClassName = DEFAULT_PANEL_CLASS_NAME;

	/**
	 * panel type class prefix.
	 */
	private String panelTypeClassPrefix = DEFAULT_PANEL_TYPE_CLASS_PREFIX;

	/**
	 * panel element.
	 */
	private String panelElement = DEFAULT_PANEL_ELEMENT;

	/**
	 * outer element.
	 */
	private String outerElement = DEFAULT_OUTER_ELEMENT;

	/**
	 * inner element.
	 */
	private String innerElement = DEFAULT_INNER_ELEMENT;

	/**
	 * messages type.
	 */
	private String messagesType = null;

	/**
	 * Flag to indicate whether html escaping is to be disabled or not
	 */
	private boolean disableHtmlEscape;

	/**
	 * Creates TagWriter
	 * 
	 * @return Created TagWriter
	 */
	TagWriter createTagWriter() {
		TagWriter tagWriter = new TagWriter(this.pageContext);
		return tagWriter;
	}

	/**
	 * メッセージグループを出力するメソッド
	 * 
	 * @throws JspException
	 *             In case when {@link JspException} is generated later in the
	 *             chain when tag configured by MessagesPanel could not be
	 *             created
	 * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
	 */
	@Override
	@SuppressWarnings("unchecked") 
	protected int doStartTagInternal() throws JspException {

		if (!StringUtils.hasText(this.panelElement)
				&& !StringUtils.hasText(this.outerElement)
				&& !StringUtils.hasText(this.innerElement)) {
			throw new JspTagException(
					"At least one out of panelElement, outerElement, innerElement should be set");
		}

		//Spring Form のTagWriterを生成する
		TagWriter tagWriter = createTagWriter();

		//Controllerでモデルに格納したメッセージグループを取得する
		//Object messages = this.pageContext.findAttribute(messagesAttributeName);
		HttpSession session = ((HttpServletRequest)this.pageContext.getRequest()).getSession(false);
		Object messages = null;
		Object messagesModel = this.pageContext.findAttribute(messagesAttributeName);
		if (session != null) {
			messages = session.getAttribute(messagesAttributeName);

			if (messages == null) {
				messages = messagesModel;
			} else if (messagesModel != null) {
				BusinessMessages bm1 = (BusinessMessages)messages;
				BusinessMessages bm2 = (BusinessMessages)messagesModel;
				bm1.getMessages().putAll(bm2.getMessages());
			}
		}

		if (messages != null) {

			if ("dialog".equals(messagesAttributeName)) {

				String url = null;			
				Iterable<?> col = (Iterable<?>) messages;
				for (Object message : col) {
					if (message != null && message instanceof BusinessMessage) {
						BusinessMessage bizMsg = (BusinessMessage)message;
						Object[] objs = bizMsg.getValues();
						if(objs != null && objs[objs.length-1] != null) {
							url = (String)objs[objs.length-1];
							String context = ((HttpServletRequest) (this.pageContext.getRequest())).getContextPath();
							url = context + "/" + url;
							tagWriter.startTag(panelElement);
							tagWriter.appendValue(getDialogScript(getText((BusinessMessage)message),url));
							tagWriter.endTag();
						}
					}
				}

			} else {

				if (StringUtils.hasText(panelElement)) {
					//Panelのスタートタグを出力
					tagWriter.startTag(panelElement); // <div>

					//スタイルを設定する一連の処理を開始する。
					StringBuilder className = new StringBuilder(panelClassName);
					//出力するメッセージグループが、error/warning/infomationのいずれかであるか判断
					String type = getType(messages);

					if (panelTypeClassPrefix != null && StringUtils.hasText(type)) {

						if (StringUtils.hasLength(className)) {
							className.append(" ");
						}
						className.append(panelTypeClassPrefix);
					}
					className.append(type);

					if (StringUtils.hasText(className)) {
						//スタイルの設定をclass属性に出力
						tagWriter.writeAttribute("class", className.toString());
					}
				}


				if (StringUtils.hasText(outerElement)) {
					tagWriter.startTag(outerElement); // <ul>
				}

				//メッセージの出力処理
				writeMessages(tagWriter, messages);

				if (StringUtils.hasText(outerElement)) {
					tagWriter.endTag(); // </ul>
				}


				if (StringUtils.hasText(panelElement)) {
					tagWriter.endTag(); // </div>
				}

			}
			session.removeAttribute(messagesAttributeName);
		}
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * メッセージグループを出力するメソッド
	 * 
	 * @param tagWriter
	 * @param messages
	 * @throws JspException
	 *             If {@link JspException} occurs in caller writeMessage
	 */
	protected void writeMessages(TagWriter tagWriter, Object messages)
			throws JspException {
		//messeagesの実クラスを取得する。
		Class<?> clazz = messages.getClass();
		//取得したクラスがIteratableの場合は、Iteratorを取得して、
		//ループしつつメッセージを出力する。
		if (Iterable.class.isAssignableFrom(clazz)) {
			Iterable<?> col = (Iterable<?>) messages;
			for (Object message : col) {
				writeMessage(tagWriter, message);
			}
		}
	}

	/**
	 * メッセージを出力するメソッド
	 * 
	 * @param tagWriter
	 * @param message
	 * @throws JspException
	 *             Occurs when {@link #JspTagException} occurs in case when
	 *             nothing is set in the configuration of the tag that
	 *             configures MessagePanel using tagWriter.
	 */
	protected void writeMessage(TagWriter tagWriter, Object message)
			throws JspException {
		//messageがBusinessMessageの場合、メッセージ出力処理を行う。
		if (message != null && message instanceof BusinessMessage) {
			BusinessMessage bizMsg = (BusinessMessage)message;
			if (StringUtils.hasText(innerElement)) {
				tagWriter.startTag(innerElement); // <li>
			}
			//メッセージのHTMLエスケープを行うかどうかの判定
			if(disableHtmlEscape) {
				tagWriter.appendValue(getText(bizMsg));
			} else {
				//tagWriter.appendValue(HtmlEscapeUtils.htmlEscape(getText(message)));
				tagWriter.appendValue(StringUtil.escapeHtml(getText(bizMsg), true));
			}

			if (StringUtils.hasText(innerElement)) {
				tagWriter.endTag(); // </li>
			}
		}
	}

	/**
	 * System_Messageテーブルからメッセージを取得して、メッセージを整形するメソッド
	 * 
	 * @param resultMessage
	 *            ResultMessage
	 */
	private String getText(BusinessMessage message) throws JspException {
		// Locale locale = getRequestContext().getLocale();
		// MessageSource messageSource = getRequestContext().getMessageSource();
		// String text = messageSource.getMessage(message.getKey(), message.getValues(), locale);
		//String text = getMessage(message.getKey(), message.getValues(), user.getLanguage());
		Object userObject = ((HttpServletRequest)this.pageContext.getRequest()).getSession().getAttribute(SystemConstant.USER_CONTAINER);
		UserContainer user = null;
		String text = null;

		if(userObject != null) {
			user = (UserContainer)userObject;

			//PGIDの追記
			user.setPgmId("MessagesPanelTag");
			
			if(user.getUserId() == null) {
				text = NO_EXIST_USER_SESSION_MESSAGE;
			} else {
				text = getMessage(message.getKey(), message.getValues(),user);
			}
		}
		return text;
	}

	private String getMessage(String messageId, Object[] values, UserContainer user) throws JspException{

		String message = null;
		try {
			message = MessageUtil.getMessage(user, messageId, user.getUserLang(), values);
			if(message == null) {
				logger.warn("messageId:" + messageId +  " lang:" + user.getUserLang() + " に対応するメッセージがDBに存在しませんでした。");
				return null;
			}
		} catch (SystemException e) {
			throw new JspException(e);
		}
		return message;

	}

	/**
	 * dialogを出力するためのスクリプトを出力するメソッド
	 * 
	 * @param url
	 * @return String
	 */

	public String getDialogScript(String message,String url) {

		//JavaScript Escape処理, 改行コー<br>はEscapeしないこと
		message = StringUtil.escapeJavaScript(message, true);
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("<script type=\"text/javascript\">");
		if("".equals(url)) {
			sbf.append("function showDialog() {window.confirm(\"" + message + "\") } " );
		} else {
			sbf.append("function showDialog() {if (window.confirm(\"" + message + "\")) {" );
			sbf.append("location.href=\"" + url + "\" }}");
		}
		sbf.append(" document.body.onload=showDialog(); </script>");

		return sbf.toString();
	}

	/**
	 * メッセージをToStringするメソッド
	 * 
	 * @param message
	 * @return String
	 */
	protected String getTextInOtherCase(Object message) {
		return message.toString();
	}

	/**
	 * Messeageがerror/warning/informationのいずれかか判断するメソッド
	 * 
	 * @param messages
	 * @return String
	 */
	private String getType(Object messages) {
		String type = "";
		if (messagesType != null) {
			type = messagesType;
		} else if (messages instanceof BusinessErrors) {
			type = "error";
		} else if (messages instanceof BusinessInformations) {
			type = "info";
		} else if (messages instanceof BusinessWarnings) {
			type = "warn";
		} 
		return type;
	}

	/**
	 * messageAttributeNameをセットするメソッド<br/>
	 * デフォルトはresultMessage
	 * 
	 * @param messagesAttributeName
	 *            Attribute name that is used to store messages
	 */
	public void setMessagesAttributeName(String messagesAttributeName) {
		this.messagesAttributeName = messagesAttributeName;
	}

	/**
	 * メッセージ出力領域全体のスタイルを定義するメソッド
	 * 
	 * @param panelClassName
	 *            CSS class
	 */
	public void setPanelClassName(String panelClassName) {
		this.panelClassName = panelClassName;
	}

	/**
	 * Sets the prefix of the class name that configures the messagesPanel tag
	 * <p>
	 * [Caution Notes]<br>
	 * {@link #messagesType} gets attached at the end. Hence the structure of
	 * class must be <br>
	 * {@link #panelTypeClassPrefix} + {@link #messagesType}.
	 * </p>
	 * 
	 * @param panelTypeClassPrefix
	 *            Prefix of CSS class
	 */
	public void setPanelTypeClassPrefix(String panelTypeClassPrefix) {
		this.panelTypeClassPrefix = panelTypeClassPrefix;
	}

	/**
	 * Sets the tag that configures MessagesPanel
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "div"<br>
	 * Specify the tag which have class property.
	 * </p>
	 * 
	 * @param panelElement
	 *            html tag
	 */
	public void setPanelElement(String panelElement) {
		this.panelElement = panelElement;
	}

	/**
	 * Sets the outer tag which configures MessagesPanel tag
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "span"<br>
	 * </p>
	 * 
	 * @param outerElement
	 *            html tag
	 */
	public void setOuterElement(String outerElement) {
		this.outerElement = outerElement;
	}

	/**
	 * Sets the inner tag which configures MessagesPanel tag
	 * <p>
	 * Points to be careful:<br>
	 * Only the tag name should be set. "<>" used while using the tag in HTML
	 * should not be included. For ex: "span"<br>
	 * </p>
	 * 
	 * @param innerElement
	 *            html tag
	 */
	public void setInnerElement(String innerElement) {
		this.innerElement = innerElement;
	}

	/**
	 * Specify the type of the message which messagesPanel will display
	 * <p>
	 * Points to be careful:<br>
	 * Should be used only when specifying {@code String} and not
	 * {@code ResultMessages}<br>
	 * When {@code ResultMessages} is being used, {@link #messagesType} will be
	 * fetched from ResultMessages<br>
	 * This method assumes that if {@link #messagesType} is being specified,
	 * BootStrap CSS is used<br>
	 * </p>
	 * 
	 * @param messagesType
	 *            type of message
	 */
	public void setMessagesType(String messagesType) {
		this.messagesType = messagesType;
	}

	/**
	 * Sets the value for disableHtmlEscape property.
	 * <p>
	 * IF set to true, html escaping is disabled. <br>
	 * By default, disableHtmlEscape is set to <code>false</code>. This means <br>
	 * html escaping is not disabled and will be performed by default. 
	 * 
	 * @param disableHtmlEscape
	 *            value of disableHtmlEscape
	 */
	public void setDisableHtmlEscape(String disableHtmlEscape) {
		if ("true".equals(disableHtmlEscape)) {
			this.disableHtmlEscape = true;
		} else {
			this.disableHtmlEscape = false;
		}
	}

}
