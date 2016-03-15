package ua.edu.lp.dirve;

import com.google.api.services.drive.Drive;

public final class App {
    public static void main(String[] args) {
        Drive drive = GoogleDriveServiceFactory.INSTANCE.getDefaultOfflineDriveService();

        new FilePrinter(drive).printAllFiles();
        new FileUploader(drive).uploadFile("/drive-api-client-secret.json");
    }
}
