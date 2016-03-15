package ua.edu.lp.dirve;

import com.google.api.services.drive.Drive;
import ua.edu.lp.dirve.util.GoogleDriveUtils;

public enum  GoogleDriveServiceFactory {
    INSTANCE;

    private static final Drive defaultOfflineDrive = GoogleDriveUtils.getDriveService();

    public Drive getDefaultOfflineDriveService() {
        return defaultOfflineDrive;
    }
}
