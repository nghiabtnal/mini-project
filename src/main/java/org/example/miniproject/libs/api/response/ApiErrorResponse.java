/*
 * @(#)ApiErrorResponse.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.api.response;

import lombok.Data;
import org.example.miniproject.libs.api.foundation.Message;

import java.io.Serializable;
import java.util.List;

@Data
public class ApiErrorResponse implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 2534751261673384421L;

    // the type value of error
    public static final String ERROR_VALIDATION = "validation";
    public static final String ERROR_SERVICE = "service";

    /** the error type */
    private String error;

    /** the messages */
    private List<Message> messages;
}
