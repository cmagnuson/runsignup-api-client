package com.mtecresults.runsignup.api.client.model.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mtecresults.runsignup.api.client.controller.RunSignUpConnector;
import com.mtecresults.runsignup.api.client.model.export.AddOnColumn;
import com.mtecresults.runsignup.api.client.model.export.Column;
import com.mtecresults.runsignup.api.client.model.export.ResponseColumn;
import com.mtecresults.runsignup.api.client.model.export.StandardColumn;
import lombok.Data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

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

    public List<ResponseColumn> getResponseColumns() {
        List<ResponseColumn> columns = new ArrayList<>();
        for(QuestionResponse qr: question_responses){
            ResponseColumn rc = new ResponseColumn(qr.question_text, qr.question_id);
                    columns.add(rc);
        }
        return columns;
    }

    public List<AddOnColumn> getAddOnColumns() {
        List<AddOnColumn> columns = new ArrayList<>();
        for(ParticipantAddon pa: participant_addons){
            AddOnColumn ac = new AddOnColumn(pa.addon_name, pa.addon_id);
            columns.add(ac);
        }
        return columns;
    }

    public static List<Column> getDefaultColumns() {
        Column[] defaultColumns = new Column[]{
                new StandardColumn("RegistrationId", participant -> "" + participant.getRegistration_id()),
                new StandardColumn("EventId", participant -> "" + participant.getEvent_id()),
                new StandardColumn("RsuTransactionId", participant -> "" + participant.getRsu_transaction_id()),
                new StandardColumn("TransactionId", participant -> "" + participant.getTransaction_id()),
                new StandardColumn("Bib", Participant::getBib_num),
                new StandardColumn("Age", participant -> "" + participant.getAge()),
                new StandardColumn("LastModified", participant -> "" + participant.getLast_modified()),
                new StandardColumn("Imported", Participant::getImported),
                new StandardColumn("Giveaway", participant -> "" + participant.getGiveaway()),
                new StandardColumn("RemovedReason", Participant::getRemoved_reason),
                new StandardColumn("NewRegistrationId", participant -> "" + participant.getNew_registration_id()),
        };
        return Arrays.asList(defaultColumns);
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
