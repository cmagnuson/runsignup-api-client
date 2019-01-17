package com.mtecresults.runsignup.api.client.model.gson;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantsRemovedQueryResponse implements ParticipantsGettable {
    EventAndParticipants event;

    @Data
    public class EventAndParticipants {
        long event_id;
        ArrayList<Participant> participants = new ArrayList<>();
    }

    public ArrayList<Participant> getParticipants() {
        return event.participants;
    }
}
