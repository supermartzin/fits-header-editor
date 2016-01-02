package cz.muni.fi.fits;

import com.google.inject.Guice;
import com.google.inject.Injector;
import cz.muni.fi.fits.common.Configuration;
import cz.muni.fi.fits.common.exceptions.ConfigurationException;
import cz.muni.fi.fits.common.loaders.ConfigurationLoader;
import cz.muni.fi.fits.common.loaders.PropertiesLoader;
import cz.muni.fi.fits.common.utils.Constants;

import java.io.IOException;
import java.util.Properties;

/**
 * Main initializing class of FITS Header Editor Tool
 *
 * @author Martin Vrábel
 * @version 1.1.1
 */
public class ApplicationInitializer {

    public static void main(String[] args) {
        try {
            // check if version asked
            if (isVersionAsked(args)) {
                System.out.println(Constants.APP_VERSION);
                return;
            }

            // load properties
            //Properties properties = PropertiesLoader.loadProperties(ApplicationInitializer.class, "/fits.properties");    // for IDE
            Properties properties = PropertiesLoader.loadProperties("." + Constants.FILE_SEPARATOR + "fits.properties");    // for JAR

            // load configuration
            Configuration config = ConfigurationLoader.loadConfiguration(properties);

            // inject all necessary dependencies
            Injector injector = Guice.createInjector(new AppInjector(args, config));

            // get instance of executive class
            FITSHeaderEditor editor = injector.getInstance(FITSHeaderEditor.class);

            // start FITS header editing operation
            editor.start();
        } catch (ConfigurationException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static boolean isVersionAsked(String[] args) {
        if (args != null) {
            if (args.length > 0 && args[0].toLowerCase().equals("-version")) {
                return true;
            }
        }

        return false;
    }
}
