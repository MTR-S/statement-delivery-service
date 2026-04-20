package com.projects.statementdeliveryservice.infrastructure.email;

import jakarta.mail.*;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class ImapEmailClient implements EmailAttachmentClient {

    private final String host;
    private final String username;
    private final String password;
    private static final String EMAIL_PROTOCOL = "imaps";

    public ImapEmailClient(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<byte[]> fetchAttachmentsByDate(LocalDate date, String fileExtension) {
        Store store = null;
        Folder inbox = null;

        try {
            store = connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = searchMessagesByDate(inbox, date);

            return processMessages(messages, fileExtension);

        } catch (Exception e) {
            System.err.println("Erro interno ao ler e-mails via IMAP: " + e.getMessage());

            return new ArrayList<>();
        } finally {
            disconnectQuietly(store, inbox);
        }
    }

    private Store connect() throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", EMAIL_PROTOCOL);

        Session session = Session.getInstance(properties);
        Store store = session.getStore(EMAIL_PROTOCOL);

        store.connect(host, username, password);

        return store;
    }

    private Message[] searchMessagesByDate(Folder inbox, LocalDate date) throws MessagingException {
        Date searchDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SearchTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.EQ, searchDate);

        return inbox.search(dateTerm);
    }

    private List<byte[]> processMessages(Message[] messages, String fileExtension) throws Exception {
        List<byte[]> attachments = new ArrayList<>();

        for (int i = messages.length - 1; i >= 0; i--) {
            Message message = messages[i];
            if (message.isMimeType("multipart/*")) {
                extractAttachment((Multipart) message.getContent(), fileExtension)
                        .ifPresent(attachments::add);
            }
        }

        return attachments;
    }

    private Optional<byte[]> extractAttachment(Multipart multipart, String extension) throws Exception {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);

            if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                    bodyPart.getFileName() != null &&
                    bodyPart.getFileName().toLowerCase().endsWith(extension)) {

                try (InputStream is = bodyPart.getInputStream()) {
                    return Optional.of(is.readAllBytes());
                }
            }
        }
        return Optional.empty();
    }

    private void disconnectQuietly(Store store, Folder inbox) {
        try {
            if (inbox != null && inbox.isOpen()) inbox.close(false);
            if (store != null && store.isConnected()) store.close();
        } catch (Exception ignored) {
        }
    }
}