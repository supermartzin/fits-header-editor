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
 * @version 1.0.2
 */
@Singleton
public class ConsoleOutputWriter implements OutputWriter {

    /**
     * Writes specified <code>infoMessage</code> to standard output
     *
     * @param infoMessage   info message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeInfo(String infoMessage) {
        System.out.println("[" + LocalDateTime.now().toString() + "] INFO >> " + infoMessage);

        return true;
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
        System.out.println("[" + LocalDateTime.now().toString() + "]" +
                " INFO >> [" + file.getName() + "]: " + infoMessage);

        return true;
    }

    /**
     * Writes specified <code>exception</code> to standard error output
     *
     * @param exception exception to be written to output
     * @return          {@inheritDoc}
     */
    @Override
    public boolean writeException(Throwable exception) {
        String exceptionType = StringUtils.getExceptionType(exception);

        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " EXCEPTION >> [" + exceptionType + "]: " + exception.getMessage());

        return true;
    }

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to standard error output
     *
     * @param errorMessage  error message to be written to output
     * @param exception     exception to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeException(String errorMessage, Throwable exception) {
        String exceptionType = StringUtils.getExceptionType(exception);

        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " EXCEPTION >> [" + exceptionType + "]: " + errorMessage);

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
        String exceptionType = StringUtils.getExceptionType(exception);

        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " EXCEPTION >>" +
                " [" + file.getName() + "] -" +
                " [" + exceptionType + "]: " +
                exception.getMessage());

        return true;
    }

    /**
     * Writes specified <code>errorMessage</code> to standard error output
     *
     * @param errorMessage  error message to be written to output
     * @return              {@inheritDoc}
     */
    @Override
    public boolean writeError(String errorMessage) {
        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " ERROR >>" + errorMessage);

        return true;
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
        System.err.println("[" + LocalDateTime.now().toString() + "]" +
                " ERROR >>" +
                " [" + file.getName() + "]: " +
                errorMessage);

        return true;
    }
}
