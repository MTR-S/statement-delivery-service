package com.projects.statementdeliveryservice.infrastructure.messaging.dto;

public record SendTextRequest(
        String number,
        String text
) {
}
