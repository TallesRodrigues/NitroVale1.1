package com.example.tallesrodrigues.nitrovale11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.File;

/**
 * Created by TallesRodrigues on 6/19/2016.
 */
public class SPAD  {
    private float Hue, spadValue, saturation, brightness;
    private float A, Y, Cr, Cb, GMR, GDR, DGCI, NRI, NGI, Lf;
    private float R, G, B, Bi, Sa,intensity;
    private final double shutterspeed = 1.1;
    private final double ISO = 0.1;



    public SPAD(String mCurrentPhotoPath) {
        File sourceFile = new File(mCurrentPhotoPath);
        Bitmap frame = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());

        //get R,G,B
        int[] pixels = new int[frame.getHeight()*frame.getWidth()];
        frame.getPixels(pixels,0,frame.getWidth(),0,0,frame.getWidth(),frame.getHeight());
        long red = 0;
        long green = 0;
        long blue = 0;
        for (int i = 0; i < pixels.length; i++) {
            red += Color.red(pixels[i]);
            green += Color.green(pixels[i]);
            blue += Color.blue(pixels[i]);
        }
        R = red/pixels.length;
        G = green/pixels.length;
        B = blue/pixels.length;
        intensity = (R+G+B)/pixels.length;
        Log.e("intensity",String.valueOf(intensity));
        A = Math.max( Math.max(R,G),B) - Math.min( Math.min(R,G),B);

        calculateParameters();

    }


    public void calculateParameters() {
        Bi=  Math.max(Math.max(R,G),B)/255;
        Y= (float) (0.257*R+0.504*G+0.098*B+16);
        Cb= (float) (-0.148*R-0.291*G+0.0439*B+128);
        Cr= (float) (0.439*R-0.368*G-0.071*B+128);
        GMR=G-R;
        GDR=G/R;
        DGCI=((Hue/60-1)+(1-Sa)+(1-Bi))/3;
        NRI=R/(R+G+B);
        NGI=G/(R+G+B);
        Lf = (float) (1/(shutterspeed * ISO));

    }

    public double calculateSPAD() {
        // Equation will be determined with Matlab
        return 12.014155 - 0.050836*R + 0.176231*G -0.008915*B;
    }

    public void setIntensity(int length){
         intensity = (R+G+B)/length;
    }
    public float getIntensity(){return intensity;}

    public float getR(){ return R; } public float getG(){ return G; } public float getB(){ return B; }

    public float getHue() {
        return Hue;
    }

    public void setHue(float hue) {
        this.Hue = hue;
    }

    public float getSpadValue() {
        return spadValue;
    }

    public void setSpadValue(float spadValue) {
        this.spadValue = spadValue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getA() {
        return A;
    }

    public void setA(float a) {
        A = a;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getCr() {
        return Cr;
    }

    public void setCr(float cr) {
        Cr = cr;
    }

    public float getCb() {
        return Cb;
    }

    public void setCb(float cb) {
        Cb = cb;
    }

    public float getGMR() {
        return GMR;
    }

    public void setGMR(float GMR) {
        this.GMR = GMR;
    }

    public float getGDR() {
        return GDR;
    }

    public void setGDR(float GDR) {
        this.GDR = GDR;
    }

    public float getDGCI() {
        return DGCI;
    }

    public void setDGCI(float DGCI) {
        this.DGCI = DGCI;
    }

    public float getNRI() {
        return NRI;
    }

    public void setNRI(float NRI) {
        this.NRI = NRI;
    }

    public float getNGI() {
        return NGI;
    }

    public void setNGI(float NGI) {
        this.NGI = NGI;
    }

    public float getLf() {
        return Lf;
    }

    public void setLf(float lf) {
        Lf = lf;
    }


}