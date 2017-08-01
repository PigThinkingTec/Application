package com.pigthinkingtec.framework.file;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import com.pigthinkingtec.framework.SystemConstant;

/**
 * Tokenizer
 * 
 * 
 * @author yizhou
 * @history
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class Tokenizer implements Enumeration {
	private String source;

	private int currentPosition;

	private int maxLength;

	private char separater = ' ';

	private int count;
	
	public Tokenizer() {
	}

	public void setNewLine(String line) {
		source = line;
		currentPosition = 0;
		maxLength = line.length();
		count = 0;
	}

	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	public boolean hasMoreTokens() {
		return nextComma(currentPosition) <= maxLength;
	}

	private int nextComma(int index) {
		boolean startQuote = true;
		for (; index < maxLength; index++) {
			char ch = source.charAt(index);
			if (startQuote && separater == ch)
				break;
			if (SystemConstant.DOUBLE_QUOTE == ch) {
				if(index == (maxLength - 1) || source.charAt(index + 1) == separater){
					startQuote = true;
				} else {
					startQuote = false;
				}
			}
		}
		return index;
	}

	public Object nextElement() {
		return nextToken();
	}

	public String nextToken() {
		if (currentPosition > maxLength)
			throw new NoSuchElementException();
		int st = currentPosition;
		currentPosition = nextComma(currentPosition);
		StringBuffer strb = new StringBuffer();
		if (st < currentPosition
				&& (source.charAt(st) != SystemConstant.DOUBLE_QUOTE || source
						.charAt(currentPosition - 1) != SystemConstant.DOUBLE_QUOTE)) {
			for (; st < currentPosition; st++)
				strb.append(source.charAt(st));

			currentPosition++;
			return new String(strb);
		}
		st++;
		char prevChar = ' ';
		for (; st < currentPosition - 1; st++) {
			char ch = source.charAt(st);
			if (ch == SystemConstant.DOUBLE_QUOTE
					&& prevChar == SystemConstant.DOUBLE_QUOTE) {
				prevChar = ' ';
			} else {
				strb.append(ch);
				prevChar = ch;
			}
		}

		currentPosition++;
		count++;
		return new String(strb);
	}

	public int countTokens() {
		int count = 0;
		for (int currpos = currentPosition; currpos <= maxLength;) {
			if (currpos > maxLength)
				break;
			currpos = nextComma(currpos) + 1;
			count++;
		}
		return count;
	}

	public int getCount(){
		return count;
	}
	
	protected void setSeparater(char separate) {
		this.separater = separate;
	}
}
