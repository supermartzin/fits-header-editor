package cz.muni.fi.fits.output.writers;

import cz.muni.fi.fits.common.utils.StringUtils;

import javax.inject.Singleton;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Writer class that writes output
 * <ul>
 *     <li>to system console</li>
 *     <li>to file specified in constructor</li>
 * </ul>
 * implements {@link OutputWriter} interface
 *
 * @author Martin Vrábel
 * @version 1.1.1
 */
@Singleton
public class FileConsoleOutputWriter implements OutputWriter {

    private final File _outputFile;

    /**
     * Creates new instance of {@link FileConsoleOutputWriter} that writes
     * to file specified by <code>filePath</code> parameter
     *
     * @param filePath  specifies file where to write output data
     */
    public FileConsoleOutputWriter(String filePath) {
        _outputFile = new File(filePath);
    }

    /**
     * Creates new instance of {@link FileConsoleOutputWriter} that writes
     * to specified <code>file</code> parameter
     *
     * @param outputFile    specifies file where to write output data
     */
    public FileConsoleOutputWriter(File outputFile) {
        _outputFile = outputFile;
    }

    /**
     * Writes specified <code>infoMessage</code> to output file and
     * to system console atomically
     *
     * @param infoMessage   info message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(String infoMessage) {
        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "] INFO >> " + infoMessage);
            }

            // write to console
            System.out.println("[" + LocalDateTime.now().toString() + "] INFO >> " + infoMessage);

            return true;
        } catch (IOException e) {
            return false;
        }

    }

    /**
     * Writes specified <code>infoMessage</code> related to specified <code>file</code>
     * to output file and to system console atomically
     *
     * @param file          file to which specific info message relates
     * @param infoMessage   message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(File file, String infoMessage) {
        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " INFO >> [" + file.getName() + "]:" + infoMessage);
            }

            // write to console
            System.out.println("[" + LocalDateTime.now().toString() + "]" +
                    " INFO >> [" + file.getName() + "]: " + infoMessage);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> to output file and
     * to system console atomically
     *
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(Throwable exception) {
        String exceptionType = StringUtils.getExceptionType(exception);

        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " EXCEPTION >> [" + exceptionType + "]: " + exception.getMessage());
            }

            // write to console
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >> [" + exceptionType + "]: " + exception.getMessage());

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to output file and to system console atomically
     *
     * @param errorMessage  error message to be written to output
     * @param exception     exception to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeException(String errorMessage, Throwable exception) {
        String exceptionType = StringUtils.getExceptionType(exception);

        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " EXCEPTION >> [" + exceptionType + "]: " + errorMessage);
            }

            // write to console
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >> [" + exceptionType + "]: " + errorMessage);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> related to specified <code>file</code>
     * to output file and to system console atomically
     *
     * @param file      file to which specific exception relates
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(File file, Throwable exception) {
        String exceptionType = StringUtils.getExceptionType(exception);

        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " EXCEPTION >>" +
                        " [" + file.getName() + "] -" +
                        " [" + exceptionType + "]: " +
                        exception.getMessage());
            }

            // write to console
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >>" +
                    " [" + file.getName() + "] -" +
                    " [" + exceptionType + "]: " +
                    exception.getMessage());

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> to output file and
     * to system console atomically
     *
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(String errorMessage) {
        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " ERROR >>" + errorMessage);
            }

            // write to console
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " ERROR >>" + errorMessage);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> related to specified <code>file</code>
     * to output file and to system console atomically
     *
     * @param file          file to which specific error message relates
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(File file, String errorMessage) {
        try {
            // write to file
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " ERROR >>" +
                        " [" + file.getName() + "]: " +
                        errorMessage);
            }

            // write to console
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " ERROR >>" +
                    " [" + file.getName() + "]: " +
                    errorMessage);

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
