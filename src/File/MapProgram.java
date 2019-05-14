/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class MapProgram {
    
    public static void main(String[] args){
        Map<String, String> languages = new HashMap<>();
        
        // Insert values into the list
        languages.put("Java", "a compiled high level, OO, platform independent");
        languages.put("Python", "an interpreted, OO, high level prorammeing lanugage with dynamic semantics");
        languages.put("BASIC", "Beginners All Putposes Symboliuc Instruction code");
        languages.put("Algol", "an algorithmic language");
        languages.put("Lisp", "i dont know this one");
        
        if(languages.containsKey("Java")){
            System.out.println("Java already in the map");
        }
        
        System.out.println(languages.get("Java"));
        //Multiple put-keys overwrites the value
        languages.put("Java", "this course is about Java");
        
        System.out.println();
        System.out.println();
        
        //languages.remove("Lisp");
        if(languages.remove("Algol", "A family of algorithmic lanugages")){
            System.out.println("Algol removed");
        }else{
            System.out.println("Algol not found");
        }
        
        System.out.println();
        System.out.println();
        
        System.out.println(languages.replace("Lisp", "a functional programming language with imperative features"));
        System.out.println(languages.replace("Scala", "this will not be added"));
        
        System.out.println();
        System.out.println();
        
        for(String key: languages.keySet()){
            System.out.println(key + " : " + languages.get(key));
        }
        
    }
    
}
