package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pigthinkingtec.framework.spring.mvc.AbstractForm;

/**
 * Tagに関するユーティリティクラス
 *
 * @author yizhou
 */
public class TagUtil {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TagUtil.class);
	
	/**
	 * タグオブジェクトが属するFormタグオブジェクトを取得するメソッド
	 * 
	 * @param tagSupport
	 * @return 
	 */
	public static FormTagImpl getFormTag(TagSupport tagSupport) {
		
		Tag tag = tagSupport.getParent();
		while (tag != null) {
			if (tag instanceof FormTagImpl) {
				return (FormTagImpl) tag;
			} else {
				tag = tag.getParent();
			}
		}
		return null;
	}
	
	/**
	 * Formクラスからマスクパターンを取得するメソッド
	 * 
	 * @param formClassName マスクパターンを取得する対象のフォームクラス名
	 * @param pagecontext JSPで使用されるPageContextオブジェクト
	 * @return マスクパターン
	 */
	public static String getMaskPattern(String formClassName, PageContext pagecontext) {
		Object obj = pagecontext.findAttribute(formClassName);
		if (obj instanceof AbstractForm) {
			AbstractForm form = (AbstractForm)obj;
			return form.getMaskPattern();
		}
		return null;
	}
	
	/**
	 * Formクラスからマスクフラグを取得するメソッド
	 * @param formClassName
	 * @param pagecontext
	 * @return マスクフラグ
	 */
	public static Boolean getMaskFlg(String formClassName, PageContext pagecontext){
		Object obj = pagecontext.findAttribute(formClassName);
		if(obj instanceof AbstractForm) {
			AbstractForm form = (AbstractForm)obj;
			return form.isMaskFlg();
		}
		return false;
	}
}
