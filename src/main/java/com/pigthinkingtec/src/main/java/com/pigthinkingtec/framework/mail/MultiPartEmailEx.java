package com.pigthinkingtec.framework.mail;

import java.io.File;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.exception.SystemException;

/**
 * Commons Email の MultiPartEmail を継承したクラス
 * 
 * @author yizhou
 */
public class MultiPartEmailEx extends MultiPartEmail implements Email  {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MultiPartEmailEx.class);
	
	public MultiPartEmailEx() {
		super();
	}

	@Override
	public Email setEmailMsg(String msg) throws SystemException {
		try {
			super.setMsg(msg);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public String sendEmail() throws SystemException {
		try {
			return super.send();
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
	}

	@Override
	public Email setEmailSubject(String aSubject) {
		super.setSubject(aSubject);
		return this;
	}

	@Override
	public Email addEmailBcc(String... emails) throws SystemException {
		try {
			super.addBcc(emails);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email addEmailCc(String... emails) throws SystemException {
		try {
			super.addCc(emails);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email addEmailTo(String... emails) throws SystemException {
		try {
			super.addTo(emails);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email setEmailFrom(String email) throws SystemException {
		try {
			super.setFrom(email);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email attachFile(EmailAttachment attachment) throws SystemException {
		try {
			super.attach(attachment);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email attachFile(File file) throws SystemException {
		try {
			super.attach(file);
		} catch (EmailException ex) {
			throw new SystemException(ex);
		}
		return this;
	}

	@Override
	public Email setHtmlEmailMsg(String aHtml) throws SystemException {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public Email setTextEmailMsg(String aText) throws SystemException {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	@Override
	public Email setEmailStartTLSEnabled(boolean startTlsEnabled) {
		super.setStartTLSEnabled(startTlsEnabled);
		return this;
	}
	
	@Override
	public Email setEmailSSLOnConnect(boolean ssl) {
		super.setSSLOnConnect(ssl);
		return this;
	}
}
