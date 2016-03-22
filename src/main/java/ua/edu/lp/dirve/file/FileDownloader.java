package ua.edu.lp.dirve.file;

import com.google.api.services.drive.Drive;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileDownloader {
    private static final String DOWNLOAD_FOLDER = "download/";
    private final Drive driveService;

    public FileDownloader(Drive driveService) {
        this.driveService = driveService;
    }

    public void downloadFile(String name) {
        try {
            driveService.files()
                    .list()
                    .execute()
                    .getFiles()
                    .stream()
                    .filter(file -> file.getName().equals(name))
                    .map(this::downloadFile)
                    .forEach(inputStream -> writeToDisk(inputStream, name));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeToDisk(InputStream inputStream, String fileName) {
        try (OutputStream out = new FileOutputStream(DOWNLOAD_FOLDER + fileName)) {
            IOUtils.copy(inputStream, out);
            System.out.printf("File %s has been successfully created\n", fileName);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private InputStream downloadFile(com.google.api.services.drive.model.File file) {
        try {
            return driveService.files().get(file.getId()).executeMediaAsInputStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
