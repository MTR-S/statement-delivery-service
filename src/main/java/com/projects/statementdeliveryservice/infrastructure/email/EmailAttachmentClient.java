package com.projects.statementdeliveryservice.infrastructure.email;

import java.time.LocalDate;
import java.util.List;

public interface EmailAttachmentClient {

    List<byte[]> fetchAttachmentsByDate(LocalDate date, String fileExtension);
}
