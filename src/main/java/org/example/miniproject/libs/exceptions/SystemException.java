/*
 * @(#)SystemException.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.exceptions;

/**
 * An exception class used to handle system error.
 *
 * @author xibin.zhang
 * @version 1.0 2020/08/27 create
 */
public class SystemException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5113185600484702232L;

    /**
     * Constructs a new system exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * Constructs a new system exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new system exception with the specified cause.
     *
     * @param cause the cause
     */
    public SystemException(Throwable cause) {
        super(cause);
    }
}
