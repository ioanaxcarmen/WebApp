package edu.fra.uas.v3autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("pleaseBuild")
public class Building implements Work {

    private static final Logger LOGGER = LoggerFactory.getLogger(Building.class);

    @Override
    public void doWork() {
        LOGGER.info("--> adding bricks");

    }

}
