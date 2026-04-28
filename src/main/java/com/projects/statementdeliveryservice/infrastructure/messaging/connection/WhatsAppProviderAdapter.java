package com.projects.statementdeliveryservice.infrastructure.messaging.connection;

import com.projects.statementdeliveryservice.domain.gateway.MessageAppGateway;
import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;
import com.projects.statementdeliveryservice.infrastructure.messaging.dto.MediaMessageRequest;
import com.projects.statementdeliveryservice.infrastructure.messaging.dto.SendMediaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class WhatsAppProviderAdapter implements MessageAppGateway {

    private final WhatsAppFeignClient whatsAppFeignClient;
    private final String instanceName;
    private final String apiKey;

    public WhatsAppProviderAdapter(
            WhatsAppFeignClient whatsAppFeignClient,
            @Value("${whatsapp.api.instance}") String instanceName,
            @Value("${whatsapp.api.key}") String apiKey) {
        this.whatsAppFeignClient = whatsAppFeignClient;
        this.instanceName = instanceName;
        this.apiKey = apiKey;
    }

    @Override
    public void sendDocument(Client client, Statement statement, String message) {
        try {
            if (statement != null && statement.getFileContent() != null) {

                String base64Pdf = Base64.getEncoder().encodeToString(statement.getFileContent());

                // 1. Monta o bloco do arquivo
                MediaMessageRequest mediaObj = MediaMessageRequest.builder()
                        .mediatype("document")
                        .mimetype("application/pdf")
                        .fileName(statement.getFileName())
                        .media(base64Pdf)
                        .caption(message)
                        .build();

                SendMediaRequest request = SendMediaRequest.builder()
                        .number(client.getMessageAppNumber())
                        .mediaMessage(mediaObj)
                        .build();

                whatsAppFeignClient.sendMedia(instanceName, apiKey, request);

                System.out.println("Extrato enviado com sucesso para o número: " + client.getMessageAppNumber());
            } else {
                System.out.println("Enviando apenas texto: " + message);
            }
        } catch (Exception e) {
            System.err.println("Erro crítico ao enviar mensagem para o WhatsApp: " + e.getMessage());
        }
    }
}
