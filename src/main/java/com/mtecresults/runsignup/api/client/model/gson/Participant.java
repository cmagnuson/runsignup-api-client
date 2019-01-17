package com.mtecresults.runsignup.api.client.model.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mtecresults.runsignup.api.client.controller.RunSignUpConnector;
import lombok.Data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class Participant {
    User user;
    long registration_id;
    long event_id;
    long rsu_transaction_id;
    long transaction_id;
    String bib_num;
    int age;
    Date registration_date;
    @JsonAdapter(value = LastModifiedTypeAdaptor.class)
    Long last_modified;

    //only apply to active participants
    String imported;
    String giveaway;
    List<QuestionResponse> question_responses;
    List<ParticipantAddon> participant_addons;

    //only apply to removed participants
    String removed_reason;
    long new_registration_id;

    @Data
    public class QuestionResponse {
        String question_id;
        String question_text;
        String response;
    }

    @Data
    public class ParticipantAddon {
        String addon_id;
        String addon_name;
        String quantity;
        String amount;
    }

    public class LastModifiedTypeAdaptor extends TypeAdapter<Long> {


        @Override
        public void write(JsonWriter out, Long value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        }

        @Override
        public Long read(JsonReader in) throws IOException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RunSignUpConnector.REMOVED_DATE_FORMAT);
            if(in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            String timeStr = in.nextString();
            try {
                if (timeStr != null) {
                    Date d = simpleDateFormat.parse(timeStr);
                    return d.getTime();
                }
            } catch (ParseException pe) {
                try{
                    return Long.valueOf(timeStr);
                }
                catch (NumberFormatException nfe){
                    throw new IOException("Can't parse date/time: "+timeStr, nfe);
                }
            }
            return null;
        }
    }
}
