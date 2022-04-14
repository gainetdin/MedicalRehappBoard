package com.telekom.javaschool.medicalrehappboard.message;

import com.telekom.javaschool.medicalrehappboard.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Slf4j
@Singleton
@Startup
public class JmsConsumerBean {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_FROM = "medrehapp-queue";
    private static final String QUEUE_TO = "medrehapp-init";
    private Connection connection;

    @EJB
    private EventService eventService;

    @PostConstruct
    private void startConsumer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            createConsumer(session);
            createProducer(session);
        } catch (JMSException e) {
            log.error(e.getMessage());
        }
    }

    private void createConsumer(Session session) throws JMSException {
        Queue queueFrom = session.createQueue(QUEUE_FROM);
        MessageConsumer consumer = session.createConsumer(queueFrom);
        EventListener eventListener = new EventListener();
        eventListener.setEventService(eventService);
        consumer.setMessageListener(eventListener);
    }

    private void createProducer(Session session) throws JMSException {
        Queue queueTo = session.createQueue(QUEUE_TO);
        MessageProducer producer = session.createProducer(queueTo);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("Asking for event list");
        producer.send(textMessage);
        log.info("Send message: " + textMessage.getText());
    }

    @PreDestroy
    private void stopConsumer() {
        try {
            connection.stop();
        } catch (JMSException e) {
            log.error(e.getMessage());;
        }
    }
}
