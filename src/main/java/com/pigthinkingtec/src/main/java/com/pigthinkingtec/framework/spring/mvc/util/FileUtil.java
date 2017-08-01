package com.pigthinkingtec.framework.spring.mvc.util;


import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


/**
 * File操作に関するUtilityクラス
 *
 * @author yizhou
 */
public class FileUtil {
    
	@SuppressWarnings("unused")
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    
    /**
     * MultipartFile オブジェクトから InputStream オブジェクトを取得するメソッド
     * 
     * @param file MultipartFile オブジェクト
     * @return InputStream オブジェクト
     * @throws SystemException 
     */
    public static InputStream getInputStream(MultipartFile file) throws SystemException {
        try {
            return file.getInputStream();
        } catch (IOException ex) {
            throw new SystemException(ex);
        }
    }
    
    /**
     * MultipartFile オブジェクトから ファイル名を取得するメソッド
     * 
     * @param file MultipartFile オブジェクト
     * @return ファイル名（フォルダ名抜き）
     */
    public static String getFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String temps[] = originalFileName.split("\\\\");
        int arraySize = temps.length;
        String fileName = temps[arraySize - 1];
        return fileName;
    }
    
    /**
     * MultipartFile オブジェクトから一行ずつ文字列にして読み込み、Listオブジェクトに追加してゆき<br>
     * 最終行までの文字列のListオブジェクトを返すメソッド<br>
     * CSVファイルのアップロードで使用する想定
     * 
     * @param file MultipartFile オブジェクト
     * @return 文字列List
     * @throws SystemException 
     */
    public static List<String> getFileLineList(MultipartFile file) throws SystemException {
    	BufferedReader br = null;
    	try {
            InputStream input = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(input, SystemConstant.ENCODE));

            List<String> rowList = new ArrayList<String>();
            String line;
            while((line = br.readLine()) != null) {
            	rowList.add(line);
            }
            return rowList;
        } catch (IOException ie) {
            throw new SystemException(ie);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                throw new SystemException(ex);
            }
        }
    }
    
    /**
     * InputStream から 指定したファイル名でFileオブジェクトを生成するメソッド
     * 
     * @param fileName ファイル名
     * @param is InputStream オブジェクト
     * @return  File オブジェクト
     */
    public static File createNewFile(String fileName, InputStream is) throws SystemException {
        String filePath = PropertiesUtil.getProperty("email.file.folder");
        
        File file = new File(filePath + fileName);
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[256];
            int i = is.read(bytes);
            while(i > 0) {
                fos.write(bytes);
                i = is.read(bytes);
            }
            is.close();
            fos.close();
        } catch (IOException ex) {
            throw new SystemException(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new SystemException(ex);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw new SystemException(ex);
                }
            }
        }
        return file;
    }
    
    /**
     * 指定したディレクトリをサブディレクトおよびファイルごと削除するメソッド
     * 
     * @param dirPath 削除するディレクトリのパス
     * @throws SystemException
     */
    public static void deleteDirectory(String dirPath) throws SystemException {
    	File dir = new File(dirPath);
    	try {
    		FileUtils.deleteDirectory(dir);
    	} catch (IOException ioe) {
    		throw new SystemException(ioe);
    	}
    }
    
}
