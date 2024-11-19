package edu.fra.uas.v3autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Apprentice {

    @Autowired
    @Qualifier("pleaseBuild")
    Work work;

    public static final Logger LOGGER = LoggerFactory.getLogger(Apprentice.class);

    public void performWork() {
        LOGGER.info("Apprentice-Klasse wurde verwendet");
        work.doWork();
    }

}
