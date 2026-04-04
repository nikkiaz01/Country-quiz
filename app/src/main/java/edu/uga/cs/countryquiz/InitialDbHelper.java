package edu.uga.cs.countryquiz;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Helper class used to copy an already populated database from the assets folder
 * into the app’s internal database directory.
 */
public class InitialDbHelper {
    /** Name of the database file stored in assets. */
    private static final String DB_NAME = "countryquiz.db";
    private final Context context;

    /**
     * Constructor that initializes the helper with a context.
     *
     * @param context application context
     */
    public InitialDbHelper(Context context) {
        this.context = context;
    }

    /**
     * Copies the database from the assets folder to the app's
     * internal database directory if it does not already exist.
     */
    public void copyDatabaseIfNeeded() {
        try {
            // Get the path where the database should be stored on the device
            File dbFile = context.getDatabasePath(DB_NAME);

            if (dbFile.exists()) {
                return;
            }
            // Ensure the parent directory (usually /databases/) exists
            File parent = dbFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            // Open the database file from the assets folder
            InputStream inputStream = context.getAssets().open(DB_NAME);
            // Create an output stream to write to the device database location
            OutputStream outputStream = new FileOutputStream(dbFile);
            // Buffer used to copy data in chunks
            byte[] buffer = new byte[1024];
            int length;
            // Read from input and write to output until done
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            // Make sure all data is written
            outputStream.flush();
            // Close streams to prevent memory leaks
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}