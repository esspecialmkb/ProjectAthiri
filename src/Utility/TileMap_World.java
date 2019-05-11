/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Cartography.*;
import Utility.SimplexNoise;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Random;

/** TileMap_World prototype.
 *  I need to find a way to break the Material dependancy between TileMap class and it's host app state
 *
 * @author esspe
 */
public class TileMap_World{
        /** Data for Tile pages are being refactored. **/
        private int size;           //  Keep track of tile size (on screen)
        private int[][] data;       //  This  should tell the build method which tiles to use
        private Quad[][] tiles;     //  Meshes used for tiles
        private Node mapNode;       //  Serves as the 'rootNode' for the tile map
        private Geometry[][] tileGeo;// Geometries attach the Quads to the scene graph globally, making them renderable
                
        /** We're going to use a single chunk of tiles for now, but let's track position... **/
        public int pageX = 0;
        public int pageY = 0;
        public float offsetX = 0.0f;   // Track the view offset
        public float offsetY = 0.0f;   // The offsets let us move the tiles relative to the viewport
        
        /** NEW DATA MEMBERS FOR TILEPAGES. **/
        private ArrayList<TileMapPage> pages;
        private int maxPages;
        
        public TileMap_World(){}
        
        /**With this class being a prototype, let's build everything in the constructor. **/
        public TileMap_World(int tileSize){
            /** Initialize our data. **/
            this.size = tileSize;
            this.data = new int[16][16];
            this.tiles = new Quad[16][16];
            this.tileGeo = new Geometry[16][16];
            this.mapNode = new Node("TileMap_root");
            
            /** Build the map. **/
            //      This class should only worry about the geometries
            //  An external control will deal with materials
            for(int dx = 0; dx < 16; dx++){
                for(int dy = 0; dy < 16; dy++){
                    /** TileMap data. **/
                    this.data[dx][dy] = 0;
                    
                    /** Quads. **/
                    this.tiles[dx][dy] = new Quad(this.size, this.size);
                    
                    /** Geometry. **/
                    this.tileGeo[dx][dy] = new Geometry("Tile: " + dx + ", " + dy, this.tiles[dx][dy]);
                    this.tileGeo[dx][dy].setLocalTranslation(dx * this.size, dy * this.size, -0.5f);    // Z = -0.5 to keep tilemap behind the player
                    //  For now, we're using a global material
                    //  When this class is refactored, we will need a reference to the available Materials
                    //this.tileGeo[dx][dy].setMaterial(i_tileMatList[1][14]);
                    
                    /** For our brute-force method, every geometry is added to the mapNode. **/
                    //this.mapNode.attachChild(this.tileGeo[dx][dy]);
                }
            }
        }
        
        /** Need a constructor for using TilePages. **/
        public TileMap_World(int tileSize, int maxActivePages){
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
        
        
        public void setTileData(int x, int y, int data){ this.data[x][y] = data;}
        public void setTileMaterial(int x, int y, Material mat){
            int pX = (int) Math.floor(x / 16);
            int pY = (int) Math.floor(y / 16);
            int dX = x % 16;
            int dY = y % 16;
            //System.out.println("Set Tile " + pX + "," + pY + ":" + dX + "," + dY);
            if(dX <= 15 && dY <= 15){
                pages.get((pX * 16) + pY).tileGeo[dX][dY].setMaterial(mat);
                //pages.get((pX * 16) + pY).setTile(x, y, mX, mY);
            }else{
                System.out.println("ARRAY INDEX ERROR!!!!!");
                System.out.println("______________________");
                System.out.println("origin point: "+ x + ","+y + "| page:"+ pX + "," + pY + "| index:" + dX + "," + dY);
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
        
        
        
        public Node getNode(){return this.mapNode;}
    } // END OF CLASS PROTOTYPE
