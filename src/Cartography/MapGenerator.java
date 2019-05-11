/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cartography;

import Utility.SimplexNoise;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.random;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class MapGenerator{
    public static String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    public static String mapDirectoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\MapData\\";
    public static String fileName = "test";
    public static String extension = ".txt";
    
    private int seedX;
    private int seedY;
    private double[][] noise;
    private int[][] terrainIndex;
    private double min,max,range,delta;
    private double freq,oct,div,exp;

    public MapGenerator(int fieldSize){
        noise = new double[fieldSize][fieldSize];
        terrainIndex = new int[fieldSize][fieldSize];

        seedX = (int) Math.floor(random() * 1000);
        seedY = (int) Math.floor(random() * 1000);
    }

    public void setFrequency(double frequency){
        freq = frequency;
    }
    public void setOctave(double octave){
        oct = octave;
    }
    public void setDivision(double division){
        div = division;
    }
    public void setExponent(double exponent){
        exp = exponent;
    }

    public void generate(){
        int tx =0;
        int ty =0;

        for(int x= 0;x < noise[0].length;x++){
            for(int y = 0; y < noise[0].length; y++){
                tx = x + seedX;
                ty = y + seedY;

                double nx = (tx/div) * freq;
                double ny = (ty/div) * freq;

                double value = oct * SimplexNoise.noise(nx, ny);

                noise[x][y] = Math.pow(value, exp);
                noise[x][y] = value;

                if(noise[x][y] > max){
                    max = noise[x][y];
                }else if(noise[x][y] < min){
                    min = noise[x][y];
                }
            }
        }

        delta = 0 - min;
        range += delta;
        range = max - min;

        //Need to convert values to terrain indices before dumping data
        //That way, the pages wont have to deal with conversion, just reading
        for(int x= 0;x < noise[0].length;x++){
            for(int y = 0; y < noise[0].length; y++){
                float value = (float) (((float)noise[x][y] + delta)/range);
                terrainIndex[x][y] = (int) Math.floor(value * 100);

                if(terrainIndex[x][y] >0 && terrainIndex[x][y] < 10){
                    terrainIndex[x][y] = 12;
                }else if(terrainIndex[x][y] >= 10 && terrainIndex[x][y] < 15){
                    terrainIndex[x][y] = 11;    // Beach dirt
                }else if(terrainIndex[x][y] >= 15 && terrainIndex[x][y] < 50){
                    terrainIndex[x][y] = 11;
                }else if(terrainIndex[x][y] >= 25 && terrainIndex[x][y] < 50){
                    terrainIndex[x][y] = 3;
                }else if(terrainIndex[x][y] >= 50 && terrainIndex[x][y] < 85){
                    terrainIndex[x][y] = 1;
                }else if(terrainIndex[x][y] >= 85 && terrainIndex[x][y] < 95){
                    terrainIndex[x][y] = 8;
                }else if(terrainIndex[x][y] >= 95 ){
                    terrainIndex[x][y] = 7;
                }
            }
        }

        dumpDataFile();
        for(int px=0; px<getFieldSize()/16;px++){
            for(int py=0; py<getFieldSize()/16; py++){
                dumpDataPages(px,py);
            }
        }
    }

    // Need a method to generate terrain
    // Need a method to generate town architecture (walls, buildings, etc..)
    private void dumpMasterFile(){
        try{
            FileWriter writer = new FileWriter(directoryName + "MyNewMap.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("MapName");
            bufferedWriter.newLine();
            bufferedWriter.write("fieldSize:" + getFieldSize());
            bufferedWriter.newLine();

            int pageDim = noise[0].length / 16;

            for(int x= 0;x < pageDim;x++){
                for(int y = 0; y < pageDim; y++){
                    //write page references
                    bufferedWriter.write("page." + x + "." + y);

                    //write page data
                    dumpDataPages(x, y);
                }
            }
            bufferedWriter.write("DataComplete");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method writes the generated data to a file so the TileManager can read it
    private void dumpDataFile(){
        //from https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
        // File writers use default char encoding 'UTF'
        // Creates new file if it doesn't exist, overwrites 
        // if it does unless true is passed as 2nd param of constructor
        // BufferedReader is the prefferred way to write text
        try{
            //FileWriter writer = new FileWriter(directoryName + "MyMap.txt", true);
            FileWriter writer = new FileWriter(directoryName + "MyMap.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("ManGenData");
            bufferedWriter.newLine();
            bufferedWriter.write("fieldSize:" + getFieldSize());
            bufferedWriter.newLine();

            for(int x= 0;x < noise[0].length;x++){
                for(int y = 0; y < noise[0].length; y++){
                     bufferedWriter.write(x + " " + y + ":" + terrainIndex[x][y]);
                     bufferedWriter.newLine();
                }
            }
            bufferedWriter.write("DataComplete");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Similar to dumpDataFile but for pages (Game Optimization???)
    private void dumpDataPages(int pageX , int pageY){
        int fieldSize = noise[0].length;
        int pages = fieldSize / 16;
        int pageNX = pageX *16;
        int pageNY = pageY *16;
        //from https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
        // File writers use default char encoding 'UTF'
        // Creates new file if it doesn't exist, overwrites 
        // if it does unless true is passed as 2nd param of constructor
        // BufferedReader is the prefferred way to write text
        try{
            //FileWriter writer = new FileWriter(directoryName + "MyPagedMap.txt", true);
            FileWriter writer = new FileWriter(mapDirectoryName + "page."+pageX+"."+pageY+".txt", false);

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write("MapGenPage");
            bufferedWriter.newLine();
            bufferedWriter.write("pageX:" + pageX+"pageY:" + pageY);
            bufferedWriter.newLine();

            //for(int px= 0;px < noise[0].length;px++){
            //    for(int py = 0; py < noise[0].length; py++){
                    //bufferedWriter.write("pageBlock:"+px+" "+py);
                    //bufferedWriter.newLine();
                    for(int x=0; x<16; x++){
                        for(int y=0; y<16; y++){
                            bufferedWriter.write(x + " " + y + ":" + terrainIndex[pageNX + x][pageNY + y]);
                            bufferedWriter.newLine();
                        }
                    }

            //    }
            //}
            bufferedWriter.write("DataComplete");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getNoiseValue(int x, int y){
        return (noise[x][y] + delta)/range;
    }
    public int getFieldSize(){return this.noise[0].length;}
}
