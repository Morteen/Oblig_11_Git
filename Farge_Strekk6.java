
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import java.awt.Component;

/**
 * 
 * @author Tine og morten
 * 
/*Oppgave 1 i oblig 11
 Denne pluggen lager auto kontrast på farge bilder
 */
 
public class Farge_Strekk6 implements PlugInFilter {

    public int setup(String arg, ImagePlus im) {

        return DOES_RGB;

    }//setup()

    public void run(ImageProcessor cp) {
        final int K = 256;

        ByteProcessor red = getKanal(cp, 1);
        ByteProcessor green = getKanal(cp, 2);
        ByteProcessor blue = getKanal(cp, 3);

        strekk(red, K);
        strekk(green, K);
        strekk(blue, K);

        visSamletBilde(cp, red, green, blue);

    }//RUN
//////////////////////////////////////////////
/**
 *  Skiller ut en farge kanal fra en RGB fargene.
 * tilsvarende  getChannel
 * @param cp Er en ColorProcessor
 * @param fargeNr RGB farge nummeret. Fra 1 til 3
 * @return En ByteProcessor med den valgte fargekanalen
 */
    public ByteProcessor getKanal(ImageProcessor cp, int fargeNr) {
        ByteProcessor temp = new ByteProcessor(cp.getHeight(), cp.getWidth());

        int[] RGB = new int[3];
        for (int y = 0; y < cp.getHeight(); y++) {
            for (int x = 0; x < cp.getWidth(); x++) {
                cp.getPixel(x, y, RGB);
                temp.putPixel(x, y, RGB[fargeNr - 1]);

            }
        }
        return temp;
    }
/**
 * Strekker bildet av den ene farge kanalen
 * @param bp ByteProcessor
 * @param K antall farger
 */
    public void strekk(ByteProcessor bp, int K) {
        int m = bp.getHeight() * bp.getWidth();

        int[] kumhist = kumulativ(bp.getHistogram());
        for (int y = 0; y < bp.getHeight(); y++) {
            for (int x = 0; x < bp.getWidth(); x++) {
                int farge = bp.getPixel(x, y);
                int utfarge = kumhist[farge] * (K - 1) / m;
                bp.putPixel(x, y, utfarge);
            }

        }
    }
/**
 * Setter sammen de tre  endrede farge kanalene i den opprinelige bildet
 * @param cp original ColorProcessor
 * @param redBp De tre Byte processorene med forskjellig farge
 * @param greenBp
 * @param blueBp 
 */
    public void visSamletBilde(ImageProcessor cp, ByteProcessor redBp, ByteProcessor greenBp, ByteProcessor blueBp) {
        int[] RGB = new int[3];
        for (int y = 0; y < cp.getHeight(); y++) {
            for (int x = 0; x < cp.getWidth(); x++) {
                RGB[0] = redBp.getPixel(x, y);
                RGB[1] = greenBp.getPixel(x, y);
                RGB[2] = blueBp.getPixel(x, y);
                cp.putPixel(x, y, RGB);

            }
        }

    }

    /////////////////////////////////////////////////////////////////////////////////
    /**
     * Lageer et kumulativt histogram
     * @param hist Et histogram fra en kanal
     * @return et kumulativt hitogram
     */
    private int[] kumulativ(int[] hist) {
        int[] khtab = new int[hist.length];
        khtab[0] = hist[0];

        for (int x = 1; x < hist.length; x++) {
            khtab[x] = khtab[x - 1] + hist[x];
        }
        return khtab;

    }

}//Klasse
