package com.mtecresults.runsignup.api.client.model.export;

import com.mtecresults.runsignup.api.client.model.gson.Participant;
import lombok.Data;

import java.util.function.Function;

@Data
public class AddOnColumn implements Column {
    private final String name;
    private final String addOnId;

    public Function<Participant, String> getAccessor() {
        return participant -> {
            for (Participant.ParticipantAddon addOn : participant.getParticipant_addons()) {
                if (addOn.getAddon_id().equals(addOnId)) {
                    return addOn.getQuantity();
                }
            }
            return "";
        };
    }
}