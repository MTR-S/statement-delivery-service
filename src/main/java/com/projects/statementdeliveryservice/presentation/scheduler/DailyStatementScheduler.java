package com.projects.statementdeliveryservice.presentation.scheduler;

import com.projects.statementdeliveryservice.application.usecases.SendDailyStatementUseCase;
import com.projects.statementdeliveryservice.domain.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class DailyStatementScheduler {

    @Value("${spring.mail.username}")
    private String MAIL_USERNAME;

    @Value("${USER_PHONE}")
    private String USER_PHONE;

    private final SendDailyStatementUseCase sendDailyStatementUseCase;

    public DailyStatementScheduler(SendDailyStatementUseCase sendDailyStatementUseCase) {
        this.sendDailyStatementUseCase = sendDailyStatementUseCase;
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void executeDailyJob() {
        System.out.println("Iniciando rotina diária de envio de extratos...");

        List<Client> activeClients = getMockClientsForPoC();

        for (Client client : activeClients) {
            try {
                sendDailyStatementUseCase.getDailyStatement(client);
            } catch (Exception e) {
                System.err.println("Falha ao processar extrato para o cliente " + client.getName() + ": " + e.getMessage());
            }
        }
    }

    private List<Client> getMockClientsForPoC() {

        return List.of(
                Client.builder()
                        .id("CLI-001")
                        .name("Cliente Teste")
                        .messageAppNumber(USER_PHONE)
                        .monitoringEmail(MAIL_USERNAME)
                        .build()
        );
    }
}
