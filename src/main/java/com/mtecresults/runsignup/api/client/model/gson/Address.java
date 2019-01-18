package com.mtecresults.runsignup.api.client.model.gson;

import com.mtecresults.runsignup.api.client.model.export.Column;
import com.mtecresults.runsignup.api.client.model.export.StandardColumn;
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
                new StandardColumn("Street", participant -> participant.getUser().getAddress().getStreet()),
                new StandardColumn("City", participant -> participant.getUser().getAddress().getCity()),
                new StandardColumn("State", participant -> participant.getUser().getAddress().getState()),
                new StandardColumn("Zipcode", participant -> participant.getUser().getAddress().getZipcode()),
                new StandardColumn("Country", participant -> participant.getUser().getAddress().getCountry_code()),
        };
        return Arrays.asList(defaultColumns);
    }
}
