/*
 * @(#)AlphanumericValidator.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.miniproject.libs.validator.Alphanumeric;
import org.springframework.util.StringUtils;

/**
 * AlphanumericValidator.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, CharSequence> {

    /**
     * Regular expression for Alphanumeric.
     */
    private static final Pattern ALPHANUMERIC_REGEXP = Pattern.compile("^[A-Za-z0-9]+$");

    @Override
    public void initialize(Alphanumeric constraintAnnotation) {
        // initialize
    }

    /**
     * validate whether The element is an alphabet or numeric string.
     * 
     * @param value   input item
     * @param context in which the constraint is evaluated.
     * @return true when the element is empty or an alphabet or numeric string,false otherwise.
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        Matcher matcher = ALPHANUMERIC_REGEXP.matcher(value);
        return matcher.matches();
    }

}
