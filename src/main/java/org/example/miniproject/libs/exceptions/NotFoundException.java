/*
 * @(#)NotFoundException.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.exceptions;

public class NotFoundException extends SystemException {

    private static final long serialVersionUID = -2786804378181105154L;

    public NotFoundException() {
        super("Not Found");
    }

}
