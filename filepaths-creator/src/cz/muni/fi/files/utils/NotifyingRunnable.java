package cz.muni.fi.files.utils;

import javafx.concurrent.Task;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * {@link Runnable} class that has specific functionality of notifying
 * when thread work is done
 *
 * @author Martin Vrábel
 * @version 1.1
 */
public class NotifyingRunnable implements Runnable {

    private Throwable _error;
    private boolean _endedWithNoError;

    private final Task _task;
    private final Set<ThreadCompleteListener> _listeners = new CopyOnWriteArraySet<>();

    /**
     * Creates new instance of {@link NotifyingRunnable} with specific {@link Task}
     * that will be executed on thread start
     *
     * @param task  task to execute by this runnable in thread
     */
    public NotifyingRunnable(Task task) {
        _task = task;
        _endedWithNoError = true;
        _error = null;
    }

    @Override
    public void run() {
        try {
            _task.run();
        } catch (Exception ex) {
            _endedWithNoError = false;
            _error = ex;
        } finally {
            notifyListeners();
        }
    }

    /**
     * Indicates whether work thread task completed successfully or not
     *
     * @return  <code>true</code> when task is successful,
     *          <code>false</code> otherwise
     */
    public boolean hasEndedWithNoError() {
        return _endedWithNoError;
    }

    /**
     * Returns exception that caused task failure if any and if method
     * #hasEndedWithNoError returns <code>false</code>
     *
     * @return  instance of {@link Throwable} if error occurs,
     *          <code>null</code> otherwise
     */
    public Throwable getError() {
        return _error;
    }

    /**
     * Adds listener which will be called when thread task completes
     *
     * @param listener  listener to add
     */
    public final void addListener(final ThreadCompleteListener listener) {
        _listeners.add(listener);
    }

    /**
     * Removes previously added listener for thread task completition
     *
     * @param listener  listener to remove
     */
    public final void removeListener(final ThreadCompleteListener listener) {
        _listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ThreadCompleteListener listener : _listeners) {
            listener.notifyWhenThreadComplete(this);
        }
    }
}
