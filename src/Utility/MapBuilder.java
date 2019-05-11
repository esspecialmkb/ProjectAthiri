/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import static java.lang.Math.random;
import java.util.Random;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class MapBuilder {
    protected int seedX, seedY;
    protected int mapSize;
    protected float min,max,range,delta;
    protected float freq, oct, div, exp;
    protected float[][] noise;
    
    /** genTerrain method changes the materials of the generated tile layers. **/
    public void genTerrain(){
    /** RANDOM OVERWORLD GEN WHOO!!!. **/

        seedX = (int) Math.floor(random() * 1000);
        seedY = (int) Math.floor(random() * 1000);

        // I need to be mindful of memory usage
        // Let's keep the world 'kinda' small 4x4 tile page chunks

        noise = new float[mapSize][mapSize];

        /** Use these to keep track of min and max. **/
        min = 0;
        max = 0;
        range = 0;
        delta = 0;

        /** Noise control. **/
        freq = 0.5f;
        oct = 0.5f;   // How large are the features?
        div = 24;     // This subdivides the sample points - inverse prop.
        exp = 1.2f;

        /** Generation. **/
        for(int pX = 0; pX < mapSize; pX++){
            for(int pY = 0; pY < mapSize; pY++){
                int wX = pX + seedX;
                int wY = pY + seedY;

                float value = (float) (oct * SimplexNoise.noise((wX/div) * freq, (wY/div) * freq, 0));

                noise[pX][pY] = (float) Math.pow(value, exp);
                noise[pX][pY] = value;
                if(noise[pX][pY] > max){
                    max = noise[pX][pY];
                }else if(noise[pX][pY] < min){
                    min = noise[pX][pY];
                }
            }
        }

        /** Calculate the range of values. **/
        delta = 0 - min;
        range += delta;
        range = max - min;
        //System.out.println("Range [" + range + "] = max [" + max + "] - min [" + min + "]");

        //if(maxPages == 0){
            //maxPages = 4;
        //}

        for(int pX = 0; pX < mapSize; pX++){
            for(int pY = 0; pY < mapSize; pY++){
                        
                /** Scale the range from raw value to 0-100. **/
                float value = (float) (((float)noise[pX][pY] + delta)/range) * 100;
            }
        }
    }
    /** Create a list of normally distributed random (or pseudo-random) numbers 
    * with a mean of   1.0   and a   standard deviation   of   0.5. **/
    public void genStdDevStub(){
        double[] list = new double[1000];
        double mean = 1.0, std = 0.5;
        Random rng = new Random();
        
        for(int i = 0;i<list.length;i++) {
            list[i] = mean + std * rng.nextGaussian();
        }
    }

    public double randNormDist(double mean, double std){
        Random rng = new Random();
        
        return mean + std * rng.nextGaussian();
    }
}
