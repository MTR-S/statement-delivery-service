package com.projects.statementdeliveryservice.infrastructure.messaging.dto;

import lombok.Builder;

@Builder
public record SendMediaRequest(
        String number,
        String mediatype,
        String mimetype,
        String fileName,
        String media,
        String caption
) {
}
