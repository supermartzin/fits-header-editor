package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.exceptions.IllegalInputDataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for extraction of files input data
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.3
 */
public class ProcessorHelper_ExtractFilesDataTest {

    private static final Path DIRECTORY1 = Paths.get("test-directory1");
    private static final Path DIRECTORY2 = Paths.get("test-directory2");
    private static final Path DIRECTORY3 = Paths.get("test-directory3");
    private static final Path NESTED_DIRECTORY = Paths.get(DIRECTORY3.toString() + "/nested-directory");

    private static final Path SAMPLE1 = Paths.get("sample1.fits");
    private static final Path SAMPLE2 = Paths.get("sample2.fits");
    private static final Path SAMPLE3 = Paths.get("sample3.fits");
    private static final Path SAMPLE4 = Paths.get(DIRECTORY2 + File.separator + "sample4.fits");
    private static final Path SAMPLE5 = Paths.get(DIRECTORY2 + File.separator + "sample5.fits");
    private static final Path SAMPLE6 = Paths.get(DIRECTORY3 + File.separator + "sample6.fits");
    private static final Path SAMPLE7 = Paths.get(NESTED_DIRECTORY + File.separator + "sample7.fits");

    private static final Path FILE_PATH = Paths.get("test-files.in");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Files.createDirectory(DIRECTORY1);
        Files.createDirectory(DIRECTORY2);
        Files.createDirectory(DIRECTORY3);
        Files.createDirectory(NESTED_DIRECTORY);

        Files.createFile(FILE_PATH);
        Files.createFile(SAMPLE1);
        Files.createFile(SAMPLE2);
        Files.createFile(SAMPLE3);
        Files.createFile(SAMPLE4);
        Files.createFile(SAMPLE5);
        Files.createFile(SAMPLE6);
        Files.createFile(SAMPLE7);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(FILE_PATH);
        Files.deleteIfExists(SAMPLE1);
        Files.deleteIfExists(SAMPLE2);
        Files.deleteIfExists(SAMPLE3);
        Files.deleteIfExists(SAMPLE4);
        Files.deleteIfExists(SAMPLE5);
        Files.deleteIfExists(SAMPLE6);
        Files.deleteIfExists(SAMPLE7);

        Files.deleteIfExists(NESTED_DIRECTORY);
        Files.deleteIfExists(DIRECTORY1);
        Files.deleteIfExists(DIRECTORY2);
        Files.deleteIfExists(DIRECTORY3);
    }

    @Test
    public void testExtractFilesData_NullFilePath() throws Exception {
        exception.expect(IllegalArgumentException.class);
        CmdArgumentsProcessorHelper.extractFilesData(null);
    }

    @Test
    public void testExtractFilesData_FilePathDoesNotExists() throws Exception {
        Path path = Paths.get("files_not_exist.in");

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("does not exist");
        CmdArgumentsProcessorHelper.extractFilesData(path.toString());
    }

    @Test
    public void testExtractFilesData_DirectoryPathDoesNotExists() throws Exception {
        Path path = Paths.get("directory_not_exist");

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("does not exist");
        CmdArgumentsProcessorHelper.extractFilesData(path.toString());
    }

    @Test
    public void testExtractFilesData_FileIsEmpty() throws Exception {
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertTrue(fitsFiles.isEmpty());
    }

    @Test
    public void testExtractFilesData_DirectoryIsEmpty() throws Exception {
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY1.toString());

        assertTrue(fitsFiles.isEmpty());
    }

    @Test
    public void testExtractFilesData_OnlyCommentedPaths() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                "# "   + SAMPLE1.toString(),
                "#"    + SAMPLE2.toString(),
                "### " + DIRECTORY1.toString());
        Files.write(FILE_PATH, fitsFilesLines);

        HashSet<File> fitsFiles = new HashSet<>(CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString()));

        assertTrue(fitsFiles.size() == 0);
    }

    @Test
    public void testExtractFilesData_CorrectFile() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                       SAMPLE1.toString(),
                       SAMPLE2.toString(),
                       SAMPLE3.toString(),
                "##" + SAMPLE4.toString(),
                       "sample6.fits");
        Files.write(FILE_PATH, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExtractFilesData_CorrectDirectory() throws Exception {
        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY2.toString());

        assertNotNull(files);
        assertEquals(2, files.size());
    }

    @Test
    public void testExtractFilesData_FilesAndDirsInInputFile() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY1.toString(),
                "nonexistent-dir1",
                "nonexistent-dir2");
        Files.write(FILE_PATH, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertNotNull(files);
        assertEquals(4, files.size());
    }

    @Test
    public void testExtractFilesData_OnlyDirsInInputFile() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                DIRECTORY1.toString(),
                DIRECTORY2.toString(),
                DIRECTORY3.toString());
        Files.write(FILE_PATH, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExtractFilesData_SamePathsInInputFile() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE1.toString(),
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY3.toString());
        Files.write(FILE_PATH, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertNotNull(files);
        assertEquals(5, files.size());
    }

    @Test
    public void testExtractFilesData_NestedDirectoryInInputFile() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE2.toString(),
                DIRECTORY3.toString());
        Files.write(FILE_PATH, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_PATH.toString());

        assertNotNull(files);
        assertEquals(2, files.size());
    }

    @Test
    public void testExtractFilesData_DirectoryWithNestedDirectory() throws Exception {
        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY3.toString());

        assertNotNull(files);
        assertEquals(1, files.size());
    }
}
