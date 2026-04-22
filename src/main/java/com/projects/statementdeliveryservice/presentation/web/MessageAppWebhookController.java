package com.projects.statementdeliveryservice.presentation.web;

import com.projects.statementdeliveryservice.application.usecases.RequestStatementOnDemandUseCase;
import com.projects.statementdeliveryservice.domain.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook/whatsapp")
public class MessageAppWebhookController {

    @Value("${spring.mail.username}")
    private static String MAIL_USERNAME;

    @Value("${USER_PHONE}")
    private String USER_PHONE;

    private final RequestStatementOnDemandUseCase onDemandUseCase;

    public MessageAppWebhookController(RequestStatementOnDemandUseCase onDemandUseCase) {
        this.onDemandUseCase = onDemandUseCase;
    }

    @PostMapping("/receive")
    public ResponseEntity<Void> receiveMessage(@RequestBody Map<String, Object> payload) {

        String incomingMessage = extractTextFromPayload(payload);
        String senderNumber = extractNumberFromPayload(payload);

        if (incomingMessage != null && incomingMessage.trim().equalsIgnoreCase("#extrato")) {

            System.out.println("Gatilho #extrato acionado pelo número: " + senderNumber);

            Client client = mockClientDatabaseSearch();

            if (client != null) {
                new Thread(() -> onDemandUseCase.getOnDemandStatement(client)).start();
            }
        }

        return ResponseEntity.ok().build();
    }

    private String extractTextFromPayload(Map<String, Object> payload) {
        // Exemplo fictício. Você ajustará isso quando plugar o Evolution API ou Twilio.
        try {
            return (String) ((Map<?, ?>) payload.get("message")).get("text");
        } catch (Exception e) {
            return null;
        }
    }

    private String extractNumberFromPayload(Map<String, Object> payload) {
        try {
            return (String) payload.get("sender");
        } catch (Exception e) {
            return null;
        }
    }

    private Client mockClientDatabaseSearch() {
        return Client.builder()
                .id("CLI-001")
                .name("Cliente Teste")
                .messageAppNumber(USER_PHONE)
                .monitoringEmail(MAIL_USERNAME)
                .build();
    }
}
