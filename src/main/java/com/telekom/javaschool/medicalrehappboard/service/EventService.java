package com.telekom.javaschool.medicalrehappboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telekom.javaschool.medicalrehappboard.model.EventBoard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Getter
@Slf4j
@Stateless
public class EventService {

    private List<EventBoard> events;

    @Inject
    @Push(channel = "medRehappBoard")
    private PushContext pushContext;

    public List<EventBoard> convertTextToEvents(String text) {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        try {
            events = mapper.readValue(text, new TypeReference<List<EventBoard>>() {});
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        pushContext.send("Updated");
        return events;
    }
}
