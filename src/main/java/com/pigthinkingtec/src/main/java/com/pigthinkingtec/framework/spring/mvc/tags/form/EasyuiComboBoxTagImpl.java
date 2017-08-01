package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 * SpringのSelectTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class EasyuiComboBoxTagImpl extends SelectTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EasyuiComboBoxTagImpl.class);
	
	private boolean required = false; 
	
	private String maskPatterns = null;
	
	private boolean escapeMaskFlg = false;

	/**
	 * 必須項目かどうか確認する
	 * 
	 * @return 
	 */
	public boolean getRequired() {
		return required;
	}

	/**
	 * 必須項目かどうか設定する
	 * 
	 * @param required 
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
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
		setStyleSheet();
		
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
	
	private void setStyleSheet(){
		if(!required){
			setCssClass("text easyui-combobox");
			setCssErrorClass("text_error easyui-combobox");
		} else {
			setCssClass("require easyui-combobox");
			setCssErrorClass("text_error easyui-combobox");
		}
	}
	
	@Override
	public void doFinally() {
		super.doFinally();
		// tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
        	this.required = false;
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			setDisabled(false);
			setCssClass(null);
			setCssErrorClass(null);
		}
	}
}
