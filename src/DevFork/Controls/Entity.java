/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Controls;

import DevFork.Tiles.TileChunk;
import DevFork.VirtualMesh;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public abstract class Entity extends AbstractControl{
    String name;

    float x,y;
    float vX, vY;
    float tX, tY;
    int facing;
    TileChunk chunk;
    VirtualMesh m;
    Geometry g;
    Node n;

    public Entity(){}

    public abstract int getType();
    public abstract void buildEntity(TileChunk chunk, Material mat);
    public abstract void buildMesh();
    
    //  Library Methods
    public void setActiveChunk(TileChunk chunk){this.chunk = chunk;}
    public void setLocation(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setVelocity(float x, float y){
        this.vX = x;
        this.vY = y;
    }
    public void setTarget(float x, float y){

    }
    public void setFacing(int face){ this.facing = face;}
    
    public TileChunk getActiveChunk(){ return this.chunk;}
    public Vector2f getLocation(){ return new Vector2f(this.x, this.y);}
    public Vector2f getVelocity(){ return new Vector2f(this.vX, this.vY);}
    public Vector2f getTarget(){ return new Vector2f(this.tX, this.tY);}
    public int getFacing(){ return this.facing;}
    
    public Geometry getGeometry(){ return this.g;}
}
