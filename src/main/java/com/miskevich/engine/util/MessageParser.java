package com.miskevich.engine.util;

import com.miskevich.engine.model.Document;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public abstract class MessageParser {

    public static Document messageToDocument(Message message) {
        TextMessage textMessage = (TextMessage) message;
        Document document;
        try {
            document = JsonConverter.fromJson(textMessage.getText(), Document.class);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
