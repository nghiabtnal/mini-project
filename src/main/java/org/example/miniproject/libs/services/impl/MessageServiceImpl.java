package org.example.miniproject.libs.services.impl;

import jakarta.annotation.Resource;
import org.example.miniproject.libs.api.foundation.Message;
import org.example.miniproject.libs.services.MessageService;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MessageServiceImpl implements MessageService {

    /**
     * Spring messageSource
     */
    @Resource
    private MessageSource messageSource;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(String code, Object... args) {
        return getMessage(LocaleContextHolder.getLocale(), code, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, code, locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolveMessageBody(List<Message> messages) {

        if (messages == null) {
            return;
        }

        for (Message message : messages) {
            if (message.getBody() == null) {
                message.setBody(getMessage(message.getCode(), message.getArguments()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> convertObjectErrors(List<ObjectError> errors) {

        List<Message> messages = new ArrayList<>();

        if (errors == null) {
            return messages;
        }

        Locale locale = LocaleContextHolder.getLocale();
        for (ObjectError error : errors) {
            Message message = new Message(getCode(error), getArguments(locale, error));
            message.setTargetField(getField(error));
            message.setBody(getMessage(locale, error));
            messages.add(message);
        }

        return messages;
    }

    /**
     * get message code from {@code MessageSourceResolvable}.
     *
     * @param resolvable {@code MessageSourceResolvable}
     * @return message code
     */
    protected String getCode(MessageSourceResolvable resolvable) {
        String[] codes = resolvable.getCodes();
        return (codes != null && codes.length > 0) ? codes[codes.length - 1] : null;
    }

    /**
     * get Messages Arguments from {@code MessageSourceResolvable}.
     *
     *
     * @param locale     locale
     * @param resolvable {@code MessageSourceResolvable}
     * @return Messages Arguments
     */
    protected Object[] getArguments(Locale locale, MessageSourceResolvable resolvable) {

        // when FieldError or ObjectError
        // the type of arguments is also a MessageSourceResolvable,
        // here should be resolve message deeply.
        if (resolvable instanceof ObjectError) {

            Object[] targets = resolvable.getArguments();
            if (targets == null) {
                return new Object[0];
            }

            Object[] args = new Object[targets.length];
            for (int i = 0; i < targets.length; i++) {
                if (targets[i] instanceof MessageSourceResolvable) {
                    args[i] = getMessage(locale, (MessageSourceResolvable) targets[i]);
                } else {
                    args[i] = targets[i];
                }
            }

            return args;
        }

        return resolvable.getArguments();
    }

    /**
     * get field Name from {@code ObjectError} Or {@code FieldError}
     *
     * @param resolvable {@code ObjectError}
     * @return Messages Arguments
     */
    protected String getField(ObjectError resolvable) {

        if (resolvable instanceof FieldError) {
            return ((FieldError) resolvable).getField();
        }

        return resolvable.getObjectName();
    }

    /**
     * Try to resolve the message using all the attributes contained within the
     * {@code MessageSourceResolvable} argument that was passed in.
     *
     * @param resolvable the value object storing attributes required to resolve a message
     * @param locale     the locale in which to do the lookup
     * @return the resolved message
     * @see org.springframework.context.MessageSource#getMessage
     */
    protected String getMessage(Locale locale, MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, locale);
    }
}
