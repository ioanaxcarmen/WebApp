package edu.fra.uas.service;

import org.springframework.stereotype.Component;

@Component
public class MessageService {

    int counter;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int increment(int counter){
        counter += 1;
        return counter;
    }
}
