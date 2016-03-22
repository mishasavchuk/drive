package ua.edu.lp.dirve.file;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang3.StringUtils;
import ua.edu.lp.dirve.util.GoogleDriveServiceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class FileUploader {
    private final Drive driveService;

    public FileUploader(Drive driveService) {
        this.driveService = driveService;
    }

    public void uploadFile(String path) {
        try (InputStream in = GoogleDriveServiceFactory.class.getResourceAsStream(path)) {
            Objects.requireNonNull(in, String.format("File %s not found", path));

            File content = new File();
            String name = StringUtils.substringAfterLast(path, "/");
            content.setName(name);

            driveService.files()
                    .create(content, new InputStreamContent("*/*", in))
                    .execute();

            System.out.printf("File %s was successfully uploaded", name);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
