package com.telekom.javaschool.medicalrehappboard.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventBoard {

    private String patientName;

    private LocalDateTime dateTime;

    private String treatmentName;
}
