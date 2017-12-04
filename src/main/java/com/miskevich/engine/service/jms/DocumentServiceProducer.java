package com.miskevich.engine.service.jms;

import com.miskevich.engine.model.Document;
import com.miskevich.engine.util.JsonConverter;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class DocumentServiceProducer {
    private static final String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String SUBJECT = "add.response.queue";
    private static volatile DocumentServiceProducer instance;
    private Session session;
    private MessageProducer producer;

    private DocumentServiceProducer() {
        init();
    }

    public static DocumentServiceProducer getInstance() {
        if (instance == null) {
            synchronized (DocumentServiceProducer.class) {
                if (instance == null) {
                    instance = new DocumentServiceProducer();
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
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(SUBJECT);
            producer = session.createProducer(destination);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushIntoQueue(Message request, Document document) {
        try {
            TextMessage response = session.createTextMessage(JsonConverter.toJson(document));
            response.setJMSCorrelationID(request.getJMSCorrelationID());
            producer.send(response);
            System.out.println("Sending text '" + response.getText() + "'");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
