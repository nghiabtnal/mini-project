/*
 * @(#)AlphabetValidator.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.validator.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.miniproject.libs.validator.Alphabet;
import org.springframework.util.StringUtils;

/**
 * AlphabetValidator.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
public class AlphabetValidator implements ConstraintValidator<Alphabet, CharSequence> {

    /**
     * Regular expression for alphabet.
     */
    private static final Pattern ALPHABET_REGEXP = Pattern.compile("^[A-Za-z]+$");

    @Override
    public void initialize(Alphabet constraintAnnotation) {
        // initialize
    }

    /**
     * validate whether the element is an alphabet string.
     *
     * @param value   input item
     * @param context in which the constraint is evaluated.
     * @return true when value is empty or an alphabet string,false otherwise.
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // when empty return true
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // validate whether the value is an alphabet string
        Matcher matcher = ALPHABET_REGEXP.matcher(value);
        return matcher.matches();
    }

}
