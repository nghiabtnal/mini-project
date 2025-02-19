/*
 * @(#)Number.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.miniproject.libs.validator.impl.NumericValidator;

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
 * The element must be a number string,empty is considered valid.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { NumericValidator.class })
public @interface Numeric {

    String message() default "{cn.com.zensho.gzeos.api.fw.validator.Numeric.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link Numeric} annotations on the same element.
     *
     * @see cn.com.zensho.gzeos.api.fw.validator.Numeric
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Numeric[] value();
    }
}
