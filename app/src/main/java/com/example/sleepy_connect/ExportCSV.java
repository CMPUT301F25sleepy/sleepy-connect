package com.example.sleepy_connect;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportCSV {
     // This function makes the CSV file once it is called, basically a fancy spreadsheet
     public void exportCSVFile(Context context, Event event, String fileName){
         List<String> acceptedList = event.getAcceptedList();
         // Makes a new File object for the CSV
         File csvFile = new File(context.getExternalFilesDir(null), fileName);

         // Tries to append the entrants on acceptedList
         try (FileWriter writer = new FileWriter(csvFile)) {
             for (String item : acceptedList) {
                 writer.append(item);
                 // A new row for the CSV file
                 writer.append("\n");
             }
             // Clears writer
             writer.flush();
             // Reports if the export succeeded or not
             Log.d("CSV Export", "File saved: " + csvFile.getAbsolutePath());
         } catch (IOException e) {
             Log.e("CSV Export", "Error writing CSV file", e);
         }
     }
     // How to use this method: exportCSVFile(this, event, "events.csv");
     // Where event is an Event object and "events.csv" is the filename
     // Saves the people as an Android ID and not their name but I dont want to deal with that rn :)
}
