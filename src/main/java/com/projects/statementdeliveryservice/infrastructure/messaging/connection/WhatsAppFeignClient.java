package com.projects.statementdeliveryservice.infrastructure.messaging.connection;

import com.projects.statementdeliveryservice.infrastructure.messaging.dto.SendMediaRequest;
import com.projects.statementdeliveryservice.infrastructure.messaging.dto.SendTextRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "messageAppClient", url = "${whatsapp.api.url}")
public interface WhatsAppFeignClient {

    @PostMapping(value = "/message/sendMedia/{instance}", consumes = "application/json")
    void sendMedia(
            @PathVariable("instance") String instance,
            @RequestHeader("apikey") String apiKey,
            @RequestBody SendMediaRequest request
    );

    @PostMapping(value = "/message/sendText/{instance}", consumes = "application/json")
    void sendText(
            @PathVariable("instance") String instance,
            @RequestHeader("apikey") String apiKey,
            @RequestBody SendTextRequest request
    );
}
//test