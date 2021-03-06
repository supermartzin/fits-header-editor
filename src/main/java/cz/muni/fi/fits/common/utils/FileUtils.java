package cz.muni.fi.fits.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class for smoother working with files and their paths
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public final class FileUtils {

    /**
     * Check whether provided {@link Path} is Windows shortcut,
     * that is it ends with <code>.lnk</code> extension
     *s
     * @param path  path to file to check
     * @return      <code>true</code> if provided <code>path</code> is Windows shorcut,
     *              <code>false</code> otherwise
     */
    public static boolean isWindowsShortcut(Path path) {
        if (path == null)
            throw new IllegalArgumentException("path is null");

        return path.toString().endsWith(".lnk");
    }

    /**
     * Checks whether provided {@link Path} is symbolic link
     *
     * @param path  path to file to check
     * @return      <code>true</code> if provided <code>path</code> is symbolic link,
     *              <code>false</code> otherwise
     */
    public static boolean isSymbolicLink(Path path) {
        if (path == null)
            throw new IllegalArgumentException("path is null");

        return Files.isSymbolicLink(path);
    }

    /**
     * Reads the target of provided {@link Path} that points to symbolic link
     *
     * @param path  path to file with symbolic link
     * @return      target {@link Path} from symbolic link,
     *              or <code>null</code> if target cannot be read
     */
    public static Path readSymbolicLink(Path path) {
        if (path == null)
            throw new IllegalArgumentException("path is null");

        try {
            return Files.readSymbolicLink(path);
        } catch (IOException ioEx) {
            return null;
        }
    }

    /**
     * Checks whether provided {@link String} contains valid parsable {@link Path}
     *
     * @param string    string to check
     * @return          <code>true</code> if provided string contains valid {@link Path},
     *                  <code>false</code> otherwise
     */
    public static boolean isValidPath(String string) {
        if (string == null)
            throw new IllegalArgumentException("string is null");

        try {
            Path path = Paths.get(string);

            if (Files.exists(path)) {
                // if path exists then it's valid
                return true;
            } else {
                // if doesn't exist try to create it and then delete
                Files.createFile(Paths.get(string));
                Files.deleteIfExists(Paths.get(string));
                return true;
            }
        } catch (InvalidPathException
                | IOException ex) {
            return false;
        }
    }

    private FileUtils() {}
}
