package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.models.inputdata.ChangeKeywordInputData;
import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.utils.Constants;
import cz.muni.fi.fits.gui.utils.Constrainer;
import cz.muni.fi.fits.gui.exceptions.ValidationException;
import cz.muni.fi.fits.gui.utils.dialogs.WarningDialog;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class ChangeKeywordTabController extends OperationTabController {

    public CheckBox removeSwitchField;
    public TextField oldKeywordField;
    public TextField newKeywordField;

    private Validator _validator;

    @Override
    public void init() {
        _tabName = _resources.getString("tab.change.keyword");
        _validator = new Validator();

        setFieldsConstraints();
    }

    @Override
    public InputData getInputData() {
        try {
            // validate fields
            _validator.validateOldKeywordField();
            _validator.validateNewKeywordField();

            // get keywords
            String oldKeyword = oldKeywordField.getText();
            String newKeyword = newKeywordField.getText();
            // get switch
            boolean removeValueOfNewIfExists = removeSwitchField.isSelected();

            return new ChangeKeywordInputData(oldKeyword, newKeyword, removeValueOfNewIfExists);
        } catch (ValidationException vEx) {
            // validation errors
            return null;
        }
    }

    private void setFieldsConstraints() {
        Constrainer.constrainTextFieldWithRegex(oldKeywordField, Constants.KEYWORD_PATTERN);
        Constrainer.constrainTextFieldWithRegex(newKeywordField, Constants.KEYWORD_PATTERN);
    }


    /**
     * TODO insert description
     */
    class Validator {

        /**
         *
         * @throws ValidationException
         */
        void validateOldKeywordField()
                throws ValidationException {
            if (oldKeywordField.getText().isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.change.alert.content.keyword.old.empty"),
                        _mainApp);

                throw new ValidationException("Old keyword field is not set");
            }
        }

        /**
         *
         * @throws ValidationException
         */
        void validateNewKeywordField()
                throws ValidationException {
            if (newKeywordField.getText().isEmpty()) {
                WarningDialog.show(
                        _resources.getString("oper.common.alert.title"),
                        _resources.getString("oper.common.alert.header"),
                        _resources.getString("oper.change.alert.content.keyword.new.empty"),
                        _mainApp);

                throw new ValidationException("New keyword field is not set");
            }
        }
    }
}
