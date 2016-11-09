
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import java.awt.Component;
import java.awt.Color;

/**
 *
 * @author Tine og morten 
 * Oppgave 2 i oblig 11 Denne pluggen lager  gjør om RGB til HSB gjør auto kontrast og deretter gjør om fargene til RGB igjen
 * på farge bilder med bruker input
 */

public class Auto_Kontrast implements PlugInFilter {

    public int setup(String arg, ImagePlus im) {

        return DOES_RGB;

    }//setup()

    public void run(ImageProcessor cp) {

        justerBildet(cp, showDialog());

    }

//////////////////////////////////////////////METODER///////////////////////////////////////////////////////////////
    /**
     * Strekker bildet avhengig av bruker ønsket
     *
     * @param cp ColorProcessor
     * @param sjekkBoks er en true or false tabell med bruker ønsker
     */
    public void justerBildet(ImageProcessor cp, boolean[] sjekkBoks) {
        final int K = 256;
        int b = cp.getWidth();
        int h = cp.getHeight();
        float m = b * h;
        float[] hsb = new float[3];

        if (!sjekkBoks[0]) {

            float[][] tempTab = lagKumTab(cp, K);//Lager kumulerte histogram

            float[] kumSat = tempTab[0]; //Vi kunne bare brukt tempTab med indeks nr men valgte en mer lesbar versjon
            float[] kumbr = tempTab[1];
            
            for (int y = 0; y < cp.getHeight(); y++) {
                for (int x = 0; x < cp.getWidth(); x++) {
                    int f = cp.getPixel(x, y);
                    ///Separere de forskjellige fargene 
                    int r = (f & 0xff0000) >> 16;
                    int g = (f & 0x00ff00) >> 8;
                    int bl = (f & 0x0000ff);

                    hsb = Color.RGBtoHSB(r, g, bl, hsb);
                    float hue = hsb[0];
                    float saturation = hsb[1];
                    float brightness = hsb[2];

                    ///Strekker kontrasten
                    float utSat;
                    float utBr;
                    if (sjekkBoks[1]) {
                        utSat = ((kumSat[(int) Math.round(saturation * 255)]) / 255) * (K - 1) / m;
                    } else {
                        utSat = saturation;
                    }
                    if (sjekkBoks[2]) {
                        utBr = ((kumbr[(int) Math.round(brightness * 255)]) / 255) * (K - 1) / m;
                    } else {
                        utBr = brightness;
                    }

                    ///lager en farge verdi
                    f = Color.HSBtoRGB(hue, utSat, utBr);

                    cp.putPixel(x, y, f);

                }
            }

        }
    }

    /**
     * Lager kumulerte histogram for hver farge kanal
     *
     * @param cp Color processor
     * @param K antall farger
     * @return en tabell med et kumulert histogram for hver kanal
     */
    float[][] lagKumTab(ImageProcessor cp, int K) {
        int h = cp.getHeight();
        int b = cp.getWidth();
        float[] hsb = new float[3];

        float[] sat = new float[K];
        float[] br = new float[K];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < b; x++) {

                int c = cp.getPixel(x, y);

                int r = (c & 0xff0000) >> 16;
                int g = (c & 0x00ff00) >> 8;
                int bl = (c & 0x0000ff);

                hsb = Color.RGBtoHSB(r, g, bl, hsb);
                float hue = hsb[0];
                float saturation = hsb[1] * 255;
                float brightness = hsb[2] * 255;

                sat[(int) Math.round(saturation)] = sat[(int) Math.round(saturation)] + 1;
                br[(int) Math.round(brightness)] = br[(int) Math.round(brightness)] + 1;

            }
        }

        //Lager kumulative tabeller av de to kanalene
        float[] kumSat = kumulativ(sat);
        float[] kumBr = kumulativ(br);

        float[] returTab[] = {
            kumSat,
            kumBr
        };

        return returTab;
    }

    /**
     * Lager et kumulert histogram av et histogram
     *
     * @param hist
     * @return et kumulert histogram
     */
    private float[] kumulativ(float[] hist) {
        float[] khtab = new float[hist.length];
        khtab[0] = hist[0];

        for (int x = 1; x < hist.length; x++) {
            khtab[x] = khtab[x - 1] + hist[x];
        }
        return khtab;

    }

    /**
     * Henter bruker valg
     *
     * @return en true og false tabell med valgene brukeren gjør
     */
    private boolean[] showDialog() {
        boolean sat = false;
        boolean lys = false;
        boolean cansel = false;
        boolean[] tempTab = new boolean[3];
        /*
         * Create and display generic dialog
         */
        GenericDialog gd = new GenericDialog("Dynamisk Kontrast");
        gd.addCheckbox("Juster med metning", sat);
        gd.addCheckbox("Juster med lysstyrke", lys);
        gd.showDialog();
        if (gd.wasCanceled()) {
            cansel = true;
        }
        tempTab[0] = cansel;

        sat = gd.getNextBoolean();
        tempTab[1] = sat;
        lys = gd.getNextBoolean();
        tempTab[2] = lys;
        return tempTab;
    }

}//Klasse
