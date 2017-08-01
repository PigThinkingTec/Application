package com.pigthinkingtec.framework.spring.mvc.token;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tokenチェックを行わないクラスまたはメソッドに記述する
 *
 * @author yizhou
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotCheckToken {
}
