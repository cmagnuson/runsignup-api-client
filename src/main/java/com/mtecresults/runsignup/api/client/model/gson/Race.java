package com.mtecresults.runsignup.api.client.model.gson;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Race {
    long race_id;
    String name;
    Date last_modified;
    List<Event> events;

    public List<Long> getEventIds() {
        return events.stream().map(event -> event.event_id).collect(Collectors.toList());
    }
}
