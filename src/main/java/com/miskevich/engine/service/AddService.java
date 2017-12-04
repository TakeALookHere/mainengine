package com.miskevich.engine.service;

import com.miskevich.engine.model.Document;
import com.miskevich.engine.service.jms.DocumentServiceConsumer;
import com.miskevich.engine.service.jms.DocumentServiceProducer;
import com.miskevich.engine.util.MessageParser;

import javax.jms.Message;
import java.util.Optional;
import java.util.UUID;

public class AddService {
    private final DocumentServiceConsumer CONSUMER = DocumentServiceConsumer.getInstance();
    private final DocumentServiceProducer PRODUCER = DocumentServiceProducer.getInstance();

    public void run() {
        Optional<Message> optional = CONSUMER.getFromQueue();
        if (optional.isPresent()) {
            Message message = optional.get();
            Document document = MessageParser.messageToDocument(message);
            document.setId(UUID.randomUUID().toString());
            PRODUCER.pushIntoQueue(message, document);

        }
    }
}
