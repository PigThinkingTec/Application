package com.pigthinkingtec.framework.spring.mvc.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * とりあえずファイルをダウンロードしたいときに使用するViewクラス
 *
 * @author yizhou
 */
@Component
public class OtherFileDownloadView extends AbstractFileDownloadView {
	
	private final static Logger logger = LoggerFactory.getLogger(OtherFileDownloadView.class);

	@Override
	protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {
		Object obj = model.get("File");
		
		if (obj==null) {
			logger.error("File infomation doesn't exist.");
			return null;
		}
		
		if (obj instanceof InputStream) {
			return (InputStream)obj;
		} else if (obj instanceof File) {
			return new FileInputStream((File)obj);
		} else {
			return null;
		}
	}

	@Override
	protected void addResponseHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
		String fileName = (String)model.get("FileName");
		String fileNameUnicode;
		try {
			if (fileName != null) { 
				fileNameUnicode = URLEncoder.encode(fileName, SystemConstant.ENCODE);
			} else {
				fileNameUnicode = "";
			}
		} catch (UnsupportedEncodingException ex) {
			logger.error("encoding error.", ex);
			return;
		}
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileNameUnicode + "\"");
		response.setContentType(SystemConstant.CONTENT_TYPE);
		response.setCharacterEncoding(SystemConstant.ENCODE);
	}
}
