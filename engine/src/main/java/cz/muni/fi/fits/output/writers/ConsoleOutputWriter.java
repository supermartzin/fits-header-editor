package cz.muni.fi.fits.output.writers;

import com.google.inject.Singleton;
import cz.muni.fi.fits.common.utils.StringUtils;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Writer class that writes output
 * <ul>
 *     <li>to system console</li>
 * </ul>
 * implements {@link OutputWriter} interface
 *
 * @author Martin Vrábel
 * @version 1.1.1
 */
@Singleton
public class ConsoleOutputWriter implements OutputWriter {

    private static final String UNKNOWN_FILE_NAME = "Unknown file";

    /**
     * Writes specified <code>infoMessage</code> to standard output
     *
     * @param infoMessage   info message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(String infoMessage) {
        // write only if infoMessage parameter is correct
        if (infoMessage != null) {
            System.out.println("[" + LocalDateTime.now().toString() + "] INFO >> " + infoMessage);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>infoMessage</code> related to specified <code>file</code>
     * to standard output
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

        // write only if infoMessage parameter is correct
        if (infoMessage != null) {
            System.out.println("[" + LocalDateTime.now().toString() + "]" +
                    " INFO >> [" + filename + "]: " + infoMessage);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> to standard error output
     *
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(Throwable exception) {
        if (exception != null) {
            String exceptionType = StringUtils.getExceptionType(exception);

            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >>" +
                    " [" + exceptionType + "]: " +
                    exception.getMessage());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to standard error output
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

        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " EXCEPTION >>" +
                " [" + exceptionType + "]: " +
                errorMessage);

        return true;
    }

    /**
     * Writes specified <code>exception</code> related to specified <code>file</code>
     * to standard error output
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

            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " EXCEPTION >>" +
                    " [" + filename + "] -" +
                    " [" + exceptionType + "]: " +
                    exception.getMessage());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> to standard error output
     *
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(String errorMessage) {
        if (errorMessage != null) {
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " ERROR >>" + errorMessage);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes specified <code>errorMessage</code> related to specified <code>file</code>
     * to standard error output
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
            System.err.println("[" + LocalDateTime.now().toString() + "]" +
                    " ERROR >>" +
                    " [" + filename + "]: " +
                    errorMessage);

            return true;
        } else {
            return false;
        }
    }
}
