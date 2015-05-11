package cz.muni.fi.fits.engine.models;

/**
 * Class for computing heliocentric julian date of some object from provided parameters
 *
 * @author Martin Vrábel
 * @version 1.0.1
 */
public final class HeliocentricJulianDate {

    private static final double PI = Math.PI;
    private static final double RADS = PI / 180.0;
    private static final double ECL = 23.439292 * RADS;
    private static final double AUSEC = 8.3168775;

    private final double _julianDate;
    private final double _rightAscension;
    private final double _declination;

    /**
     * Creates new {@link HeliocentricJulianDate} object with provided parameters
     *
     * @param julianDate        {@link JulianDate} object
     * @param rightAscension    {@link RightAscension} object
     * @param declination       {@link Declination} object
     */
    public HeliocentricJulianDate(JulianDate julianDate, RightAscension rightAscension, Declination declination) {
        _julianDate = julianDate.getJulianDate();
        _rightAscension = rightAscension.getRightAscension();
        _declination = declination.getDeclination();
    }

    /**
     * Creates new {@link HeliocentricJulianDate} object with provided parameters
     *
     * @param julianDate        {@link JulianDate} value
     * @param rightAscension    {@link RightAscension} value
     * @param declination       {@link Declination} value
     */
    public HeliocentricJulianDate(double julianDate, double rightAscension, double declination) {
        _julianDate = julianDate;
        _rightAscension = rightAscension;
        _declination = declination;
    }

    /**
     * Computes object's heliocentric julian date from provided object's parameters
     *
     * @return  computed object's heliocentric julian date
     */
    public double computeHeliocentricJulianDate() {
        double pe = (102.94719 + 0.00000911309 * (_julianDate - 2451545)) * RADS;
        double ae = 1.00000011 - 1.36893E-12 * (_julianDate - 2451545);
        double ee = 0.01671022 - 0.00000000104148 * (_julianDate - 2451545);
        double le = (RADS * (100.46435 + 0.985609101 * (_julianDate - 2451545)));

        // Earth position by Kepler equation
        double B = (le - pe) / (2 * PI);
        double me = 2 * PI * (B - Math.floor(Math.abs(B)));
        if (B < 0)
            me = 2 * PI * (B + Math.floor(Math.abs(B)));
        if (me < 0)
            me += 2 * PI;

        double e = me;
        double delta = 0.05;
        while (Math.abs(delta) >= Math.pow(10, -12)) {
            delta = e - ee * Math.sin(e) - me;
            e = e - delta / (1 - ee * Math.cos(e));
        }

        double ve = 2 * Math.atan(Math.pow(((1 + ee) / (1 - ee)), 0.5) * Math.tan(0.5 * e));
        if (ve < 0) ve += 2 * PI;
        double re = ae * (1 - Math.pow(ee, 2)) / (1 + ee * Math.cos(ve));
        double xe = re * Math.cos(ve + pe);
        double ye = re * Math.sin(ve + pe);
        double ze = 0;

        // Sun coordinates
        double yeq = ye * Math.cos(ECL) - ze * Math.sin(ECL);
        double zeq = ye * Math.sin(ECL) + ze * Math.cos(ECL);

        double DEC = -180 * Math.atan(zeq / Math.pow((Math.pow(xe, 2) + Math.pow(yeq, 2)), 0.5)) / PI;
        double RA = 12 + Math.atan(yeq / xe) * 12 / PI;
        if (xe < 0) RA += 12;
        if (ye < 0 && xe > 0) RA += 24;

        DEC *= -1;
        RA += 12;
        if (RA > 24) RA -= 24;

        // compute Heliocentric Julian Date
        // earth
        double earthX = Math.cos(RA * PI / 12.0) * Math.cos(DEC * PI / 180.0);
        double earthY = Math.sin(RA * PI / 12.0) * Math.cos(DEC * PI / 180.0);
        double earthZ = Math.sin(DEC * PI / 180.0);
        // object
        double objectX = Math.cos(_rightAscension * PI / 180.0) * Math.cos(_declination * PI / 180.0);
        double objectY = Math.sin(_rightAscension * PI / 180.0) * Math.cos(_declination * PI / 180.0);
        double objectZ = Math.sin(_declination * PI / 180.0);

        double correction = AUSEC * (earthX * objectX
                + earthY * objectY
                + earthZ * objectZ);

        return _julianDate + correction / (24.0 * 60.0);
    }
}
