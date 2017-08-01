package com.pigthinkingtec.framework.spring.mvc;

import com.pigthinkingtec.framework.SystemConstant;
import com.pigthinkingtec.framework.databean.DataBeanInterface;
import com.pigthinkingtec.framework.databean.OnlineOrder;
import com.pigthinkingtec.framework.databean.OnlineReport;
import com.pigthinkingtec.framework.databean.UserContainer;
import com.pigthinkingtec.framework.databean.message.BusinessDialogs;
import com.pigthinkingtec.framework.databean.message.BusinessError;
import com.pigthinkingtec.framework.databean.message.BusinessErrors;
import com.pigthinkingtec.framework.databean.message.BusinessInformation;
import com.pigthinkingtec.framework.databean.message.BusinessInformations;
import com.pigthinkingtec.framework.databean.message.BusinessMessage;
import com.pigthinkingtec.framework.databean.message.BusinessMessages;
import com.pigthinkingtec.framework.databean.message.BusinessWarning;
import com.pigthinkingtec.framework.databean.message.BusinessWarnings;
import com.pigthinkingtec.framework.databean.message.MessageContainer;
import com.pigthinkingtec.framework.databean.message.MessageType;
import com.pigthinkingtec.framework.exception.SystemException;
import com.pigthinkingtec.framework.service.ServiceInterface;
import com.pigthinkingtec.framework.spring.mvc.download.DownloadFileType;
import com.pigthinkingtec.framework.util.DateUtils;
import com.pigthinkingtec.framework.util.FwPropertyReader;
import com.pigthinkingtec.framework.util.UserUtil;
import com.pigthinkingtec.framework.validator.DateFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * SpringWebMVCのコントローラ作成時に基底とするクラス
 * 
 * @author yizhou
 */
public abstract class AbstractController {

	private final static Logger logger = LoggerFactory.getLogger(AbstractController.class);

	/* Validation ErrorメッセージID */
	private static String MESSAGEID_VALIDATION_ERROR = "";

	static {
		/* Validation ErrorメッセージID */
		// Batch,Online共に使えるようにするために、PropertiesUtilではなく、FwPropertyReaderを使うようにする
		MESSAGEID_VALIDATION_ERROR = FwPropertyReader.getProperty(SystemConstant.MESSAGEID_VALIDATION_ERROR_KEY, null);
	}

	/**
	 * 
	 * @param request
	 * @param form
	 * @param model
	 * @param service
	 * @param dataBean
	 * @param scope
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected OnlineReport executeService(HttpServletRequest request, AbstractForm form, Model model,
			ServiceInterface service, DataBeanInterface dataBean, int scope) throws SystemException {

		// ビジネスロジック層に渡すInputデータオブジェクト
		OnlineOrder order = new OnlineOrder();

		// Formの内容をデータビーンにセット
		convertBean(dataBean, form);

		HttpSession session = request.getSession(false);

		UserContainer user = null;
		String sessionId = null;

		if (session != null) {
			sessionId = session.getId();
			user = (UserContainer) session.getAttribute(SystemConstant.USER_CONTAINER);
			if (user == null) {
				user = UserUtil.getUserContainer();
			}
		} else {
			user = UserUtil.getUserContainer();
		}

		// Beanの日付項目を内部形式に変換
		changeToInnerDateFormat(dataBean, form, user);

		// Serviceを呼び出すためにOrderオブジェクトに必要な情報をセット
		order.setInputDataBean(dataBean);
		order.setTransactionScope(scope);
		order.setLoginUser(user);
		order.setSessionId(sessionId);
		// Serviceの呼び出し処理
		service.setOrder(order);
		service.execute();
		OnlineReport report = (OnlineReport) service.getReport();

		DataBeanInterface outputBean = report.getOutputDataBean();

		// formの値を内部形式から外部形式に変換
		changeFromInnerDateFormat(outputBean, form, user);

		// Serviceを呼び出した結果を、Formオブジェクトへセット
		convertBean(form, outputBean);

		if (session == null) {
			session = request.getSession(true);
		}
		session.setAttribute(SystemConstant.USER_CONTAINER, user);

		// メッセージに関する処理
		// ビジネスロジック層の処理結果から、メッセージオブジェクトを取得
		MessageContainer msgContainer = report.getMessageContainer();

		if (msgContainer != null) {
			// エラーメッセージグループを取得
			BusinessErrors errors = msgContainer.getErrors();
			// ワーニングメッセージグループを取得
			BusinessWarnings warns = msgContainer.getWarnings();
			// インフォメーションメッセージグループを取得
			BusinessInformations infos = msgContainer.getInformations();
			// ダイアログメッセージグループを取得
			BusinessDialogs dialogs = msgContainer.getDialogs();
			// エラーメッセージが存在した場合は、モデルオブジェクトにセット
			if (errors != null && errors.hasMessage()) {
				BusinessErrors bErrors = (BusinessErrors) session.getAttribute("error");
				if (bErrors == null) {
					session.setAttribute("error", errors);
				} else {
					bErrors.getErrors().putAll(errors.getMessages());
				}
				// model.addAttribute("error", errors);
			}
			// ワーニングメッセージが存在した場合は、モデルオブジェクトにセット
			if (warns != null && warns.hasMessage()) {
				BusinessWarnings bWarns = (BusinessWarnings) session.getAttribute("warn");
				if (bWarns == null) {
					session.setAttribute("warn", warns);
				} else {
					bWarns.getMessages().putAll(warns.getMessages());
				}
				// model.addAttribute("warn", warns);
			}
			// インフォメーションメッセージが存在した場合は、モデルオブジェクトにセット
			if (infos != null && infos.hasMessage()) {
				BusinessInformations bInfos = (BusinessInformations) session.getAttribute("info");
				if (bInfos == null) {
					session.setAttribute("info", infos);
				} else {
					bInfos.getMessages().putAll(infos.getMessages());
				}
				// model.addAttribute("info", infos);
			}

			// ダイアログメッセージが存在した場合は、モデルオブジェクトにセット
			if (dialogs != null && dialogs.hasMessage()) {
				BusinessDialogs bDialogs = (BusinessDialogs) session.getAttribute("dialog");
				if (bDialogs == null) {
					session.setAttribute("dialog", dialogs);
				} else {
					bDialogs.getMessages().putAll(dialogs.getMessages());
				}
				// model.addAttribute("info", infos);
			}

		}
		return report;
	}

	/**
	 * オブジェクトのプロパティコピーを行うメソッド
	 * 
	 * @param target
	 * @param source
	 * @throws SystemException
	 */
	private void convertBean(Object target, Object source) {
		BeanUtils.copyProperties(source, target);
	}

	/**
	 * DataBeanの日付項目を内部形式(YYYYMMDD)に変換するメソッド
	 * 
	 * @param dataBean
	 * @param form
	 * @param user
	 * @throws SystemException
	 */
	private void changeToInnerDateFormat(DataBeanInterface dataBean, AbstractForm form, UserContainer user)
			throws SystemException {
		// フィールドでループ
		for (Field formField : form.getClass().getDeclaredFields()) {
			// @DateFormatアノテーションがついたFormのフィールドに対応するBeanのフィールドを書きかえ
			for (Annotation annot : formField.getAnnotations()) {
				if (annot instanceof DateFormat) {
					try {
						DateFormat dateAnnot = (DateFormat) annot;
						Field beanField = dataBean.getClass().getDeclaredField(formField.getName());
						// privateフィールドも強制的に読み込み可に設定
						beanField.setAccessible(true);

						// beanのDate情報をフォーマット情報を元に修正
						String dateValue = (String) beanField.get(dataBean);

						String dateFormat = user.getDateFormat();
						if (dateFormat == null) {
							dateFormat = SystemConstant.DATEFORMAT_YYYYMMDD;
						}

						if (dateValue != null) {
							dateValue = DateUtils.changeToInnerFormat(dateValue, dateFormat, dateAnnot.format());
						}

						beanField.set(dataBean, dateValue);
					} catch (NoSuchFieldException fieldException) {
						// formに対応するBeanのフィールドがない場合、何もしない
						logger.warn(fieldException.getMessage());
					} catch (IllegalArgumentException argException) {
						// 基本的には発生しないエラー
						logger.error(argException.getMessage());
						throw new SystemException(argException);
					} catch (IllegalAccessException accessException) {
						// 基本的には発生しないエラー
						logger.error(accessException.getMessage());
						throw new SystemException(accessException);
					} catch (IndexOutOfBoundsException indexException) {
						// 変換に失敗した場合、そのまま戻す
						logger.warn(indexException.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Formの内部形式の日付データを外部形式に変換するメソッド
	 * 
	 * @param form
	 * @param user
	 * @throws SystemException
	 */
	private void changeFromInnerDateFormat(DataBeanInterface dataBean, AbstractForm form, UserContainer user)
			throws SystemException {
		// フィールドでループ
		for (Field formField : form.getClass().getDeclaredFields()) {
			// @DateFormatアノテーションがついたFormのフィールドに対応するBeanのフィールドを書きかえ
			for (Annotation annot : formField.getAnnotations()) {
				if (annot instanceof DateFormat) {
					try {
						DateFormat dateAnnot = (DateFormat) annot;
						Field beanField = dataBean.getClass().getDeclaredField(formField.getName());
						// privateフィールドも強制的に読み込み可に設定
						beanField.setAccessible(true);

						// beanのDate情報をフォーマット情報を元に修正
						String dateValue = (String) beanField.get(dataBean);

						String dateFormat = user.getDateFormat();
						if (dateFormat == null) {
							dateFormat = SystemConstant.DATEFORMAT_YYYYMMDD;
						}

						if (dateValue != null) {
							dateValue = DateUtils.changeFromInnerFormat(dateValue, dateFormat, dateAnnot.format());
						}
						beanField.set(dataBean, dateValue);
					} catch (NoSuchFieldException fieldException) {
						// formに対応するBeanのフィールドがない場合、何もしない
						logger.warn(fieldException.getMessage());
					} catch (IllegalArgumentException argException) {
						// 基本的には発生しないエラー
						logger.error(argException.getMessage());
						throw new SystemException(argException);
					} catch (IllegalAccessException accessException) {
						// 基本的には発生しないエラー
						logger.error(accessException.getMessage());
						throw new SystemException(accessException);
					} catch (IndexOutOfBoundsException indexException) {
						// 変換に失敗した場合、そのまま戻す
						logger.warn(indexException.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Redirect用のDownloadを行うためにフレームワークへ処理を依頼するメソッド
	 * 
	 * @param request
	 * @param file
	 * @param fileName
	 * @param fileType
	 */
	protected void preparedDownload(HttpServletRequest request, Object file, Object fileName,
			DownloadFileType fileType) {
		HttpSession session = request.getSession(false);
		session.setAttribute("File", file);
		session.setAttribute("FileName", fileName);
		session.setAttribute("FileType", fileType.getType());
	}

	/**
	 * SessionからFormオブジェクトを削除するメソッド
	 * 
	 * @param request
	 */
	protected void clearFormFromSession(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session != null) {

			// Enumration<String> で実装するとConcurrentModificationExceptionが発生するため、
			// CopyOnWriteArrayListに入れ替える
			CopyOnWriteArrayList<String> enumSession = new CopyOnWriteArrayList<>(
					Collections.list(session.getAttributeNames()));

			Object obj = null;
			for (String attributeName : enumSession) {
				obj = session.getAttribute(attributeName);
				if (obj instanceof AbstractForm) {
					session.removeAttribute(attributeName);
				}
			}
		}
	}

	/**
	 * Messageをモデルにセットするメソッド
	 * 
	 * @param messageType
	 * @param model
	 * @param key
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	protected final void saveMessage(MessageType messageType, Model model, String key, String... args) {

		BusinessMessage message;
		String messageTypeStr = messageType.toString();
		switch (messageType) {
		case Error:
			message = new BusinessError(key, args);
			break;
		case Infomation:
			message = new BusinessInformation(key, args);
			break;
		case Warning:
			message = new BusinessWarning(key, args);
			break;
		default:
			return;
		}

		BusinessMessages messages = (BusinessMessages) (model.asMap().get(messageType.toString()));
		if (messages == null) {
			switch (messageType) {
			case Error:
				messages = new BusinessErrors();
				break;
			case Infomation:
				messages = new BusinessInformations();
				break;
			case Warning:
				messages = new BusinessWarnings();
				break;
			default:
				messages = new BusinessErrors();
			}
			messages.getMessages().put(messageTypeStr + message.hashCode(), message);
			model.addAttribute(messageTypeStr, messages);
		} else {
			messages.getMessages().put(messageTypeStr + message.hashCode(), message);
		}
	}

	/**
	 * Validatorでエラーが発生したかをチェックするメソッド
	 * 
	 * @param result
	 * @param model
	 * @return
	 */
	protected final boolean hasError(BindingResult result, Model model) {
		if (result.hasErrors()) {
			saveMessage(MessageType.Error, model, MESSAGEID_VALIDATION_ERROR);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 機能間遷移時に遷移もとで引継ぎ情報を設定するために使用するメソッド
	 * 
	 * @param key
	 *            引継ぎキー
	 * @param value
	 *            引継ぎ値
	 * @param request
	 *            リクエストオブジェクト
	 */
	protected final void setTakeOverInfo(String key, String value, HttpServletRequest request) {
		UserContainer userInfo = getUserContainer(request);
		userInfo.setTakeOverInfo(key, value);
	}

	/**
	 * ユーザコンテナを取得するメソッド
	 * 
	 * @param request
	 *            リクエスト
	 * @return UserContainerオブジェクト
	 */
	protected final UserContainer getUserContainer(HttpServletRequest request) {
		UserContainer user = (UserContainer) request.getSession(true).getAttribute(SystemConstant.USER_CONTAINER);
		if (user == null) {
			user = UserUtil.getUserContainer();
		}

		return user;
	}

	/**
	 * 機能間遷移時に遷移先で引継ぎ情報を取得するために使用するメソッド
	 * 
	 * @param key
	 *            引継ぎキー
	 * @param request
	 *            リクエスト
	 * @return 引継ぎ値
	 */
	protected final String getTakeOverInfo(String key, HttpServletRequest request) {
		return getTakeOverInfo(key, true, request);
	}

	/**
	 * 機能間遷移時に遷移先で引継ぎ情報を取得するために使用するメソッド
	 * 
	 * @param key
	 *            引継ぎキー
	 * @param removeFlg
	 *            引継ぎ値を取得後に引継ぎ情報を格納先から削除するかどうか
	 * @param request
	 *            リクエスト
	 * @return 引継ぎ値
	 */
	protected final String getTakeOverInfo(String key, boolean removeFlg, HttpServletRequest request) {
		UserContainer userInfo = getUserContainer(request);
		String value = userInfo.getTakeOverInfo(key);
		if (removeFlg) {
			userInfo.removeTakeOverInfo(key);
		}
		return value;
	}

	/**
	 * 1画面で複数の名称を出力する必要がある際に使用する画面判断用のIDを設定するメソッド
	 * 
	 * @param form
	 *            Formオブジェクト
	 * @param screenPattern
	 *            画面パターンID（画面名を判断するID）
	 */
	protected void setScreenPattern(AbstractForm form, String screenPattern) {
		form.setScreenPattern(screenPattern);
	}

}
