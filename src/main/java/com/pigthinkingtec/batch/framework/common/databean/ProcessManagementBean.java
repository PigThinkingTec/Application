package com.pigthinkingtec.batch.framework.common.databean;

import java.util.List;

import com.pigthinkingtec.framework.databean.AbstractDataBean;
import com.pigthinkingtec.framework.databean.DataBeanInterface;

import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter
public class ProcessManagementBean extends AbstractDataBean implements DataBeanInterface, Cloneable {
	
	/**
	  * @Fields serialVersionUID : 1L
	  */
	private static final long serialVersionUID = 1L;
	
	/**
	  * @Fields errorList : エラーリスト（アップロード処理用）
	  */
	private List<ErrorListBean> errorList;
	
	/**
	  * @Fields resultCode : バッチ機能の終了コード
	  */
	private int resultCode = 0;
	
	/**
	  * @Fields resultCodeKey : バッチ機能の終了コードのKey
	  * バッチ側は戻り値ではなく、戻り値のKeyを戻すときに使う想定
	  */
	private String resultCodeKey = "";
	
	private int errKensu;
	private String errKbn;
	private String errListKbn;
	
	private String statusKbn;
	
	private String batchPgId;
	
    /** SHORI_KAISHI_DATE  処理開始日時  */
    private String shoriKaishiDate = null;
    /** SHORI_SHURYO_DATE  処理終了日時  */
    private String shoriShuryoDate = null;
    
	@Override
	public ProcessManagementBean clone() {	//throwsを無くす
		try {
			return (ProcessManagementBean) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

}
