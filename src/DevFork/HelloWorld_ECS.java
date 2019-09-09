import java.util.HashMap;
import java.util.*;

//testing via https://www.tutorialspoint.com/compile_java_online.php

public class HelloWorld{
    HashMap<Long, Integer> entityMap;
    HashMap<Long, Position> positionMap;
    HashMap<Long, Mesh> meshMap;
    HashMap<Long, Name> nameMap;
    HashMap<Long, Velocity> velocityMap;
    
    EntityIDGen idGen;
    
    int simUpdateCount = 0;
    int simUpdateMax = 1;

    public static void main(String []args){
        System.out.println("Hello World");
        new HelloWorld();
    }
    
    HelloWorld(){
        entityMap = new HashMap<>();
        positionMap = new HashMap<>();
        meshMap = new HashMap<>();
        nameMap = new HashMap<>();
        velocityMap = new HashMap<>();
        
        idGen = new EntityIDGen();
        
        populateEntityMap();
        
        run();
    } 
    
    public void populateEntityMap(){
        System.out.println("Populating EntityMap");
        System.out.println();
        
        for(int i = 0; i < 10 ; i++){
            newEntity(i);
        }
    }
    
    public long newEntity(){
        long newID = idGen.getNewID();
        entityMap.put(newID, 0);
        
        System.out.println("New Entity id: " + newID);
        
        return newID;
    }
    
    public void newEntity(int componentMask){
        long newID = newEntity();
        
        String message = "Adding components: ";
        
        if((componentMask & 1) == 1){
            message += "Position...";
            positionMap.put(newID, new Position(newID, 0, 0));
        }if((componentMask & 2) == 2){
            message += "Mesh...";
            meshMap.put(newID, new Mesh(newID, 0));
        }if((componentMask & 4) == 4){
            message += "Name...";
            nameMap.put(newID, new Name(newID, "New Entity"));
        }if((componentMask & 8) == 8){
            message += "Velocity...";
            velocityMap.put(newID, new Velocity(newID, 0,0));
        } 
        
        System.out.println(message += " component mask = " + componentMask);
        System.out.println();
    }
    
    public void run(){
        System.out.println("Starting simulation steps");
        System.out.println();
        
        while(simUpdateCount < simUpdateMax){
            simUpdate();
            simUpdateCount++;
        }
        
        System.out.println();
        System.out.println("Finished simulation steps");
    }
    
    public void simUpdate(){
        System.out.println("Simulation step " + simUpdateCount);
        System.out.println();
        
        // Get the set of keys (entity ids) contained within this map
        Set<Long> entityKeys = entityMap.keySet();
        
        System.out.println("There are "+ entityKeys.size() + " keys present in entityMap");
        
        Set<Long> positionKeys = positionMap.keySet();
        System.out.println("There are "+ positionKeys.size() + " keys present in positionMap");
        
        Set<Long> meshKeys = meshMap.keySet();
        System.out.println("There are "+ meshKeys.size() + " keys present in meshMap");
        
        Set<Long> nameKeys = nameMap.keySet();
        System.out.println("There are "+ nameKeys.size() + " keys present in nameMap");
        
        Set<Long> velocityKeys = velocityMap.keySet();
        System.out.println("There are "+ velocityKeys.size() + " keys present in velocityMap");
        
    }
    
}

class EntityIDGen{
    long entityIDBase;
    
    EntityIDGen(){
        entityIDBase = 0L;
    }
    
    long getNewID(){
        entityIDBase += 1;
        return entityIDBase;
    }
}

class PositionManager{
    
}

interface Component{
    public int getType();
    public long getEntityID();
    public boolean isActive();
}

class Position implements Component{
    final float x,y;
    final long entityID;
    
    Position(long entityID, float x, float y){
        this.entityID = entityID;
        this.x = x;
        this.y = y;
    }
    
    public float getX(){ return this.x; }
    
    public float getY(){ return this.y; }
    
    @Override
    public int getType(){ return 1;}
    
    @Override
    public long getEntityID(){ return entityID;}
    
    @Override
    public boolean isActive(){ return true;}
}

class Mesh implements Component{
    final long entityID;
    final int meshID;
    
    Mesh(long entityID, int meshID){
        this.entityID = entityID;
        this.meshID = meshID;
    }
    
    public int getMeshID(){ return this.meshID;}
    
    @Override
    public int getType(){ return 2;}
    
    @Override
    public long getEntityID(){ return entityID;}
    
    @Override
    public boolean isActive(){ return true;}
}

class Name implements Component{
    final long entityID;
    final String name;
    
    Name(long entityID, String name){
        this.entityID = entityID;
        this.name = name;
    }
    
    public String getName(){ return this.name;}
    
    @Override
    public int getType(){ return 4;}
    
    @Override
    public long getEntityID(){ return entityID;}
    
    @Override
    public boolean isActive(){ return true;}
}

class Velocity implements Component{
    final float xV,yV;
    final long entityID;
    
    Velocity(long entityID, float x, float y){
        this.entityID = entityID;
        this.xV = x;
        this.yV = y;
    }
    
    public float getXV(){ return this.xV; }
    
    public float getYV(){ return this.yV; }
    
    @Override
    public int getType(){ return 8;}
    
    @Override
    public long getEntityID(){ return entityID;}
    
    @Override
    public boolean isActive(){ return true;}
}
