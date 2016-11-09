
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
Oppgave 1 i oblig 11
 Denne pluggen lager auto kontrast på farge bilder
 */
 
public class Farge_Terskling implements PlugInFilter {

    public int setup(String arg, ImagePlus im) {

        this.imp = imp;

        return DOES_RGB;

        int redLav,greenLav,blueLav = 75;
        int redHeigh,greenHeigh,blueHeige=200;


    }//setup()

    public void run(ImageProcessor cp) {
        


        
    String title = "Terskelbilde";
        ImagePlus terskel = new ImagePlus(title, setTerskel(cpredLav,greenLav,blueLav,redHeigh,greenHeigh,blueHeige));            
        terskel.show();
      

    }//RUN
//////////////////////////////////////////////

    public ByteProcessor setTerskel(ImageProcessor cp, int redlow, int greenlow,int bluelow,int redHeigh,int greenHeigh,int blueHeigh) {
        ByteProcessor temp = new ByteProcessor(cp.getHeight(), cp.getWidth());

        int[] RGB = new int[3];

        for (int y = 0; y < cp.getHeight(); y++) {
            for (int x = 0; x < cp.getWidth(); x++) {
                cp.getPixel(x, y, RGB);
               if(terskel(  RGB[0],redlow,redHeigh)||
                terskel(RGB[1],greenlow,greenHeigh)||
                terskel(RGB[2],bluelow,blueHeigh)){
                    temp.putPixel(255);
                }else temp.putPixel(0);

            }
        }
        return temp;
    }

    

    /////////////////////////////////////////////////////////////////////////////////
    

    private int terskel(int pix,int lav, int high){
         boolean verdi;
         if(pix<lav||pix>high){
            verdi=false;
         }else {
            verdi =true;
         }
        retur verdi;
    }

}//Klasse
