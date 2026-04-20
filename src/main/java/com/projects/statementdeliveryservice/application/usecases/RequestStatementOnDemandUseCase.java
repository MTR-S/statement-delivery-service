package com.projects.statementdeliveryservice.application.usecases;

import com.projects.statementdeliveryservice.domain.gateway.MessageAppGateway;
import com.projects.statementdeliveryservice.domain.gateway.StatementProviderGateway;
import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class RequestStatementOnDemandUseCase {

    private static final String SUCCESS_MSG = "Conforme solicitado, aqui está o seu extrato mais recente (consolidação da manhã).";
    private static final String ERROR_MSG = "Ainda não recebemos o seu extrato de hoje do banco. Tente novamente mais tarde.";

    private final StatementProviderGateway providerGateway;
    private final MessageAppGateway messageAppGateway;

    public void getOnDemandStatement(Client client) {
        LocalDate today = LocalDate.now();

        Optional<Statement> statementOpt = providerGateway.fetchStatementForDate(client, today);

        if (statementOpt.isPresent()) {
            messageAppGateway.sendDocument(client, statementOpt.get(), SUCCESS_MSG);
        } else {
            messageAppGateway.sendDocument(client, null, ERROR_MSG);
        }
    }
}
