package com.mtecresults.runsignup.api.client.controller;

import com.mtecresults.runsignup.api.client.model.ApiCredentials;
import com.mtecresults.runsignup.api.client.model.ErrorWithRawJson;
import com.mtecresults.runsignup.api.client.model.gson.Race;
import com.spencerwi.either.Either;
import okhttp3.OkHttpClient;
import okhttp3.mock.MockInterceptor;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static okhttp3.mock.ClasspathResources.resource;
import static okhttp3.mock.MediaTypes.MEDIATYPE_JSON;


import static org.junit.Assert.assertEquals;


public class RunSignUpConnectorTest {

    private final ApiCredentials credentials = new ApiCredentials("key", "secret");
    private final long raceId = 1;

    @Test
    public void getRace() {
        MockInterceptor interceptor = new MockInterceptor();

        interceptor.addRule()
                .get()
                .respond(resource("race.json"), MEDIATYPE_JSON);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        RunSignUpConnector connector = new RunSignUpConnector(client, credentials, raceId);
        Either<ErrorWithRawJson, Race> race = connector.getRace();
        assertTrue(race.isRight());
        assertEquals(1, race.getRight().getEvents().size());
    }

//    @Test
//    public void getParticipants() {
//        MockInterceptor interceptor = new MockInterceptor();
//
//        interceptor.addRule()
//                .get()
//                .anyTimes()
//                .respond(resource("participants.json"), MEDIATYPE_JSON);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();
//
//        RunSignUpConnector connector = new RunSignUpConnector(client, credentials, raceId);
//        Either<ErrorWithRawJson, List<Participant>> participants = connector.getParticipants(Collections.singletonList(1l));
//        assertTrue(participants.isRight());
//        assertEquals(2, participants.getRight().size());
//    }
}