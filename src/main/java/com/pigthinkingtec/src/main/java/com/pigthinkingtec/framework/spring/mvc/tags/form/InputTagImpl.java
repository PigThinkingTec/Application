package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.google.common.base.Strings;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.taglib.TagFormat;
import com.pigthinkingtec.framework.taglib.TaglibFwFormatManager;
import com.pigthinkingtec.framework.util.NumberUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;

import java.math.BigDecimal;
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
public class InputTagImpl extends InputTag {

    private static final Logger logger = LoggerFactory.getLogger(InputTagImpl.class);

	private static final boolean VIEW_ZERO_FLG;
	static {
		String _viewZeroFlg = PropertiesUtil.getProperty("input.view.zero.flag");
		if (SystemConstant.FLAG_ON.equals(_viewZeroFlg)) {
			VIEW_ZERO_FLG = true;
		} else {
			VIEW_ZERO_FLG = false;
		}
	}
	
    /* 要素格納用変数 */
    protected boolean required;
    private String fwformat = null;
	private String maskPatterns = null;
	private boolean escapeMaskFlg = false;
    private boolean zeroViewFlg = VIEW_ZERO_FLG;

    public String getFwformat() {
        return fwformat;
    }

    public void setFwformat(String fwformat) {
        this.fwformat = fwformat;
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
	 * ゼロを表示させるかどうかの個別設定フラグを取得する
	 * 
	 * @return zeroViewFlg
	 */
	public boolean isZeroViewFlg() {
		return zeroViewFlg;
	}

	/**
	 * ゼロを表示させるかどうかの個別設定フラグを設定する
	 * 
	 * @param zeroViewFlg 設定する zeroViewFlg
	 */
	public void setZeroViewFlg(boolean zeroViewFlg) {
		this.zeroViewFlg = zeroViewFlg;
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
    	
        prepare(fwformat);
        return super.writeTagContent(tagWriter);
    }

	/**
	 * Tag出力前の準備処理を行うメソッド
	 * 
	 * @param fwformat フォーマット
	 * @throws JspException
	 */
    private void prepare(String fwformat) throws JspException {

        if (Strings.isNullOrEmpty(fwformat)) {
            fwformat = "NONE";
        }

        TagFormat tf = TaglibFwFormatManager.get(fwformat);
        if (tf == null) {
            throw new JspException("Invalid fwformat = " + fwformat);
        } else {

            if (tf.getMaxlength() != null && Strings.isNullOrEmpty(getMaxlength())) {
                setMaxlength(tf.getMaxlength());
            }
            if (tf.getStyleclass() != null && Strings.isNullOrEmpty(getCssClass())) {            	
            	setCssClass(tf.getStyleclass());
            }
            if (tf.getErrorstyleclass() != null && Strings.isNullOrEmpty(getCssErrorClass())) {
                setCssErrorClass(tf.getErrorstyleclass());
            }

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

            if (!isReadonly() && !isDisabled()) {

                if (tf.getOnblur() != null && Strings.isNullOrEmpty(getOnblur())) {
                    setOnblur(tf.getOnblur());
                }
                if (tf.getOnfocus() != null && Strings.isNullOrEmpty(getOnfocus())) {
                    setOnfocus(tf.getOnfocus());
                }
                if (tf.getOnchange() != null && Strings.isNullOrEmpty(getOnchange())) {
                	setOnchange(tf.getOnchange());
                }
            }else{
				//　 calendarクラスを取り除く
				String c = getCssClass();
				if (!StringUtil.isBlank(c)) {
					c = c.replaceAll("calendar", "code");
					setCssClass(c);
				}
				String cError = getCssErrorClass();
				if (!StringUtil.isBlank(cError)) {
					cError = cError.replaceAll("calendar", "code");
					setCssErrorClass(cError);
				}
            }
        }
    }

    @Override
    public void doFinally() {
        super.doFinally();
        
        // tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
            this.fwformat = null;
            this.required = false;
			this.maskPatterns = null;
			this.escapeMaskFlg = false;
            this.zeroViewFlg = VIEW_ZERO_FLG;
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
        if (Strings.isNullOrEmpty(fwformat)) {
            fwformat = "NONE";
        }
        
        //ユーザ情報の取得
        UserContainer user = UserUtil.createUserContainer(pageContext);
    	
    	TagFormat tf = TaglibFwFormatManager.get(fwformat);
    	if (tf == null) {
            throw new JspException("Invalid fwformat = " + fwformat);
        } else {
            try {
            	value = TaglibFwFormatManager.formatValue(tf, value, user);

				//数字ゼロを画面に表示させるかどうかを判定した上で、表示させない場合は、valueをBlankにする。
				String c = getCssClass();
				if (!zeroViewFlg && !StringUtil.isBlank(value) && c.contains("numeric") && NumberUtil.isNumber(value)) {
					if ((new BigDecimal(value)).compareTo(BigDecimal.ZERO) == 0) {
						value = SystemConstant.BLANK;
					}
				}

            } catch (SystemException ex) {
                logger.error("system error: {}", ex);
                throw new JspException(ex);
            }
        }
    	tagWriter.writeAttribute("value", value);
    }
}
