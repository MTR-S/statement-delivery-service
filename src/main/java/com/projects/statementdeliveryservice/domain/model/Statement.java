package com.projects.statementdeliveryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

    private String clientId;
    private LocalDate referenceDate;
    private byte[] fileContent;
    private String fileName;
}
