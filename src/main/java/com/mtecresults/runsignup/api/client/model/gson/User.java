package com.mtecresults.runsignup.api.client.model.gson;

import com.mtecresults.runsignup.api.client.model.export.Column;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class User {
    long user_id;
    String first_name;
    String middle_name;
    String last_name;
    String email;
    Address address;
    String dob;
    String gender;
    String phone;

    public static List<Column> getDefaultColumns() {
        Column[] defaultColumns = new Column[]{
                new Column("user-id", participant -> ""+participant.getUser().getUser_id()),
                new Column("First Name", participant -> participant.getUser().getFirst_name()),
                new Column("Middle Name", participant -> participant.getUser().getMiddle_name()),
                new Column("Last Name", participant -> participant.getUser().getLast_name()),
                new Column("Email", participant -> participant.getUser().getEmail()),
                new Column("DOB", participant -> participant.getUser().getDob()),
                new Column("Gender", participant -> participant.getUser().getGender()),
                new Column("Phone", participant -> participant.getUser().getPhone()),
        };
        return Arrays.asList(defaultColumns);
    }
}
