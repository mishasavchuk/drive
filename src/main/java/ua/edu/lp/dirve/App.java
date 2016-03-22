package ua.edu.lp.dirve;

import com.google.api.services.drive.Drive;
import ua.edu.lp.dirve.file.FilePrinter;
import ua.edu.lp.dirve.util.GoogleDriveServiceFactory;

public final class App {
    public static void main(String[] args) {
        Drive myDrive = GoogleDriveServiceFactory.createServiceFor("dobrovolskyi.dmytro");
        Drive mishaDrive = GoogleDriveServiceFactory.createServiceFor("misha.savchuk");

        new FilePrinter(myDrive).printAllFiles();
        System.out.println("===========================================");
        new FilePrinter(mishaDrive).printAllFiles();

    }
}
