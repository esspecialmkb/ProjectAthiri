/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cartography;

import Utility.SimplexNoise;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Random;

/** TileMap prototype.
 *
 * @author esspe
 */
public class TileMap{
        /** Data for Tile pages are being refactored. **/
        public int size;           //  Keep track of tile size (on screen)
        /** data == 0 -> walkable, == 1 -> Blocking. **/
        public int[][] data;       //  This tells the build method which tiles to use
        public Quad[][] tiles;     //  Meshes used for tiles
        public Node mapNode;       //  Serves as the 'rootNode' for the tile map
        public Geometry[][] tileGeo;// Geometries attach the Quads to the scene graph, making them renderable
        /** Adding a second tile layer for architecture tiles. **/
        public Geometry[][] archGeo;
        public boolean isPaged;
        
        /** External material references. **/
        public Material[][] i_tileMatList;
        
        /** We're going to use a single chunk of tiles for now, but let's track position... **/
        public int pageX = 0;
        public int pageY = 0;
        public float offsetX = 0.0f, playerX = 0.0f, playerOffsetX;   // Track the view offset
        public float offsetY = 0.0f, playerY = 0.0f, playerOffsetY;   // The offsets let us move the tiles relative to the viewport
        
        /** NEW DATA MEMBERS FOR TILEPAGES. **/
        public ArrayList<TileMapPage> pages;
        public int maxPages;
        public int pageSize;
        
        /** SERIALIZATION - MANUAL SETUP. **/
        public TileMap(){}
        
        /**With this class being a prototype, let's build everything in the constructor. **/
        public TileMap(int tileSize){
            /** Initialize our data. **/
            this.size = tileSize;
            this.data = new int[16][16];
            this.tiles = new Quad[16][16];
            this.tileGeo = new Geometry[16][16];
            this.mapNode = new Node("TileMap_root");
            
            /** Build the map. **/
            //      This is a brute-force approach (individual Geometries for each tile
            //  instead of a single effecient Geometry with custom Mesh) and serves 
            //  as a personal proof-of-concept
            //      In the future, this entire double for-loop will be refactored
            for(int dx = 0; dx < 16; dx++){
                for(int dy = 0; dy < 16; dy++){
                    /** TileMap data. **/
                    this.data[dx][dy] = 0;
                    
                    /** Quads. **/
                    this.tiles[dx][dy] = new Quad(this.size, this.size);
                    
                    /** Geometry. **/
                    this.tileGeo[dx][dy] = new Geometry("Tile: " + dx + ", " + dy, this.tiles[dx][dy]);
                    this.tileGeo[dx][dy].setLocalTranslation(dx * this.size, dy * this.size, -0.5f);    // Z = -0.5 to keep tilemap behind the player
                                        
                    /** For our brute-force method, every geometry is added to the mapNode. **/
                    this.mapNode.attachChild(this.tileGeo[dx][dy]);
                }
            }
        }
        
        /** Need a constructor for using TilePages. **/
        public TileMap(int tileSize, int maxActivePages){
            /** Some of this implementation is borrowed from the single-page constructor. **/
            this.size = tileSize;
            this.data = new int[maxActivePages][maxActivePages];
            //this.tiles = new Quad[16][16];
            //this.tileGeo = new Geometry[16][16];
            this.maxPages = maxActivePages;
            this.mapNode = new Node("TileMap_root");
            this.pages = new ArrayList<>();
            
            /** For now, let's assume we need every page chunk. **/
            for(int bx = 0; bx < maxActivePages; bx++){
                for(int by =0; by < maxActivePages; by++){
                    // Create new pages and add to the list
                    TileMapPage newPage = new TileMapPage(tileSize, bx, by);
                    pages.add(newPage); // index = bx + (maxActivePages * by)
                    // New pages still need to be added to the scene graph
                    this.mapNode.attachChild(newPage.getNode());
                }
            }
        }
        
        /** Utility constructor for using TilePages in AppStates. **/
        public TileMap(int tileSize, int maxActivePages, Material[][] tileMatList){
            /** We need to maintain an external material reference for now. **/
            this.i_tileMatList = tileMatList;
            /** Some of this implementation is borrowed from the single-page constructor. **/
            this.size = tileSize;
            this.data = new int[maxActivePages][maxActivePages];
            //this.tiles = new Quad[16][16];
            //this.tileGeo = new Geometry[16][16];
            this.maxPages = maxActivePages;
            this.mapNode = new Node("TileMap_root");
            this.pages = new ArrayList<>();
            
            /** For now, let's assume we need every page chunk. **/
            for(int bx = 0; bx < maxActivePages; bx++){
                for(int by =0; by < maxActivePages; by++){
                    // Create new pages and add to the list
                    TileMapPage newPage = new TileMapPage(tileSize, bx, by, tileMatList);
                    pages.add(newPage); // index = bx + (maxActivePages * by)
                    // New pages still need to be added to the scene graph
                    this.mapNode.attachChild(newPage.getNode());
                }
            }
        }
        
        public boolean checkRange(int x, int y){
            int index = (x * maxPages) + y;
            return (index > 0 && index < (maxPages * maxPages));
        }
        public int getPageIndex(int x, int y){ return (x * maxPages) + y;}
        
        public void checkPages(int x, int y, Material[] matsel){
            
            int px = x / (pageSize);
            int py = y / (pageSize);
            int index = (px * maxPages) + py;
            //System.out.println("checkPages: " + x + ", " +  y + ". Page: " + px + ", " +  py + ", index: " +  index);
            
            
            
            if(checkRange(px,py) == true){
                /** This is the page that the player is standing on. **/
                //System.out.println("Center page: " + index + ", calc: " + getPageIndex(px,py));
                //addPage(index);
                //pages.get(getPageIndex(px,py)).setState(1);
            }
            
            int countActive = 0;
            int countInactive = 0;
            for(int p = 0; p < pages.size();p++){
                int dx = px - pages.get(p).pageX;
                int dy = py - pages.get(p).pageY;
                if(pages.get(p).state == 0){
                    countInactive++;
                    if(Math.abs(dx) < 2 || Math.abs(dy) < 2){
                        addPage(p);
                    }
                }if(pages.get(p).state == 1){
                    countActive++;
                    if(Math.abs(dx) > 3 || Math.abs(dy) > 3){
                        removePage(p);
                    }
                }
            }
            //System.out.println("Done checkPages: " + countActive + " active. " + countInactive + " inactive.");
            pageX = px;
            pageY = py;
        }
        
        public void removePage(int index){
            if(pages.get(index).getState() != 0){
                mapNode.detachChild(pages.get(index).pageNode);
                pages.get(index).setState(0);
            }
        }
        
        public void addPage(int index){
            if(pages.get(index).getState() == 0){
                mapNode.attachChild(pages.get(index).pageNode);
                pages.get(index).setState(1);
            }
        }
        
        /** Add mob relative to map. **/
        public void buildMob(float x, float y){
            // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
            /*mobQuad = new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize);
            mobGeo = new Geometry("Player", mobQuad);
            
            /** Set the proper Material to the Geometry and attach to guiNode. /**
            mobGeo.setMaterial(mobMatList[0][0]);
            // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen
            // v3 Addition, set the player position members
            mobWorldX = (x * size) - (((mobWidth / 16) * tileScreenSize) / 2);
            mobWorldY = (y * size);
            mobGeo.setLocalTranslation(mobWorldX, mobWorldY, 0);
            mapNode.attachChild(mobGeo);*/
        }
        
        /** This method does not create the map, only sets the tiles. **/
        public void genOverworld(){
            /** RANDOM OVERWORLD GEN WHOO!!!. **/
            
            int seedX = (int) Math.floor(random() * 1000);
            int seedY = (int) Math.floor(random() * 1000);
            
            // I need to be mindful of memory usage
            // Let's keep the world 'kinda' small 4x4 tile page chunks
            
            double[][] noise = new double[64][64];
            
            double min = 0;
            double max = 0;
            double range = 0;
            double delta = 0;
            
            float freq = 0.5f;
            float oct = 0.5f;
            float div = 24;
            float exp = 1.2f;
            
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
            //System.out.println("Page Count [" + pages.size() + "]");
            
            delta = 0 - min;
            //System.out.println("Delta [" + delta + "]");
            range += delta;
            range = max - min;
            //System.out.println("Range [" + range + "] = max [" + max + "] - min [" + min + "]");
            
            for(int pX = 0; pX < maxPages; pX++){
                for(int pY = 0; pY < maxPages; pY++){
                    TileMapPage genPage = pages.get((pX * maxPages) + pY);
                    for(int tX = 0; tX < 16; tX++){
                        for(int tY = 0; tY < 16; tY++){
                            int wX = (pX * 16) + tX;
                            int wY = (pY * 16) + tY;
                            //genPage.tileGeo[tX][tY].setMaterial(tileMatList[mX][mY]);
                            float value = (float) (((float)noise[wX][wY] + delta)/range);
                            /** Parse the value according to height ranges. **/
                            if(value < 0.1d){ // Water layer [2,0]
                                //this.setTile(pX,pY,2,0);
                                genPage.tileGeo[tX][tY].setMaterial(i_tileMatList[2][0]);
                            }else if(value >= 0.1d && value < 0.6d){ // Grass layers
                                //this.setTile(pX,pY,0,0);
                                genPage.tileGeo[tX][tY].setMaterial(i_tileMatList[0][0]);
                            }else if(value >= 0.7d && value < 0.8d){ // Moutain layers
                                //this.setTile(pX,pY,3,1);
                                genPage.tileGeo[tX][tY].setMaterial(i_tileMatList[3][1]);
                            }else if(value >= 0.8d && value < 0.9d){ // Snow layers
                                //this.setTile(pX,pY,1,1);
                                genPage.tileGeo[tX][tY].setMaterial(i_tileMatList[4][3]);
                            }else if(value >= 0.9d ){ // Snow layers
                                //this.setTile(pX,pY,1,1);
                                genPage.tileGeo[tX][tY].setMaterial(i_tileMatList[1][1]);
                            }
                        }
                    }
                }
            }
        }
        
        public void setTile(int x, int y, int mX, int mY){
            int pX = (int) Math.floor(x / 16);
            int pY = (int) Math.floor(y / 16);
            int dX = x % 16;
            int dY = y % 16;
            //System.out.println("Set Tile " + pX + "," + pY + ":" + dX + "," + dY);
            if(dX <= 15 && dY <= 15){
                //pages.get((pX * 16) + pY).tileGeo[dX][dY].setMaterial(i_tileMatList[mX][mY]);
                pages.get((pX * 16) + pY).setTile(x, y, mX, mY);
            }else{
                System.out.println("ARRAY INDEX ERROR!!!!!");
                System.out.println("______________________");
                System.out.println("origin point: "+ x + ","+y + "| page:"+ pX + "," + pY + "| index:" + dX + "," + dY);
            }
        }
        public int getTileData(int x, int y){
            if(isPaged){
                int px = x / pageSize;
                int tx = x % pageSize;
                int py = y / pageSize;
                int ty = y % pageSize;
                if(px > -1 && px < maxPages * pageSize){
                    if(py > -1 && py < maxPages * pageSize){
                        if(tx > -1 && tx < 16 && ty > -1 && ty < 16){
                           return this.pages.get((px * maxPages) + py).data[tx][ty]; 
                        }
                    }
                }
                return -1;
            }else{
                return this.data[x][y];
            }
        }
        
        /** TileMap shouldn't be concerned with Materials. **/
        public void setTileMaterial(int x, int y, Material mat){ 
            if(isPaged){
                int px = x / pageSize;
                int tx = x % pageSize;
                int py = y / pageSize;
                int ty = y % pageSize;
                
                int index = (px * maxPages) + py;
                //System.out.println("x:" + x + ", y:" + y + ", px:" + px + ", py:" + py + ", tx:" + tx + ", ty:" + ty + ", (px * pageSize) + py) = " + index);
                this.pages.get((px * maxPages) + py).tileGeo[tx][ty].setMaterial(mat);
            }else{
                this.tileGeo[x][y].setMaterial(mat);
            }
        }
        public void setArchTileMaterial(int x, int y, Material mat){ 
            /** We're going to refactor this to add geometry instead of using blank/clear texture. **/
            if(isPaged){
                int px = x / pageSize;
                int tx = x % pageSize;
                int py = y / pageSize;
                int ty = y % pageSize;
                
                int index = (px * maxPages) + py;
                //System.out.println("x:" + x + ", y:" + y + ", px:" + px + ", py:" + py + ", tx:" + tx + ", ty:" + ty + ", (px * pageSize) + py) = " + index);
                this.pages.get((px * maxPages) + py).archGeo[tx][ty].setMaterial(mat);
                
                //this.pages.get((px * maxPages) + py).getNode();
            }else{
                this.tileGeo[x][y].setMaterial(mat);
            }
        }
        public void setTileData(int x, int y, int data){
            if(isPaged){
                int px = x / pageSize;
                int tx = x % pageSize;
                int py = y / pageSize;
                int ty = y % pageSize;
                
                this.pages.get((px * maxPages) + py).data[tx][ty] = data;
            }else{
                this.data[x][y] = data;
            }
        }
        public void addArchTile(int x, int y, Geometry newTile, Material mat){
            // Take the prefab geometry and add to the correct tile page
            newTile.setMaterial(mat);
            if(isPaged){
                int px = x / pageSize;
                int tx = x % pageSize;
                int py = y / pageSize;
                int ty = y % pageSize;
                
                this.pages.get((px * maxPages) + py).getNode().attachChild(newTile);
            }else{
                this.mapNode.attachChild(newTile);
            }
        }
        
        /** Create a list of normally distributed random (or pseudo-random) numbers 
         * with a mean of   1.0   and a   standard deviation   of   0.5. **/
        public void genStdDevStub(){
            double[] list = new double[1000];
            double mean = 1.0, std = 0.5;
            Random rng = new Random();
            for(int i = 0;i<list.length;i++) {
              list[i] = mean + std * rng.nextGaussian();
            }
        }
        
        public double randNormDist(double mean, double std){
            Random rng = new Random();
            
            return mean + std * rng.nextGaussian();
        }
        
        public Node getNode(){return this.mapNode;}

    } // END OF CLASS PROTOTYPE
