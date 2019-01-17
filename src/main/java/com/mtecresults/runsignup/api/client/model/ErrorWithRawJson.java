package com.mtecresults.runsignup.api.client.model;

import lombok.Data;

@Data
public class ErrorWithRawJson {
    final Error error;
    final String rawJson;
    final String url;
    final Exception exception;
}
