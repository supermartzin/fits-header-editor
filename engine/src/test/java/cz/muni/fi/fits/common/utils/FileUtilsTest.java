package cz.muni.fi.fits.common.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Tests for {@link FileUtils} helper class
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FileUtilsTest {

    private Path SAMPLE_FITS_FILE;
    private Path WINDOWS_SHORTCUT;
    private Path SAMPLE_FITS_FILE_SYMLINK;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        SAMPLE_FITS_FILE = Paths.get(getClass().getResource("/sample1.fits").toURI());
        WINDOWS_SHORTCUT = Paths.get(getClass().getResource("/shortcut.lnk").toURI());

        SAMPLE_FITS_FILE_SYMLINK = Paths.get(getClass().getResource("").getAuthority() + "sample1-symlink.fits");
        Files.createSymbolicLink(SAMPLE_FITS_FILE_SYMLINK, SAMPLE_FITS_FILE);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(SAMPLE_FITS_FILE_SYMLINK);
    }

    @Test
    public void testIsWindowsShortcut_Path_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        FileUtils.isWindowsShortcut(null);
    }

    @Test
    public void testIsWindowsShortcut_Correct() throws Exception {
        boolean isShortcut = FileUtils.isWindowsShortcut(WINDOWS_SHORTCUT);
        assertTrue(isShortcut);

        boolean isNotShortcut = FileUtils.isWindowsShortcut(SAMPLE_FITS_FILE);
        assertFalse(isNotShortcut);
    }

    @Test
    public void testIsSymbolicLink_Path_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        FileUtils.isSymbolicLink(null);
    }

    @Test
    public void testIsSymbolicLink_Correct() throws Exception {
        boolean isSymbolicLink = FileUtils.isSymbolicLink(SAMPLE_FITS_FILE_SYMLINK);
        assertTrue(isSymbolicLink);

        boolean isNotSymbolicLink = FileUtils.isSymbolicLink(SAMPLE_FITS_FILE);
        assertFalse(isNotSymbolicLink);
    }

    @Test
    public void testReadSymbolicLink_Path_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        FileUtils.readSymbolicLink(null);
    }

    @Test
    public void testReadSymbolicLink_Correct() throws Exception {
        Path notSymbolicLink = FileUtils.readSymbolicLink(SAMPLE_FITS_FILE);
        assertNull(notSymbolicLink);

        Path symbolicLinkTarget = FileUtils.readSymbolicLink(SAMPLE_FITS_FILE_SYMLINK);
        assertNotNull(symbolicLinkTarget);
        assertEquals(SAMPLE_FITS_FILE, symbolicLinkTarget);
    }

    @Test
    public void testIsValidPath_Path_Null() throws Exception {
        exception.expect(IllegalArgumentException.class);
        FileUtils.isValidPath(null);
    }

    @Test
    public void testIsValidPath_Path_Valid() throws Exception {
        String validPathString = SAMPLE_FITS_FILE.toAbsolutePath().toString();

        boolean isValidPath = FileUtils.isValidPath(validPathString);
        assertTrue(isValidPath);
    }

    @Test
    public void testIsValidPath_Path_Invalid() throws Exception {
        String invalidPathString = "invalid/path//-/-somewhere";

        boolean isInvalidPath = FileUtils.isValidPath(invalidPathString);
        assertFalse(isInvalidPath);
    }
}