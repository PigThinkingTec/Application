package com.pigthinkingtec.framework.function;

/**
 * ファンクション用列挙型
 * @author yizhou
 * @history
 *
 */
public enum FunctionStatus {
	
	NORMAL("normal"),WARNING("warning"),ERROR("error");
	
	@SuppressWarnings("unused")
	private String status;
	
	private FunctionStatus(String status) {
		this.status = status;
	}
	
}
