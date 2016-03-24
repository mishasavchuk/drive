package ua.edu.lp.drive;

import com.google.api.services.drive.Drive;
import lombok.SneakyThrows;
import ua.edu.lp.drive.file.FilePrinter;
import ua.edu.lp.drive.util.GoogleDriveServiceFactory;

import java.io.IOException;

public final class App {

    @SneakyThrows(IOException.class)
    public static void main(String[] args) {
        Drive myDrive = GoogleDriveServiceFactory.createFor("dobrovolskyi.dmytro");
        Drive mishaDrive = GoogleDriveServiceFactory.createFor("misha.savchuk");

        new FilePrinter(myDrive).printAllFiles();
        System.out.println("===========================================");
        new FilePrinter(mishaDrive).printAllFiles();

    }
}
