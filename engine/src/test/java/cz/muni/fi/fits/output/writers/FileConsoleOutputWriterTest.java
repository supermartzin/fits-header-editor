package cz.muni.fi.fits.output.writers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for methods of {@link FileConsoleOutputWriter} class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FileConsoleOutputWriterTest {

    private OutputWriter _fileConsoleOutputWriter;

    private static final String INFO_MESSAGE_LEADER = "INFO";
    private static final String ERROR_MESSAGE_LEADER = "ERROR";
    private static final String EXCEPTION_MESSAGE_LEADER = "EXCEPTION";
    private static final String UNKNOWN_FILE_SUBSTRING = "Unknown file";
    private static final File TEST_FILE = new File(Paths.get("testOutput.txt").toUri());

    private static final Path OUTPUT_FILE = Paths.get("test-output-file");
    private static final PrintStream ORIGINAL_STDOUT = System.out;
    private static final PrintStream ORIGINAL_STDERR = System.err;
    private OutputStream _standardOutputStream;
    private OutputStream _errorOutputStream;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        // initialize proxy output streams
        _standardOutputStream = new ByteArrayOutputStream();
        _errorOutputStream = new ByteArrayOutputStream();

        // temporarily replace original System outputs
        System.setOut(new PrintStream(_standardOutputStream));
        System.setErr(new PrintStream(_errorOutputStream));

        // create output file
        Files.createFile(OUTPUT_FILE);

        _fileConsoleOutputWriter = new FileConsoleOutputWriter(OUTPUT_FILE.toAbsolutePath().toFile());
    }

    @After
    public void tearDown() throws Exception {
        System.out.flush();
        System.err.flush();

        // set back original System output
        System.setOut(ORIGINAL_STDOUT);
        System.setErr(ORIGINAL_STDERR);

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
        boolean result = _fileConsoleOutputWriter.writeInfo(null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteInfo1_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileConsoleOutputWriter.writeInfo(message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(INFO_MESSAGE_LEADER));

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(INFO_MESSAGE_LEADER));
    }

    @Test
    public void testWriteInfo1_Correct() throws Exception {
        String message = "Testing";

        boolean result = _fileConsoleOutputWriter.writeInfo(message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(INFO_MESSAGE_LEADER));

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(INFO_MESSAGE_LEADER));
    }

    @Test
    public void testWriteInfo2_File_Null() throws Exception {
        String message = "Testing";

        boolean result = _fileConsoleOutputWriter.writeInfo(null, message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(fileOutputString.contains(INFO_MESSAGE_LEADER));

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(consoleOutputString.contains(INFO_MESSAGE_LEADER));
    }

    @Test
    public void testWriteInfo2_InfoMessage_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeInfo(TEST_FILE, null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteInfo2_InfoMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileConsoleOutputWriter.writeInfo(TEST_FILE, message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(INFO_MESSAGE_LEADER));

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(TEST_FILE.getName()));
        assertTrue(consoleOutputString.contains(INFO_MESSAGE_LEADER));
    }

    @Test
    public void testWriteInfo2_Parameters_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeInfo(null, null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteInfo2_Parameters_Correct() throws Exception {
        String message = "Testing";

        boolean result = _fileConsoleOutputWriter.writeInfo(TEST_FILE, message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(INFO_MESSAGE_LEADER));

        String consoleOutputString = _standardOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(TEST_FILE.getName()));
        assertTrue(consoleOutputString.contains(INFO_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException1_Exception_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeException(null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteException1_Exception_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException(exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(exception.getMessage()));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(exception.getMessage()));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException2_ErrorMessage_Null() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException((String) null, exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(exception.getMessage()));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(exception.getMessage()));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException2_ErrorMessage_Empty() throws Exception {
        String message = "";
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException(message, exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(exception.getMessage()));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(exception.getMessage()));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException2_Exception_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _fileConsoleOutputWriter.writeException(message, null);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException2_Parameters_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeException((String) null, null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteException2_Parameters_Correct() throws Exception {
        String message = "Testing error message";
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException(message, exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException3_File_Null() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException((File) null, exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(exception.getMessage()));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(!fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(exception.getMessage()));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteException3_Exception_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeException(TEST_FILE, null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteException3_Parameters_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeException((File) null, null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteException3_Parameters_Correct() throws Exception {
        Throwable exception = new IllegalArgumentException("Testing exception message");
        String testingExceptionType = "IllegalArgumentException";

        boolean result = _fileConsoleOutputWriter.writeException(TEST_FILE, exception);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(exception.getMessage()));
        assertTrue(fileOutputString.contains(testingExceptionType));
        assertTrue(fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(EXCEPTION_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(exception.getMessage()));
        assertTrue(consoleOutputString.contains(testingExceptionType));
        assertTrue(consoleOutputString.contains(TEST_FILE.getName()));
        assertTrue(consoleOutputString.contains(EXCEPTION_MESSAGE_LEADER));
    }

    @Test
    public void testWriteError1_ErrorMessage_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeError(null);

        assertFalse(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteError1_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileConsoleOutputWriter.writeError(message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    @Test
    public void testWriteError1_ErrorMessage_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _fileConsoleOutputWriter.writeError(message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    @Test
    public void testWriteError2_File_Null() throws Exception {
        String message = "Testing error message";

        boolean result = _fileConsoleOutputWriter.writeError(null, message);

        assertTrue(result);

        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(UNKNOWN_FILE_SUBSTRING));
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    @Test
    public void testWriteError2_ErrorMessage_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeError(TEST_FILE, null);

        assertFalse(result);
        
        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteError2_ErrorMessage_Empty() throws Exception {
        String message = "";

        boolean result = _fileConsoleOutputWriter.writeError(TEST_FILE, message);

        assertTrue(result);
        
        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(TEST_FILE.getName()));
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    @Test
    public void testWriteError2_Parameters_Null() throws Exception {
        boolean result = _fileConsoleOutputWriter.writeError(null, null);

        assertFalse(result);
        
        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.isEmpty());

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.isEmpty());
    }

    @Test
    public void testWriteError2_Parameters_Correct() throws Exception {
        String message = "Testing error message";

        boolean result = _fileConsoleOutputWriter.writeError(TEST_FILE, message);

        assertTrue(result);
        
        String fileOutputString = readOutputFile();
        assertNotNull(fileOutputString);
        assertTrue(fileOutputString.contains(message));
        assertTrue(fileOutputString.contains(TEST_FILE.getName()));
        assertTrue(fileOutputString.contains(ERROR_MESSAGE_LEADER));

        String consoleOutputString = _errorOutputStream.toString();
        assertNotNull(consoleOutputString);
        assertTrue(consoleOutputString.contains(message));
        assertTrue(consoleOutputString.contains(TEST_FILE.getName()));
        assertTrue(consoleOutputString.contains(ERROR_MESSAGE_LEADER));
    }

    private String readOutputFile() throws IOException {
        return new String(Files.readAllBytes(OUTPUT_FILE), Charset.defaultCharset());
    }
}