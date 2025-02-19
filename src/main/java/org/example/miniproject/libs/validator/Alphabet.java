/*
 * @(#)Alphabet.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.miniproject.libs.validator.impl.AlphabetValidator;

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
 * The element must be an alphabet string,empty is considered valid.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { AlphabetValidator.class })
public @interface Alphabet {

    String message() default "{cn.com.zensho.gzeos.api.fw.validator.Alphabet.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link Alphabet} annotations on the same element.
     *
     * @see cn.com.zensho.gzeos.api.fw.validator.Alphabet
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Alphabet[] value();
    }
}
