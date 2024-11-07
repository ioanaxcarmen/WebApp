package edu.fra.uas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class MessageService {
private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private String message;

    public String getMessage() {
        log.debug("Die Methode getMessage wurde aufgerufen.");
        return message;
    }

    public void setMessage(String message) {
        log.debug("Die Methode setMessage wurde aufgerufen.");
        this.message = message;
    }

}
