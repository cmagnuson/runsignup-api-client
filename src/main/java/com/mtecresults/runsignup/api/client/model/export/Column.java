package com.mtecresults.runsignup.api.client.model.export;

import com.mtecresults.runsignup.api.client.model.gson.Participant;
import lombok.Data;

import java.util.function.Function;

@Data
public class Column {
    private final String name;
    private final Function<Participant, String> accessor;
}
