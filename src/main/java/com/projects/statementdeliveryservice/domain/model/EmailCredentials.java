package com.projects.statementdeliveryservice.domain.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCredentials {

    private String host;
    private String username;
    private String password;
}
