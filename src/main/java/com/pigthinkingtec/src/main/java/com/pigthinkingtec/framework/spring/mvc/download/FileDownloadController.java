package com.pigthinkingtec.framework.spring.mvc.download;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pigthinkingtec.framework.spring.mvc.token.NotCheckToken;
import com.pigthinkingtec.framework.spring.mvc.token.NotSaveToken;

/**
 * ダウンロード処理を行うコントローラクラス
 * 
 * @author yizhou
 */
@Controller
@RequestMapping(value = "download")
@NotSaveToken
@NotCheckToken
public class FileDownloadController {
	
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(FileDownloadController.class);
	
	/**
	 * CSVダウンロード用のコントローラメソッド
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "csv")
	public String downloadCsv(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		process(model, request, response);
		
		return "csvFileDownloadView";
	}
	
	
	/**
	 * Excelダウンロード用のコントローラメソッド
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "excel")
	public String downloadExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		process(model, request, response);
		
		return "excelFileDownloadView";
	}
	
	/**
	 * 上記以外のファイルダウンロード用のコントローラメソッド
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "other")
	public String downloadOther(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		process(model, request, response);
		
		return "otherFileDownloadView";
	}
	
	/**
	 * ダウンロード処理における共通処理を切り出したメソッド
	 * 
	 * @param model
	 * @param request
	 * @param response 
	 */
	private void process(Model model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		
		Object file = session.getAttribute("File");
		Object fileName = session.getAttribute("FileName");
		
		model.asMap().put("File", file);
		model.asMap().put("FileName", fileName);
		
		session.removeAttribute("File");
		session.removeAttribute("FileName");
		session.removeAttribute("FileType");
	}
}
