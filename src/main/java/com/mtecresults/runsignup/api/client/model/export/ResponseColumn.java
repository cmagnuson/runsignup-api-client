package com.mtecresults.runsignup.api.client.model.export;

import com.mtecresults.runsignup.api.client.model.gson.Participant;
import lombok.Data;

import java.util.function.Function;

@Data
public class ResponseColumn implements Column {
    private final String name;
    private final String responseId;


    public Function<Participant, String> getAccessor() {
        return participant -> {
            for (Participant.QuestionResponse questionResponse : participant.getQuestion_responses()) {
                if (questionResponse.getQuestion_id().equals(responseId)) {
                    return questionResponse.getResponse();
                }
            }
            return "";
        };
    }
}