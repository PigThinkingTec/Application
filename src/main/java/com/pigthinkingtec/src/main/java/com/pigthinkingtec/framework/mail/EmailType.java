package com.pigthinkingtec.framework.mail;

/**
 * Emailに対するEnum
 *
 * @author yizhou
 */
public enum EmailType {
	
	Simple("com.pigthinkingtec.framework.mail.SimpleEmailEx"),
	MultiPart("com.pigthinkingtec.framework.mail.MultiPartEmailEx"),
	Html("com.pigthinkingtec.framework.mail.HtmlEmailEx");
	
	private String className = null;
	
	private EmailType(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return this.className;
	}
}
