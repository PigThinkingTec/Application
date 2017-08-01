package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * SpringのErrorsTagを継承したクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class ErrorsTagImpl extends ErrorsTag {
	
	/** Loggerオブジェクト */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ErrorsTag.class);
	
	/** エラーメッセージ出力時に先頭に改行を設定するかどうかのフラグ。エラーメッセージを項目の下に出力するか横に出力するかを判定するフラグ（Defaultは下） */
	private boolean startWithDelimiter = true;
	
	/**
	 * 先頭に改行を設定するかどうかを判定するメソッド
	 * trueの場合は、改行を設定される。falseの場合は、改行が設定されない。
	 * 
	 * @return	判定結果
	 */
	public boolean isStartWithDelimiter() {
		return startWithDelimiter;
	}
	
	/**
	 * エラーメッセージの出力場所を設定するメソッド
	 * trueの場合は先頭に改行を設定する。
	 * 
	 * @param startWithDelimiter
	 */
	public void setStartWithDelimiter(boolean startWithDelimiter){
		this.startWithDelimiter = startWithDelimiter; 
	}

	@Override
	protected void renderDefaultContent(TagWriter tagWriter) throws JspException {
		//CssClassが設定されていなかった場合は、デフォルトで、errormsgを設定する。
		if (getCssClass() == null || getCssClass().equals("")) {
			setCssClass("errormsg");
		}
		tagWriter.startTag(getElement());
		writeDefaultAttributes(tagWriter);
		String delimiter = ObjectUtils.getDisplayString(evaluate("delimiter", getDelimiter()));
		String[] errorMessages = getBindStatus().getErrorMessages();
		for (int i = 0; i < errorMessages.length; i++) {
			//強制的に項目の下に出力する。
			if (i == 0 && this.startWithDelimiter) {
				tagWriter.appendValue(delimiter);
			}
			String errorMessage = errorMessages[i];
			if (i > 0) {
				tagWriter.appendValue(delimiter);
			}
			tagWriter.appendValue(getDisplayString(errorMessage));
		}
		tagWriter.endTag();
	}
}
