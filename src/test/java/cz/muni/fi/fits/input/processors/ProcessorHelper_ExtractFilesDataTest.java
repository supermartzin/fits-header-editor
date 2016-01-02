package cz.muni.fi.fits.input.processors;

import cz.muni.fi.fits.common.exceptions.IllegalInputDataException;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for extraction of files input data
 * in {@link CmdArgumentsProcessorHelper} class
 *
 * @author Martin Vrábel
 * @version 1.4
 */
public class ProcessorHelper_ExtractFilesDataTest {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    // directories
    private Path DIRECTORY1;
    private Path DIRECTORY2;
    private Path DIRECTORY3;
    private Path DIRECTORY4;
    private Path NESTED_DIRECTORY;

    // files
    private Path SAMPLE1;
    private Path SAMPLE2;
    private Path SAMPLE3;
    private Path SAMPLE4;
    private Path SAMPLE5;
    private Path SAMPLE6;
    private Path SAMPLE7;
    private Path SAMPLE8;

    private Path SHORTCUT;

    private static final Path FILE_IN = Paths.get("test-files.in");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        DIRECTORY1 = Paths.get(getClass().getResource("/test-directory1").toURI());
        DIRECTORY2 = Paths.get(getClass().getResource("/test-directory2").toURI());
        DIRECTORY3 = Paths.get(getClass().getResource("/test-directory3").toURI());
        DIRECTORY4 = Paths.get(getClass().getResource("/test-directory4").toURI());
        NESTED_DIRECTORY = Paths.get(getClass().getResource("/test-directory3/nested-test-directory").toURI());

        SAMPLE1 = Paths.get(getClass().getResource("/sample1.fits").toURI());
        SAMPLE2 = Paths.get(getClass().getResource("/sample2.fits").toURI());
        SAMPLE3 = Paths.get(getClass().getResource("/sample3.fits").toURI());
        SAMPLE4 = Paths.get(getClass().getResource("/test-directory2/sample4.fits").toURI());
        SAMPLE5 = Paths.get(getClass().getResource("/test-directory2/sample5.fits").toURI());
        SAMPLE6 = Paths.get(getClass().getResource("/test-directory3/sample6.fits").toURI());
        SAMPLE7 = Paths.get(getClass().getResource("/test-directory3/nested-test-directory/sample7.fits").toURI());
        SAMPLE8 = Paths.get(getClass().getResource("/﴾šťčßØæ₡ščžť®¶﴿.fits").toURI());

        SHORTCUT = Paths.get(getClass().getResource("/shortcut.lnk").toURI());

        Files.createFile(FILE_IN);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(FILE_IN);
    }

    @Test
    public void testExtractFilesData_Path_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        CmdArgumentsProcessorHelper.extractFilesData(null);
    }

    @Test
    public void testExtractFilesData_Path_Invalid() throws Exception {
        String invalidPath = "\\\\\\invalid path ***\"\" ";

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("is not valid");
        CmdArgumentsProcessorHelper.extractFilesData(invalidPath);
    }

    @Test
    public void testExtractFilesData_Path_ShortcutLink() throws Exception {
        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("is not supported");
        CmdArgumentsProcessorHelper.extractFilesData(SHORTCUT.toString());
    }

    @Test
    public void testExtractFilesData_FilePath_DoesNotExist() throws Exception {
        Path path = Paths.get("file_not_exist.in");

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("does not exist");
        CmdArgumentsProcessorHelper.extractFilesData(path.toString());
    }

    @Test
    public void testExtractFilesData_DirectoryPath_DoesNotExist() throws Exception {
        Path path = Paths.get("directory_not_exist");

        exception.expect(IllegalInputDataException.class);
        exception.expectMessage("does not exist");
        CmdArgumentsProcessorHelper.extractFilesData(path.toString());
    }

    @Test
    public void testExtractFilesData_File_Empty() throws Exception {
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertTrue(fitsFiles.isEmpty());
    }

    @Test
    public void testExtractFilesData_File_ContainsInvalidPath() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                    SAMPLE4.toString(),
                    SAMPLE7.toString(),
                    "**this is not a valid path**");
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(fitsFiles);
        assertEquals(2, fitsFiles.size());
    }

    @Test
    public void testExtractFilesData_File_ContainsNonexistingPath() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE6.toString(),
                "does_not_exist.fits");
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(fitsFiles);
        assertEquals(1, fitsFiles.size());
    }

    @Test
    public void testExtractFilesData_File_OnlyCommentedPaths() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                "# "   + SAMPLE1.toString(),
                "#"    + SAMPLE5.toString(),
                "### " + DIRECTORY1.toString());
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(fitsFiles);
        assertEquals(0, fitsFiles.size());
    }

    @Test
    public void testExtractFilesData_File_ContainsShortcutLink() throws Exception {
        List<String> fitsFilesLines = Collections.singletonList(SHORTCUT.toString());
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(fitsFiles);
        assertEquals(0, fitsFiles.size());
    }

    @Test
    public void testExtractFilesData_File_Valid() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                       SAMPLE1.toString(),
                       SAMPLE6.toString(),
                       SAMPLE3.toString(),
                "##" + SAMPLE4.toString(),
                       "sample6.fits");
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExtractFilesData_File_MixedFilesAndDirectories() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE1.toString(),
                SAMPLE2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY1.toString(),
                "nonexistent-dir1",
                "nonexistent-dir2");
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(4, files.size());
    }

    @Test
    public void testExtractFilesData_File_OnlyDirectories() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                DIRECTORY1.toString(),
                DIRECTORY2.toString(),
                NESTED_DIRECTORY.toString());
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExtractFilesData_File_MultipleIdenticalPaths() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE7.toString(),
                SAMPLE7.toString(),
                SAMPLE7.toString(),
                SAMPLE2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY2.toString(),
                DIRECTORY3.toString());
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(5, files.size());
    }

    @Test
    public void testExtractFilesData_File_PathWithUTF8Characters() throws Exception {
        Writer writer = Files.newBufferedWriter(FILE_IN, Charset.forName("UTF-8"));
        writer.write(SAMPLE1.toString() + LINE_SEPARATOR);
        writer.write(SAMPLE3.toString() + LINE_SEPARATOR);
        writer.write(SAMPLE8.toString() + LINE_SEPARATOR);
        writer.close();

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(3, files.size());
    }

    @Test
    public void testExtractFilesData_File_WithNestedDirectoryInside() throws Exception {
        List<String> fitsFilesLines = Arrays.asList(
                SAMPLE2.toString(),
                DIRECTORY3.toString());
        Files.write(FILE_IN, fitsFilesLines);

        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(FILE_IN.toString());

        assertNotNull(files);
        assertEquals(2, files.size());
    }

    @Test
    public void testExtractFilesData_Directory_Empty() throws Exception {
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY1.toString());

        assertTrue(fitsFiles.isEmpty());
    }

    @Test
    public void testExtractFilesData_Directory_WithNestedDirectoryInside() throws Exception {
        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY3.toString());

        assertNotNull(files);
        assertEquals(1, files.size());
    }

    @Test
    public void testExtractFilesData_Directory_ContainsShortcutLink() throws Exception {
        Collection<File> fitsFiles = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY4.toString());

        assertNotNull(fitsFiles);
        assertEquals(1, fitsFiles.size());
    }

    @Test
    public void testExtractFilesData_Directory_Valid() throws Exception {
        Collection<File> files = CmdArgumentsProcessorHelper.extractFilesData(DIRECTORY2.toString());

        assertNotNull(files);
        assertEquals(2, files.size());
    }
}
