/*
 * @(#)ServiceException.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.exceptions;

import java.util.Arrays;
import java.util.List;

import org.example.miniproject.libs.api.foundation.Message;
import org.example.miniproject.libs.utils.JsonUtils;

public class ServiceException extends SystemException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4317008216260142438L;

    /**
     * the {@linkplain Message} List
     */
    private final List<Message> messages;

    public ServiceException(Throwable e, List<Message> messages) {
        super(resolveMessages(messages), e);
        this.messages = messages;
    }

    /**
     * create a new service exception with original exception.
     * 
     * @param e        original exception
     * @param messages array of {@linkplain Message}
     */
    public ServiceException(Throwable e, Message... messages) {
        this(e, Arrays.asList(messages));
    }

    /**
     * create a new actually business exception.
     * 
     * @param message array of {@linkplain Message}
     */
    public ServiceException(Message... message) {
        this(null, message);
    }

    /**
     * get the message array.
     * 
     * @return message array
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * resolve {@linkplain Message} to Exception Message String.
     * 
     * @param messages {@code Message}
     * @return Exception Message String
     */
    private static String resolveMessages(List<Message> messages) {
        if (messages != null) {
            return JsonUtils.serializeQuietly(messages);
        } else {
            return "Business Exception.";
        }
    }
}
