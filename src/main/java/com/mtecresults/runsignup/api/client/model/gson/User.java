package com.mtecresults.runsignup.api.client.model.gson;

import lombok.Data;

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

    @Data
    public class Address {
        String street;
        String city;
        String state;
        String zipcode;
        String country_code;
    }
}
