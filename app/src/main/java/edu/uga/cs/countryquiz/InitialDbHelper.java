package edu.uga.cs.countryquiz;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class InitialDbHelper {

    private static final String DB_NAME = "countryquiz.db";
    private final Context context;

    public InitialDbHelper(Context context) {
        this.context = context;
    }

    public void copyDatabaseIfNeeded() {
        try {
            File dbFile = context.getDatabasePath(DB_NAME);

            if (dbFile.exists()) {
                return;
            }

            File parent = dbFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            InputStream inputStream = context.getAssets().open(DB_NAME);
            OutputStream outputStream = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}