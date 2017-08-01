package com.pigthinkingtec.framework.file;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

import com.pigthinkingtec.framework.exception.SystemErrException;

/**
*
* XML形式でデータを出力する
*
* @author yizhou
* @history
*
* 
*/
public class XMLWrite extends XMLFilterImpl {
	private Writer output = null;

	private final Attributes EMPTY_ATTS = new AttributesImpl();

	/**
	 * コンストラクター
	 * 
	 */
	public XMLWrite() {
		init(null);
	}

	/**
	 * コンストラクター
	 * 
	 * @param writer
	 */
	public XMLWrite(Writer writer) {
		init(writer);
	}

	/**
	 * コンストラクター
	 * 
	 * @param response
	 * @throws SystemErrException
	 */
	public XMLWrite(HttpServletResponse response) throws SystemErrException{
		try {
			init(response.getWriter());
		} catch (IOException e) {
			throw new SystemErrException(e);
		}
	}
	
	/**
	 * 初期処理を行う
	 * 
	 * @param writer
	 */
	private void init(Writer writer) {
		setOutput(writer);
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	/**
	 * 開始要素を作成する
	 * 
	 * @param localName
	 * @throws SAXException
	 */
	public void startElement(String localName) throws SAXException {
		write('<');
		write(localName);
		write('>');
		super.startElement("", localName, "", EMPTY_ATTS);
	}

	/**
	 * 終了要素を作成する
	 * 
	 * @param localName
	 * @throws SAXException
	 */
	public void endElement(String localName) throws SAXException {
		write("</");
		write(localName);
		write('>');
		super.endElement("", localName, "");
	}

	/**
	 * 子要素を作成する
	 * 
	 * @param localName
	 * @param content
	 * @throws SAXException
	 */
	public void dataElement(String localName, String content)
			throws SAXException {
		startElement(localName);
		characters(content);
		endElement(localName);
	}

	/* (非 Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		// write('\n');
		super.endDocument();
		try {
			output.flush();
			output.close();
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * データを書き込む
	 * 
	 * @param data
	 * @throws SAXException
	 */
	public void characters(String data) throws SAXException {
		String tmp = data;
		if(tmp == null){
			tmp = "";
		}
		char ch[] = tmp.toCharArray();
		writeEsc(ch, 0, ch.length, false);
		super.characters(ch, 0, ch.length);
	}

	/**
	 * Writerにデータを書き込む
	 * 
	 * @param param
	 * @throws SAXException
	 */
	private void write(String param) throws SAXException {
		try {
			output.write(param);
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * 対象データをエスケープして書き込む 
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 * @param isAttVal
	 * @throws SAXException
	 */
	private void writeEsc(char ch[], int start, int length, boolean isAttVal)
			throws SAXException {
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '&':
				write("&amp;");
				break;
			case '<':
				write("&lt;");
				break;
			case '>':
				write("&gt;");
				break;
			case '\"':
				if (isAttVal) {
					write("&quot;");
				} else {
					write('\"');
				}
				break;
			default:
				if (ch[i] > '\u007f') {
					write("&#");
					write(Integer.toString(ch[i]));
					write(';');
				} else {
					write(ch[i]);
				}
			}
		}
	}

	/**
	 * Writerにデータを書き込む
	 * 
	 * @param param
	 * @throws SAXException
	 */
	private void write(char param) throws SAXException {
		try {
			output.write(param);
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	/**
	 * Writerを登録する
	 * 
	 * @param writer
	 */
	private void setOutput(Writer writer) {
		if (writer == null) {
			output = new OutputStreamWriter(System.out);
		} else {
			output = writer;
		}
	}
}
