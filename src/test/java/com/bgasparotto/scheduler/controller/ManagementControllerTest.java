package com.bgasparotto.scheduler.controller;

import com.bgasparotto.scheduler.message.RunUpdate;
import com.bgasparotto.scheduler.messaging.producer.RunUpdateProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ManagementController.class)
public class ManagementControllerTest {

    @MockBean
    private RunUpdateProducer producer;

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldTriggerUpdateMessageAndReturn200WhenGetIsCalled() throws Exception {
        mvc.perform(get("/management/trigger-update-now"))
                .andExpect(status().isOk());

        verify(producer).produce(any(RunUpdate.class));
    }
}
