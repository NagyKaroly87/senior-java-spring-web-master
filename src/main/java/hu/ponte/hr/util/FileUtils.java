package hu.ponte.hr.util;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

    public static String getExtensionWithDot(String filename) {
        return "." + FilenameUtils.getExtension(filename);
    }

}
