package cz.muni.fi.fits;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 *
 * TODO description
 */
public class ApplicationInitializer {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppInjector(args));

        FITSHeaderEditor editor = injector.getInstance(FITSHeaderEditor.class);

        editor.start();
    }
}
