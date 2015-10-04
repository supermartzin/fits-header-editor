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
     *                      <code>false</code> when error occurs during writing to output or
     *                      <code>infoMessage</code> parameter is <code>null</code>
     */
    boolean writeInfo(String infoMessage);

    /**
     * Writes specified <code>infoMessage</code> related to specified <code>file</code>
     * to output
     *
     * @param file          file to which specific info message relates
     * @param infoMessage   message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output or
     *                      <code>infoMessage</code> parameter is <code>null</code>
     */
    boolean writeInfo(File file, String infoMessage);

    /**
     * Writes specified <code>exception</code> to output
     *
     * @param exception exception to be written to output
     * @return          <code>true</code> when writing to output is successful,
     *                  <code>false</code> when error occurs during writing to output or
     *                  <code>exception</code> parameter is <code>null</code>
     */
    boolean writeException(Throwable exception);

    /**
     * Writes specified <code>exception</code> along with <code>errorMessage</code>
     * to output
     *
     * @param errorMessage  error message to be written to output,
     *                      if <code>null</code> or empty, message from <code>exception</code>
     *                      parameter is taken
     * @param exception     exception to be written to output,
     *                      if <code>null</code>, only <code>errorMessage</code> id written as error
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output
     */
    boolean writeException(String errorMessage, Throwable exception);

    /**
     * Writes specified <code>exception</code> related to specified <code>file</code>
     * to output
     *
     * @param file      file to which specific exception relates,
     *                  if <code>null</code> then it is written as regular exception
     * @param exception exception to be written to output
     * @return          <code>true</code> when writing to output is successful,
     *                  <code>false</code> when error occurs during writing to output or
     *                  <code>exception</code> parameter is <code>null</code>
     */
    boolean writeException(File file, Throwable exception);

    /**
     * Writes specified <code>errorMessage</code> to output
     *
     * @param errorMessage  error message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output or
     *                      <code>errorMessage</code> parameter is <code>null</code>
     */
    boolean writeError(String errorMessage);

    /**
     * Writes specified <code>errorMessage</code> related to specified <code>file</code>
     * to output
     *
     * @param file          file to which specific error message relates,
     *                      if <code>null</code> then it is written as regular error
     * @param errorMessage  error message to be written to output
     * @return              <code>true</code> when writing to output is successful,
     *                      <code>false</code> when error occurs during writing to output or
     *                      <code>errorMessage</code> parameter is <code>null</code>
     */
    boolean writeError(File file, String errorMessage);

    /**
     * Enum with types of {@link OutputWriter}
     */
    enum Type {
        FILE,
        CONSOLE,
        FILE_AND_CONSOLE
    }
}
