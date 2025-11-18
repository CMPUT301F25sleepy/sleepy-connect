package com.example.sleepy_connect;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.sleepy_connect.eventdetails.EventDetailsFragment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * class to export the final list of enrolled entrants is a CSV format
 */
public class ExportCSV {

    private static final String TAG = "CSV Export";

    public ExportCSV() {
        // Empty constructor
    }

    public void exportCSVFile(Context context, Event event, String fileName) {
        if (event == null || event.getAcceptedList() == null || event.getAcceptedList().isEmpty()) {
            Toast.makeText(context, "No data to export", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> acceptedList = event.getAcceptedList();

        try {
            Uri fileUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ → use MediaStore to save in Downloads
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                fileUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            } else {
                // Older Android → save to external files dir
                java.io.File file = new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                fileUri = Uri.fromFile(file);
            }

            if (fileUri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(fileUri)) {
                    StringBuilder sb = new StringBuilder();
                    // Add a header row (optional)
                    sb.append("Accepted Users\n");

                    for (String item : acceptedList) {
                        sb.append(item).append("\n");
                    }

                    outputStream.write(sb.toString().getBytes());
                    outputStream.flush();

                    Toast.makeText(context, "CSV exported successfully!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "CSV saved to: " + fileUri.toString());
                }
            } else {
                Toast.makeText(context, "Failed to create CSV file", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to create CSV file URI");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error exporting CSV", e);
            Toast.makeText(context, "Error exporting CSV: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
     // How to use this method: exportCSVFile(this, event, "events.csv");
     // Where event is an Event object and "events.csv" is the filename
     // Saves the people as an Android ID and not their name but I dont want to deal with that rn :)
}
