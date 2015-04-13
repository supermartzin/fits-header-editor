package cz.muni.fi.fits.output.writers;

import java.io.File;

/**
 * Writer interface for writing output data
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public interface OutputWriter {

    /**
     * Writes specified <code>infoMessage</code> to output
     *
     * @param infoMessage   info message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeInfo(String infoMessage);

    /**
     * Writes specified <code>infoMessage</code> related to specified <code>file</code>
     * to output
     *
     * @param file          file to which specific info message relates
     * @param infoMessage   message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeInfo(File file, String infoMessage);

    /**
     * Writes specified <code>exception</code> to output
     *
     * @param exception exception to be written to output
     * @return          <code>true</code> when writing to output is successful,
     *                  <code>false</code> when error occurs during writing to output
     */
    boolean writeException(Throwable exception);

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to output
     *
     * @param errorMessage  error message to be written to output
     * @param exception     exception to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeException(String errorMessage, Throwable exception);

    /**
     * Writes specified <code>exception</code> related to specified <code>file</code>
     * to output
     *
     * @param file      file to which specific exception relates
     * @param exception exception to be written to output
     * @return          <code>true</code> when writing to output is successful,
     *                  <code>false</code> when error occurs during writing to output
     */
    boolean writeException(File file, Throwable exception);

    /**
     * Writes specified <code>errorMessage</code> to output
     *
     * @param errorMessage  error message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeError(String errorMessage);

    /**
     * Writes specified <code>errorMessage</code> related to specified <code>file</code>
     * to output
     *
     * @param file          file to which specific error message relates
     * @param errorMessage  error message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeError(File file, String errorMessage);
}
