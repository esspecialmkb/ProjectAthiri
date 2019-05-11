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
import com.jme3.math.FastMath;
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
import java.util.ArrayList;

/**
 * @TODO Implement Paging system
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
    public Material[] debugMat;
    public Geometry debugGeo;
    public Geometry geoPrefab;
    public Geometry[] debugPageGeo;
    
    public boolean debug = false;
    private int maxPages;
    private int seedX;
    private int seedY;
    private float mapX, mapY, moveX, moveY;
    public float playerX, playerY;
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
    
    /** genTerrain method changes the materials of the generated tile layers. **/
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
        //System.out.println("Range [" + range + "] = max [" + max + "] - min [" + min + "]");

        if(maxPages == 0){
            maxPages = 4;
        }

        for(int pX = 0; pX < mapSize; pX++){
            for(int pY = 0; pY < mapSize; pY++){
                map.setArchTileMaterial(pX, pY, archMat[20]);
                        
                /** Scale the range from raw value to 0-1. **/
                float value = (float) (((float)noise[pX][pY] + delta)/range);
                //System.out.println("Value: " + value + " x: "+pX + ", y: " + pY);
                /** Parse the value according to height ranges. **/
                if(value < 0.1d){ // Water layer [2,0]
                    map.setTileMaterial(pX, pY, terrainMat[3]);
                    map.setTileData(pX, pY, 1);
                }else if(value >= 0.1d && value < 0.6d){ // Grass layers
                    map.setTileMaterial(pX, pY, terrainMat[2]);
                    map.setTileData(pX, pY, 0);
                }else if(value >= 0.6d && value < 0.8d){ // Moutain layers
                    map.setTileMaterial(pX, pY, terrainMat[8]);
                    map.setTileData(pX, pY, 0);
                }else if(value >= 0.8d && value < 0.9d){ // Snow layers
                    map.setTileMaterial(pX, pY, terrainMat[4]);
                    map.setTileData(pX, pY, 0);
                }else if(value >= 0.9d ){ // Snow layers
                    map.setTileMaterial(pX, pY, terrainMat[7]);
                    map.setTileData(pX, pY, 0);
                }
            }
        }
    }
    
    public int getArchMatIndex(int ix, int iy){
        return ix + (iy * 16);
    }
    
    public void genArchitecture_Dev(){
        for(int px = 0; px < mapSize; px++){
            for(int py = 0; py < mapSize; py++){
                if( (px == 0)){
                    map.setArchTileMaterial(px, py, archMat[19]);
                    map.setTileData(px, py, 2);
                }else if(px == 63){
                    map.setArchTileMaterial(px, py, archMat[19]);
                    map.setTileData(px, py, 2);
                }else if(py == 0){
                    map.setArchTileMaterial(px, py, archMat[4]);
                    map.setTileData(px, py, 2);
                }else if(py == 63){
                    map.setArchTileMaterial(px, py, archMat[4]);
                    map.setTileData(px, py, 2);
                }else{
                    map.setArchTileMaterial(px, py, archMat[20]);
                    map.setTileData(px, py, 0);
                }
                
                if(px == 0 && py == 0){
                    map.setArchTileMaterial(px, py, archMat[8]);
                    map.setTileData(px, py, 2);
                }if(px == 0 && py == 63){
                    map.setArchTileMaterial(px, py, archMat[9]);
                    map.setTileData(px, py, 2);
                }if(px == 63 && py == 63){
                    map.setArchTileMaterial(px, py, archMat[16]);
                    map.setTileData(px, py, 2);
                }if(px == 63 && py == 0){
                    map.setArchTileMaterial(px, py, archMat[15]);
                    map.setTileData(px, py, 2);
                }
            }
        }
        
        //map.addArchTile(seedX, seedY, debugGeo);
    }
    
    public void genWall(){
        for(int py = 1; py < mapSize - 1; py++){
            map.addArchTile(0, py, geoPrefab.clone(),archMat[19]);
            map.setTileData(0, py, 2);
            map.addArchTile(mapSize-1, py, geoPrefab.clone(),archMat[19]);
            map.setTileData(mapSize-1, py, 2);
        }
        
        for(int px = 1; px < mapSize - 1; px++){
            //Top row of the bottom wall
            map.addArchTile( px, 3, geoPrefab.clone(),archMat[14]);
            //Wall sections below the top
            
            map.addArchTile( px, 2, geoPrefab.clone(),archMat[2]);
            map.addArchTile( px, 1, geoPrefab.clone(),archMat[2]);
            map.addArchTile( px, 0, geoPrefab.clone(),archMat[11]);
            
            map.setTileData(px, 3, 2);
            map.setTileData(px, 2, 2);
            map.setTileData(px, 1, 2);
            map.setTileData(px, 0, 2);
            
            //Top row of the top wall
            map.addArchTile( px,mapSize-1, geoPrefab.clone(),archMat[14]);
            
            map.addArchTile( px,mapSize - 2, geoPrefab.clone(),archMat[2]);
            map.addArchTile( px,mapSize - 3, geoPrefab.clone(),archMat[2]);
            map.addArchTile( px,mapSize - 4, geoPrefab.clone(),archMat[11]);
            
            map.setTileData(px, mapSize-1, 2);
            map.setTileData(px, mapSize - 2, 2);
            map.setTileData(px, mapSize - 3, 2);
            map.setTileData(px, mapSize - 4, 2);
        }
        
        map.addArchTile( 0, 0, geoPrefab.clone(),archMat[8]);
        map.addArchTile( 63, 0, geoPrefab.clone(),archMat[15]);
        map.addArchTile( 63, 63, geoPrefab.clone(),archMat[16]);
        map.addArchTile( 0, 63, geoPrefab.clone(),archMat[9]);
        
        map.setTileData(0, 0, 2);
        map.setTileData(63, 0, 2);
        map.setTileData(63, 63, 2);
        map.setTileData(0, 63, 2);
    }
    
    public void genArchitecture(){
        /** WRAP THIS IN AN INTERFACE. **/
        
        for(int px = 0; px < mapSize; px++){
            for(int py = 0; py < mapSize; py++){
                if( (px == 0)){
                    map.setArchTileMaterial(px, py, archMat[19]);
                    map.setTileData(px, py, 2);
                }else if(px == mapSize - 1){
                    map.setArchTileMaterial(px, py, archMat[19]);
                    map.setTileData(px, py, 2);
                }else if(py == 0){
                    map.setArchTileMaterial(px, py, archMat[4]);
                    map.setTileData(px, py, 2);
                }else if(py == mapSize - 1){
                    map.setArchTileMaterial(px, py, archMat[4]);
                    map.setTileData(px, py, 2);
                }else{
                    map.setArchTileMaterial(px, py, archMat[20]);
                    map.setTileData(px, py, 0);
                }
                
                if(px == 0 && py == 0){
                    map.setArchTileMaterial(px, py, archMat[8]);
                    map.setTileData(px, py, 2);
                }if(px == 0 && py == mapSize - 1){
                    map.setArchTileMaterial(px, py, archMat[9]);
                    map.setTileData(px, py, 2);
                }if(px == mapSize - 1 && py == mapSize - 1){
                    map.setArchTileMaterial(px, py, archMat[16]);
                    map.setTileData(px, py, 2);
                }if(px == mapSize - 1 && py == 0){
                    map.setArchTileMaterial(px, py, archMat[15]);
                    map.setTileData(px, py, 2);
                }
            }
        }
        
        
    }
    
    public void buildMap(int tileScreenSize){
        /** Create the TileMap. **/
        //int tileScreenSize = 720 /16;
        //int mapSize = 64;
        
        /** REFACTORED TILEMAP CONSTRUCTOR. **/
        /** Initialize our data. **/
        map.size = tileScreenSize;
        map.data = new int[mapSize][mapSize];
        map.tiles = new Quad[mapSize][mapSize];
        map.tileGeo = new Geometry[mapSize][mapSize];
        map.archGeo = new Geometry[mapSize][mapSize];
        map.mapNode = new Node("TileMap_root");
        map.isPaged = false;

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
                map.tileGeo[dx][dy].setLocalTranslation(dx * map.size, dy * map.size, -0.5f);    // Z = -0.5 to keep archmap behind the player
                
                /** Second geometry layer for architecture. **/
                map.archGeo[dx][dy] = new Geometry("Arch Tile: " + dx + ", " + dy, new Quad(map.size, map.size));
                map.archGeo[dx][dy].setLocalTranslation(dx * map.size, dy * map.size, 0.25f);    // Z = -0.25 to keep archmap behind the player and in front of landscape tiles
                
                /** Materials are currently managed outside of this class. **/
                /*if(terrainMat != null){
                    map.tileGeo[dx][dy].setMaterial(terrainMat[2]);
                }*/
                /** For our brute-force method, every geometry is added to the mapNode. **/
                map.mapNode.attachChild(map.tileGeo[dx][dy]);
                map.mapNode.attachChild(map.archGeo[dx][dy]);
            }
        }

        map.getNode().setLocalTranslation(0,0,-0.5f);

        //sysLog("Gen map");
        genTerrain();
        //genArchitecture();
        genWall();
    }
    
    /** Entry for WorldAppState. **/
    public void buildPagedMap(int tileScreenSize){
        /** Create the TileMap. **/
        map.size = tileScreenSize;
        map.mapNode = new Node("TileMap_root");
        map.isPaged = true;

        buildPages(8,16);
        if(debugGeo != null){
            debug = true;
            debugGeo.setMaterial(debugMat[0]);
            map.getNode().attachChild(debugGeo);
        }
        map.getNode().setLocalTranslation(0,0,-0.5f);
        System.out.println("Player-map offset: " + new Vector2f(playerX - mapX, playerY - mapY));

        //NEED PAGE VARIANTS
        genTerrain();
        //genArchitecture();
        genWall();
    }
    
    public void buildPages(int pageDim, int pageSize){
        //map.data = new int[pageDim][pageDim];
        map.maxPages = pageDim;
        mapSize = map.maxPages * pageSize;
        map.pageSize = pageSize;
        //map.mapNode = new Node("TileMap_root");
        map.pages = new ArrayList<>();
        Quad tilePrefab = new Quad(map.size, map.size);
        geoPrefab = new Geometry("Prefab",tilePrefab);  // This Geometry will be copied for arch layer
        
        /** For now, let's assume we need every page chunk. **/
        for(int bx = 0; bx < pageDim; bx++){
            for(int by =0; by < pageDim; by++){
                // Create new pages and add to the list
                TileMapPage newPage = new TileMapPage();
                newPage.pageX = bx;
                newPage.pageY = by;
                newPage.state = 0;
                /** REFACTORED TILEMAPPAGE CONSTRUCTOR. **/
                /** Initialize our data. **/
                
                newPage.data = new int[pageSize][pageSize];
                newPage.tileGeo = new Geometry[pageSize][pageSize];
                newPage.archGeo = new Geometry[pageSize][pageSize];
                newPage.pageNode = new Node("TileMap_root");
                                
                /** Build the Map Page. **/
                //      This is a brute-force approach (individual Geometries for each tile
                //  instead of a single effecient Geometry with custom Mesh) and serves 
                //  as a personal proof-of-concept
                //      In the future, map entire double for-loop will be refactored
                for(int dx = 0; dx < pageSize; dx++){
                    for(int dy = 0; dy < pageSize; dy++){
                        /** TileMap data. **/
                        newPage.data[dx][dy] = 0;

                        /** Geometry. **/
                        newPage.tileGeo[dx][dy] = new Geometry("Tile: " + dx + ", " + dy, tilePrefab);
                        newPage.tileGeo[dx][dy].setLocalTranslation(dx * map.size, dy * map.size, -0.5f);    // Z = -0.5 to keep archmap behind the player

                        /** Second geometry layer for architecture. **/
                        newPage.archGeo[dx][dy] = new Geometry("Page "+bx+", "+by+" Arch Tile: " + dx + ", " + dy, tilePrefab);
                        newPage.archGeo[dx][dy].setLocalTranslation(dx * map.size, dy * map.size, 0.25f);    // Z = -0.25 to keep archmap behind the player and in front of landscape tiles
                        
                        /** For our brute-force method, every geometry is added to the mapNode. **/
                        newPage.pageNode.attachChild(newPage.tileGeo[dx][dy]);
                        newPage.pageNode.attachChild(newPage.archGeo[dx][dy]);
                    }
                }
                newPage.pageNode.setLocalTranslation(newPage.pageX * (map.size * 16), newPage.pageY * (map.size * 16),0);
                map.pages.add(newPage); // index = bx + (maxActivePages * by)
                // New pages still need to be added to the scene graph
                /** Dont add the page if we are paging. **/
                //map.mapNode.attachChild(newPage.getNode());
            }
        }
    }
    
    public void moveMap(float mx, float my){
        // the movement vector is in raw tile measurements
        this.moveX = mx;
        this.moveY = my;
    }
    
    public Vector2f getWorldPosition(){
        Vector3f localTranslation = map.mapNode.getLocalTranslation();
        
        return new Vector2f(localTranslation.x, localTranslation.y);
    }
    
    public Vector2f getMapCoords(){return new Vector2f(mapX,mapY);}
    
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls TileMap movement,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        
        
        // Reset movement to ZERO after moving map 
        
        System.out.println("X:" + playerX/map.size + " Y:" + playerY/map.size + "pX:" + playerX + " pY:" + playerY);
        float movementX = moveX * map.size;
        float movementY = moveY * map.size;
        
        /** Rudimentary player offset tracking for page loading. **/
        playerX -= movementX;
        playerY -= movementY;
        mapX = playerX/map.size;
        mapY = playerY/map.size;
        
        if(debug == true){
            
            debugGeo.setLocalTranslation(FastMath.floor(mapX)* map.size, FastMath.floor(mapY)* map.size, 0.25f);
            System.out.println("TileData: " + map.getTileData((int)mapX, (int)mapY));
        }
        
        // This control will also need to check if any TileMapPages need to be added or removed
        
        map.checkPages((int)FastMath.floor(mapX), (int)FastMath.floor(mapY), debugMat);
        map.getNode().move(movementX, movementY,0);
        //updatePages();
        
    }
    
    protected void updatePages(){
        //System.out.println(map.pages.size());
        for(int p = 0; p < map.pages.size(); p++){
            //System.out.println("Center page: " + p + ", state: " + map.pages.get(p).state + ", checked/changed: " + map.pages.get(p).checked + "/" + map.pages.get(p).stateChange);
            //if(map.pages.get(p).stateChanged() == true){
                /** If the state of the page has changed. **/
                if(map.pages.get(p).wasChecked() == true){
                    /** These pages were checked by the sweep, add if not active. **/
                    if(map.pages.get(p).getState() == 0){
                        /** New page. **/
                        map.addPage(p);
                        //map.pages.get(p).debugGeo.setMaterial(debugMat[2]);
                    }
                    map.pages.get(p).checked = false;
                }else{
                    /** These pages were not checked, remove if still active. **/
                    if(map.pages.get(p).getState() == 1){
                        map.removePage(p);
                        //map.pages.get(p).debugGeo.setMaterial(debugMat[1]);
                    }
                }
                //map.pages.get(p).checked = false;
                map.pages.get(p).stateChange = false;
            //}
        }
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
