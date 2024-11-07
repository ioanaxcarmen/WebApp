package edu.fra.uas.beanexample;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.fra.uas.controller.BeanController;
import edu.fra.uas.service.MessageService;

@SpringBootTest
public class ControllerTest {
    
    @Autowired
    private BeanController beanController;

    @Autowired
    public MessageService messageService;

    @Test
    void testController() {
        assertThat(beanController.putMessage("Das ist ein Test")).isEqualTo("Das ist ein Test");
    }

    @Test
    void testCounter() {
        assertThat(messageService.increment(2)).isEqualTo(3);
    }
}
