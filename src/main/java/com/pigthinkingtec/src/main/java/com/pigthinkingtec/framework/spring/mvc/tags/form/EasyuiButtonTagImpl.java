package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.google.common.base.Strings;
import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.form.ButtonTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのButtonTagを継承したクラス
 *
 * @author yizhou
 */
@SuppressWarnings("serial")
public class EasyuiButtonTagImpl extends ButtonTag {

	private static final Logger logger = LoggerFactory.getLogger(EasyuiButtonTagImpl.class);

    private TagWriter tagWriter;
    
	/**
	 * The name of the '{@code buttonTextId}' attribute.
	 */
	private static final String BUTTON_TEXT_ID_ATTRIBUTE = "buttonTextId";
	
	/**
	 * The value of the '{@code buttonTextId}' attribute.
	 */
	private String buttonTextId;
	
    /* ボタン押下時に呼び出されるActionのパス（formタグのaction属性と異なるパスを呼び出す際に指定する） */
    private String action = null;

    private String formid = null;

    private String index = null;
	
	private String maskPatterns = null;
	
	private Boolean escapeMaskFlg = false;

	private String buttonText = null;
		
    @Override
    public void setOnclick(String onclick) {
        super.setOnclick(onclick); //To change body of generated methods, choose Tools | Templates.
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public void setIndex(String index) {
        this.index = index;
    }
	
	public String getMaskPatterns() {
		return maskPatterns;
	}
	
	public void setMaskPatterns(String maskPatterns) {
		this.maskPatterns = maskPatterns;
	}

	public Boolean isEscapeMaskFlg() {
		return escapeMaskFlg;
	}
	
	public void setEscapeMaskFlg(Boolean escapeMaskFlg) {
		this.escapeMaskFlg = escapeMaskFlg;
	}
	
	public String getButtonTextId() {
		return buttonTextId;
	}

	public void setButtonTextId(String buttonTextId) {
		this.buttonTextId = buttonTextId;
	}
	
    public String getAction() {

        //Context Pathを取得
        String context
                = ((HttpServletRequest) (this.pageContext.getRequest())).getContextPath();
        //Formタグのaction属性に指定された値を取得

        String returnAction = null;

        //otheraction属性が指定されていない場合はcotextをかえす
        if (Strings.isNullOrEmpty(action)) {
            return context;
        }

        //action属性にcontextが含まれていなければ、contextを追加する。
        if (action.contains(context)) {
            returnAction = action;
        } else {
            returnAction = context + action;
        }
        //responseオブジェクトを取得
        ServletResponse response = this.pageContext.getResponse();
        //responseオブジェクトがHttpServletResponseだった場合は、
        //actionをencedeURLする。
        if (response instanceof HttpServletResponse) {
            returnAction = ((HttpServletResponse) response).encodeURL(returnAction);
        }
        return returnAction;
    }

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
    	prepare(tagWriter);

        tagWriter.startTag("button");
        		
		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", getType());
		writeValue(tagWriter);
		if (isDisabled()) {
			tagWriter.writeAttribute(DISABLED_ATTRIBUTE, "disabled");
		}
		tagWriter.forceBlock();
		
		tagWriter.appendValue(getButtonTextFromDB());
		 
		this.tagWriter = tagWriter;
		return EVAL_BODY_INCLUDE;
		
//        return super.writeTagContent(tagWriter); //To change body of generated methods, choose Tools | Templates.
       
    }
    
	protected void prepare(TagWriter tagWriter) throws JspException {
		FormTagImpl formTag = TagUtil.getFormTag(this);
		String formClassName = formTag.getModelAttribute();
		

        if (Strings.isNullOrEmpty(super.getOnclick())) {
            StringBuilder sb = new StringBuilder();
            sb.append("$(this).customSubmit(\'");
            if (Strings.isNullOrEmpty(getFormid())) {
                FormTagImpl formtag = TagUtil.getFormTag(this);
                if (Strings.isNullOrEmpty(formtag.getId())) {
                    sb.append(formtag.getModelAttribute());
                } else {
                    sb.append(formtag.getId());
                }
            } else {
                sb.append(getFormid());
            }

            //otheraction属性のチェック
            if (!Strings.isNullOrEmpty(action)) {
                sb.append("\','");
                sb.append(getAction());
                sb.append("\'");
            } else {
                sb.append("\'");
            }

            sb.append(",");
            sb.append(this.index);
            sb.append(")");

            setOnclick(sb.toString());
        }
		
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

        //スタイルシートの設定
        if (Strings.isNullOrEmpty(getCssClass())) {
            setCssClass("button easyui-linkbutton");
        }else{
        	setCssClass(StringUtil.build(getCssClass(), " ", "button easyui-linkbutton"));
        }
	}

	/**
	 * Determine the '{@code buttonTextId}' attribute value for this tag,
	 * @see #getLocal()
	 * @see #autogenerateLocal()
	 */
	protected String getButtonTextFromDB() throws JspException {

		UserContainer user = UserUtil.createUserContainer(pageContext);
		 
		if (StringUtils.hasText(this.buttonTextId)) {
			try {					
				    user.setPgmId("ButtonTagImpl");
					
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
    protected String getType() {
        return "button";
    }

    @Override
    public void doFinally() {
        super.doFinally();
        this.setOnclick(null);

        // tomcatで実行する場合は初期化処理を実施。
        if (PropertiesUtil.getProperty("cleanAtDoFinally").equals("true")) {
            this.formid = null;
            this.action = null;
            this.maskPatterns = null;
            this.escapeMaskFlg = false;
			this.setCssClass(null);
            this.setCssStyle(null);
			this.setDisabled(false);
			
        }
    }
    
	/**
	 * Closes the '{@code button}' block tag.
	 */
	@Override
	public int doEndTag() throws JspException {
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}
}
