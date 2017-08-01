package com.pigthinkingtec.framework.spring.mvc.tags.form;

import com.pigthinkingtec.framework.spring.mvc.inputhelp.AbstractIHForm;
import com.pigthinkingtec.framework.util.StringUtil;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InputHelp画面に配置し、親画面への値のコピーやInputHelpウィンドウのクローズなどの 機能を制御するタグ
 *
 * @author yizhou
 */
@SuppressWarnings("serial") 
public class InputHelpControlTag extends TagSupport {

	private Logger logger = LoggerFactory.getLogger(InputHelpControlTag.class);

	/* IH画面のFormの名前 */
	private String name = null;

	/* Formを探索するスコープ。デフォルトはnull(pageContext.findAttributeで全スコープを探索する) */
	// private String scope = null;
	private String parentFormName = null;

	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @param name
	 */
	public void setParentFormName(String name) {
		this.parentFormName = name;
	}

	/*
	 * no-javadoc
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {

			// セッションからフォームの情報を取り出す。
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			Object obj = request.getSession().getAttribute(name);
			AbstractIHForm form = (AbstractIHForm) obj;

			if (obj == null) {
				return SKIP_BODY;
			}

			// 　選択ボタンが押された場合
			if ((form.getButtonName() != null)
					&& form.getButtonName().equals("choose")) {

				// 親画面のフォームに選択した情報をセットするスクリプトを出力
				processChoose(form);
			}

			// 選択ボタン、または閉じるボタンが押された場合,
			// セッションをクリアし、次画面を閉じる
			if ((form.getButtonName() != null)
					&& (form.getButtonName().equals("choose") || form
							.getButtonName().equals("close"))) {

				// 選択画面を閉じるスクリプトを出力
				pageContext
						.getOut()
						.println(
								"<SCRIPT>window.opener.ihwindow = null;self.close();</SCRIPT>");

				// 　フォームの情報をセッションから削除する。
				request.getSession().removeAttribute(name);
			}
		} catch (SocketException e) {
			// PROD-10953 ヘルプボタン連打やヘルプ表示前にブラウザの閉じるボタンを押すとエラーログが出る件の対応
			logger.warn("SocketException occurred.");
		} catch (IOException e) {
			logger.error("IOException occurred", e);
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	/**
	 * Chooseボタンが押された際に、親画面の該当コンポーネントに値をコピーするJavaScriptを生成する。
	 *
	 * @throws Exception
	 */
	private void processChoose(AbstractIHForm form) throws IOException,
			JspException {

		// 一覧から親画面へ戻す対象が選択されていない場合は何もしない
		if ((form.getSelected() == null) || (form.getSelected().equals(""))) {
			return;
		}

		int selected_row = Integer.parseInt(form.getSelected());
		String columnName = form.getOnFocusColumn();
		String valueField = form.getValueField();
		Map<String, String> ctrl_mapping = form.getCtrlMapping();
		StringBuilder buf = new StringBuilder("<SCRIPT>");
		String value = null;
		String formName = form.getParentFormName();
		for (String key : ctrl_mapping.keySet()) {

			try {
				value = BeanUtils
						.getProperty(form.getResult(selected_row), key);
			} catch (Exception e) {
				logger.error("porperty get error : ", e);
			}

			if (value == null) {
				value = "";
			} else {
				value = StringEscapeUtils.escapeJavaScript(String
						.valueOf(value));
			}

			buf.append("window.opener.document.forms['");
			buf.append(formName);
			buf.append("'].item('");
			buf.append(ctrl_mapping.get(key));
			buf.append("').value='");
			buf.append(value);
			buf.append("';");
			
			//変更した親画面のOnChangeイベントを発火させる
			buf.append("window.opener.$(\"*[name='" + 
						ctrl_mapping.get(key) + "']\").change();");
		}

		if (StringUtil.isNotBlank(columnName)) {
			buf.append("window.opener.");
			buf.append(columnName);
			buf.append("(window.opener.document.forms['");
			buf.append(this.parentFormName);
			buf.append("'].item('");
			buf.append(valueField);
			buf.append("').value);");
		}

		buf.append("</SCRIPT>");

		pageContext.getOut().println(buf.toString());
	}
}
