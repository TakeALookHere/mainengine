package com.miskevich.engine.service.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Optional;

public class DocumentServiceConsumer {
    private static final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String SUBJECT = "add.request.queue";
    private static volatile DocumentServiceConsumer instance;
    private MessageConsumer consumer;

    private DocumentServiceConsumer() {
        init();
    }

    public static DocumentServiceConsumer getInstance() {
        if (instance == null) {
            synchronized (DocumentServiceConsumer.class) {
                if (instance == null) {
                    instance = new DocumentServiceConsumer();
                }
            }
        }
        return instance;
    }


    private void init() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(SUBJECT);
            consumer = session.createConsumer(destination);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Message> getFromQueue() {
        try {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Received '" + textMessage.getText() + "'");
                return Optional.of(textMessage);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
