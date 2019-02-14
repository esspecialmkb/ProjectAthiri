/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Cartography.TileMap;
import Cartography.TileMapPage;
import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import java.io.IOException;
import static java.lang.Math.random;

/**
 * Moves the map and culls TileMapPages within TileMaps
 * @author esspe
 */
public class TileMapControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    
    private TileMap map;
    private TileMap_World mapWorld; // Experimental
    public Material[] archMat;
    public Material[] terrainMat;
    private int maxPages;
    private int seedX;
    private int seedY;
    private float mapX, mapY, moveX, moveY;
    private int mapSize = 64;
    
    public TileMapControl(){
        map = new TileMap();
    }
    
    public void buildMatLists(AssetManager assetManager){
        archMat = new Material[256];
        terrainMat = new Material[16];
    }
    
    public Node getMapNode(){return map.mapNode;}
    
    public void genTerrainOld(){
    /** RANDOM OVERWORLD GEN WHOO!!!. **/

        seedX = (int) Math.floor(random() * 1000);
        seedY = (int) Math.floor(random() * 1000);

        // I need to be mindful of memory usage
        // Let's keep the world 'kinda' small 4x4 tile page chunks

        double[][] noise = new double[64][64];

        /** Use these to keep track of min and max. **/
        double min = 0;
        double max = 0;
        double range = 0;
        double delta = 0;

        /** Noise control. **/
        float freq = 0.5f;
        float oct = 0.5f;   // How large are the features?
        float div = 24;     // This subdivides the sample points - inverse prop.
        float exp = 1.2f;

        /** Generation. **/
        for(int pX = 0; pX < 64; pX++){
            for(int pY = 0; pY < 64; pY++){
                int wX = pX + seedX;
                int wY = pY + seedY;

                double value = oct * SimplexNoise.noise((wX/div) * freq, (wY/div) * freq, 0);

                noise[pX][pY] = Math.pow(value, exp);
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

        if(maxPages == 0){
            maxPages = 4;
        }

        for(int pX = 0; pX < maxPages; pX++){
            for(int pY = 0; pY < maxPages; pY++){
                TileMapPage genPage = map.pages.get((pX * maxPages) + pY);
                for(int tX = 0; tX < 16; tX++){
                    for(int tY = 0; tY < 16; tY++){
                        int wX = (pX * 16) + tX;
                        int wY = (pY * 16) + tY;
                        
                        /** Scale the range from raw value to 0-1. **/
                        float value = (float) (((float)noise[wX][wY] + delta)/range);
                        
                        /** Parse the value according to height ranges. **/
                        if(value < 0.1d){ // Water layer [2,0]
                            //this.setTile(pX,pY,2,0);
                            genPage.tileGeo[tX][tY].setMaterial(terrainMat[3]);
                        }else if(value >= 0.1d && value < 0.6d){ // Grass layers
                            //this.setTile(pX,pY,0,0);
                            genPage.tileGeo[tX][tY].setMaterial(terrainMat[10]);
                        }else if(value >= 0.7d && value < 0.8d){ // Moutain layers
                            //this.setTile(pX,pY,3,1);
                            genPage.tileGeo[tX][tY].setMaterial(terrainMat[8]);
                        }else if(value >= 0.8d && value < 0.9d){ // Snow layers
                            //this.setTile(pX,pY,1,1);
                            genPage.tileGeo[tX][tY].setMaterial(terrainMat[4]);
                        }else if(value >= 0.9d ){ // Snow layers
                            //this.setTile(pX,pY,1,1);
                            genPage.tileGeo[tX][tY].setMaterial(terrainMat[7]);
                        }
                    }
                }
            }
        }
    }
    
    public void genTerrain(){
    /** RANDOM OVERWORLD GEN WHOO!!!. **/

        seedX = (int) Math.floor(random() * 1000);
        seedY = (int) Math.floor(random() * 1000);

        // I need to be mindful of memory usage
        // Let's keep the world 'kinda' small 4x4 tile page chunks

        double[][] noise = new double[mapSize][mapSize];

        /** Use these to keep track of min and max. **/
        double min = 0;
        double max = 0;
        double range = 0;
        double delta = 0;

        /** Noise control. **/
        float freq = 0.5f;
        float oct = 0.5f;   // How large are the features?
        float div = 24;     // This subdivides the sample points - inverse prop.
        float exp = 1.2f;

        /** Generation. **/
        for(int pX = 0; pX < mapSize; pX++){
            for(int pY = 0; pY < mapSize; pY++){
                int wX = pX + seedX;
                int wY = pY + seedY;

                double value = oct * SimplexNoise.noise((wX/div) * freq, (wY/div) * freq, 0);

                noise[pX][pY] = Math.pow(value, exp);
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
        System.out.println("Range [" + range + "] = max [" + max + "] - min [" + min + "]");

        if(maxPages == 0){
            maxPages = 4;
        }

        for(int pX = 0; pX < mapSize; pX++){
            for(int pY = 0; pY < mapSize; pY++){
                        
                /** Scale the range from raw value to 0-1. **/
                float value = (float) (((float)noise[pX][pY] + delta)/range);
                System.out.println("Value: " + value + " x: "+pX + ", y: " + pY);
                /** Parse the value according to height ranges. **/
                if(value < 0.1d){ // Water layer [2,0]
                    //map.setTile(pX,pY,2,0);
                    map.tileGeo[pX][pY].setMaterial(terrainMat[3]);
                }else if(value >= 0.1d && value < 0.6d){ // Grass layers
                    //map.setTile(pX,pY,0,0);
                    map.tileGeo[pX][pY].setMaterial(terrainMat[2]);
                }else if(value >= 0.6d && value < 0.8d){ // Moutain layers
                    //map.setTile(pX,pY,3,1);
                    map.tileGeo[pX][pY].setMaterial(terrainMat[8]);
                }else if(value >= 0.8d && value < 0.9d){ // Snow layers
                    //map.setTile(pX,pY,1,1);
                    map.tileGeo[pX][pY].setMaterial(terrainMat[4]);
                }else if(value >= 0.9d ){ // Snow layers
                    //map.setTile(pX,pY,1,1);
                    map.tileGeo[pX][pY].setMaterial(terrainMat[7]);
                }
            }
        }
    }
    
    public void buildMap(int tileScreenSize){
        /** Create the TileMap. **/
        //int tileScreenSize = 720 /16;
        int mapSize = 64;
        
        /** REFACTORED TILEMAP CONSTRUCTOR. **/
        /** Initialize our data. **/
        map.size = tileScreenSize;
        map.data = new int[mapSize][mapSize];
        map.tiles = new Quad[mapSize][mapSize];
        map.tileGeo = new Geometry[mapSize][mapSize];
        map.mapNode = new Node("TileMap_root");

        /** Build the map. **/
        //      This is a brute-force approach (individual Geometries for each tile
        //  instead of a single effecient Geometry with custom Mesh) and serves 
        //  as a personal proof-of-concept
        //      In the future, map entire double for-loop will be refactored
        for(int dx = 0; dx < mapSize; dx++){
            for(int dy = 0; dy < mapSize; dy++){
                /** TileMap data. **/
                map.data[dx][dy] = 0;

                /** Quads. **/
                map.tiles[dx][dy] = new Quad(map.size, map.size);

                /** Geometry. **/
                map.tileGeo[dx][dy] = new Geometry("Tile: " + dx + ", " + dy, map.tiles[dx][dy]);
                map.tileGeo[dx][dy].setLocalTranslation(dx * map.size, dy * map.size, -0.5f);    // Z = -0.5 to keep tilemap behind the player
                /** Material. **/
                if(terrainMat != null){
                    //map.tileGeo[dx][dy].setMaterial(terrainMat[2]);
                }
                /** For our brute-force method, every geometry is added to the mapNode. **/
                map.mapNode.attachChild(map.tileGeo[dx][dy]);
            }
        }

        map.getNode().setLocalTranslation(0,0,-0.5f);

        //sysLog("Gen map");
        genTerrain();
    }
    
    public void moveMap(float mx, float my){
        // the movement vector should be scaled by tileSize here
        this.moveX = mx * map.size;
        this.moveY = my * map.size;
    }
    
    public Vector2f getWorldPosition(){
        Vector3f localTranslation = map.mapNode.getLocalTranslation();
        
        return new Vector2f(localTranslation.x, localTranslation.y);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls TileMap movement,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        map.mapNode.move(moveX,moveY,0);
        
        // Reset movement to ZERO after moving map 
        mapX += moveX;
        mapY += moveY;
        moveX = 0;
        moveY = 0;
        
        // This control will also need to check if any TileMapPages need to be added or removed
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        TileMapControl control = new TileMapControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
    
}
