package com.mtecresults.runsignup.api.client.model.gson;

import lombok.Data;

import java.util.Date;

@Data
public class Event {
    long event_id;
    String name;
    Date start_time;
    String distance;
}
