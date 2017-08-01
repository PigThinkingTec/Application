package com.pigthinkingtec.framework.databean;

/**
 * ナビゲーションコントロール情報を格納するBean
 * 
 * @author yizhou
 * @version
 * @history
 */
@SuppressWarnings("serial")
public class NaviData implements DataBeanInterface {

	private String name = null;
	private String link = null;
	private String funcId = null;
	
	/**
	 * コンストラクタ
	 * @param name　タイトル
	 * @param link　パス
	 * @param funcId　機能ID
	 */
	public NaviData (String name, String link, String funcId) {
		this.name = name;
		this.link = link;
		this.funcId = funcId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	
}
