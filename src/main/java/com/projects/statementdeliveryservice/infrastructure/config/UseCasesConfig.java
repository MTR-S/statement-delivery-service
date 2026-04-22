package com.projects.statementdeliveryservice.infrastructure.config;

import com.projects.statementdeliveryservice.application.usecases.RequestStatementOnDemandUseCase;
import com.projects.statementdeliveryservice.application.usecases.SendDailyStatementUseCase;
import com.projects.statementdeliveryservice.domain.gateway.StatementProviderGateway;
import com.projects.statementdeliveryservice.domain.gateway.MessageAppGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public SendDailyStatementUseCase sendDailyStatementUseCase(
            StatementProviderGateway providerGateway,
            MessageAppGateway messageAppGateway) {
        return new SendDailyStatementUseCase(providerGateway, messageAppGateway);
    }

    @Bean
    public RequestStatementOnDemandUseCase requestStatementOnDemandUseCase(
            StatementProviderGateway providerGateway,
            MessageAppGateway messageAppGateway) {
        return new RequestStatementOnDemandUseCase(providerGateway, messageAppGateway);
    }
}
