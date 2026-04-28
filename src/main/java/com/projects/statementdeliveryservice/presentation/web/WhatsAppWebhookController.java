package com.projects.statementdeliveryservice.presentation.web;

import com.projects.statementdeliveryservice.application.usecases.RequestStatementOnDemandUseCase;
import com.projects.statementdeliveryservice.domain.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook/whatsapp")
public class WhatsAppWebhookController {

    private final RequestStatementOnDemandUseCase onDemandUseCase;
    private final String mailUsername;
    private final String userPhone;


    public WhatsAppWebhookController(
            RequestStatementOnDemandUseCase onDemandUseCase,
            @Value("${spring.mail.username}") String mailUsername,
            @Value("${USER_PHONE}") String userPhone) {

        this.onDemandUseCase = onDemandUseCase;
        this.mailUsername = mailUsername;
        this.userPhone = userPhone;
    }

    @PostMapping("/receive")
    public ResponseEntity<Void> receiveMessage(@RequestBody Map<String, Object> payload) {

        System.out.println("====== PAYLOAD RECEBIDO DA EVOLUTION ======");
        System.out.println(payload);
        System.out.println("===========================================");

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
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Map<String, Object> message = (Map<String, Object>) data.get("message");

            if (message.containsKey("conversation")) {
                return (String) message.get("conversation");

            } else if (message.containsKey("extendedTextMessage")) {

                Map<String, Object> extended = (Map<String, Object>) message.get("extendedTextMessage");

                return (String) extended.get("text");
            }
        } catch (Exception e) {
            System.err.println("Erro ao extrair texto do webhook: " + e.getMessage());
        }
        return null;
    }

    private String extractNumberFromPayload(Map<String, Object> payload) {
        try {
            String sender = (String) payload.get("sender");

            if (sender != null && sender.contains("@")) {
                return sender.split("@")[0];
            }
            return sender;
        } catch (Exception e) {
            System.err.println("Erro ao extrair número do webhook: " + e.getMessage());
            return null;
        }
    }

    private Client mockClientDatabaseSearch() {
        return Client.builder()
                .id("CLI-001")
                .name("Cliente Teste")
                .messageAppNumber(userPhone)
                .monitoringEmail(mailUsername)
                .build();
    }
}
