package com.mtecresults.runsignup.api.client.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mtecresults.runsignup.api.client.model.ApiCredentials;
import com.mtecresults.runsignup.api.client.model.ErrorWithRawJson;
import com.mtecresults.runsignup.api.client.model.gson.*;
import com.spencerwi.either.Either;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.Error;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@Data
public class RunSignUpConnector {

    public static final String REMOVED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final OkHttpClient client;
    private final ApiCredentials creds;
    private final long raceId;


    public Either<ErrorWithRawJson, Race> getRace() {
        HttpUrl.Builder urlBuilder = getDefaultUrlBuilder(creds, raceId);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            final Gson gson = new GsonBuilder().setDateFormat("M/d/yyyy HH:mm").create();
            Either<ErrorWithRawJson, RaceQueryResponse> raceQueryResponse = tryParse(gson, response, RaceQueryResponse.class);
            return raceQueryResponse.map(errorWithRawJson -> errorWithRawJson, RaceQueryResponse::getRace);
        }
        catch (IOException e){
            return Either.left(new ErrorWithRawJson(null, null, request.url().toString(), e));
        }
    }

    public Either<ErrorWithRawJson, List<Participant>> getParticipants(List<Long> eventIds) {
        return getParticipants(eventIds, false, 0);
    }

    public Either<ErrorWithRawJson, List<Participant>> getParticipants(List<Long> eventIds, boolean removed, long lastModified) {
        //RunSignUp does not do the correct >=/> comparison on last modified timestamp
        //so increment by 1 to ensure we are only getting modified after as desired
        lastModified++;

        HttpUrl.Builder urlBuilder = getDefaultUrlBuilder(creds, raceId);
        urlBuilder.addEncodedPathSegment(""+raceId);
        urlBuilder.addPathSegment(removed ? "removed-participants" : "participants");

        StringJoiner sj = new StringJoiner(",");
        for(Long eventId: eventIds){
            sj.add(""+eventId);
        }
        urlBuilder.addQueryParameter("event_id", sj.toString());
        if(!removed) {
            urlBuilder.addQueryParameter("include_questions", "T");
            urlBuilder.addQueryParameter("include_registration_addons", "T");
            urlBuilder.addQueryParameter("include_registration_notes", "T");
        }
        urlBuilder.addQueryParameter("results_per_page", "1000");
        urlBuilder.addQueryParameter("after_registration_id", "1");
        urlBuilder.addQueryParameter("modified_after_timestamp", ""+lastModified);

        List<Participant> participantsAll = new ArrayList<>();
        long lastRegistrationId = 1;
        boolean hasMoreRegistrations = true;
        while(hasMoreRegistrations) {
            urlBuilder.setQueryParameter("after_registration_id", ""+lastRegistrationId);

            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                Either<ErrorWithRawJson, ? extends ParticipantsGettable[]> participantsQueryResponseE;

                if(removed){
                    final Gson gson = new GsonBuilder().setDateFormat(REMOVED_DATE_FORMAT).create();
                    participantsQueryResponseE = tryParse(gson, response, ParticipantsRemovedQueryResponse[].class);
                }
                else{
                    final Gson gson = new GsonBuilder().setDateFormat("M/d/yyyy HH:mm").create();
                    participantsQueryResponseE = tryParse(gson, response, ParticipantsQueryResponse[].class);
                }

                if(participantsQueryResponseE.isLeft()){
                    return Either.left(participantsQueryResponseE.getLeft());
                }
                else {
                    List<? extends ParticipantsGettable> participantsQueryResponse = Arrays.asList(participantsQueryResponseE.getRight());
                    List<Participant> participants = participantsQueryResponse.stream().flatMap(resp -> resp.getParticipants().stream()).collect(Collectors.toList());
                    participantsAll.addAll(participants);
                    if (participants.isEmpty()) {
                        hasMoreRegistrations = false;
                    } else {
                        for (Participant p : participants) {
                            if (p.getRegistration_id() > lastRegistrationId) {
                                lastRegistrationId = p.getRegistration_id();
                            }
                        }
                    }
                }
            }
            catch (IOException e){
                return Either.left(new ErrorWithRawJson(null, null, request.url().toString(), e));
            }
        }
        return Either.right(participantsAll);
    }

    private static <T> Either<ErrorWithRawJson, T> tryParse(Gson gson, Response response, Class<T> tClass) throws IOException {
        String body = response.body().string();

        try {
            return Either.right(gson.fromJson(body, tClass));
        }
        catch(JsonParseException jse){
            log.warn("Error parsing expected response: ", jse);
            //try and parse as error code
            try{
                Error error = gson.fromJson(body, Error.class);
                return Either.left(new ErrorWithRawJson(error, body, response.request().url().toString(), null));
            }
            catch(JsonParseException jse2){
                return Either.left(new ErrorWithRawJson(null, body, response.request().url().toString(), null));
            }
        }
    }

    private static HttpUrl.Builder getDefaultUrlBuilder(ApiCredentials creds, long raceId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://runsignup.com/rest/race/").newBuilder();
        urlBuilder.addQueryParameter("api_key", creds.getKey());
        urlBuilder.addQueryParameter("api_secret", creds.getSecret());
        urlBuilder.addQueryParameter("race_id", ""+raceId);
        urlBuilder.addQueryParameter("format", "json");
        return urlBuilder;
    }
}
