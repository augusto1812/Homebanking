package com.santander.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageSource message;

    public String getMessage(String msg) {
        return message.getMessage(msg, null, LocaleContextHolder.getLocale());
    }
}
