package cz.muni.fi.fits.output.writers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Tests for methods of {@link ConsoleOutputWriter} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ConsoleOutputWriterTest {

    private OutputWriter _consoleOutputWriter;

    private static final String INFO_MESSAGE_LEADER = "INFO";
    private static final String ERROR_MESSAGE_LEADER = "ERROR";
    private static final String EXCEPTION_MESSAGE_LEADER = "EXCEPTION";
    private static final String UNKNOWN_FILE_SUBSTRING = "Unknown file";
    private static final File TEST_FILE = new File(Paths.get("testOutput.txt").toUri());

    private static final PrintStream ORIGINAL_STDOUT = System.out;
    private static final PrintStream ORIGINAL_STDERR = System.err;
    private OutputStream _standardOutputStream;
    private OutputStream _errorOutputStream;


    @Before
    public void setUp() throws Exception {
        // initialize proxy output streams
        _standardOutputStream = new ByteArrayOutputStream();
        _errorOutputStream = new ByteArrayOutputStream();

        // temporarily replace original System outputs
        System.setOut(new PrintStream(_standardOutputStream));
        System.setErr(new PrintStream(_errorOutputStream));

        _consoleOutputWriter = new ConsoleOutputWriter();
    }

    @After
    public void tearDown() throws Exception {
        System.out.flush();
        System.err.flush();

        // set back original System output
        System.setOut(ORIGINAL_STDOUT);
        System.setErr(ORIGINAL_STDERR);
    }

    @Test
    public void testWriteInfo1_InfoMessage_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeInfo(null);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteInfo1_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _consoleOutputWriter.writeInfo(message);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo1_Correct() throws Exception {
        String message = "Testing";

        boolean result = _consoleOutputWriter.writeInfo(message);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_File_Null() throws Exception {
        String message = "Testing";

        boolean result = _consoleOutputWriter.writeInfo(null, message);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_InfoMessage_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeInfo(TEST_FILE, null);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteInfo2_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _consoleOutputWriter.writeInfo(TEST_FILE, message);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteInfo2_Parameters_Correct() throws Exception {
        String message = "Testing";

        boolean result = _consoleOutputWriter.writeInfo(TEST_FILE, message);

        String outputString = _standardOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(INFO_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException1_Exception_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeException(null);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException1_Exception_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _consoleOutputWriter.writeException(exception);

        String outputString = _errorOutputStream.toString();
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

        boolean result = _consoleOutputWriter.writeException((String) null, exception);

        String outputString = _errorOutputStream.toString();
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

        boolean result = _consoleOutputWriter.writeException(message, exception);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_Exception_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _consoleOutputWriter.writeException(message, null);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException2_Parameters_Correct() throws Exception {
        String message = "Testing error message";
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _consoleOutputWriter.writeException(message, exception);

        String outputString = _errorOutputStream.toString();
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

        boolean result = _consoleOutputWriter.writeException((File) null, exception);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(!outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteException3_Exception_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeException(TEST_FILE, null);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteException3_Parameters_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _consoleOutputWriter.writeException(TEST_FILE, exception);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(exception.getMessage()));
        assertTrue(outputString.contains(testingExceptionType));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(EXCEPTION_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeError(null);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _consoleOutputWriter.writeError(message);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError1_ErrorMessage_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _consoleOutputWriter.writeError(message);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_File_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _consoleOutputWriter.writeError(null, message);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_ErrorMessage_Null() throws Exception {
        boolean result = _consoleOutputWriter.writeError(TEST_FILE, null);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.isEmpty());
        assertFalse(result);
    }

    @Test
    public void testWriteError2_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _consoleOutputWriter.writeError(TEST_FILE, message);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }

    @Test
    public void testWriteError2_Parameters_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _consoleOutputWriter.writeError(TEST_FILE, message);

        String outputString = _errorOutputStream.toString();
        assertNotNull(outputString);
        assertTrue(outputString.contains(message));
        assertTrue(outputString.contains(TEST_FILE.getName()));
        assertTrue(outputString.contains(ERROR_MESSAGE_LEADER));
        assertTrue(result);
    }
}