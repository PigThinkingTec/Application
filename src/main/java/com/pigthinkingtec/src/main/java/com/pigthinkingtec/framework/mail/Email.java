package com.pigthinkingtec.framework.mail;

import java.io.File;
import org.apache.commons.mail.EmailAttachment;

import com.pigthinkingtec.framework.exception.SystemException;

/**
 * Emailクラスに対するインターフェース
 *
 * @author yizhou
 */
public interface Email {
	
	public Email setEmailMsg(String msg) throws SystemException;

	public String sendEmail() throws SystemException;
	
	public Email setEmailSubject(String aSubject);

	public Email addEmailBcc(String... emails) throws SystemException;

	public Email addEmailCc(String... emails) throws SystemException;

	public Email addEmailTo(String... emails) throws SystemException;

	public Email setEmailFrom(String email) throws SystemException;
	
	public Email setEmailStartTLSEnabled(boolean startTlsEnabled);

	public void setCharset(String newCharset);
	
	public Email attachFile(EmailAttachment attachment) throws SystemException;
	
	public Email attachFile(File file) throws SystemException;
	
	public Email setHtmlEmailMsg(String aHtml) throws SystemException;
	
	public Email setTextEmailMsg(String aText) throws SystemException;
	
	public void setHostName(String aHostName);
	
	public void setAuthentication(String userName, String password);
	
	public void setSmtpPort(int port);
	
	public Email setEmailSSLOnConnect(boolean ssl);
	
}
