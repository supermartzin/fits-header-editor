package cz.muni.fi.fits.common.loaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for loading user defined properties from file
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public final class PropertiesLoader {

    /**
     * Loads properties from specified properties file using spcified <code>mainClass</code>
     * class loader
     *
     * @param clazz         class containing <code>main</code> method
     * @param filePath      path to properties file
     * @return              object with properties
     * @throws IOException  when some error occurs during reading properties
     */
    public static Properties loadProperties(Class<?> clazz, String filePath)
            throws IOException {
        if (clazz == null)
            throw new IllegalArgumentException("class parameter is null");
        if (filePath == null)
            throw new IllegalArgumentException("filePath is null");

        Properties props = new Properties();

        try {
            props.load(clazz.getResourceAsStream(filePath));
        } catch (NullPointerException npEx) {
            throw new IOException("File does not exists: " + filePath);
        }

        return props;
    }

    /**
     * Loads properties from specified properties file
     *
     * @param filePath      path to properties file
     * @return              object with properties
     * @throws IOException  when some error occurs during reading properties
     */
    public static Properties loadProperties(String filePath)
            throws IOException {
        if (filePath == null)
            throw new IllegalArgumentException("filePath is null");

        Properties props = new Properties();
        
        try (FileInputStream file = new FileInputStream(filePath)) {
            props.load(file);
        }

        return props;
    }
}
