package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.google.common.base.Strings;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.FormTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのFormTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class FormTagImpl extends FormTag {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FormTagImpl.class);
	
    @Override
    protected String getAction() {
        //Context Pathを取得
        String context = 
                ((HttpServletRequest)(this.pageContext.getRequest())).getContextPath();
        //Formタグのaction属性に指定された値を取得
        String action = super.getAction();
        String returnAction = null;
        //action属性が指定されていない場合はcotextをかえす
        if (Strings.isNullOrEmpty(action)) return context;

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
            returnAction = ((HttpServletResponse)response).encodeURL(returnAction);
	}
        return returnAction;
    }

    @Override
    public String getModelAttribute() {
        return super.getModelAttribute();
    }

    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
		prepare(tagWriter);
        super.writeTagContent(tagWriter);
        
        String token = (String)this.pageContext.getRequest().getAttribute("token");
        
        if (!Strings.isNullOrEmpty(token)) {
            tagWriter.startTag("input");
            writeOptionalAttribute(tagWriter, "type", "hidden");
            writeOptionalAttribute(tagWriter, "name", "token");
            writeOptionalAttribute(tagWriter, "value", token);
            tagWriter.endTag();
	}
        
        tagWriter.startTag("input");
        writeOptionalAttribute(tagWriter, "type", "hidden");
        writeOptionalAttribute(tagWriter, "name", "changeIndex");
        tagWriter.endTag();
        
        
        String contextPathString = ((HttpServletRequest)this.pageContext.getRequest()).getContextPath();
        
        tagWriter.startTag("input");
        writeOptionalAttribute(tagWriter, "type", "hidden");
        writeOptionalAttribute(tagWriter, "id", "applicationContextPath");
        writeOptionalAttribute(tagWriter, "value", contextPathString);
        tagWriter.endTag();
        
        return EVAL_BODY_INCLUDE;
    }
    
	/**
	 * writeTagContent の前処理
	 * 
	 * @param tagWriter
	 * @throws JspException
	 */
	protected void prepare(TagWriter tagWriter) throws JspException {
		
	}
}
