/*
 * @(#)DateStringValidator.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */

package org.example.miniproject.libs.validator.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.miniproject.libs.validator.DateString;
import org.springframework.util.StringUtils;

/**
 * DateStringValidator.
 * 
 * @author xibin.zhang
 * @version 1.0 2020/12/08 create
 */
public class DateStringValidator implements ConstraintValidator<DateString, String> {
    /**
     * Defines the date format.
     */
    private DateTimeFormatter fomatter;

    /**
     *
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(DateString dateFormat) {
        // Get the format of a string date.
        this.fomatter = DateTimeFormatter.ofPattern(dateFormat.format());
    }

    /**
     * validate whether the element is a date string at specified date {@code format}
     *
     * @see javax.validation.ConstraintValidator#isValid(Object,
     *      javax.validation.ConstraintValidatorContext)
     * @param value   input item.
     * @param context from ConstraintValidatorContext.
     * @return true when value is empty or a date string at specified date {@code format}.false
     *         otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            // The input item is blank.
            if (StringUtils.isEmpty(value)) {
                return true;
            }
            return value.equals(LocalDate.parse(value, fomatter).format(fomatter));
        } catch (DateTimeParseException e) {
            return false;
        }

    }
}
