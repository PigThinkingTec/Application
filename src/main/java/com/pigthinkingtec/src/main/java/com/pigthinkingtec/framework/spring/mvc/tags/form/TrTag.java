package com.pigthinkingtec.framework.spring.mvc.tags.form;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 明細画面で使用するTRタグクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class TrTag extends TagSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(TrTag.class);
	
	/* 適応するクラス */
	private static final String STYLE_CLASS = "tablebg";
	private static final String STYLE_CLASS_2 = "tablebg2";
	/* 明細行 */
	private Integer index = null;
	

	/**
	 * 明細行を取得する。
	 * 
	 * @return Returns the index.
	 */
	public Integer getIndex() {
			return index;
	}

	/**
	 * 明細行を設定する。
	 * 
	 * @param index The index to set.
	 */
	public void setIndex(Integer index) {
			this.index = index;
	}

	@Override
	public int doEndTag() throws JspException {
		Writer writer = pageContext.getOut();
		try {
			writer.write("</tr>");
		} catch (IOException ex) {
			logger.error("io error: ", ex);
		}
		return (EVAL_PAGE);
	}

	@Override
	public int doStartTag() throws JspException {
		Writer writer = pageContext.getOut();
		try {
			writer.write(renderTrStartElement());
		} catch (IOException ex) {
			logger.error("io error: ", ex);
			return (SKIP_BODY);
		}
		return (EVAL_BODY_INCLUDE);
	}
	
	/**
	 * 開始タグを生成する。
	 * 
	 * @return 開始タグ文字列
	 */
	protected String renderTrStartElement() {
		StringBuilder br = new StringBuilder();
		br.append("<tr");
		if (index == null || index.intValue() % 2 != 0) {
			//奇数行の処理(スタイルシートの適応)
			br.append(" class=\"");
			br.append(STYLE_CLASS_2);
			br.append("\"");
		} else {
			//偶数行の処理（スタイルシートの適応）
			br.append(" class=\"");
			br.append(STYLE_CLASS);
			br.append("\"");
		}
		br.append(">");
		return br.toString();
	}
}
