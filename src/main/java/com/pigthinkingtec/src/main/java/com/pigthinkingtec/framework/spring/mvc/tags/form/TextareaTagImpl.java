package com.pigthinkingtec.framework.spring.mvc.tags.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.servlet.tags.form.TextareaTag;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 * SpringのTextareaTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class TextareaTagImpl extends TextareaTag {
    
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TextareaTagImpl.class);
    
	private boolean required;
    protected boolean ime;
    private String wrap = null;
    private final String WRAPS_ATTRIBUTE = "wraps";
	private String maskPatterns = null;
	private boolean escapeMaskFlg = false;

    /**
     * imeを取得する
     *
     * @return Returns the ime.
     */
    public boolean getIme() {
        return ime;
    }

    /**
     * imeをセットする
     * 
     * @param ime The ime to set.
     */
    public void setIme(boolean ime) {
        this.ime = ime;
        //setStyleSheet();
    }

    public boolean getRequired() {
        return required;
    }

    public String getWrap() {
        return wrap;
    }

    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    public void setRequired(boolean required) {
        this.required = required;
        //setStyleSheet();
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
	
    private void setStyleSheet(){
        if(required){
            if(ime){
                setCssClass("require ime_mode_on");
                setCssErrorClass("require_error ime_mode_on");
            }
            else{
                setCssClass("require ime_mode_off");
                setCssErrorClass("require_error ime_mode_off");
            }
        } else {
            if(ime){
                setCssClass("text ime_mode_on");
                setCssErrorClass("text_error ime_mode_on");
            }
            else{
                setCssClass("text ime_mode_off");
                setCssErrorClass("text_error ime_mode_off");
            }
        }
    }
		
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
		
        tagWriter.startTag("textarea");
        writeDefaultAttributes(tagWriter);
        writeOptionalAttribute(tagWriter, ROWS_ATTRIBUTE, getRows());
        writeOptionalAttribute(tagWriter, COLS_ATTRIBUTE, getCols());
        writeOptionalAttribute(tagWriter, WRAPS_ATTRIBUTE, getWrap());
        writeOptionalAttribute(tagWriter, ONSELECT_ATTRIBUTE, getOnselect());
		if (getDisabledFlg()==true) {
			writeOptionalAttribute(tagWriter, DISABLED_ATTRIBUTE, "true");
		}
		
        String value = getDisplayString(getBoundValue(), getPropertyEditor());
        tagWriter.appendValue(processFieldValue(getName(), value, "textarea"));
        tagWriter.endTag();
        return SKIP_BODY;	
    }

    @Override
    public void doFinally() {
		// tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
			super.doFinally();
			this.ime = false;
			this.required = false;
			this.wrap = null;
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			this.setRows(null);
			this.setCols(null);
			this.setOnselect(null);
			this.setDisabled(false);
		}
    }
	
	/**
	 * Disabledにするかどうかを判定するメソッド
	 * 
	 * @return
	 * @throws JspException 
	 */
	protected boolean getDisabledFlg() throws JspException {
		
		if (isDisabled()) {
			return true;
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
						return true;
					}
				}
			}
		}
		
		//マスクフラグに応じたdisable設定
		Boolean maskFlg =  TagUtil.getMaskFlg(formClassName, pageContext);
		if (maskFlg == true && escapeMaskFlg == false) {
			return true;
		}
		return false;
	}
	
	/**
	 * writeTagContent の 前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
		setStyleSheet();
	}
}