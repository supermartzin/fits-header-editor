package cz.muni.fi.fits.utils.loaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for loading user defined properties from file
 *
 * @author Martin Vrábel
 * @version 1.0.1
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
    public static Properties loadProperties(Class<?> clazz, String filePath) throws IOException {
        Properties props = new Properties();

        props.load(clazz.getResourceAsStream(filePath));

        return props;
    }

    /**
     * Loads properties from specified properties file
     *
     * @param filePath      path to properties file
     * @return              object with properties
     * @throws IOException  when some error occurs during reading properties
     */
    public static Properties loadProperties(String filePath) throws IOException {
        Properties props = new Properties();
        
        try (FileInputStream file = new FileInputStream(filePath)) {
            props.load(file);
        }

        return props;
    }
}
