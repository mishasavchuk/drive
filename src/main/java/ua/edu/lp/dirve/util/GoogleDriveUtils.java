package ua.edu.lp.dirve.util;

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public final class GoogleDriveUtils {
    private static final String APPLICATION_NAME = "drive";
    private static final String ACCESS_TYPE = "offline";
    private static final String CREDENTIALS_FILE_NAME = ".credentials/drive.json";
    private static final String CLIENT_SECRET_FILE_NAME = "/drive-api-client-secret.json";
    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), CREDENTIALS_FILE_NAME);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static final FileDataStoreFactory DATA_STORE_FACTORY;
    private static final HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException | GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     */
    public static Credential authorize() {
        try (InputStream in = GoogleDriveUtils.class.getResourceAsStream(CLIENT_SECRET_FILE_NAME)) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets, SCOPES)
                    .setDataStoreFactory(DATA_STORE_FACTORY)
                    .setAccessType(ACCESS_TYPE)
                    .build();

            Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
            System.out.printf("Credentials saved to %s\n", DATA_STORE_DIR.getAbsolutePath());
            return credential;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     */
    public static Drive getDriveService() {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
