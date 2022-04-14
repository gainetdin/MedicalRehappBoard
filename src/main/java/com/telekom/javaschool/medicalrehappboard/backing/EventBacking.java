package com.telekom.javaschool.medicalrehappboard.backing;

import com.telekom.javaschool.medicalrehappboard.model.EventBoard;
import com.telekom.javaschool.medicalrehappboard.service.EventService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Getter
@Slf4j
@Named("eventBacking")
@RequestScoped
public class EventBacking {

    private List<EventBoard> events;

    @EJB
    private EventService eventService;

    @PostConstruct
    public void init() {
        events = eventService.getEvents();
    }
}
