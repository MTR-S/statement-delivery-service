package com.projects.statementdeliveryservice.infrastructure.messaging.dto;

import lombok.Builder;

@Builder
public record SendMediaRequest(
        String number,
        MediaMessageRequest mediaMessage
) {
}
