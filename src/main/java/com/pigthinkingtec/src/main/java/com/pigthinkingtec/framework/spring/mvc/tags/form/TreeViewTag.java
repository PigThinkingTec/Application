package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;
import com.pigthinkingtec.framework.util.StringUtil;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.util.label.LabelUtil;

public class TreeViewTag extends BodyTagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4684614984842191376L;

	private Logger logger = LoggerFactory.getLogger(TreeViewTag.class);

	private Tag parentTag = null;

	private boolean iconflg;

	private String key;
	
	public Tag getParentTag() {
		return parentTag;
	}

	public void setParentTag(Tag parentTag) {
		this.parentTag = parentTag;
	}

	public boolean getIconflg() {
		return iconflg;
	}

	public void setIconflg(boolean iconflg) {
		this.iconflg = iconflg;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void doInitBody() throws JspException {
		
	}

	public int doAfterBody() throws JspException {		
		return SKIP_BODY;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag tag) {
		this.parentTag = tag;
	}

	public Tag getParent() {
		return parentTag;
	}
	
	//「ロール」「ユーザー」のラベルID
	public static final String ROLE_LABELID = "07010003";
	public static final String USER_LABELID = "00000142";
	
	//ロールツリー、ユーザツリーのキーとなる文字列
	public static final String ROLE_KEY = "role";
	public static final String USER_KEY = "user";
	
	//ツリーの開閉の初期状態を決める文字列
	public static final String OPENED = "opened";
	public static final String CLOSED = "closed";
	
	//jQueryを適用する際のID
	public static final String TREEID = "roleUserTree";
	
	//スタイルシートで画像指定する際のClass名
	public static final String ROLEBRANCH_CLASS = "roleBranch";
	public static final String ROLENODE_CLASS = "roleNode";
	public static final String USERBRANCH_CLASS = "userBranch";
	public static final String USERNODE_CLASS = "userNode";
	
	public int doStartTag() throws JspException {
		
		UserContainer user = UserUtil.createUserContainer(pageContext);
		//ラベル情報の取得
		String roleLabel;
		String userLabel;
		
		try {					
			user.setPgmId("TreeViewTag");
			
			//ユーザと繋がる言語情報を取得する
			String lang = user.getUserLang();

			if (lang == null ) {
				lang = PropertiesUtil.getProperty(SystemConstant.DEFAULT_LANG_KEY);
				//万が一Propertiesの設定がもれてしまった場合
				if (lang == null ) {
					logger.error("The lang info is not set .");
					throw new JspException("The lang info is not set .");
				}
			}
			
			roleLabel =  LabelUtil.getLabel(user, ROLE_LABELID, lang);
			userLabel =  LabelUtil.getLabel(user, USER_LABELID, lang);
				
			//HTML Escapeする、改行コードはEscape対象外
			roleLabel = StringUtil.escapeHtml(roleLabel, true);
			userLabel = StringUtil.escapeHtml(userLabel, true);
			
		} catch(SystemException e) {
			logger.error(e.getMessage());
			throw new JspException(e);
		} 
		
		String errorMsg; 
		StringBuffer buffer = new StringBuffer();
		
		// ツリーの開始
		buffer.append("<ul id='" + TREEID + "'>\n");
		//　RequestからMapを取得
		Object tempObj = pageContext.getAttribute(key,PageContext.REQUEST_SCOPE);
		if (tempObj == null) {
			// キー取得値がnullの場合、エラー出力
			errorMsg = "TreeViewTag error: requestオブジェクト内にキー " + key + " に対応する値が見つかりません";
			logger.error(errorMsg);
			throw new JspException(errorMsg);
		} else {
			try {
				// Mapへのキャスト
				@SuppressWarnings("unchecked")
				Map<String,Map<String,String>> map = (Map<String,Map<String,String>>)tempObj;
				// roleの処理 -----------------------------------------------------
				Map<String,String> roleMap = (Map<String,String>)map.get(ROLE_KEY);
				buffer.append(makeInnerList(roleLabel,ROLEBRANCH_CLASS,ROLENODE_CLASS,roleMap));
				
				// userの処理 -----------------------------------------------------
				Map<String,String> userMap = (Map<String,String>)map.get(USER_KEY);
				buffer.append(makeInnerList(userLabel,USERBRANCH_CLASS,USERNODE_CLASS,userMap));
			} catch (ClassCastException e) {
				//不適切なMapが渡された場合、エラーメッセージを表示
				errorMsg ="TreeViewTag error: requestオブジェクトの型が不正です。" +
						"Map<String,Map<String,String>>型の値を渡してください。";
				logger.error(errorMsg);
				throw new JspException(errorMsg);
			}
		}			

		// ツリーの終了
		buffer.append("</ul>");
		
		// 書き出し
		try {	
			JspWriter writer = pageContext.getOut();
			writer.println(buffer.toString());
		} catch(IOException e) {
			logger.error(e.getMessage());
			throw new JspException(e.getMessage());
		}

		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void release() {}
	
	
	/** マップからTreeViewの各ブランチ(1階層)を作るメソッド.
	 * 
	 * @param listTitle ブランチにあたるオブジェクトの表示名
	 * @param branchClass ブランチ画像表示時に適用するスタイルシートで指定したクラス名
	 * @param nodeClass ノード画像表示時に適用するスタイルシートで指定したクラス名
	 * @param targetMap <ファイル名,飛び先URL>が定義されたマップ
	 * @return TreeViewのフォルダとなるHTML文字列
	 */
	private String makeInnerList(String listTitle, String branchClass,
				String nodeClass, Map<String,String> targetMap) 
				throws JspException {
		
		String errorMsg;
		StringBuffer buffer = new StringBuffer();
		
		if (targetMap == null) {
			logger.warn("Map内に" + listTitle + "ツリーが見つかりません");
		} else {
			// ツリーオープンフラグの取得 opened or closed
			String openFlgStr = targetMap.get(null);
			
			// ツリーを作成
			if (OPENED.equals(openFlgStr)) {
				buffer.append("<li>");
			} else if (CLOSED.equals(openFlgStr)) {
				buffer.append("<li class='closed'>");
			} else {
				errorMsg ="TreeViewTag error: 開閉フラグの値が不正です。" +
						"内側マップのキーnullに対してopened/closedいずれかの値を指定してください。";
				logger.error(errorMsg);
				throw new JspException(errorMsg);
			}
			//画像ありの場合、画像をつけてフォルダ名を表示
			if (iconflg) {
				buffer.append("<span class='" + branchClass + "'>" + listTitle + "</span>\n");
			} else {
				buffer.append("<span>" + listTitle + "</span>\n");
			}
			
			//フォルダ内ファイルのリストを表示
			buffer.append("<ul>\n");
			
			// Mapに入っているすべての要素に対してループ
			for (String k : targetMap.keySet()) {
				// キーがnullならオープンフラグなので何もしない
				if (k != null ) {
					buffer.append("<li>");
					// actionのURLにリンク
					if (targetMap.get(k) != null) {
						buffer.append("<a href='" + targetMap.get(k) + "'>");
					}
					//画像ありの場合、画像をつけて表示
					if (iconflg) {
						buffer.append("<span class='" + nodeClass + "'>" + k);
					} else {
						buffer.append("<span>" + k);
					}
					buffer.append("</span>");
					
					if (targetMap.get(k) != null) {
						buffer.append("</a>");
					}
					buffer.append("</li>\n");
				}
			}
			
			//フォルダ内ファイルの終了
			buffer.append("</ul>\n");
			
			//フォルダの終了
			buffer.append("</li>\n");
		}
		return buffer.toString();
	}

	
}