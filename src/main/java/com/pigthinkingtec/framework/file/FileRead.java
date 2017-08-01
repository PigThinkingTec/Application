package com.pigthinkingtec.framework.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * ファイル読込みのSuperClass
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class FileRead {

	private Log log = LogFactory.getLog(this.getClass().getName());
	private FileReader fRead = null;
	private BufferedReader bRead = null;
	private int colCount = 0;

	/**
	 * コンストラクター
	 * 
	 */
	public FileRead() {
	}

	/**
	 * 読込むファイルを指定する（ファイルパス・ファイル名）
	 * 
	 * @param fileName
	 * @throws FileNotFoundException 
	 */
	public void setFile(String fileName) throws FileNotFoundException {
		fRead = new FileReader(fileName);
		bRead = new BufferedReader(fRead);
	}
	
	
	/**
	 * 読込むファイルを指定する（FormFile型）
	 * 
	 * @param fileName
	 * @throws FileNotFoundException 
	 * @throws SystemException 
	 */
	public void setFile(MultipartFile file) throws SystemException {
		try {
			bRead = new BufferedReader(new InputStreamReader(file.getInputStream(), SystemConstant.ENCODE));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new SystemException(e);
		}
	}
	

	/**
	 * ファイルをFetchして行データをオブジェクトにセットする
	 * 
	 * @return ParseResult ファイルの行データオブジェクト
	 * @throws SystemException 
	 */
	public ParseResult execute() throws SystemException {
		String line = null;
		ParseResult rst = new ParseResult();
		int count = 0;
		try {
			while ((line = bRead.readLine()) != null) {
				setArgument(rst, line, count);
				if (hasLayoutError()){
					return null;
				}
				count++;
			}
			rst.setCount(colCount);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new SystemException(e);
		} finally {
			try {
				bRead.close();
				if(fRead != null){
					fRead.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new SystemException(e);
			}
		}
		return rst;
	}

	/**
	 * ファイルをFetchしてKeyとValueをMapにセットする
	 * 
	 * @return Map ファイルのマップオブジェクト
	 * @throws SystemException 
	 * 
	 */
	public Map<String, String> parseFile(String separator) throws SystemException{
		String line = null;
		HashMap<String, String> map = new HashMap<String, String>();
		StringTokenizer st;
		try {
			while ((line = bRead.readLine()) != null) {
				st = new StringTokenizer(line,separator);
				String key = st.nextToken();
				String value = st.nextToken();
				map.put(key,value);
			}
		} catch (IOException e) {
			log.error("", e);
			throw new SystemException(e);
		} finally {
			try {
				bRead.close();
				if(fRead != null){
					fRead.close();
				}
			} catch (IOException e) {
				log.error("", e);
				throw new SystemException(e);
			}
		}
		return map;
	}
	
	
	/**
	 * 項目数を指定する(execute()メソッドをCallする前に必ずセットする必要がある)
	 * 
	 * @param colCount
	 */
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	
	
	/**
	 * 結果リストに行イメージをセットする
	 * CSV/TABに関しては、オーバーライドして区切り文字を追加する
	 * 
	 * @param rst
	 * @param line
	 * @param linecount
	 */
	protected void setArgument(ParseResult rst, String line, int linecount) {
		rst.add(line);
	}

	/**
	 * ログオブジェクトを取得する
	 * 
	 * @return Log
	 */
	protected Log getLog() {
		return this.log;
	}
	
	protected boolean hasLayoutError(){
		return false;
	}
	
}
