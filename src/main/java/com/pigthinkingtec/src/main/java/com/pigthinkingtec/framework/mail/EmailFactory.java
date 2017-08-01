package com.pigthinkingtec.framework.mail;

import com.google.common.base.Strings;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Emailオブジェクトを生成するFactoryクラス
 *
 * @author yizhou
 */
public class EmailFactory {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EmailFactory.class);
	
	private EmailFactory() {
	}
	
	/**
	 * typeで指定したメール用のEmailクラスを取得するメソッド
	 * 
	 * @param type 送信するEmailの種類（Simple,Multipart,Html）
	 * @return Emailクラス
	 * @throws SystemException 
	 */
	public static Email getEmail(EmailType type) throws SystemException  {
		try {
			@SuppressWarnings("unchecked")
			Class<Email> clazz = (Class<Email>) Class.forName(type.getClassName());
			Email email = clazz.newInstance();
			email.setHostName(PropertiesUtil.getProperty("mail.server.name"));
			String userName = PropertiesUtil.getProperty("mail.user.name");
			String password = PropertiesUtil.getProperty("mail.user.password");
			if (!Strings.isNullOrEmpty(userName) && !Strings.isNullOrEmpty(password)) {
				email.setAuthentication(userName, password);
			}
			String port = PropertiesUtil.getProperty("mail.server.port");
			if (!Strings.isNullOrEmpty(port)) {
				email.setSmtpPort(Integer.parseInt(port));
			}
			String starttls = PropertiesUtil.getProperty("mail.starttls");
			if (!Strings.isNullOrEmpty(starttls)) {
				email.setEmailStartTLSEnabled(Boolean.valueOf(starttls));
			}
			String sslOnConnect = PropertiesUtil.getProperty("mail.ssl");
			if (!Strings.isNullOrEmpty(sslOnConnect)) {
				email.setEmailSSLOnConnect(Boolean.valueOf(sslOnConnect));
			}
			String charset = PropertiesUtil.getProperty("mail.charset");
			if (!Strings.isNullOrEmpty(charset)) {
				email.setCharset(charset);
			}
			return email;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			throw new SystemException(ex);
		}
	}
}
