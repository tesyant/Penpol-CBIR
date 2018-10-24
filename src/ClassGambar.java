
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Afif
 */
public class ClassGambar {
    // Variabel Global
public ImageIcon SourceIcon; 
public Image SourceImage;
public ImageIcon ScaleIcon; 
public Image ScaleImage;
public Image ResultImage; 
public Image ScaleResultImage;
public ImageIcon ScaleResultIcon;
public String URLImage;
public boolean ScaledFlag=false; 
public BufferedImage SourceBuffer; 
public BufferedImage ResultBuffer; 
public long sWidth;
public long sHeight;
public ImageIcon[] hasil = new ImageIcon[5];


//konstruktor
ClassGambar(String Url, long width, long height){
    URLImage=Url;
    if(width<=0 ||height <=0)
    {
        ScaledFlag=false;
    }
    else
    {
        ScaledFlag=true;
        sWidth=width;
        sHeight=height;
    }
}

    public ClassGambar() {
    }


public ImageIcon GetIcon(){
    if(!URLImage.equals(""))
    {
        SourceIcon = new ImageIcon(URLImage); 
        SourceImage = SourceIcon.getImage(); 
    try{
        SourceBuffer=ImageIO.read(new File(URLImage));
        SourceBuffer = resize(SourceBuffer, 120, 120);
    }
    catch(IOException x){
        JOptionPane.showMessageDialog(null, x.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
    }
    
    System.out.println(SourceIcon.getIconWidth());
    if(ScaledFlag)
        {
        ScaleImage=SourceImage.getScaledInstance((int)sWidth, (int)sHeight,
        Image.SCALE_DEFAULT);
        ScaleIcon=new ImageIcon(ScaleImage); 
        return ScaleIcon;
        }
    else
    {
        return SourceIcon;
    }
    }
    else
    {
        return null;
    }
}

public int[] getRGB(BufferedImage b){
    int tWidth=b.getWidth(); 
    int tHeight=b.getHeight();
    int x,y;
    int RGB,Red=0,Green=0,Blue=0;
    int[] R = new int[tWidth*tHeight];
    int[] G = new int[tWidth*tHeight];
    int[] B = new int[tWidth*tHeight];
    Color tWarna; 
    for(x=0;x<tWidth;x++)
    {
        for(y=0;y<tHeight;y++)
        {   
            RGB=b.getRGB((int)x, (int)y);
            tWarna=new Color(RGB);
            R[(x*tHeight)+y]= tWarna.getRed();
            G[(x*tHeight)+y]= tWarna.getGreen();
            B[(x*tHeight)+y]=tWarna.getBlue();
        }
    }
    for(int i=0; i<R.length;i++){
        Red = Red+R[i];
        Green = Green+G[i];
        Blue = Blue+B[i];
    }
    Red = Red/R.length;
    Green = Green/G.length;
    Blue = Blue/B.length;
    
    int[] hasil = {Red,Green,Blue};
    return hasil;
}

//fungsi Grayscale
public void Proses(){ 
    ResultBuffer=deepCopy(SourceBuffer);
    int tWidth=ResultBuffer.getWidth(); 
    int tHeight=ResultBuffer.getHeight();
    int x,y;
    int RGB,Red=0,Green=0,Blue=0;
    int[] R = new int[tWidth*tHeight];
    int[] G = new int[tWidth*tHeight];
    int[] B = new int[tWidth*tHeight];
    Color tWarna; 
    for(x=0;x<tWidth;x++)
    {
        for(y=0;y<tHeight;y++)
        {   
            RGB=ResultBuffer.getRGB((int)x, (int)y);
            tWarna=new Color(RGB);
            R[(x*tHeight)+y]= tWarna.getRed();
            G[(x*tHeight)+y]= tWarna.getGreen();
            B[(x*tHeight)+y]=tWarna.getBlue();
//            System.out.println(R[(x*tHeight)+y]+", "+G[(x*tHeight)+y]+", "+B[(x*tHeight)+y]);
        }
    }
    for(int i=0; i<R.length;i++){
        Red = Red+R[i];
        Green = Green+G[i];
        Blue = Blue+B[i];
    }
    Red = Red/R.length;
    Green = Green/G.length;
    Blue = Blue/B.length;
    
    
    loadData(Red, Green, Blue);
}

static BufferedImage deepCopy(BufferedImage bi){ 
    ColorModel cm=bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied(); 
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm,raster,isAlphaPremultiplied,null);
}

private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
}

// File representing the folder that you select using a FileChooser
    static final File dir = new File("C:\\Users\\Afif\\Pictures\\brg");

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
        "gif", "png", "bmp", "JPG", "jpg"
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    public double jarak(int r1, int g1, int b1, int r2, int g2, int b2){
        return Math.sqrt(((r2-r1)*(r2-r1))+((g2-g1)*(g2-g1))+((b2-b1)*(b2-b1)));
    }

    public void loadData(int R, int G, int B) {
        int[] rgb = new int[3]; 
        if (dir.isDirectory()) { // make sure it's a directory
            int index=0;
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(f);
                    img = resize(img,120,120);
                    rgb=getRGB(img);
                    if(jarak(R,G,B,rgb[0],rgb[1],rgb[2])<40){
                        
                        if(index<5){
                            hasil[index]=new ImageIcon(img);
                        }
                        index++;
                    }
                    System.out.println("image: " + f.getName());
                    System.out.println(jarak(R,G,B,rgb[0],rgb[1],rgb[2]));
                    // you probably want something more involved here
                    // to display in your UI
                } catch (final IOException e) {
                    // handle errors here
                }
                
            }
            
        }
    }

}
