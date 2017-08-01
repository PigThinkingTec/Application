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
import com.pigthinkingtec.framework.util.label.LabelUtil;

/**
 * JSTL:Out タグを拡張したクラス
 *
 * @author yizhou
 */
public class LabelTextTag extends OutSupport {

    /**
	 * 
	 */
	private static final long serialVersionUID = -781915953811133839L;

	private static final Logger logger = LoggerFactory.getLogger(LabelTextTag.class);

    private String labelId_;			// stores EL-based property
    private String default_;			// stores EL-based property
    private String escapeXml_;			// stores EL-based property
    private Object labelId;
    private String label;
    private Boolean escapeXml;
    private Boolean escapeHtml = Boolean.TRUE;  // true: HTMLEscape false:JavascriptEscape default:true
    private String def;

    public LabelTextTag() {
        super();
        init();
    }

    public void release() {
        super.release();
        init();
    }
    
    public void setLabelId(String labelId_) {
        this.labelId_ = labelId_;
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
	labelId_ = default_ = escapeXml_ = null;
    }

    private void evaluateExpressions() throws JspException {
        try {
            labelId = ExpressionUtil.evalNotNull(
                    "labelText", "labelId", labelId_, Object.class, this, pageContext);
            
            if (labelId instanceof String) {
            	labelId = (String)labelId;
            } else {
                labelId = labelId.toString();
            }
            
        } catch (NullAttributeException ex) {
            // explicitly allow 'null' for value
            labelId = null;
        }
        
        label = getTextFromDBorFile((String)labelId);
        
        try {
            def = (String) ExpressionUtil.evalNotNull(
                    "labelText", "default", default_, String.class, this, pageContext);
        } catch (NullAttributeException ex) {
            // explicitly allow 'null' for def
            def = null;
        }
        escapeXml = true;
        Boolean escape = ((Boolean) ExpressionUtil.evalNotNull(
                "labelText", "escapeXml", escapeXml_, Boolean.class, this, pageContext));
        if (escape != null) {
            escapeXml = escape.booleanValue();
        }
    }

	/**
	 * Determine the '{@code value}' attribute value for this tag,
	 * @see #getLocal()
	 * @see #autogenerateLocal()
	 */
	protected String getTextFromDBorFile(String value) throws JspException {

		//sessionからユーザ情報を取得する
		UserContainer user = UserUtil.createUserContainer(pageContext);
		 
		if (StringUtils.hasText(value)) {
			try {					
					user.setPgmId("LabelTextTag");
					
					//ユーザと繋がる言語情報を取得する
					String lang = user.getUserLang();

					if (lang == null ) {
						lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
					}
					//万が一Propertiesの設定がもれてしまった場合
					if (lang == null ) throw new JspException("The lang info is not set .");
					
					//IDと言語情報によって、DBから該当言語の文字列を取得する
					return LabelUtil.getLabel(user, value, lang);
				
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
			return StringUtil.escapeHtml(label, true);
		} else {
			return StringUtil.escapeJavaScript(label, true);
		}
	}
}
