package com.pigthinkingtec.framework.spring.mvc.tags.form;

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
import org.springframework.web.servlet.tags.form.RadioButtonTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのRadioButtonTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class RadioButtonTagImpl extends RadioButtonTag {
	
	private static final Logger logger = LoggerFactory.getLogger(RadioButtonTagImpl.class);
	
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
	 * エスケープマスクフラグを設定する
	 * @param escapeMaskFlg
	 */
	public void setEscapeMaskFlg (Boolean escapeMaskFlg) {
		this.escapeMaskFlg = escapeMaskFlg;
	}
	
	/**
	 * The value of the LabelText getting from DB.
	 */
	private String labelText;
		
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		
		 UserContainer user = UserUtil.createUserContainer(pageContext);

		try {					
			user.setPgmId("RadioButtonTagImpl");
			
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
				
				if (! isHtmlEscape()) {
					// htmlEscape フラグはFalseの場合
					// 改行コード以外のHTML Escapeを行う
					labelText = StringUtil.escapeHtml(labelText, true);
				}
			} catch(SystemException e) {
				logger.error(e.toString());
				throw new JspException(e);
			} 

        setLabel(labelText);
       
        prepare(tagWriter);
		return super.writeTagContent(tagWriter);
	}
	
	
	/**
	 * writeTagContent の 前処理
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
}
