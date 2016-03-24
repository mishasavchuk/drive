package ua.edu.lp.drive.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public final class GoogleDriveServiceFactory {
    private static final String ACCESS_TYPE = "offline";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final String username;

    @SneakyThrows({IOException.class, GeneralSecurityException.class})
    private GoogleDriveServiceFactory(String username) {
        this.username = username;
        this.jsonFactory = JacksonFactory.getDefaultInstance();
        this.httpTransport =  GoogleNetHttpTransport.newTrustedTransport();
    }

    public static Drive createFor(String username) throws IOException {
        return new GoogleDriveServiceFactory(username).getDriveService();
    }

    private Drive getDriveService() throws IOException {
        return new Drive.Builder(httpTransport, jsonFactory, authorize())
                .setApplicationName(String.format("%s-drive", username))
                .build();
    }

    private Credential authorize() throws IOException {
        try (InputStream in = GoogleDriveServiceFactory.class.getResourceAsStream(
                String.format("/%s-client-secret.json", username)
        )) {
            printHeader();

            File dataStoreDir = new File(System.getProperty("user.home"), username);
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                    clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(dataStoreDir))
                    .setAccessType(ACCESS_TYPE)
                    .build();

            Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(username);
            System.out.printf("Credentials saved to %s\n", dataStoreDir.getAbsolutePath());
            return credential;
        }
    }

    private void printHeader() {
        System.out.println("**************************************");
        System.out.printf("Trying to login as %s\n", username);
        System.out.println("**************************************");
    }
}
