package ru.goryachev.chatfb;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConnectConfig {

    private FirebaseDatabase database = null;
    private static ConnectConfig instance = null;

    public ConnectConfig() {
        init();
    }

    private void init() {
        try {
            Properties properties = new Properties();
            properties.loadFromXML(new FileInputStream("properties.xml"));
            String firebaseCredentialsFilename = properties.getProperty("firebase_admin_sdk_service_key_filename");
            String firebaseDbUrl = properties.getProperty("firebase_db_url");

            FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsFilename);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(firebaseDbUrl).build();

            FirebaseApp defaultApp = FirebaseApp.initializeApp(options); // Access to Firebase

            database = FirebaseDatabase.getInstance(defaultApp);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static ConnectConfig getInstance() {
        if (instance == null) {
            instance = new ConnectConfig();
        }

        return instance;
    }

    public DatabaseReference getDBRef(String path) {
        return database.getReference(path);
    }

}
