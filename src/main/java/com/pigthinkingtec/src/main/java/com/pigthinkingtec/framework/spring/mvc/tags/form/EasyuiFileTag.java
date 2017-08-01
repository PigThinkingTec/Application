package com.pigthinkingtec.framework.spring.mvc.tags.form;

import java.util.Map;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class EasyuiFileTag extends InputTag {

	private static final Logger logger = LoggerFactory.getLogger(EasyuiFileTag.class);
	
	private String maskPatterns = null;
	
	private boolean escapeMaskFlg = false;
	
	protected boolean required;
	
	/**
	 * The name of the '{@code buttonTextId}' attribute.
	 */
	private static final String BUTTON_TEXT_ID_ATTRIBUTE = "buttonTextId";
	
	private String buttonTextId = null;
	/**
	 * マスクパターンを取得する
	 * 
	 * @return 
	 */
	public String getMaskPatterns() {
		return maskPatterns;
	}
	
	/**
	 * Maskするパターンを設定する
	 * 複数してする場合は、カンマ区切りで指定する
	 * 
	 * @param maskPatterns 
	 */
	public void setMaskPatterns(String maskPatterns) {
		this.maskPatterns = maskPatterns;
	}
	
	/**
	 * エスケープマスクフラグを取得する
	 * @return
	 */
	public Boolean isEscapeMaskFlg() {
		return escapeMaskFlg;
	}
	
	/**
	 * エスケープマスクフラグを設定する
	 * @param escapeMaskFlg
	 */
	public void setEscapeMaskFlg (Boolean escapeMaskFlg) {
		this.escapeMaskFlg = escapeMaskFlg;
	}
	
	/**
	 * ボタンテキストIDを取得する
	 * @return
	 */
	public String isButtonTextId() {
		return buttonTextId;
	}
	
	/**
	 * ボタンテキストIDを設定する
	 * @param buttonTextId
	 */
	public void setButtonTextId (String buttonTextId) {
		this.buttonTextId = buttonTextId;
	}

	/**
	 * requiredを取得する
	 *
	 * @return Returns the required.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * requiredをセットする
	 *
	 * @param required The required to set.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		return super.writeTagContent(tagWriter);
	}
	
	/**
	 * writeTagContent の前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {

		if (required) {
			setCssClass("require easyui-filebox");
			setCssErrorClass("require_error easyui-filebox");
		} else {
			setCssClass("text easyui-filebox");
			setCssErrorClass("text_error easyui-filebox");
		}
		
		FormTagImpl formTag = TagUtil.getFormTag(this);
		String formClassName = formTag.getModelAttribute();
		
		//マスクパターンに応じたdisable設定
		if (maskPatterns != null) {
			String maskPattern = TagUtil.getMaskPattern(formClassName, pageContext);
			if (maskPattern != null) {
				String[] maskPatternArray = maskPatterns.split(",");
				for (int i = 0; i < maskPatternArray.length; i++) {
					if (maskPattern.equals(maskPatternArray[i])) {
						setDisabled(true);
						break;
					}
				}
			}
		}
		
		//マスクフラグに応じたdisable設定
		Boolean maskFlg =  TagUtil.getMaskFlg(formClassName, pageContext);
		if (maskFlg == true && escapeMaskFlg == false) {
			setDisabled(true);
		}
		
		//ボタンテキストの多言語対応
        Map<String, Object> dynamicAttribute = getDynamicAttributes();       
        if(dynamicAttribute != null){
	        if(buttonTextId != null){
	        	String buttonText = getButtonTextFromDB();
		        if(dynamicAttribute.containsKey("data-options")){
		        	String dataOptions = (String) dynamicAttribute.get("data-options");	        	
		        	dynamicAttribute.put("data-options",StringUtil.build(dataOptions, "," ,"buttonText:'" + buttonText +"'"));
		        }else{
		        	dynamicAttribute.put("data-options","buttonText:'" + buttonText +"'");
		        }
	        }
        }else{
        	String buttonText = getButtonTextFromDB();
        	setDynamicAttribute("", "data-options", "buttonText:'" + buttonText +"'");
        }   		
	}
	
	/**
	 * Determine the '{@code buttonTextId}' attribute value for this tag,
	 * @see #getLocal()
	 * @see #autogenerateLocal()
	 */
	protected String getButtonTextFromDB() throws JspException {
		String buttonText;
		UserContainer user = UserUtil.createUserContainer(pageContext);
		 
		if (StringUtils.hasText(this.buttonTextId)) {
			try {					
				    user.setPgmId("EasyuiFileTagImpl");
					
					//ユーザと繋がる言語情報を取得する
					String lang = user.getUserLang();

					if (lang == null ) {
						lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
					}
					//万が一Propertiesの設定がもれてしまった場合
					if (lang == null ) throw new JspException("The lang info is not set .");
					
					buttonText =  LabelUtil.getLabel(user,
													 getDisplayString(evaluate(BUTTON_TEXT_ID_ATTRIBUTE, this.buttonTextId)), 
													 lang);
					
				return buttonText;
				
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
	public void doFinally() {
		// tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
			super.doFinally();
			this.required = false;
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			setDisabled(false);
			setCssClass(null);
			setCssErrorClass(null);
		}
	}
}
