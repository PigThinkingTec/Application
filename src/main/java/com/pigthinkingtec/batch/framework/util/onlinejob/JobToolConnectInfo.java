package com.pigthinkingtec.batch.framework.util.onlinejob;


/**
 * ジョブツールと接続用情報を保存するクラス
 * @author yizhou
 *
 */
public class JobToolConnectInfo {

	private String server;
	private int port;
	private String user;
	private String password;
	// yyyy-mm-dd hh:MM:ssの形または、now、now+30の形で指定
	private String starttime; 
	private String webServiceUrlPattern;

	OnlineJobParaBean[] paraBeans = null;

	public JobToolConnectInfo(String server, 
			                  int port, 
			                  String user, 
			                  String password, 
			                  String webServiceUrlPattern,
			                  String starttime, 
			                  OnlineJobParaBean[]  paraBeans) {
		super();
		this.server = server;
		this.port = port;
		this.user = user;
		this.password = password;
		this.starttime = starttime;
		this.webServiceUrlPattern = webServiceUrlPattern;
		this.paraBeans = paraBeans;
	}
	

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getWebserviceUrlPattern() {
		return webServiceUrlPattern;
	}

	public void setWebServiceUrlPattern(String webServiceUrlPattern) {
		this.webServiceUrlPattern = webServiceUrlPattern;
	}

	public OnlineJobParaBean[] getParaBeans() {
		return paraBeans;
	}

	public void setParaBeans(OnlineJobParaBean[] paraBeans) {
		this.paraBeans = paraBeans;
	}

}
