package com.projects.statementdeliveryservice.infrastructure.integration;

import com.projects.statementdeliveryservice.domain.gateway.StatementProviderGateway;
import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;
import com.projects.statementdeliveryservice.infrastructure.email.EmailAttachmentClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class EmailStatementProviderAdapter implements StatementProviderGateway {

    private final EmailAttachmentClient emailClient;

    public EmailStatementProviderAdapter(EmailAttachmentClient emailClient) {
        this.emailClient = emailClient;
    }

    @Override
    public Optional<Statement> fetchStatementForDate(Client client, LocalDate date) {

        List<byte[]> pdfAttachments = emailClient.fetchAttachmentsByDate(date, ".pdf");

        if (pdfAttachments.isEmpty()) {
            return Optional.empty();
        }

        byte[] latestPdf = pdfAttachments.getFirst();

        Statement statement = Statement.builder()
                .clientId(client.getId())
                .referenceDate(date)
                .fileContent(latestPdf)
                .fileName("Extrato_" + date + ".pdf")
                .build();

        return Optional.of(statement);
    }
}
