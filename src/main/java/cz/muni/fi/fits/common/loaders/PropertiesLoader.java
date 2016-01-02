package cz.muni.fi.fits.common.loaders;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Class for loading user defined properties from file
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public final class PropertiesLoader {

    /**
     * Charset with which to load properties from file
     */
    private static final String PROPERTIES_CHARSET = "UTF-8";

    /**
     * Loads properties from specified properties file with <code>UTF-8</code> encoding
     * using spcified <code>mainClass</code> class loader
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
        InputStreamReader reader = null;

        try {
            URL resource = clazz.getResource(filePath);
            reader = new InputStreamReader(resource.openStream(), PROPERTIES_CHARSET);
            props.load(reader);
        } catch (NullPointerException npEx) {
            throw new IOException("File does not exists: " + filePath);
        } finally {
            // close reader
            if (reader != null)
                reader.close();
        }

        return props;
    }

    /**
     * Loads properties from specified properties file with <code>UTF-8</code> encoding
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
        
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), PROPERTIES_CHARSET)) {
            props.load(reader);
        }

        return props;
    }
}
