package com.pigthinkingtec.framework.spring.mvc.download;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * Excel用のDownloadView クラス
 * 
 * @author yizhou
 */
@Component
public class ExcelFileDownloadView extends AbstractView {

	//Memoryが足りない場合、毎回100行のみ書き出すように制御する
	private final static int WRITE_MAX_ROW_EVERY_TIME=100;
	
    private final static Logger logger = LoggerFactory.getLogger(ExcelFileDownloadView.class);

    private boolean memLimitFlg = false;
    
    /**
     * Memory制限がある場合、このFlgを立ててください。（注意：性能に影響があり！）
     * @param memLimitFlg
     */
    public void setMemoLimitFlg(boolean memLimitFlg){
    	this.memLimitFlg = memLimitFlg;
    }
    
    /**
     * 
     * @param model
     * @param request
     * @param response
     * @throws Exception 
     */
    @Override
    public void renderMergedOutputModel(Map<String, Object> model, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Object obj = model.get("File");
        String fileName = (String)model.get("FileName");
        
        if (obj == null || fileName == null) {
            logger.error("File infomation doesn't exist.");
            return;
        }
        
        String fileNameUnicode = URLEncoder.encode(fileName, SystemConstant.ENCODE);
        
        response.setContentType(SystemConstant.CONTENT_TYPE);
        response.setHeader("Content-Disposition","attachment; filename=\"" + fileNameUnicode + "\"");
        response.setCharacterEncoding(SystemConstant.ENCODE);
        
        if (obj instanceof HSSFWorkbook) {
        	//xlsタイプ対応
            HSSFWorkbook wkBook = (HSSFWorkbook)obj;
            
            OutputStream out = response.getOutputStream();
            wkBook.write(out);
            out.flush();
            
        } else if (obj instanceof XSSFWorkbook) {
        	//xlsxタイプ対応
        	XSSFWorkbook wkBook = (XSSFWorkbook)obj;
        	
        	if (memLimitFlg) {
        		//Memory制限がある場合
        		SXSSFWorkbook wb = new SXSSFWorkbook(wkBook, WRITE_MAX_ROW_EVERY_TIME);
                OutputStream out = response.getOutputStream();
                wb.write(out);
	            out.flush();
	            
        	} else{
        		//Memory制限がない場合
	            OutputStream out = response.getOutputStream();
	            wkBook.write(out);
	            out.flush();
        	}
            
        }else if (obj instanceof SXSSFWorkbook) {
        	//Memory制限がある場合、このタイプを使うこと
        	SXSSFWorkbook wkBook = (SXSSFWorkbook)obj;
        	 
        	OutputStream out = response.getOutputStream();
            wkBook.write(out);
            out.flush();
            
        } else {
            logger.error("File type not support. fileName =" + fileName);
        }
    }
}
