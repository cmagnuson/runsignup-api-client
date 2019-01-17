package com.mtecresults.runsignup.api.client.model.gson;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ParticipantsQueryResponse implements ParticipantsGettable {
    ArrayList<Participant> participants = new ArrayList<>();
}

