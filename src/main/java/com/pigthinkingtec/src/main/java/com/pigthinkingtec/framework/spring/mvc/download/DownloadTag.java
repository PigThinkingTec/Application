package com.pigthinkingtec.framework.spring.mvc.download;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Download 処理を、ブラウザからのリダイレクトを返して行う場合に使用するタグクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class DownloadTag extends TagSupport {
	
	private final static Logger logger = LoggerFactory.getLogger(DownloadTag.class);

	/**
	 * Download機能用のカスタムタグ
	 * 
	 * @return
	 * @throws JspException 
	 */
	@Override
	public int doStartTag() throws JspException {
		
		//String fileType = (String)this.pageContext.getSession().getAttribute("FileType");
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		String fileType = (String)request.getSession(false).getAttribute("FileType");
		if (fileType == null) return SKIP_BODY;
		
		Writer writer = pageContext.getOut();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<form id=\"download\" name=\"download\" action=\"");
		String url = this.pageContext.getServletContext().getContextPath() + "/download/" + fileType; 
		//sb.append(this.pageContext.getServletContext().getContextPath());
		//sb.append("/download/");
		//sb.append(fileType);
		ServletResponse response = this.pageContext.getResponse();
		 
		sb.append(((HttpServletResponse)response).encodeRedirectURL(url));
		sb.append("\"></form><script type=\"text/javascript\">$(document).ready(function() {$('#download').submit();});</script>");
		//sb.append("</form>");
		try {
			writer.write(sb.toString());
		} catch (IOException ex) {
			logger.error("io error: ", ex);
		}
		
		return SKIP_BODY;
	}
}
