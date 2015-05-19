package cz.muni.fi.files.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Service class for file creation on background thread
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class FileCreationService extends Service {

    private final Task _task;

    /**
     * Creates new instance of {@link FileCreationService} with specified
     * {@link Task} which will be run after start of this service
     *
     * @param task  instance of {@link Task} to run after this service start
     */
    public FileCreationService(Task task) {
        _task = task;
    }

    @Override
    protected Task createTask() {
        return _task;
    }
}
