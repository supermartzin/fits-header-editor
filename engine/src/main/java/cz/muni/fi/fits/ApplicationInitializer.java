package cz.muni.fi.fits;

import com.google.inject.Guice;
import com.google.inject.Injector;
import cz.muni.fi.fits.common.Configuration;
import cz.muni.fi.fits.common.exceptions.ConfigurationException;
import cz.muni.fi.fits.common.loaders.ConfigurationLoader;
import cz.muni.fi.fits.common.loaders.PropertiesLoader;

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
            // load properties
            //Properties properties = PropertiesLoader.loadProperties(ApplicationInitializer.class, "/fits.properties");     // for IDE
            Properties properties = PropertiesLoader.loadProperties("./fits.properties");                                // for JAR

            // load configuration
            Configuration config = ConfigurationLoader.loadConfiguration(properties);

            // inject all necessary dependencies
            Injector injector = Guice.createInjector(new AppInjector(args, config));

            // get instance of executive class
            FITSHeaderEditor editor = injector.getInstance(FITSHeaderEditor.class);

            // start FITS header edit operation
            editor.start();
        } catch (ConfigurationException | IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
