package cz.muni.fi.fits.common.loaders;

import cz.muni.fi.fits.common.Configuration;
import cz.muni.fi.fits.common.exceptions.ConfigurationException;
import cz.muni.fi.fits.common.utils.Placeholders;
import cz.muni.fi.fits.output.writers.OutputWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link ConfigurationLoader} helper class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ConfigurationLoaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testLoadConfiguration_Properties_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        ConfigurationLoader.loadConfiguration(null);
    }

    @Test
    public void testLoadConfiguration_Properties_Empty() throws Exception {
        Properties properties = new Properties();

        exception.expect(ConfigurationException.class);
        exception.expectMessage("empty");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_NotPresent() throws Exception {
        Properties properties = new Properties();
        properties.put("property", "value");

        exception.expect(ConfigurationException.class);
        exception.expectMessage("Cannot find property");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_Empty() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "");

        exception.expect(ConfigurationException.class);
        exception.expectMessage("Cannot find property");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_UnknownValue() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "server");

        exception.expect(ConfigurationException.class);
        exception.expectMessage("Unknown value for property");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_Console_CorrectValue() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "console");

        Configuration configuration = ConfigurationLoader.loadConfiguration(properties);
        assertNotNull(configuration);
        assertEquals(OutputWriter.Type.CONSOLE, configuration.getOutputWriterType());
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_File_MissingOutputFileProperty() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "file");

        exception.expect(ConfigurationException.class);
        exception.expectMessage("Cannot find property");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_File_CorrectValue() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "file");
        properties.put(Placeholders.OUTPUT_FILE_PROPERTY, "output.txt");

        Configuration configuration = ConfigurationLoader.loadConfiguration(properties);
        assertNotNull(configuration);
        assertEquals(OutputWriter.Type.FILE, configuration.getOutputWriterType());
        assertEquals("output.txt", configuration.getOutputFilePath());
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_FileAndConsole_MissingOutputFileProperty() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "file, console");

        exception.expect(ConfigurationException.class);
        exception.expectMessage("Cannot find property");
        ConfigurationLoader.loadConfiguration(properties);
    }

    @Test
    public void testLoadConfiguration_OutputWriterProperty_FileAndConsole_CorrectValue() throws Exception {
        Properties properties = new Properties();
        properties.put(Placeholders.OUTPUT_WRITER_PROPERTY, "file, console");
        properties.put(Placeholders.OUTPUT_FILE_PROPERTY, "output.txt");

        Configuration configuration = ConfigurationLoader.loadConfiguration(properties);
        assertNotNull(configuration);
        assertEquals(OutputWriter.Type.FILE_AND_CONSOLE, configuration.getOutputWriterType());
        assertEquals("output.txt", configuration.getOutputFilePath());
    }
}