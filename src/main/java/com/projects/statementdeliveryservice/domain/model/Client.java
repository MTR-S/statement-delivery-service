package com.projects.statementdeliveryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private String id;
    private String name;
    private String messageAppNumber;
    private String monitoringEmail;
}
