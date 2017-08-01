package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.message.MessageUtil;

/**
 * JSTL:Out タグを拡張したクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class MessageTextTag extends OutSupport {

	private static final Logger logger = LoggerFactory.getLogger(MessageTextTag.class);

    private String messageId_;			// stores EL-based property
    private String default_;			// stores EL-based property
    private String escapeXml_;			// stores EL-based property
    private Object messageId;
    private String message;
    private Boolean escapeXml;
    private Boolean escapeHtml = Boolean.FALSE;  // true: HTMLEscape false:JavascriptEscape default:false
    private String def;
    
    private String[] args = {null,null,null,null,null,null,null};
    
    public void setArgs0(String arg0) {
    	args[0] = arg0;
    }
    
    public void setArgs1(String arg1) {
    	args[1] = arg1;
    }
    
    public void setArgs2(String arg2) {
    	args[2] = arg2;
    }
    
    public void setArgs3(String arg3) {
    	args[3] = arg3;
    }
    
    public void setArgs4(String arg4) {
    	args[4] = arg4;
    }
    
    public void setArgs5(String arg5) {
    	args[5] = arg5;
    }
    
    public void setArgs6(String arg6) {
    	args[6] = arg6;
    }

    public MessageTextTag() {
        super();
        init();
    }

    public void release() {
        super.release();
        init();
    }
    
    public void setmessageId(String messageId_) {
        this.messageId_ = messageId_;
    }

    public void setDefault(String default_) {
        this.default_ = default_;
    }

    public void setEscapeXml(String escapeXml_) {
        this.escapeXml_ = escapeXml_;
    }
    
    public void setEscapeHtml(String escapeHtml) {
        this.escapeHtml = Boolean.valueOf(escapeHtml);
    }

    @Override
    public int doStartTag() throws JspException {
        evaluateExpressions();
        return super.doStartTag();
    }
    
    private void init() {
	messageId_ = default_ = escapeXml_ = null;
    }

    private void evaluateExpressions() throws JspException {
        try {
            messageId = ExpressionUtil.evalNotNull(
                    "messageText", "messageId", messageId_, Object.class, this, pageContext);
            
            if (messageId instanceof String) {
            	messageId = (String)messageId;
            } else {
                messageId = messageId.toString();
            }
            
        } catch (NullAttributeException ex) {
            // explicitly allow 'null' for value
            messageId = null;
        }
        
        message = getMessageFromDBorFile((String)messageId);
        
        try {
            def = (String) ExpressionUtil.evalNotNull(
                    "messageText", "default", default_, String.class, this, pageContext);
        } catch (NullAttributeException ex) {
            // explicitly allow 'null' for def
            def = null;
        }
        escapeXml = true;
        Boolean escape = ((Boolean) ExpressionUtil.evalNotNull(
                "messageText", "escapeXml", escapeXml_, Boolean.class, this, pageContext));
        if (escape != null) {
            escapeXml = escape.booleanValue();
        }
    }

	/**
	 * Determine the '{@code value}' attribute value for this tag,
	 * @see #getLocal()
	 * @see #autogenerateLocal()
	 */
	protected String getMessageFromDBorFile(String value) throws JspException {

		//sessionからユーザ情報を取得する
		UserContainer user = UserUtil.createUserContainer(pageContext);
		 
		if (StringUtils.hasText(value)) {
			try {					
					user.setPgmId("MessageTextTag");
					
					//ユーザと繋がる言語情報を取得する
					String lang = user.getUserLang();

					if (lang == null ) {
						lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
					}
					//万が一Propertiesの設定がもれてしまった場合
					if (lang == null ) throw new JspException("The lang info is not set .");
					
					//IDと言語情報によって、DBから該当言語のメッセージを取得する
					String messageStr = MessageUtil.getMessage(user, value, lang, (Object[])args);
					return messageStr;
				
			} catch(SystemException e) {
				logger.error(e.toString());
				throw new JspException(e);
			} 
		}
		else {
			return "";
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
		if (escapeHtml) {
			return StringUtil.escapeHtml(message, true);
		} else {
			return StringUtil.escapeJavaScript(message, true);
		}
		
		
	}
}
