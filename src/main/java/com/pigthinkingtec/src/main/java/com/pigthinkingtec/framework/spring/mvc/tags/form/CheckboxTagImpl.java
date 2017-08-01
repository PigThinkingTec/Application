package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

/**
 * SpringのCheckboxTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class CheckboxTagImpl extends AbstractSingleCheckedElementTag {

	private static final Logger logger = LoggerFactory.getLogger(CheckboxTagImpl.class);

	private String maskPatterns = null;
	
	private boolean escapeMaskFlg = false;
	
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
	 * The value of the LabelText getting from DB.
	 */
	private String labelText;
		
	
	/**
	 * エスケープマスクフラグを設定する
	 * @param escapeMaskFlg
	 */
	public void setEscapeMaskFlg (Boolean escapeMaskFlg) {
		this.escapeMaskFlg = escapeMaskFlg;
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		UserContainer user = UserUtil.createUserContainer(pageContext);

		try {					
			    user.setPgmId("CheckboxTagImpl");
			
				//ユーザと繋がる言語情報を取得する
				String lang = user.getUserLang();

				if (lang == null ) {
					lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
				}
				//万が一Propertiesの設定がもれてしまった場合
				if (lang == null ) throw new JspException("The lang info is not set .");
			
				labelText =  LabelUtil.getLabel(user,
						                        getDisplayString(evaluate("label", getLabel())), 
						                        lang);
				
			} catch(SystemException e) {
				logger.error(e.toString());
				throw new JspException(e);
			} 
		
		if (!isDisabled()) {
            // Write out the 'field was present' marker.
            tagWriter.startTag("input");
            tagWriter.writeAttribute("type", "hidden");
            String name = WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName();        
            //String name = getName();
            tagWriter.writeAttribute("id", getId());
            tagWriter.writeAttribute("name", name);
            tagWriter.writeAttribute("value", processFieldValue(name, SystemConstant.FLAG_OFF, getInputType()));
            tagWriter.endTag();
		}
		
        setLabel(labelText);
        
		super.writeTagContent(tagWriter);
		
		return SKIP_BODY;
	}

	@Override
	protected void writeTagDetails(TagWriter tagWriter) throws JspException {
		tagWriter.writeAttribute("type", getInputType());

		Object boundValue = getBoundValue();
		Class<?> valueType = getBindStatus().getValueType();

		if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
			// the concrete type may not be a Boolean - can be String
			if (boundValue instanceof String) {
				boundValue = Boolean.valueOf((String) boundValue);
			}
			Boolean booleanValue = (boundValue != null ? (Boolean) boundValue : Boolean.FALSE);
			renderFromBoolean(booleanValue, tagWriter);
		} else if (String.class.equals(valueType) && getValue() == null) {
			renderFromValue(SystemConstant.FLAG_ON, tagWriter);
			//if (SystemConstant.FLAG_ON.equals(boundValue)) {
			//	renderFromValue(SystemConstant.FLAG_ON, tagWriter);
			//} else {
			//	renderFromValue(SystemConstant.FLAG_OFF, tagWriter);
			//}
		}

		else {
			Object value = getValue();
			if (value == null) {
				throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
			}
			Object resolvedValue = (value instanceof String ? evaluate("value", value) : value);
			renderFromValue(resolvedValue, tagWriter);
		}
	}
	
	/**
	 * writeTagContent の前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
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
	}
	
	@Override
	public void doFinally() {
		// tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
			super.doFinally();
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			setDisabled(false);
		}
	}
	
	@Override
	protected String getInputType() {
		return "checkbox";
	}

	@Override
	protected String getName() throws JspException {
		if (getPath()==null) {
			return super.getName();
		} else {
			return getPath();
		}
	}
	
	@Override
    protected BindStatus getBindStatus() throws JspException {
    	BindStatus bindStatus = null;
        if (bindStatus == null) {
                // HTML escaping in tags is performed by the ValueFormatter class.
                String nestedPath = getNestedPath();
                String pathToUse = (nestedPath != null ? nestedPath + getPath() : getPath());
                if (pathToUse.endsWith(PropertyAccessor.NESTED_PROPERTY_SEPARATOR)) {
                        pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
                }
                bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
        }
        return bindStatus;
    }
	
	
}
