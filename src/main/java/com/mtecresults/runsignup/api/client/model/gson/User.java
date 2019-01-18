package com.mtecresults.runsignup.api.client.model.gson;

import com.mtecresults.runsignup.api.client.model.export.Column;
import com.mtecresults.runsignup.api.client.model.export.StandardColumn;
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
                new StandardColumn("user-id", participant -> ""+participant.getUser().getUser_id()),
                new StandardColumn("First Name", participant -> participant.getUser().getFirst_name()),
                new StandardColumn("Middle Name", participant -> participant.getUser().getMiddle_name()),
                new StandardColumn("Last Name", participant -> participant.getUser().getLast_name()),
                new StandardColumn("Email", participant -> participant.getUser().getEmail()),
                new StandardColumn("DOB", participant -> participant.getUser().getDob()),
                new StandardColumn("Gender", participant -> participant.getUser().getGender()),
                new StandardColumn("Phone", participant -> participant.getUser().getPhone()),
        };
        return Arrays.asList(defaultColumns);
    }
}
