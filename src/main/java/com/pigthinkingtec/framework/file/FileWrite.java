package com.pigthinkingtec.framework.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import com.pigthinkingtec.framework.dbaccess.QueryResult;
import com.pigthinkingtec.framework.exception.SystemException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ファイル書込みのSuperClass
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
public class FileWrite {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private FileOutputStream fStream = null;
	private BufferedWriter bWriter = null;
	private OutputStreamWriter oWriter = null;
	
	//ファイル書き込みの為のインターフェースを追加する
	
	/**
	 * コンストラクター
	 *  
	 */
	public FileWrite() {
	}

	/**
	 * 出力ファイル名をセットする
	 * 
	 * @param fileName 出力ファイル名
	 * @throws SystemException 
	 */
	public void setFile(String fileName) throws SystemException {
		setFile(fileName, false);
	}
	
	/**
	 * 出力ファイル名をセットする
	 * 
	 * @param fileName 出力ファイル名
	 * @param append true の場合、バイトはファイルの先頭ではなく最後に書き込まれる
	 * @throws SystemException 
	 */
	public void setFile(String fileName, boolean append) throws SystemException {
		try {
			File file = new File(fileName);
			fStream = new FileOutputStream(file, append);
			oWriter = new OutputStreamWriter(fStream);
			bWriter = new BufferedWriter(oWriter);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		}
	}
	
	/**
	 * 出力ファイル名をセットする
	 * 
	 * @param fileName 出力ファイル名
	 * @param append true の場合、バイトはファイルの先頭ではなく最後に書き込まれる
	 * @param enc エンコード
	 * @throws SystemException 
	 */
	public void setFile(String fileName, boolean append, String enc) throws SystemException {
		try {
			File file = new File(fileName);
			fStream = new FileOutputStream(file, append);
			oWriter = new OutputStreamWriter(fStream, enc);
			bWriter = new BufferedWriter(oWriter);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		}
	}
	
	public void close() throws SystemException{
		try {
			cleanUp();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		}
	}

	/**
	 * ファイルに書込む
	 * 
	 * @param param
	 * @throws SystemException 
	 */
	public void execute(String param) throws SystemException {
		writeFile(new String[] { param });
	}

	/**
	 * ファイルに書込む
	 * 
	 * @param param
	 * @throws SystemException 
	 */
	public void execute(String[] param) throws SystemException {
		writeFile(param);
	}

	/**
	 * 配列分行に書込む
	 * 
	 * @param param 行の配列
	 * @throws SystemException 
	 */
	private void writeFile(String[] param) throws SystemException {

		try {
			for (int i = 0; i < param.length; i++) {
				bWriter.write(parseLine(param[i]));
				bWriter.newLine();
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		}
	}

	/**
	 * オブジェクトをCloseする
	 * 
	 * @throws IOException
	 */
	protected void cleanUp() throws IOException {
		bWriter.flush();
		bWriter.close();
		oWriter.close();
		fStream.close();
	}

	/**
	 * ファイルに書込む
	 * 
	 * @param line
	 * @throws IOException
	 */
	protected void writeLine(String line) throws IOException {	
		bWriter.write(line);
	}
	
	protected void writeNewLine() throws IOException {
		bWriter.newLine();
	}
	
	/**
	 * ファイルに書込む
	 * 
	 * @param rst
	 * @throws SystemException
	 */
	public void write(QueryResult rst) throws SystemException {
		
		try{
			for(int i = 0 ; i < rst.getRowCount() ; i++){
				//実データを出力する前にデータを書き込む
				writeHeader(i);						
				writeLine(makeLine(rst.getRow(i),i));
				writeNewLine();	
			}
		}catch(IOException e){
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		} finally{
			try {
				cleanUp();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
				throw new SystemException(e);
			}
		}
	}

	/**
	 * ファイルに書き込む
	 * 
	 * @param ifFileList
	 * @throws SystemException
	 */
	public void write(List<String> list) throws SystemException {
		try{
			int count = 0;
			for(Iterator<String> ite = list.iterator(); ite.hasNext(); count++){
				//実データを出力する前にデータを書き込む
				writeHeader(count);						
				writeLine(ite.next());
				writeNewLine();	
			}
		}catch(IOException e){
			log.error(e.getMessage(),e);
			throw new SystemException(e);
		} finally{
			try {
				cleanUp();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
				throw new SystemException(e);
			}
		}
	}

	
	protected void writeHeader(int count) throws IOException{
	}

	protected String makeLine(String[] line, int linecount) {

		StringBuffer buf = new StringBuffer();
		for(int i = 0 ; i < line.length ; i++){
			buf.append(line[i]);
		}

		return buf.toString();
	}
	
	/**
	 * 行をパースする
	 * 
	 * @param param
	 * @return
	 */
	protected String parseLine(String param) {
		return param;
	}
	
	protected Log getLog(){
		return log;
	}

}
