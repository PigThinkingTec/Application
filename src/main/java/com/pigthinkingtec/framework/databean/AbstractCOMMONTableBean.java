package com.pigthinkingtec.framework.databean;

import java.io.Serializable;

import com.pigthinkingtec.framework.SystemConstant;

import lombok.Getter;
import lombok.Setter;

/**
 * 抽象クラス（Enityクラスのベースとなるクラス）
 * 
 * @author Infra Team
 * @version $Revision: 1.4 $ $Date: 2010/03/30 01:33:36 $
 * @history
 * 
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class AbstractCOMMONTableBean implements Serializable{

	public AbstractCOMMONTableBean(){
	}

	/** selectedFlag */
	private String selectedFlag = SystemConstant.FLAG_OFF;
}