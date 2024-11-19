package edu.fra.uas.v3autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Journeyman {
    @Autowired
    Apprentice apprentice;

    public void performWork() {
        apprentice.performWork();
    }
}
