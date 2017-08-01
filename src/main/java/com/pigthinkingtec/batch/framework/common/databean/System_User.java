package com.pigthinkingtec.batch.framework.common.databean;

import com.pigthinkingtec.framework.databean.AbstractCOMMONTableBean;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
public class System_User extends AbstractCOMMONTableBean {

    /** userID  ユーザID  */
    private String userID = null;
    /** familyName  ユーザの姓  */
    private String familyName = null;
    /** firstName  ユーザの名  */
    private String firstName = null;
    /** password  パスワード  */
    private String password = null;
    /** roleID  ロールID  */
    private String roleID = null;
    /** initSelectionNo1  優先表示される担当品目グループ（TEMP4では不使用）  */
    private String initSelectionNo1 = null;
    /** initSelectionNo2  優先表示される担当品目グループ（TEMP4では不使用）  */
    private String initSelectionNo2 = null;
    /** initSelectionNo3  優先表示される担当品目グループ（TEMP4では不使用）  */
    private String initSelectionNo3 = null;
    /** initSelectionNo4  優先表示される担当品目グループ（TEMP4では不使用）  */
    private String initSelectionNo4 = null;
    /** initSelectionNo5  優先表示される担当品目グループ（TEMP4では不使用）  */
    private String initSelectionNo5 = null;
    /** language  言語キー  */
    private String language = null;
    /** menuID  メニューID  */
    private String menuID = null;


    /** 一覧表示で自項目が選択されたことを判定する数値フラグ **/
    private Integer selectedIndex = null;

    /** IT  IT  */
    private String it = null;
    /** IU  IU  */
    private String iu = null;
    /** IP  IP  */
    private String ip = null;
    /** IC  IC  */
    private String ic = null;
    /** UT  UT  */
    private String ut = null;
    /** UU  UU  */
    private String uu = null;
    /** UP  UP  */
    private String up = null;
    /** UC  UC  */
    private String uc = null;

    /*
     * コンストラクタ
     */
    public System_User() {
        super();
    }

}

