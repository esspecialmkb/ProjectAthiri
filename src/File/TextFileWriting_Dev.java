/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * // ref https://docs.oracle.com/javase/tutorial/essential/io/
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TextFileWriting_Dev {
    public static String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    public static String fileName = "test";
    public static String extension = ".txt";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    //from https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
    // File writers use default char encoding 'UTF'
    // Creates new file if it doesn't exist, overwrites 
    // if it does unless true is passed as 2nd param of constructor
    public static void read1(){
        try {
            FileWriter writer = new FileWriter(directoryName + "MyFile.txt", true);
            writer.write("Hello World");
            writer.write("\r\n");   // write new line
            writer.write("Good Bye!");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // BufferedReader is the prefferred way to write text
    public static void read2(){
        try {
            FileWriter writer = new FileWriter(directoryName + "MyFile.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
 
            bufferedWriter.write("Hello World");
            bufferedWriter.newLine();
            bufferedWriter.write("See You Again!");
 
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void read3(){
        try {
            FileOutputStream outputStream = new FileOutputStream(directoryName + "MyFile.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            
            //Vietnamese unicode string
            bufferedWriter.write("Xin chào");
            bufferedWriter.newLine();
            bufferedWriter.write("Hẹn gặp lại!");
             
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
