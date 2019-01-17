package com.mtecresults.runsignup.api.client;

import com.mtecresults.runsignup.api.client.controller.ManifestVersionProvider;
import com.mtecresults.runsignup.api.client.controller.RunSignUpConnector;
import com.mtecresults.runsignup.api.client.model.ApiCredentials;
import com.mtecresults.runsignup.api.client.model.ErrorWithRawJson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import picocli.CommandLine;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@CommandLine.Command(versionProvider = ManifestVersionProvider.class,
        headerHeading = "Usage:%n",
        synopsisHeading = "",
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "%nOptions:%n",
        header = "Download RunSignUp registrations via command line",
        description = "Use your provided api key/secret and race id to download entries.%n%n" +
                "You can include arguments (api keys in particular) in a file and call with @FILE_PATH to include in command, for easier reuse between races.")
public class RunSignUpCli implements Callable<Void> {

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "usage help")
    private boolean helpRequested;

    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true, description = "Print version info")
    boolean versionRequested;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Verbose")
    boolean verbose;

    @CommandLine.Option(names = {"-r", "--race"}, required=true, description = "race id")
    private long raceId;

    @CommandLine.Option(names = {"-k", "--key"}, required=true, description = "api key")
    private String apiKey;

    @CommandLine.Option(names = {"-s", "--secret"}, required=true, description = "api secret")
    private String apiSecret;

    @CommandLine.Option(names = {"-d", "--deleted"}, description = "download deleted (not-active) registrations")
    private boolean deleted = false;

    @CommandLine.Option(names = {"-m", "--modified"}, description = "download ")
    private long modifiedAfterTimestamp = 1;

    public static void main(String[] args) {
        CommandLine.call(new RunSignUpCli(), args);
    }

    @Override
    public Void call() {
        OkHttpClient client = new OkHttpClient();
        ApiCredentials credentials = new ApiCredentials(apiKey, apiSecret);

        RunSignUpConnector connector = new RunSignUpConnector(client, credentials, raceId);

        connector.getRace().run(RunSignUpCli::handleError, race -> {
            log.info("Race: "+ (verbose ? race : raceId));
            connector.getParticipants(race.getEventIds(), deleted, modifiedAfterTimestamp).run(RunSignUpCli::handleError, participants -> {
                log.info((deleted ? "Deleted" : "") + "Participants: "+participants.size());
                final AtomicLong lastModified = new AtomicLong(1);
                participants.forEach(p -> {
                    if(p.getLast_modified() > lastModified.get()){
                        lastModified.set(p.getLast_modified());
                    }
                });
                log.info("Last Modified Time: "+lastModified);
                if(verbose){
                    participants.forEach(participant -> {log.info("\t"+participant);});
                }
            });
        });
        return null;
    }

    private static void handleError(ErrorWithRawJson e){
        log.error("Error making API call: "+e);
    }
}


