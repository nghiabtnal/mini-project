/*
 * @(#)MessageManager.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.services;

import java.util.List;
import java.util.Locale;

import org.example.miniproject.libs.api.foundation.Message;
import org.springframework.validation.ObjectError;

public interface MessageService {

    /**
     * Try to resolve the message.
     * 
     * @param code the code to lookup up, such as 'calculator.noRateSet'
     * @param args an array of arguments that will be filled in
     * @return the resolved message
     * @see java.text.MessageFormat
     */
    String getMessage(String code, Object... args);

    /**
     * Try to resolve the message.
     * 
     * 
     * @param locale the locale in which to do the lookup
     * @param code   the code to lookup up, such as 'calculator.noRateSet'
     * @param args   an array of arguments that will be filled in
     * @return the resolved message
     * @see java.text.MessageFormat
     */
    String getMessage(Locale locale, String code, Object... args);

    /**
     * Try to resolve the message using all the attributes contained within the
     * {@code MessageSourceResolvable} argument that was passed in.
     * 
     * @param message the {@linkplain Message} list
     * @return the {@linkplain Message}
     * @see java.text.MessageFormat
     */
    void resolveMessageBody(List<Message> message);

    /**
     * convert the {@linkplain ObjectError} list to the {@linkplain Message} list.
     * 
     * @param error  the {@linkplain ObjectError} list
     * @return the {@linkplain Message} list
     */
    List<Message> convertObjectErrors(List<ObjectError> error);

}
