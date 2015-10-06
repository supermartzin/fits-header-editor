package cz.muni.fi.fits.output.writers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for methods of {@link FileOutputWriter} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FileOutputWriterTest {

    private OutputWriter _fileOutputWriter;

    private static final String INFO_MESSAGE_LEADER = "INFO";
    private static final String ERROR_MESSAGE_LEADER = "ERROR";
    private static final String EXCEPTION_MESSAGE_LEADER = "EXCEPTION";
    private static final String UNKNOWN_FILE_SUBSTRING = "Unknown file";
    private static final File TEST_FILE = new File(Paths.get("testOutput.txt").toUri());

    private static final Path OUTPUT_FILE = Paths.get("test-output-file");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        // create output file
        Files.createFile(OUTPUT_FILE);

        _fileOutputWriter = new FileOutputWriter(OUTPUT_FILE.toAbsolutePath().toFile());
    }

    @After
    public void tearDown() throws Exception {
        // delete output file
        Files.deleteIfExists(OUTPUT_FILE);
    }

    @Test
    public void testConstructor1_FilePath_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        new FileOutputWriter((String) null);
    }

    @Test
    public void testConstructor1_FilePath_Illegal() throws Exception {
        String filePath = "..;';;'\\;\\.;.;,;,";

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid filepath");
        new FileOutputWriter(filePath);
    }

    @Test
    public void testConstructor2_File_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        new FileOutputWriter((File) null);
    }

    @Test
    public void testConstructor2_File_Illegal() throws Exception {
        File file = new File("..;';;'\\;\\.;.;,;,");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid file");
        new FileOutputWriter(file);
    }


    @Test
    public void testWriteInfo1_InfoMessage_Null() throws Exception {
        boolean result = _fileOutputWriter.writeInfo(null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteInfo1_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileOutputWriter.writeInfo(message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo1_Correct() throws Exception {
        String message = "Testing";

        boolean result = _fileOutputWriter.writeInfo(message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_File_Null() throws Exception {
        String message = "Testing";

        boolean result = _fileOutputWriter.writeInfo(null, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_InfoMessage_Null() throws Exception {
        boolean result = _fileOutputWriter.writeInfo(TEST_FILE, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteInfo2_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileOutputWriter.writeInfo(TEST_FILE, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_Parameters_Null() throws Exception {
        boolean result = _fileOutputWriter.writeInfo(null, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteInfo2_Parameters_Correct() throws Exception {
        String message = "Testing";

        boolean result = _fileOutputWriter.writeInfo(TEST_FILE, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException1_Exception_Null() throws Exception {
        boolean result = _fileOutputWriter.writeException(null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException1_Exception_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException(exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_ErrorMessage_Null() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException((String) null, exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_ErrorMessage_Empty() throws Exception {
        String message = "";
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException(message, exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_Exception_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _fileOutputWriter.writeException(message, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_Parameters_Null() throws Exception {
        boolean result = _fileOutputWriter.writeException((String) null, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException2_Parameters_Correct() throws Exception {
        String message = "Testing error message";
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException(message, exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException3_File_Null() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException((File) null, exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(!outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException3_Exception_Null() throws Exception {
        boolean result = _fileOutputWriter.writeException(TEST_FILE, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException3_Parameters_Null() throws Exception {
        boolean result = _fileOutputWriter.writeException((File) null, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException3_Parameters_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileOutputWriter.writeException(TEST_FILE, exception);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Null() throws Exception {
        boolean result = _fileOutputWriter.writeError(null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileOutputWriter.writeError(message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _fileOutputWriter.writeError(message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_File_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _fileOutputWriter.writeError(null, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_ErrorMessage_Null() throws Exception {
        boolean result = _fileOutputWriter.writeError(TEST_FILE, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteError2_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileOutputWriter.writeError(TEST_FILE, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_Parameters_Null() throws Exception {
        boolean result = _fileOutputWriter.writeError(null, null);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteError2_Parameters_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _fileOutputWriter.writeError(TEST_FILE, message);

        String outputString = readOutputFile();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    private String readOutputFile() throws IOException {
        return new String(Files.readAllBytes(OUTPUT_FILE), Charset.defaultCharset());
    }
}