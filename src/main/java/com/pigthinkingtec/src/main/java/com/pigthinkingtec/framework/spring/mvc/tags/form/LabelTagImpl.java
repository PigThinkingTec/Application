package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.LabelTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

/**
 * SpringのLabelTagを継承したクラス
 * 
 * @author AB-ZHOU
 */
@SuppressWarnings("serial")
public class LabelTagImpl extends LabelTag {
	
	private static final Logger logger = LoggerFactory.getLogger(OutTagImpl.class);
	
	/**
	 * The HTML '{@code label}' tag.
	 */
	private static final String LABEL_TAG = "label";
	
	/**
	 * The name of the '{@code for}' attribute.
	 */
	private static final String FOR_ATTRIBUTE = "for";
	
	/**
	 * The {@link TagWriter} instance being used.
	 * <p>Stored so we can close the tag on {@link #doEndTag()}.
	 */
	private TagWriter tagWriter;
	 
	/**
	 * The name of the '{@code labelId}' attribute.
	 */
	private static final String LABEL_ID_ATTRIBUTE = "labelId";
	
	/**
	 * The name of the '{@code requiredMarkId}' attribute.
	 */
	private static final String REQUIRED_MARK_ID_ATTRIBUTE = "requiredMarkId";
	
	/**
	 * The name of the '{@code local}' attribute.
	 */
	private static final String REQUIRED_FLG_TEXT = " *";
	
	/**
	 * The value of the '{@code labelId}' attribute.
	 */
	private String labelId;
	
	/**
	 * The value of the '{@code showRequiredMark}' attribute.
	 */
	private boolean showRequiredMark;
	
	/**
	 * The value of the '{@code requiredMarkId}' attribute.
	 */
	private String requiredMarkId;
	
	/**
	 * The value of the LabelText getting from DB.
	 */
	private String labelText;
	
	
	/**
	 * Set the value of the '{@code labelId}' attribute.
	 */
	public void setLabelId(String labelId) {
		Assert.notNull(labelId, "'labelId' must not be null");
		this.labelId = labelId;
	}

	/**
	 * Get the value of the '{@code labelId}' attribute.
	 * <p>May be a runtime expression.
	 */
	public String getLabelId() {
		return this.labelId;
	}
	
	/**
	 * Set the value of the '{@code showRequiredMark}' attribute.
	 */
	public void setShowRequiredMark(boolean showRequiredMark) {
		this.showRequiredMark = showRequiredMark;
	}

	/**
	 * Get the value of the '{@code showRequiredMark}' attribute.
	 * <p>May be a runtime expression.
	 */
	public boolean getShowRequiredMark() {
		return this.showRequiredMark;
	}
	
	/**
	 * Set the value of the '{@code requiredMarkId}' attribute.
	 */
	public void setRequiredMarkId(String requiredMarkId) {
		this.requiredMarkId = requiredMarkId;
	}
	
	/**
	 * Get the value of the '{@code requiredMarkId}' attribute.
	 * <p>May be a runtime expression.
	 */
	public String getRequiredMarkId() {
		return requiredMarkId;
	}

	
	/**
	 * Writes the opening '{@code label}' tag and forces a block tag so
	 * that body content is written correctly.
	 * @return {@link javax.servlet.jsp.tagext.Tag#EVAL_BODY_INCLUDE}
	 */
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {

		tagWriter.startTag(LABEL_TAG);
		
		tagWriter.writeAttribute(FOR_ATTRIBUTE, resolveFor());
		writeDefaultAttributes(tagWriter);
		
//		tagWriter.writeOptionalAttributeValue(LABEL_ID_ATTRIBUTE, getLabexText());
		tagWriter.appendValue(getLabelText());
		
		tagWriter.forceBlock();
        
		this.tagWriter = tagWriter;
		
		return EVAL_BODY_INCLUDE;
//		return SKIP_BODY;
	}
	
	/**
	 * Determine the '{@code labelId}' attribute value for this tag
	 * @see #getLocal()
	 * @see #autogenerateLocal()
	 */
	protected String getLabelText() throws JspException {

		 UserContainer user = UserUtil.createUserContainer(pageContext);
		 
		if (StringUtils.hasText(this.labelId)) {						
			//ユーザと繋がる言語情報を取得する
			String lang = user.getUserLang();
			user.setPgmId("LabelTagImpl");
			
			if (lang == null ) {
				lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
			}
			//万が一Propertiesの設定がもれてしまった場合
			if (lang == null ) throw new JspException("The lang info is not set .");
			
			
			labelText = null;
			
			try {
				labelText = LabelUtil.getLabel(
						                user,
						                getDisplayString(evaluate(LABEL_ID_ATTRIBUTE, this.labelId)), 
						                lang);
				
				if (showRequiredMark) {
					labelText = labelText + "<font color = \"red\">" +
											REQUIRED_FLG_TEXT +  
											LabelUtil.getLabel(user, 
													           getDisplayString(evaluate(REQUIRED_MARK_ID_ATTRIBUTE, this.requiredMarkId)),
													           lang) +  
											" </font>";
				}
				
			} catch (SystemException e) {
				logger.error(e.toString());
				throw new JspException(e);
			}					
			
		return labelText;
			
		}
		else {
			return "";
		}
	}
	
	
	/**
	 * Close the '{@code label}' tag.
	 */
	@Override
	public int doEndTag() throws JspException {
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}

	/**
	 * Disposes of the {@link TagWriter} instance.
	 */
	@Override
	public void doFinally() {
		super.doFinally();
		this.tagWriter = null;
	}

}
