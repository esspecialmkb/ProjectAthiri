import java.util.HashMap;
import java.util.*;

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
        // Setup maps
        entityMap = new HashMap<>();
        positionMap = new HashMap<>();
        meshMap = new HashMap<>();
        nameMap = new HashMap<>();
        velocityMap = new HashMap<>();
        
        idGen = new EntityIDGen();
        
        run();
    } 
    
    public void populateEntityMap(){
        System.out.println("Populating EntityMap");
        System.out.println();
        
        long entity1 = newEntity(1 + 8);
        long entity2 = newEntity(1 + 8);
        long entity3 = newEntity(1 + 8);
        long entity4 = newEntity(1 + 8);
        
        setComponent(entity1, new Position(entity1, 10, 20));
        setComponent(entity2, new Position(entity1, 15, 20));
        setComponent(entity3, new Position(entity1, 50, 20));
        setComponent(entity4, new Position(entity1, 10, 20));
        
    }
    
    public long newEntity(){
        long newID = idGen.getNewID();
        entityMap.put(newID, 0);
        
        System.out.println("New Entity id: " + newID);
        
        return newID;
    }
    
    public long newEntity(int componentMask){
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
        
        return newID;
    }
    
    public Component getComponent(long entityID, int type){
        if(type == 1){
            return positionMap.get(entityID);
        }if(type == 2){
            return meshMap.get(entityID);
        }if(type == 4){
            return nameMap.get(entityID);
        }if(type == 8){
            return velocityMap.get(entityID);
        }
    }
    
    public void setComponent(long entityID, Component component){
        int type = component.getType();
        int newType = 0;
        
        if(type == 1){
            if( positionMap.put(entityID,component) == null){
                newType = 1;
            }
        }if(type == 2){
            if( meshMap.put(entityID,component) == null){
                newType = newType | 2;
            }
        }if(type == 4){
            if( nameMap.put(entityID,component) == null){
                newType = newType | 4;
            }
        }if(type == 8){
            if( velocityMap.put(entityID,component) == null){
                newType = newType | 8;
            }
        }
        
        if(newType > 0){
            bitMask = entityMap.get(entityID);
            entityMap.put(entityID, bitMask | newType);
        }
        
    }
    
    public void run(){
        System.out.println("Setup...");
        System.out.println();
        populateEntityMap();
        
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
