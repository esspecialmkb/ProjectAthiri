/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Tiles;

import DevFork.Controls.Entity;
import DevFork.OpenSimplexNoise;
import DevFork.VirtualMesh;
import Utility.SimplexNoise;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  TileChunk maintains a page of tiles
 * 
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TileChunk extends AbstractControl{
    int x;
    int y;
    float sceneX;
    float sceneY;
    double[][] tileData;
    int[][] colData;
    Tile[][] tiles;
    float tileSize;
    //Node tileChunkNode;
    boolean isLoaded;
    
    Collection<Entity> entityList = new ArrayList<>();

    //  Scene element
    VirtualMesh m;
    Geometry g;
    Node n;
    Material tileMat;
    boolean active;
    
    //  State control
    int state = -1;
    int newState = -1;
    
    public TileChunk(){
        isLoaded = false;
    }
    
    public TileChunk(int x, int y, float tileSize){
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        tileData = new double[16][16];
        colData = new int[16][16];
        tiles = new Tile[16][16];
        m = new VirtualMesh();
        n = new Node("TileChunk " + x + ", " + y);
        //isLoaded = false;
        
        this.state = 1;
    }
    public float getTileSize(){ return this.tileSize;}
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public int getState(){return this.state;}
    public void setState(int newState){
        this.newState = newState;
    }
    
    public void addEntity(Entity entity){ entityList.add(entity);}
    public void removeEntity(Entity entity){ entityList.remove(entity);}
    
    public void setScenePos(float sceneX, float sceneY){
        this.sceneX = sceneX;
        this.sceneY = sceneY;
    }
    public void move(float movX,float movY){
        sceneX += movX;
        sceneY += movY;
    }
    public void setActive(){
        active = true;
    }
    
    public void setTileMat(Material mat){ this.tileMat = mat;}
    public void unloadChunk(){
        if(isLoaded){
            //clear tile array

            isLoaded = false;
        }
    }

    public void loadChunk(){
        if(!isLoaded){
            // Load the chunk data
            for(int iX = 0; iX < 24; iX++){
                for(int iY = 0; iY < 24; iY++){
                    //int tileX = (this.x * (chunkSize * tileSize)) + (x * tileSize);
                    //int tileY = (this.y * (chunkSize * tileSize)) + (y * tileSize);

                    // Generate or load tile data
                    // var perlinValue = noise.perlin2(tileX / 100, tileY / 100); 

                    // var key = “”;
                    // var animationKey = “”;
                }
            }
        }
    }
    public Node getNode(){return n;}
    public void generateChunkTerrain(OpenSimplexNoise noise, float FEATURE_SIZE){
        //if(!isLoaded){
            //tileData = new double[16][16];
            // Load the chunk data
            for(int iX = 0; iX < 16; iX++){
                for(int iY = 0; iY < 16; iY++){
                    int tileX = (this.x * (16)) + (iX);
                    int tileY = (this.y * (16)) + (iY);
                    // Generate or load tile data
                    // var perlinValue = noise.perlin2(tileX / 100, tileY / 100);
                    double value = noise.eval(tileX / FEATURE_SIZE,tileY / FEATURE_SIZE, 0.0) + 0.5d;
                    //  assumed range: 150

                    //System.out.println("ChunkGen " + x + ", " + y + ": Tile "+ iX +", "+ iY + ": " + value);
                    //System.out.println("ChunkGen " + tileX + ", " + tileY + ": " + value);

                    tileData[iX][iY] = ((value * 100));

                    //System.out.println("POSTGen " + tileX + ", " + tileY + ": " + tileData[iX][iY]);

                    // var key = “”;
                    // var animationKey = “”;
                }
            }
            isLoaded = true;
        //}
    }
    public void buildMesh(){
        double range = 150;

        //if(isLoaded){
            for(int x =0; x < 16; x++){
                for(int y = 0; y < 16; y++){
                    double value = (tileData[x][y] + 50f);
                    value = (float)((value / range) * 100);
                    tiles[x][y] = new Tile(x,y,tileSize);
                    //value =value + 10;

                    //System.out.println(x + ", " + y + " Build value: " + value);

                    if(value < 10){
                        //
                        tiles[x][y].setColor(ColorRGBA.Blue);
                        colData[x][x] = 0;

                    }else if((value >= 10) && (value < 20)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Cyan);
                        colData[x][x] = 0;

                    }else if((value >= 20) && (value < 30)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Yellow);
                        colData[x][x] = 0;

                    }else if((value >= 30) && (value < 40)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Green);
                        colData[x][x] = 0;

                    }else if((value >= 40) && (value < 50)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Green);
                        colData[x][x] = 0;

                    }else if((value >= 50) && (value < 60)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Green);
                        colData[x][x] = 0;

                    }else if((value >= 60) && (value < 70)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Brown);
                        colData[x][x] = 0;

                    }else if((value >= 70) && (value < 80)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Brown);
                        colData[x][x] = 0;

                    }else if((value >= 80) && (value < 90)){
                        //
                        tiles[x][y].setColor(ColorRGBA.Black);
                        colData[x][x] = 0;

                    }else if((value >= 90) /*&& (value < 100)*/){
                        //
                        tiles[x][y].setColor(ColorRGBA.White);
                        colData[x][x] = 0;

                    }

                    tiles[x][y].createTile(m);

                }
            }

            m.buildMesh();
            
            n.setLocalTranslation(x*16*tileSize,y*16*tileSize,0);
            g = new Geometry("Chunk", m.getMesh());
            g.setMaterial(tileMat);
            n.attachChild(g);
            n.addControl(this);
        //}                
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(!active){
            //
        }else if(active){
            System.out.println("Chunk " + x + ", " + y + " active!");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        System.out.println("Write chunk...");
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        
        //  Write the chunk state
        oc.write(this.state, "ChunkState", 0);
        
        if(this.state > 0){
            // pos
            oc.write(this.x, "ChunkX", 0);
            oc.write(this.y, "ChunkY", 0);
        }if(this.state > 1){
            // tileData
            oc.write(tileData, "TileData", null);
        }if(this.state > 2){
            // mesh data
            // Create getters for VirtualMesh or make it savable
            // Save the Geometry for now!
            oc.write(g, "ChunkGeometry", null);
            
        }if(this.state > 3){
            // entity data
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        //  Read the chunk state
        this.state = ic.readInt("ChunkState", -1);
        
        if(this.state > 0){
            // pos
            this.x = ic.readInt("ChunkX", 0);
            this.y = ic.readInt("ChunkY", 0);
        }if(this.state > 1){
            // tileData
            this.tileData = ic.readDoubleArray2D("TileData", null);
        }if(this.state > 2){
            // mesh data
            this.g = (Geometry) ic.readSavable("ChunkGeometry", null);
        }if(this.state > 3){
            // entity data
        }
    }
}// End of TileChunk.class
