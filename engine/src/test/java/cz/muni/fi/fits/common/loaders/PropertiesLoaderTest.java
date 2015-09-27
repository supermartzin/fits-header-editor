package cz.muni.fi.fits.common.loaders;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link PropertiesLoader} helper class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class PropertiesLoaderTest {

    private static final String TEST_PROPERTIES_FILE_PATH = "/test.properties";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testLoadProperties_FilePath_Null_1() throws Exception {
        exception.expect(IllegalArgumentException.class);
        PropertiesLoader.loadProperties(null);
    }

    @Test
    public void testLoadProperties_FilePath_Null_2() throws Exception {
        exception.expect(IllegalArgumentException.class);
        PropertiesLoader.loadProperties(PropertiesLoader.class, null);
    }

    @Test
    public void testLoadProperties_FilePath_Incorrect_1() throws Exception {
        exception.expect(IOException.class);
        PropertiesLoader.loadProperties("random/file/path");
    }

    @Test
    public void testLoadProperties_FilePath_Incorrect_2() throws Exception {
        exception.expect(IOException.class);
        PropertiesLoader.loadProperties(PropertiesLoader.class, "random/file/path");
    }

    @Test
    public void testLoadProperties_Class_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        PropertiesLoader.loadProperties(null, "path/to/file");
    }

    @Test
    public void testLoadProperties_Correct_1() throws Exception {
        String filePath = getClass().getResource(TEST_PROPERTIES_FILE_PATH).toURI().getPath();
        Properties properties = PropertiesLoader.loadProperties(filePath);

        assertNotNull(properties);
    }

    @Test
    public void testLoadProperties_Correct_2() throws Exception {
        Properties properties = PropertiesLoader.loadProperties(PropertiesLoaderTest.class, TEST_PROPERTIES_FILE_PATH);

        assertNotNull(properties);
    }
}