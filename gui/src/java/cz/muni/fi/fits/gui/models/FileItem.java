package cz.muni.fi.fits.gui.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * TODO insert description
 */
public class FileItem implements Comparable<FileItem> {

    private final StringProperty _filename;
    private final StringProperty _filepath;
    private final BooleanProperty _selected;

    public FileItem(String filename, String filepath) {
        _filename = new SimpleStringProperty(filename);
        _filepath = new SimpleStringProperty(filepath);
        _selected = new SimpleBooleanProperty(false);
    }

    public String getFilename() {
        return _filename.get();
    }

    public StringProperty filenameProperty() {
        return _filename;
    }

    public void setFilename(String filename) {
        this._filename.set(filename);
    }

    public String getFilepath() {
        return _filepath.get();
    }

    public StringProperty filepathProperty() {
        return _filepath;
    }

    public void setFilepath(String filepath) {
        this._filepath.set(filepath);
    }

    public boolean isSelected() {
        return _selected.get();
    }

    public BooleanProperty selectedProperty() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        this._selected.set(selected);
    }

    @Override
    public String toString() {
        return _filename.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null
                || getClass() != o.getClass())
            return false;

        FileItem fileItem = (FileItem) o;
        return Objects.equals(_filename.get(), fileItem._filename.get()) &&
                Objects.equals(_filepath.get(), fileItem._filepath.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_filename.get(), _filepath.get());
    }

    @Override
    public int compareTo(FileItem fileItem) {
        return _filename.get().compareTo(fileItem._filename.get());
    }
}
