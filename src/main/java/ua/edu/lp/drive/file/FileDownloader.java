package ua.edu.lp.drive.file;

import com.google.api.services.drive.Drive;
import lombok.SneakyThrows;
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

    public void toInputStream(String name) throws IOException {
        driveService.files()
                .list()
                .execute()
                .getFiles()
                .stream()
                .filter(file -> file.getName().equals(name))
                .map(this::toInputStream)
                .forEach(inputStream -> writeToDisk(inputStream, name));
    }

    @SneakyThrows(IOException.class)
    private void writeToDisk(InputStream inputStream, String fileName) {
        try (OutputStream out = new FileOutputStream(DOWNLOAD_FOLDER + fileName);
             InputStream in = inputStream
        ) {
            IOUtils.copy(in, out);
            System.out.printf("File %s has been successfully created\n", fileName);
        }
    }

    @SneakyThrows(IOException.class)
    private InputStream toInputStream(com.google.api.services.drive.model.File file) {
        return driveService.files().get(file.getId()).executeMediaAsInputStream();
    }
}
