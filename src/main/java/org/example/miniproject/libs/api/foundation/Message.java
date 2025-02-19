/*
 * @(#)ResolvedMessageImpl.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.api.foundation;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -2744139326899137224L;

    /** the message code */
    private String code;

    /** the arguments of message */
    private transient Object[] arguments;

    /** the message body */
    @JsonProperty("message")
    private String body;

    /** the field name of the target */
    private String targetField;

    /** the row number for import error */
    private Integer row;

    /** the col number for import error */
    private Integer col;

    /**
     * the default constructor.
     */
    public Message() {
    }

    /**
     * the constructor.
     *
     * @param code      the message code
     * @param arguments the message arguments
     */
    public Message(String code, Object... arguments) {
        this.code = code;
        this.arguments = arguments;
    }
}
