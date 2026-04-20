package com.projects.statementdeliveryservice.domain.gateway;

import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;

import java.time.LocalDate;
import java.util.Optional;

public interface StatementProviderGateway {

    Optional<Statement> fetchStatementForDate(Client client, LocalDate date);
}
