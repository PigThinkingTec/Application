package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのInputTagを継承したクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class EasyuiDateBoxTagImpl extends InputTag {

    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EasyuiDateBoxTagImpl.class);

	
    /* 要素格納用変数 */
    protected boolean required;
	private String maskPatterns = null;
	private boolean escapeMaskFlg = false;


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



    /* (non-Javadoc)
     * 開始タグを生成するメソッド
     * 
     * テキストタグを生成する。
     * input textフィールドに要素があれば作成
     * 
     * @return super.doStartTag
     * @throws JspException
     * @see org.apache.struts.taglib.html.BaseFieldTag#doStartTag()
     */
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
    	
        prepare();
        return super.writeTagContent(tagWriter);
    }

	/**
	 * Tag出力前の準備処理を行うメソッド
	 * 
	 * @throws JspException
	 */
    private void prepare() throws JspException {    	

    	setCssClass(StringUtil.build(getCssClass(), " ", "easyui-datebox  text ime_mode_off"));
    	setCssErrorClass(StringUtil.build(getCssErrorClass(), " ", "easyui-datebox text_error ime_mode_off code"));

        if (required) {
            setCssClass(StringUtil.build(getCssClass(), " ", "require"));
            //setCssErrorClass(StringUtil.build(getCssErrorClass(), " ", "require_error"));
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
        super.doFinally();
        
        // tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
            this.required = false;
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
			setDisabled(false);
            setMaxlength(null);
            setCssClass(null);
            setCssErrorClass(null);
            setOnblur(null);
            setOnfocus(null);
            setOnchange(null);
        }
    }
    
    @Override
    protected void writeValue(TagWriter tagWriter) throws JspException{
    	String value = getDisplayString(getBoundValue(),getPropertyEditor());
    	tagWriter.writeAttribute("value", value);

    }
}
