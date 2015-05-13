package cz.muni.fi.files.utils;

import javafx.concurrent.Task;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * TODO
 */
public class NotifyingRunnable implements Runnable {

    private boolean _endedWithNoError;

    private final Task _task;
    private final Set<ThreadCompleteListener> _listeners = new CopyOnWriteArraySet<>();

    public NotifyingRunnable(Task task) {
        _task = task;
        _endedWithNoError = true;
    }

    @Override
    public void run() {
        try {
            _task.run();
        } catch (Exception ex) {
            _endedWithNoError = false;
        } finally {
            notifyListeners();
        }
    }

    public boolean hasEndedWithNoError() {
        return _endedWithNoError;
    }

    public final void addListener(final ThreadCompleteListener listener) {
        _listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener) {
        _listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ThreadCompleteListener listener : _listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }
}
