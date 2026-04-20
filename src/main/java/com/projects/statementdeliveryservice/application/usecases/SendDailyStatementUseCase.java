package com.projects.statementdeliveryservice.application.usecases;

import com.projects.statementdeliveryservice.domain.gateway.MessageAppGateway;
import com.projects.statementdeliveryservice.domain.gateway.StatementProviderGateway;
import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class SendDailyStatementUseCase {

    private static final String SUCCESS_MSG = "Bom dia! Aqui está o seu extrato consolidado de hoje.";
    private static final String ERROR_MSG = "Nenhum extrato encontrado hoje.";

    private final StatementProviderGateway providerGateway;
    private final MessageAppGateway messageAppGateway;

    public void getDailyStatement(Client client) {
        LocalDate today = LocalDate.now();

        Optional<Statement> statementOpt = providerGateway.fetchStatementForDate(client, today);

        if (statementOpt.isPresent()) {
            messageAppGateway.sendDocument(client, statementOpt.get(), SUCCESS_MSG);
        } else {
            System.out.println(ERROR_MSG);
        }
    }
}
