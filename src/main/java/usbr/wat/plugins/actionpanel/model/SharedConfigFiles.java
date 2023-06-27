/*
 *
 *  * Copyright 2023 United States Bureau of Reclamation (USBR).
 *  * United States Department of the Interior
 *  * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 *  * Source may not be released without written approval
 *  * from USBR
 *
 */
package usbr.wat.plugins.actionpanel.model;

import com.rma.model.Project;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class SharedConfigFiles
{
    private static final Path BASE_FOLDER = Paths.get("shared/config");
    private static final String DEFAULT_RIVER_LOCATIONS_FILE = "downstream_control_pts.config";
    private static final String RIVER_LOCATIONS_FILE_PROPERTY = "WTMP.riverLocationsFile";

    private SharedConfigFiles()
    {
        throw new AssertionError("Utility class. Don't instantiate");
    }
    public static Path getRelativeRiverLocationsFile()
    {
        String file = System.getProperty(RIVER_LOCATIONS_FILE_PROPERTY, BASE_FOLDER.resolve(DEFAULT_RIVER_LOCATIONS_FILE).toString());
        return Paths.get(file);
    }

    public static Path getRiverLocationsFile()
    {
        String absFile = Project.getCurrentProject().getAbsolutePath(getRelativeRiverLocationsFile().toString());
        return Paths.get(absFile);
    }

}
