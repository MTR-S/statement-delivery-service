package com.projects.statementdeliveryservice.infrastructure.messaging.dto;

import lombok.Builder;

@Builder
public record MediaMessageRequest(
        String mediatype,
        String mimetype,
        String fileName,
        String media,
        String caption
) {
}
