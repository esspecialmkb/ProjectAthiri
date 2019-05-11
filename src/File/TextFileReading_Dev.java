/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * // ref https://docs.oracle.com/javase/tutorial/essential/io/
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TextFileReading_Dev {

    public static String fileName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\TextData.txt";
    public static void main(String[] args) {
	    // write your code here
        try {
            read1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    // augmented with https://www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
    public static void read1() throws IOException {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        File file = new File(fileName);
        
        //augemented utf encoding
        InputStreamReader reader = new InputStreamReader(
        new FileInputStream(fileName), "UTF-8");

        BufferedReader br = new BufferedReader(reader);
        //reader
        //new FileReader(file)

        String st;
        while ((st = br.readLine()) != null)
            System.out.println(": " + st);
        
        br.close();
        reader.close();
    }

    public static void read2() throws IOException {
        // pass the path to the file as a parameter
        FileReader fr = new FileReader(fileName);

        int i;
        while ((i=fr.read()) != -1)
            System.out.print((char) i);
        
        fr.close();
    }

    public static void read3() throws FileNotFoundException {
        // pass the path to the file as a parameter
        File file =
                new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine())
            System.out.println(sc.nextLine());
        
        sc.close();
    }

    public static void read4() throws FileNotFoundException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        // we just need to use \\Z as delimiter
        sc.useDelimiter("\\Z");

        System.out.println(sc.next());
        
        sc.close();
    }

    public static String readFileAsString(String aFileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public static void read5() throws Exception {
        String data = readFileAsString("C:\\Users\\pankaj\\Desktop\\test.java");
        System.out.println(data);
    }
    
    public static void readUTF16(){
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-16");
            int character;
 
            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
            reader.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
