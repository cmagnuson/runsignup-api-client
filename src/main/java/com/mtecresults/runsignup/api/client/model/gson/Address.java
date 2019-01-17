package com.mtecresults.runsignup.api.client.model.gson;

import com.mtecresults.runsignup.api.client.model.export.Column;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Address {
    String street;
    String city;
    String state;
    String zipcode;
    String country_code;

    public static List<Column> getDefaultColumns() {
        Column[] defaultColumns = new Column[]{
                new Column("Street", participant -> participant.getUser().getAddress().getStreet()),
                new Column("City", participant -> participant.getUser().getAddress().getCity()),
                new Column("State", participant -> participant.getUser().getAddress().getState()),
                new Column("Zipcode", participant -> participant.getUser().getAddress().getZipcode()),
                new Column("Country", participant -> participant.getUser().getAddress().getCountry_code()),
        };
        return Arrays.asList(defaultColumns);
    }
}
