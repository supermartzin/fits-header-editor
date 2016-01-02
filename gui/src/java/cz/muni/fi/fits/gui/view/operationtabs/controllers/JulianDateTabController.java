package cz.muni.fi.fits.gui.view.operationtabs.controllers;

import cz.muni.fi.fits.gui.models.inputdata.InputData;
import cz.muni.fi.fits.gui.models.inputdata.JulianDateInputData;
import cz.muni.fi.fits.gui.exceptions.ValidationException;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class JulianDateTabController extends JulianDateBasedOperationTabController {

    private Validator _validator;

    @Override
    public void init() {
        super.init();

        _tabName = _resources.getString("tab.jd");
        _validator = new Validator();
    }

    @Override
    public InputData getInputData() {
        try {
            // validate fields
            _validator.validateDatetimeRecordTypeField();
            _validator.validateDatetimeValueField();
            _validator.validateExposureRecordTypeField();
            _validator.validateExposureValueField();

            // get datetime
            String datetime = getDatetimeFieldValue();

            //get exposure
            String exposure = getExposureFieldValue();

            // get comment
            String comment = commentField.getText();

            return new JulianDateInputData(datetime, exposure, comment);
        } catch (ValidationException vEx) {
            // validation errors
            return null;
        }
    }


    /**
     * TODO insert description
     */
    class Validator extends JulianDateBasedOperationTabController.Validator { }
}
