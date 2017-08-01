package com.pigthinkingtec.framework.spring.mvc.download;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * CSV用のDownloadView クラス
 * 
 * @author yizhou
 */
@Component
public class CsvFileDownloadView extends AbstractFileDownloadView {
	
	private final static Logger logger = LoggerFactory.getLogger(CsvFileDownloadView.class);

	@SuppressWarnings("unchecked")
	@Override
	protected InputStream getInputStream(Map<String, Object> model, HttpServletRequest request) throws IOException {
		Object obj = model.get("File");
		
		InputStream returnInputStream = null;
		
		if (obj==null) {
			logger.error("File infomation doesn't exist.");
			return null;
		}
		
		if (obj instanceof List) {
			List<String> strings = (List<String>)obj;
			StringBuilder sb = new StringBuilder();
			for(String s : strings){
				sb.append(s);
				sb.append(SystemConstant.LINE_FEED);
			}
			returnInputStream = new ByteArrayInputStream(sb.toString().getBytes());
			
		} else if (obj instanceof InputStream) {
			returnInputStream = (InputStream)obj;
			
		} else if (obj instanceof String) {
			returnInputStream = new FileInputStream((String)obj);
		} else {
			logger.error("File type not support.");
		}
		
		return returnInputStream;
				
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
