package com.pigthinkingtec.framework.spring.mvc.tags.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.pigthinkingtec.framework.spring.mvc.util.PropertiesUtil;

/**
 * @author yizhou
 * @history
 */
@SuppressWarnings("serial")
public class ListButtonTagImpl extends ButtonTagImpl {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(ListButtonTagImpl.class);
	
	private static String DEFAULT_HEIGHT;
	
	private static String DEFAULT_WIDTH;
	
	private static String DEFAULT_TOP;
	
	private static String DEFAULT_LEFT;
	
	private static String DEFAULT_RESIZABLE;
	
	static {
		String height = PropertiesUtil.getProperty("inputhelp.default.height");
		String width = PropertiesUtil.getProperty("inputhelp.default.width");
		String top = PropertiesUtil.getProperty("inputhelp.default.top");
		String left = PropertiesUtil.getProperty("inputhelp.default.left");
		String resizable = PropertiesUtil.getProperty("inputhelp.default.resizable");
		if (height == null) {
			DEFAULT_HEIGHT = "800";
		} else {
			DEFAULT_HEIGHT = height;
		}
		if (width == null) {
			DEFAULT_WIDTH = "1000";
		} else {
			DEFAULT_WIDTH = width;
		}
		if (top == null) {
			DEFAULT_TOP = "20";
		} else {
			DEFAULT_TOP = top;
		}
		if (left == null) {
			DEFAULT_LEFT = "20";
		} else {
			DEFAULT_LEFT = left;
		}
		if (resizable == null) {
			DEFAULT_RESIZABLE = "yes";
		} else {
			DEFAULT_RESIZABLE = resizable;
		}
	}
    /* テキストボックス名を何列目にマッピングさせるかをIHに引き渡すリクエストパラメータ名*/
    private static final String[] requestParamNames = {"col1", "col2", "col3", "col4", "col5", "col6", "col7", "col8", "col9", "col10", "col11", "col12", "col13", "col14", "col15", "col16", "col17", "col18", "col19", "col20", "col21", "col22", "col23", "col24", "col25", "col26", "col27", "col28", "col29", "col30", "col31", "col32", "col33", "col34", "col35"};

    /* InputHelpを呼び出すURL文字列*/
    private String url;

    /* テキストボックス名のマッピングを保持する配列*/
    private String[] cols;

    /* InputHelp画面に引き渡したいパラメータ名称を保持する配列 */
    private String[] pnames;

    /* InputHelp画面に引き渡したいパラメータ値を保持する配列 */
    private String[] pvalues;

    /* 明細行にListボタンを配置する際の、明細行ビーンの参照名 */
    private String listname;

    /* InputHelp画面の幅 */
    private String width;

    /* InputHelp画面の高さ */
    private String height;

    /* InputHelp画面の親画面からの相対座標（Y軸)*/
    private String top;

    /* InputHelp画面の親画面からの相対座標（X軸)*/
    private String left;

    /*複数出力する場合のインデックス*/
    private String index;
    
	/* InputHelp画面サイズを変更可能にするかどうか */
	private String resizable;
    
    /* 立ち上げ画面ID */
    private String screenId;

	/**
     * デフォルトコンストラクタ
     */
	public ListButtonTagImpl() {
		url = null;
		cols = new String[35];
		pnames = new String[15];
		pvalues = new String[15];
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		top = DEFAULT_TOP;
		left = DEFAULT_LEFT;
		resizable = DEFAULT_RESIZABLE;
	}

    /**
     * URLをセットする
     *
     * @param value
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * 1列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol1(String value) {
        this.cols[0] = value;
    }

    /**
     * 2列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol2(String value) {
        this.cols[1] = value;
    }

    /**
     * 3列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol3(String value) {
        this.cols[2] = value;
    }

    /**
     * 4列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol4(String value) {
        this.cols[3] = value;
    }

    /**
     * 5列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol5(String value) {
        this.cols[4] = value;
    }

    /**
     * 6列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol6(String value) {
        this.cols[5] = value;
    }

    /**
     * 7列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol7(String value) {
        this.cols[6] = value;
    }

    /**
     * 8列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol8(String value) {
        this.cols[7] = value;
    }

    /**
     * 9列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol9(String value) {
        this.cols[8] = value;
    }

    /**
     * 10列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol10(String value) {
        this.cols[9] = value;
    }

    /**
     * 11列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol11(String value) {
        this.cols[10] = value;
    }

    /**
     * 12列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol12(String value) {
        this.cols[11] = value;
    }

    /**
     * 13列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol13(String value) {
        this.cols[12] = value;
    }

    /**
     * 14列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol14(String value) {
        this.cols[13] = value;
    }

    /**
     * 15列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol15(String value) {
        this.cols[14] = value;
    }

    /**
     * 16列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol16(String value) {
        this.cols[15] = value;
    }

    /**
     * 17列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol17(String value) {
        this.cols[16] = value;
    }

    /**
     * 18列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol18(String value) {
        this.cols[17] = value;
    }

    /**
     * 19列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol19(String value) {
        this.cols[18] = value;
    }

    /**
     * 20列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol20(String value) {
        this.cols[19] = value;
    }

    /**
     * 21列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol21(String value) {
        this.cols[20] = value;
    }

    /**
     * 22列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol22(String value) {
        this.cols[21] = value;
    }

    /**
     * 23列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol23(String value) {
        this.cols[22] = value;
    }

    /**
     * 24列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol24(String value) {
        this.cols[23] = value;
    }

    /**
     * 25列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol25(String value) {
        this.cols[24] = value;
    }

    /**
     * 26列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol26(String value) {
        this.cols[25] = value;
    }

    /**
     * 27列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol27(String value) {
        this.cols[26] = value;
    }

    /**
     * 28列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol28(String value) {
        this.cols[27] = value;
    }

    /**
     * 29列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol29(String value) {
        this.cols[28] = value;
    }

    /**
     * 30列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol30(String value) {
        this.cols[29] = value;
    }

    /**
     * 31列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol31(String value) {
        this.cols[30] = value;
    }

    /**
     * 32列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol32(String value) {
        this.cols[31] = value;
    }

    /**
     * 33列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol33(String value) {
        this.cols[32] = value;
    }

    /**
     * 34列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol34(String value) {
        this.cols[33] = value;
    }

    /**
     * 35列目にマッピングする列名をセットする
     *
     * @param value
     */
    public void setCol35(String value) {
        this.cols[34] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名１をセットする
     *
     * @param value
     */
    public void setPname1(String value) {
        this.pnames[0] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名2をセットする
     *
     * @param value
     */
    public void setPname2(String value) {
        this.pnames[1] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名3をセットする
     *
     * @param value
     */
    public void setPname3(String value) {
        this.pnames[2] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名4をセットする
     *
     * @param value
     */
    public void setPname4(String value) {
        this.pnames[3] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名5をセットする
     *
     * @param value
     */
    public void setPname5(String value) {
        this.pnames[4] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名6をセットする
     *
     * @param value
     */
    public void setPname6(String value) {
        this.pnames[5] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名7をセットする
     *
     * @param value
     */
    public void setPname7(String value) {
        this.pnames[6] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名8をセットする
     *
     * @param value
     */
    public void setPname8(String value) {
        this.pnames[7] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名9をセットする
     *
     * @param value
     */
    public void setPname9(String value) {
        this.pnames[8] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名10をセットする
     *
     * @param value
     */
    public void setPname10(String value) {
        this.pnames[9] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名11をセットする
     *
     * @param value
     */
    public void setPname11(String value) {
        this.pnames[10] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名12をセットする
     *
     * @param value
     */
    public void setPname12(String value) {
        this.pnames[11] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名13をセットする
     *
     * @param value
     */
    public void setPname13(String value) {
        this.pnames[12] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名14をセットする
     *
     * @param value
     */
    public void setPname14(String value) {
        this.pnames[13] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ名14をセットする
     *
     * @param value
     */
    public void setPname15(String value) {
        this.pnames[14] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値１をセットする
     *
     * @param value
     */
    public void setPvalue1(String value) {
        this.pvalues[0] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値2をセットする
     *
     * @param value
     */
    public void setPvalue2(String value) {
        this.pvalues[1] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値3をセットする
     *
     * @param value
     */
    public void setPvalue3(String value) {
        this.pvalues[2] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値4をセットする
     *
     * @param value
     */
    public void setPvalue4(String value) {
        this.pvalues[3] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値5をセットする
     *
     * @param value
     */
    public void setPvalue5(String value) {
        this.pvalues[4] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値6をセットする
     *
     * @param value
     */
    public void setPvalue6(String value) {
        this.pvalues[5] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値7をセットする
     *
     * @param value
     */
    public void setPvalue7(String value) {
        this.pvalues[6] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値8をセットする
     *
     * @param value
     */
    public void setPvalue8(String value) {
        this.pvalues[7] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値9をセットする
     *
     * @param value
     */
    public void setPvalue9(String value) {
        this.pvalues[8] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値10をセットする
     *
     * @param value
     */
    public void setPvalue10(String value) {
        this.pvalues[9] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値7をセットする
     *
     * @param value
     */
    public void setPvalue11(String value) {
        this.pvalues[10] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値8をセットする
     *
     * @param value
     */
    public void setPvalue12(String value) {
        this.pvalues[11] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値9をセットする
     *
     * @param value
     */
    public void setPvalue13(String value) {
        this.pvalues[12] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値10をセットする
     *
     * @param value
     */
    public void setPvalue14(String value) {
        this.pvalues[13] = value;
    }

    /**
     * InputHelpに引き渡したいパラメータ値10をセットする
     *
     * @param value
     */
    public void setPvalue15(String value) {
        this.pvalues[14] = value;
    }

    /**
     * name属性をセットする
     *
     * @param name
     */
    public void setListname(String name) {
        this.listname = name;
    }

    /**
     * Widthをセットする
     *
     * @param value
     */
    public void setWidth(String value) {
        this.width = value;
    }

    /**
     * Heightをセットする
     *
     * @param value
     */
    public void setHeight(String value) {
        this.height = value;
    }

    /**
     * Topをセットする
     *
     * @param value
     */
    public void setTop(String value) {
        this.top = value;
    }

    /**
     * Leftをセットする
     *
     * @param value
     */
    public void setLeft(String value) {
        this.left = value;
    }

    public void setIndex(String value) {
        this.index = value;
    }
    
    public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}
    
    /**
     * オーバーライド
     *
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     * InputHelpを別スクリーンで開くためのJavaScriptを生成し、OnClickにセット
     */
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {

        //スタイルシートの設定
        setCssClass("ui-button-roupe");

        setOnclick(createScript());
        
        super.writeTagContent(tagWriter); //To change body of generated methods, choose Tools | Templates.
        
        tagWriter.startTag("div");
        writeOptionalAttribute(tagWriter, "class", "ui-icon-roupe");
        tagWriter.endTag();
        
        return EVAL_BODY_INCLUDE;
    }

    private String createScript() throws JspException {

        //InputHelp画面をオープンするJavaScriptの生成
        StringBuilder buf = new StringBuilder("");

        for (int i = 0; i < pnames.length; i++) {

            if (pnames[i] == null || pvalues[i] == null) {
                continue;
            }

            buf.append("var pValue");
            buf.append(String.valueOf(i));
            buf.append(" = document.getElementById('");
            buf.append(pvalues[i]);
            buf.append("').value;");
        }

        buf.append("var query;");

        for (int i = 0; i < pnames.length; i++) {

            if (pnames[i] == null || pvalues[i] == null) {
                continue;
            }

            buf.append("if ( pValue");
            buf.append(String.valueOf(i));
            buf.append(" !== null && pValue");
            buf.append(String.valueOf(i));
            buf.append(" !== undefined");
            buf.append(" ){ ");
            buf.append(" query += \"&");
            buf.append(pnames[i]);
            buf.append("=");
            buf.append("\" + pValue");
            buf.append(String.valueOf(i));
            buf.append(";}");
        }

        buf.append("ihwindow = window.open('");
        String targetUrl = ((HttpServletRequest) (this.pageContext.getRequest())).getContextPath() + url;
        buf.append(targetUrl);

        buf.append("?");
        
        String token = null;
        HttpSession session = ((HttpServletRequest) (this.pageContext.getRequest())).getSession();
        if (session != null) {
        	token = (String) session.getAttribute("com.pigthinkingtec.framework.spring.mvc.token.TokenProcessor@token");
        	buf.append("token=" + token + "&");
        }
       
        for (int i = 0; i < requestParamNames.length; i++) {
            if ((cols[i] != null) && !(cols[i].equals(""))) {
                buf.append(requestParamNames[i]);
                buf.append("=");

                //明細行の場合は戻し先の部品のインデックスを考慮
                if (index != null) {
                    buf.append(listname);
                    buf.append("[");
                    buf.append(index);
                    buf.append("]");
                    buf.append(".");
                }

                buf.append(cols[i]);
                buf.append("&");
            }
        }
        
        buf.append("' + query + '");
        buf.append("&init=true");
        buf.append("&parentFormName=");
        buf.append(TagUtil.getFormTag(this).getModelAttribute());
        buf.append("','");
        
        buf.append(getScreenId());

        buf.append("','toolbar");
        buf.append("=no");
        buf.append(",scrollbars");
        buf.append("=yes");
        buf.append(",status");
        buf.append("=yes");
        buf.append(",width=");
        buf.append(width);
        buf.append(",height=");
        buf.append(height);
        buf.append(",top=");
        buf.append(top);
        buf.append(",left=");
        buf.append(left);
		buf.append(",resizable=");
		buf.append(resizable);
        buf.append(",dependent=yes');ihwindow.focus();");

        return buf.toString();
    }
}
