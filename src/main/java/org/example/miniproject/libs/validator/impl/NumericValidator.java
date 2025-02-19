/*
 * @(#)NumberValidator.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.miniproject.libs.validator.Numeric;
import org.springframework.util.StringUtils;


/**
 * NumberValidator.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
public class NumericValidator implements ConstraintValidator<Numeric, CharSequence> {

    /**
     * Regular expression for number.
     */
    private static final Pattern NUMBER_REGEXP = Pattern.compile("^[0-9]+$");

    @Override
    public void initialize(Numeric constraintAnnotation) {
        // initialize
    }

    /**
     * validate whether the element is a number string
     * 

     * @param value   input item
     * @param context in which the constraint is evaluated.
     * @return true when value is empty or a number string,false otherwise.
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // when value is empty return false
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // return true when value is a number string,false otherwise
        Matcher matcher = NUMBER_REGEXP.matcher(value);
        return matcher.matches();
    }

}
