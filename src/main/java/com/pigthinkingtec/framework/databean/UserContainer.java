package com.pigthinkingtec.framework.databean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.pigthinkingtec.framework.SystemConstant;

import lombok.Data;

/**
 * ユーザ情報格納オブジェクトクラス
 * 
 * @author  yizhou
 * @history 
 * 
 */
@Data
@SuppressWarnings("serial")
public class UserContainer implements Serializable {

	/** ユーザID */
	private String userId = null;
	/** 名前(Last + First) */
	private String userName = null;
	/** 名前(First) */
	private String userFirstName = null;
	/** 名前(LastName/FamilyName) */
	private String userLastName = null;
	/** カナ(Last + First) */
	private String userNameKna = null;
	/** カナ(First) */
	private String userFirstNameKna = null;
	/** カナ(LastName/FamilyName) */
	private String userLastNameKna = null;
	/** 組織コード */
	private String organizationCode = null;
	/** 組織名 */
	private String organizationName = null;
	/** 引継情報 */
	private Map<String, String> takeOverInfoMap = new HashMap<String, String>();
	/** プログラムID */
	private String pgmId = null;
	/** JOB ID */
	private String jobId = null;
	/** Login済みかどうか */
	private String loginFlg = SystemConstant.FLAG_OFF;
	/** AsOfDate */
	private String asOfDate = null;
	/** Userの言語情報 */
	private String userLang = null;
	/** UserのTimeZone情報 */
	private String timeZone = null;
	/** 引継情報(推奨しないこと) */
	private Map<Object,Object> object;
	/** RoleID */
	private String roleId;
	/** Userの日付フォーマット情報 */
	private String dateFormat;
	/** Userの管理部門情報 */
	private String userDept;
	
	/**
	 * 機能間引き継ぎ情報を設定するメソッド
	 * 
	 * @param key 引き継ぎキー
	 * @param value 引き継ぎ情報
	 */
	public void setTakeOverInfo(String key, String value){
		this.takeOverInfoMap.put(key, value);
	}
	
	/**
	 * 機能間引き継ぎ情報を取得するメソッド
	 * 
	 * @param key 引き継ぎキー
	 * @return 引き継ぎ情報
	 */
	public String getTakeOverInfo(String key){
		return this.takeOverInfoMap.get(key);
	}
    
	/**
	 * 機能間引き継ぎ情報を削除するメソッド
	 * 
	 * @param key 引き継ぎキー
	 */
    public void removeTakeOverInfo(String key) {
        this.takeOverInfoMap.remove(key);
    }
    
	/**
	 * 機能間引き継ぎ情報を設定するメソッド
	 * 
	 * @param key 引き継ぎキー
	 * @param value 引き継ぎ情報
	 */
	public void setTakeOverInfo(Object key,Object value) {
		object.put(key, value);
	}

	/**
	 * 機能間引き継ぎ情報を取得するメソッド
	 * 
	 * @param key 引き継ぎキー
	 * @return 引き継ぎ情報
	 */
	public Object getTakeOverInfo(Object key) {
		return object.get(key);
	}
	
	/**
	 * 機能間引き継ぎ情報を削除するメソッド
	 * 
	 * @param key 引き継ぎキー
	 */
	public void removeTakeOverInfo(Object key) {
		object.remove(key);
	}
	
	/**
	 * 機能間引き継ぎ情報を初期化するメソッド
	 */
	public void initTakeOverInfo() {
		this.takeOverInfoMap.clear();
		this.object.clear();
	}
}
