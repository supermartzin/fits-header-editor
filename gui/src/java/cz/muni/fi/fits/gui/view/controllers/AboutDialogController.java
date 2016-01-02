package cz.muni.fi.fits.gui.view.controllers;

/**
 * TODO insert description
 *
 * @author Martin Vrábel
 * @version 1.0
 */
public class AboutDialogController extends Controller {

    @Override
    public void init() {

    }

    public void openBachelorThesisHomepage() {
        // TODO add homepage
    }

    public void openNomTamFitsHomepage() {
        openWebpage("http://nom-tam-fits.github.io/nom-tam-fits/");
    }

    public void openGuiceHomepage() {
        openWebpage("https://github.com/google/guice");
    }

    public void openGuavaHomepage() {
        openWebpage("https://github.com/google/guava");
    }

    public void openApacheCommonsHomepage() {
        openWebpage("https://commons.apache.org/");
    }

    public void openFreepikIconsHomepage() {
        openWebpage("http://www.flaticon.com/");
    }

    private void openWebpage(String url) {
        if (url != null)
            _mainApp.getHostServices().showDocument(url);
    }
}
