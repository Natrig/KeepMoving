package rus.app.keepmoving.Util;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {
    /**
     * Search a directory and return a lsit of all **directories** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPath (String directory) {
        ArrayList<String> pathArray = new ArrayList<>();

        File file = new File(directory);

        File[] listFiles = file.listFiles();

        for (File fileInList :listFiles) {
            if (fileInList.isDirectory()) {
                pathArray.add(fileInList.getAbsolutePath());
            }
        }

        return pathArray;
    }

    /**
     * Search a directory and return a lsit of all **files** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePath(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();

        File file = new File(directory);

        File[] listFiles = file.listFiles();

        if (listFiles == null) {
            return pathArray;
        }

        for (File fileInList :listFiles) {
            if (fileInList.isFile()) {
                pathArray.add(fileInList.getAbsolutePath());
            }
        }

        return pathArray;
    }
}
