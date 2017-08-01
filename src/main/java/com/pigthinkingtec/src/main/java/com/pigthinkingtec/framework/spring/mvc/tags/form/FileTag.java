package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class FileTag extends InputTag {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FileTag.class);
	
	private String maskPatterns = null;
	
	private boolean escapeMaskFlg = false;
	
	protected boolean required;

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
	
	@Override
	protected String getType() {
		return "file";
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
			setCssClass("require");
			setCssErrorClass("require_error");
		} else {
			setCssClass("text");
			setCssErrorClass("text_error");
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
