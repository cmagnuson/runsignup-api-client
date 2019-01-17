package com.mtecresults.runsignup.api.client.controller;

import com.mtecresults.runsignup.api.client.model.export.Column;
import com.mtecresults.runsignup.api.client.model.gson.Address;
import com.mtecresults.runsignup.api.client.model.gson.Participant;
import com.mtecresults.runsignup.api.client.model.gson.Race;
import com.mtecresults.runsignup.api.client.model.gson.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
public class CsvExporter {

    private final List<Participant> participants;
    private final Race race;

    public void exportToFile(File f, boolean overwriteIfExisting) {
        if(f.exists()){
            if(overwriteIfExisting){
                f.delete();
            }
            else {
                log.error("Unable to write to file: " + f.getAbsolutePath() + " it already exists");
                return;
            }
        }
        try (CsvListWriter writer = new CsvListWriter(new BufferedWriter(new FileWriter(f)),CsvPreference.STANDARD_PREFERENCE)) {
            List<Column> columns = getColumns(participants, race);
            writer.write(columns.stream().map(Column::getName).collect(Collectors.toList()));
            for(Participant p: participants){
                writer.write(getRow(columns, p));
            }
        }
        catch (IOException io){
            log.error("Error writing to file", io);
        }
    }

    protected List<Column> getColumns(List<Participant> participants, Race race){
        List<Column> columns = getDefaultColumns();
        for(Participant p: participants){

        }
        return columns;
    }

    protected List<String> getRow(List<Column> columns, Participant p){
        return columns.stream().map(c -> {
            try {
                return c.getAccessor().apply(p);
            } catch (NullPointerException npe) {
                //this field is non-existant for this participant
                //just put a blank
                return "";
            }
        }).collect(Collectors.toList());
    }

    protected List<Column> getDefaultColumns() {
        List<Column> defaultColumns = new ArrayList<>();
        defaultColumns.addAll(User.getDefaultColumns());
        defaultColumns.addAll(Address.getDefaultColumns());
        return defaultColumns;
    }
}
