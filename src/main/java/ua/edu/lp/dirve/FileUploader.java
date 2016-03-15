package ua.edu.lp.dirve;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import ua.edu.lp.dirve.util.GoogleDriveUtils;

import java.io.IOException;
import java.io.InputStream;

public final class FileUploader {
    private final Drive driveService;

    public FileUploader(Drive driveService) {
        this.driveService = driveService;
    }

    public void uploadFile(String path) {
        try (InputStream in = GoogleDriveUtils.class.getResourceAsStream(path)) {
            Assert.notNull(in, String.format("File %s not found", path));

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
