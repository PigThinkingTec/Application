package com.pigthinkingtec.framework.spring.beans;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.SortDefinition;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 * ページネーション用のListHolderを拡張して、Validation可能にしたクラス
 * 
 * @author yizhou
 */
@SuppressWarnings("serial")
public class ValidPagedListHolder<E> extends PagedListHolder<E> implements
		Serializable {

	/** Log出力用オブジェクト */
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory
			.getLogger(ValidPagedListHolder.class);
	/** デフォルトページサイズ */
	private final static int DEFAULT_PAGE_SIZE = Integer
			.parseInt(PropertiesUtil.getProperty("pagenation.default.pagesize"));

	/**
	 * コンストラクタ
	 * 
	 */
	public ValidPagedListHolder() {
		super();
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param source 全データのリストオブジェクト
	 */
	public ValidPagedListHolder(List<E> source) {
		super(source);
		init();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param source 全データのリストオブジェクト
	 * @param sort ソート条件
	 */
	public ValidPagedListHolder(List<E> source, SortDefinition sort) {
		super(source, sort);
		init();
	}

	/**
	 * Return a sub-list representing the current page.
	 */
	@Override
	@Valid
	public List<E> getPageList() {
		return super.getPageList();
	}
	
	/**
	 * コンストラクタ生成時の初期化処理を行うメソッド
	 */
	protected void init() {
		setPageSize(DEFAULT_PAGE_SIZE);
	}

}
