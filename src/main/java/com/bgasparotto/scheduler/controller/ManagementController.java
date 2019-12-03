package com.bgasparotto.scheduler.controller;

import com.bgasparotto.scheduler.message.RunUpdate;
import com.bgasparotto.scheduler.messaging.producer.RunUpdateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/management")
@RequiredArgsConstructor
public class ManagementController {
    private final RunUpdateProducer producer;

    @GetMapping(value = "/trigger-update-now")
    public void triggerUpdateNow() {
        RunUpdate message = RunUpdate.newBuilder()
                .setDescription("Manually triggered Hansard update from the API")
                .build();
        this.producer.produce(message);
    }
}
