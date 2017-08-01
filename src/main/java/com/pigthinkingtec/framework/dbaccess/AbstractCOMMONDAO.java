package com.pigthinkingtec.framework.dbaccess;

/**
 * AbstractDAOクラス
 * 
 * @author Infra Team
 * @version $Revision: 1.4 $ $Date: 2010/12/23 05:37:41 $
 * @history
 * 
 * 
 */
public abstract class AbstractCOMMONDAO extends AbstractDAO {
  
	/**
	 * コンストラクタ
	 * 
	 * @param userId ユーザID
	 * @param pgmId ジョブID
	 */
	public AbstractCOMMONDAO(String userId, String pgmId) {
		super(userId,pgmId);
	}
	
}
