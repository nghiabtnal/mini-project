/*
 * @(#)ValidateException.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.exceptions;

import org.springframework.validation.BeanPropertyBindingResult;

/**
 * An exception class used to handle user validate error.
 * 
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
public class ValidateException extends SystemException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1938981662736028931L;

    /**
     * BeanPropertyBindingResult
     */
    private final BeanPropertyBindingResult bindingResult;

    /**
     * create a new Validate exception with original exception.
     */
    public ValidateException(BeanPropertyBindingResult result) {
        super("a ValidateException occured.");
        this.bindingResult = result;
    }

    /**
     * Return the results of the failed validation.
     */
    public BeanPropertyBindingResult getBindingResult() {
        return this.bindingResult;
    }
}
