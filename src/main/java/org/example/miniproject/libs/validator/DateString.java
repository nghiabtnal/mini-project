/*
 * @(#)DateString.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */

package org.example.miniproject.libs.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.miniproject.libs.validator.impl.DateStringValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * The element must be a date string at specified date {@code format},empty is considered valid.
 * 
 * @author xibin.zhang
 * @version 1.0 2020/12/08 create
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { DateStringValidator.class })
public @interface DateString {

    String message() default "{cn.com.zensho.gzeos.api.fw.validator.DateString.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return the date format
     */
    String format() default "yyyy-MM-dd";

    /**
     * Defines several {@link DateString} annotations on the same element.
     *
     * @see com.fastretailing.common.validator.DateString
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        DateString[] value();
    }
}
