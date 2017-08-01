package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのCheckboxTagを継承したクラス<BR>
 * CheckboxをONにしなかった場合のDefault値の設定を行えるようにしたクラス。<BR>
 * もしDefault値を設定しなかった場合は、SystemConstant.FLAG_OFFをDefault値として扱う
 * 
 * @author yizhou
 */

@SuppressWarnings("serial")
public class CheckboxTagImpl2 extends AbstractSingleCheckedElementTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CheckboxTagImpl2.class);
	
	private String maskPatterns = null;
	
	private boolean escapeMaskFlg = false;
	/** CheckBoxをonにしなかった場合の値 */
	private String defaultValue = SystemConstant.FLAG_OFF;
	
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
	 * Default Value を取得する
	 * @return 
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Default Value　を設定する
	 * @param defaultValue 
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Checkboxの本体に関する出力は、上位クラスに任せ、ここでは、hiddenに関する処理のみ行う。
	 * 
	 */
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		super.writeTagContent(tagWriter);
		if (!isDisabled()) {
			//Checkboxとともに出力するHiddenに関する処理
			tagWriter.startTag("input");
			tagWriter.writeAttribute("type", "hidden");
			//name属性は、checkboxのname属性にplefixを付ける
			String name = StringUtil.build(WebDataBinder.DEFAULT_FIELD_DEFAULT_PREFIX, getName());
			//idは、name属性から[]を取り除く
			String id =StringUtils.deleteAny(name, "[]");
			tagWriter.writeAttribute("id", id);
			tagWriter.writeAttribute("name", name);
			
			if (defaultValue != null) {
				tagWriter.writeAttribute("value", defaultValue);
			} else {
				tagWriter.writeAttribute("value", SystemConstant.FLAG_OFF);
			}
			tagWriter.endTag();
		} else {
			//Checkboxとともに出力するHiddenに関する処理
			tagWriter.startTag("input");
			tagWriter.writeAttribute("type", "hidden");
			//name属性は、checkboxのname属性にplefixを付ける
			String name = StringUtil.build(WebDataBinder.DEFAULT_FIELD_DEFAULT_PREFIX, getName());
			//idは、name属性から[]を取り除く
			String id =StringUtils.deleteAny(name, "[]");
			tagWriter.writeAttribute("id", id);
			tagWriter.writeAttribute("name", name);
			Object valueObject = getValue();
			Object defaultValueObject = defaultValue;
			
			if (valueObject == null) {
				Class<?> type = getBindStatus().getValueType();
				if (Boolean.class.equals(type)) {
					valueObject = Boolean.TRUE;
					defaultValueObject = Boolean.FALSE;
				} else if (String.class.equals(type)) {
					valueObject = SystemConstant.FLAG_ON;
					defaultValueObject = SystemConstant.FLAG_OFF;
				}
			}
			
			if (isSelectedValue(getBindStatus(), valueObject)) {
				tagWriter.writeAttribute("value", String.valueOf(valueObject));
			} else if (defaultValue != null) {
				tagWriter.writeAttribute("value", defaultValue);
			} else {
				tagWriter.writeAttribute("value", String.valueOf(defaultValueObject));
			}
			tagWriter.endTag();
		}
		
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
		} else if (getValue() == null) {
			//値の型がStringで、valueが設定されていない場合は、FLAG_ONをvalueにする
			renderFromValue(SystemConstant.FLAG_ON, tagWriter);
		} else {
			Object value = getValue();
			if (value == null) {
				throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
			}
			Object resolvedValue = (value instanceof String ? evaluate("value", value) : value);
			renderFromValue(resolvedValue, tagWriter);
		}
	}
	
	@Override
	public void doFinally() {
		// tomcatで実行する場合は初期化処理を実施。
		if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
			super.doFinally();
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			this.defaultValue = null;
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
	
	/**
	 * Returns {@code true} if the supplied candidate value is equal to the value bound to
	 * the supplied {@link BindStatus}. Equality in this case differs from standard Java equality and
	 * is described in more detail <a href="#equality-contract">here</a>.
	 */
	public static boolean isSelectedValue(BindStatus bindStatus, Object candidateValue) {
		if (bindStatus == null) {
			return (candidateValue == null);
		}

		// Check obvious equality matches with the candidate first,
		// both with the rendered value and with the original value.
		Object boundValue = bindStatus.getValue();
		if (ObjectUtils.nullSafeEquals(boundValue, candidateValue)) {
			return true;
		}
		Object actualValue = bindStatus.getActualValue();
		if (actualValue != null && actualValue != boundValue &&
				ObjectUtils.nullSafeEquals(actualValue, candidateValue)) {
			return true;
		}

		return false;
	}
}
