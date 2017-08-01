package com.pigthinkingtec.framework.spring.mvc.download;

/**
 * フレームワークでダウンロードをサポートするFileType
 *
 * @author yizhou
 */
public enum DownloadFileType {
	
	Csv("csv"), Excel("excel"), Other("other");
	
	private final String type;
	
	private DownloadFileType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
}
