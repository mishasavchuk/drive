package ua.edu.lp.drive.file;

import com.google.api.services.drive.Drive;

import java.io.IOException;

public final class FilePrinter {
    private final Drive driveService;

    public FilePrinter(Drive driveService) {
        this.driveService = driveService;
    }

    public void printAllFiles() throws IOException {
        driveService.files()
                .list()
                .execute()
                .getFiles()
                .stream()
                .forEach(file -> System.out.println(file.getName()));
    }
}
