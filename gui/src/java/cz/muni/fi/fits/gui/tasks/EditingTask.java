package cz.muni.fi.fits.gui.tasks;

import cz.muni.fi.fits.gui.listeners.MessageListener;
import cz.muni.fi.fits.gui.ExternalProcess;
import cz.muni.fi.fits.gui.utils.Constants;
import javafx.concurrent.Task;

import java.util.Collection;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class EditingTask extends Task<Void> {

    private final ExternalProcess _fitsEditor;
    private final Collection<String> _inputArguments;
    private final int _maxProcessLength;

    private MessageListener _messageListener;

    private int _counter;
    private boolean _endedWithErrors;

    public EditingTask(ExternalProcess fitsEditor, Collection<String> inputArguments, int numberOfFiles) {
        if (fitsEditor == null)
            throw new IllegalArgumentException("External Editor is null");
        if (inputArguments == null)
            throw new IllegalArgumentException("Input arguments are null");

        _fitsEditor = fitsEditor;
        _inputArguments = inputArguments;
        _maxProcessLength = numberOfFiles > 0
                                ? numberOfFiles + 2
                                : 2;
    }

    public void setMessageListener(MessageListener messageListener) {
        if (messageListener == null)
            throw new IllegalArgumentException("Message listener is null");

        _messageListener = messageListener;
    }

    public boolean endedWithErrors() {
        return _endedWithErrors;
    }

    @Override
    protected Void call() throws Exception {
        _fitsEditor.addOutputListener(this::onMessage);

        // reset flags
        _counter = 0;
        _endedWithErrors = false;

        // run editor
        _fitsEditor.run(_inputArguments);

        // update progress to 100 percent
        if (_counter <= _maxProcessLength)
            updateProgress(1, 1);

        return null;
    }

    private void onMessage(String message) {
        updateProgress(++_counter, _maxProcessLength + 2);

        if (message.contains(Constants.INFO_IDENTIFIER)) {
            // raise onInfo event in registered listener
            if (_messageListener != null)
                _messageListener.onInfo(message);
        }
        else if (message.contains(Constants.ERROR_IDENTIFIER)) {
            _endedWithErrors = true;

            // raise onError event in registered listener
            if (_messageListener != null)
                _messageListener.onError(message);
        }
        else if (message.contains(Constants.EXCEPTION_IDENTIFIER)) {
            _endedWithErrors = true;

            // raise onInfo event in registered listener
            if (_messageListener != null)
                _messageListener.onException(message);
        }
        else {
            _endedWithErrors = true;

            // raise onError event in registered listeners on unknown message
            if (_messageListener != null)
                _messageListener.onError(message);
        }
    }
}
