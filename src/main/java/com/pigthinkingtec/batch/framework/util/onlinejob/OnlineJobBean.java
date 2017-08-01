package com.pigthinkingtec.batch.framework.util.onlinejob;

import java.util.HashMap;
import java.util.Map;

import com.pigthinkingtec.framework.databean.AbstractOnlineDataBean;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author zhou
 */
@Getter @Setter
public class OnlineJobBean extends AbstractOnlineDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobpath;
	private String jobchainpath;

	private String server;
	private int port;
	private String user;
	private String password;
	private String starttime; //yyyy-mm-dd hh:MM:ssの形または、now、now+30の形で指定
	private String webServiceUrlPattern;
	private boolean asynFlg; //true:非同期　　false:同期
	
	private String paraNames;//カマン区切りでの配列
	private String paraValues;//カマン区切りでの配列
	
	private Map<String, String> paraMap = new HashMap<String, String>();
	
	private OnlineJobParaBean[] paraBeans;

}

