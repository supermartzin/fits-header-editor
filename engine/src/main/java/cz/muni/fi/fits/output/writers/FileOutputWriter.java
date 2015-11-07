package cz.muni.fi.fits.output.writers;

import cz.muni.fi.fits.common.utils.StringUtils;

import javax.inject.Singleton;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Writer class that writes output
 * <ul>
 * <li>to file specified in constructor</li>
 * </ul>
 * implements {@link OutputWriter} interface
 *
 * @author Martin Vrábel
 * @version 1.2
 */
@Singleton
public class FileOutputWriter implements OutputWriter {

    private static final String UNKNOWN_FILE_NAME = "Unknown file";

    private final File _outputFile;

    /**
     * Creates new instance of {@link FileOutputWriter} that writes
     * to file specified by <code>filePath</code> parameter. File will be created if it does not exist
     *
     * @param filePath                  specifies file where to write output data
     * @throws IllegalArgumentException if provided <code>filepath</code> parameter contains invalid data
     */
    public FileOutputWriter(String filePath) {
        if (filePath == null)
            throw new IllegalArgumentException("filePath parameter is null");

        _outputFile = new File(filePath);

        // create output file if it does not exist
        if (!_outputFile.exists()) {
            try {
                if (!_outputFile.createNewFile())
                    throw new IllegalArgumentException("Invalid filepath parameter");
            } catch (IOException ioEx) {
                throw new IllegalArgumentException("Invalid filepath parameter", ioEx);
            }
        }
    }

    /**
     * Creates new instance of {@link FileOutputWriter} that writes
     * to specified <code>outputFile</code> parameter. File will be created if it does not exist
     *
     * @param outputFile                specifies file where to write output data
     * @throws IllegalArgumentException if provided <code>file</code> parameter contains
     *                                  invalid File data
     */
    public FileOutputWriter(File outputFile) {
        if (outputFile == null)
            throw new IllegalArgumentException("outputFile parameter is null");

        _outputFile = outputFile;

        // create output file if it does not exist
        if (!_outputFile.exists()) {
            try {
                if (!_outputFile.createNewFile())
                    throw new IllegalArgumentException("Invalid file parameter");
            } catch (IOException ioEx) {
                throw new IllegalArgumentException("Invalid file parameter", ioEx);
            }
        }
    }

    /**
     * Writes specified <code>infoMessage</code> to output file
     *
     * @param infoMessage   info message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(String infoMessage) {
        if (infoMessage != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "] INFO >> " + infoMessage);
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>infoMessage</code> related to specified <code>file</code>
     * to output file
     *
     * @param file          file to which specific info message relates
     * @param infoMessage   message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(File file, String infoMessage) {
        // set filename
        String filename;
        if (file == null)
            filename = UNKNOWN_FILE_NAME;
        else
            filename = file.getName();

        if (infoMessage != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " INFO >> [" + filename + "]: " + infoMessage);
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> to output file
     *
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(Throwable exception) {
        if (exception != null) {
            String exceptionType = StringUtils.getExceptionType(exception);

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " EXCEPTION >> [" + exceptionType + "]: " + exception.getMessage());
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to output file
     *
     * @param errorMessage  error message to be written to output,
     *                      if <code>null</code> or empty, message from <code>exception</code>
     *                      parameter is taken
     * @param exception     exception to be written to output,
     *                      if <code>null</code>, only <code>errorMessage</code> id written as error
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeException(String errorMessage, Throwable exception) {
        if (errorMessage == null || errorMessage.isEmpty())
            return writeException(exception);
        if (exception == null)
            return writeError(errorMessage);

        String exceptionType = StringUtils.getExceptionType(exception);

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
            writer.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >> [" + exceptionType + "]: " + errorMessage);
            return true;
        } catch (IOException ioEx) {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> related to specified <code>file</code>
     * to output file
     *
     * @param file      file to which specific exception relates
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(File file, Throwable exception) {
        // set filename
        String filename;
        if (file == null)
            filename = UNKNOWN_FILE_NAME;
        else
            filename = file.getName();

        if (exception != null) {
            String exceptionType = StringUtils.getExceptionType(exception);

            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " EXCEPTION >>" +
                        " [" + filename + "] -" +
                        " [" + exceptionType + "]: " +
                        exception.getMessage());
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> to output file
     *
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(String errorMessage) {
        if (errorMessage != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " ERROR >>" + errorMessage);
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> related to specified <code>file</code>
     * to output file
     *
     * @param file          file to which specific error message relates
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(File file, String errorMessage) {
        // set filename
        String filename;
        if (file == null)
            filename = UNKNOWN_FILE_NAME;
        else
            filename = file.getName();

        if (errorMessage != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(_outputFile, true)))) {
                writer.println("[" + LocalDateTime.now().toString() + "]" +
                        " ERROR >>" +
                        " [" + filename + "]: " +
                        errorMessage);
                return true;
            } catch (IOException ioEx) {
                return false;
            }
        } else {
            return false;
        }
    }
}
