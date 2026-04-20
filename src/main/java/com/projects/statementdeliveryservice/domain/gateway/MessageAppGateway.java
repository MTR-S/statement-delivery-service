package com.projects.statementdeliveryservice.domain.gateway;

import com.projects.statementdeliveryservice.domain.model.Client;
import com.projects.statementdeliveryservice.domain.model.Statement;

public interface MessageAppGateway {

    void sendDocument(Client client, Statement statement, String message);
}
