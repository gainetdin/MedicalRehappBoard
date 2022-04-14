package com.telekom.javaschool.medicalrehappboard.message;

import com.telekom.javaschool.medicalrehappboard.service.EventService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Setter
@Slf4j
public class EventListener implements MessageListener {

    private EventService eventService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String eventsAsJson = textMessage.getText();
            log.info("Message received: " + eventsAsJson);
            eventService.convertTextToEvents(eventsAsJson);
        } catch (JMSException e) {
            log.error(e.getMessage());
        }
    }
}
