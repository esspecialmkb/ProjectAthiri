/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cartography;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/** TileMap prototype.
 * All private data members made public to use this class as a 'data-container'
 *
 * @author Michael A. Bradford
 */
public class TileMapPage{
    /** TileMapPage is just a junior TileMap. **/
    /** Data copied from tile maps. **/
    public int size;           //  Might be more effiecent to make the page reference a global size
    public int[][] data;       //  This tells the build method which tiles to use
    public Quad[][] tiles;     //  Meshes used for tiles
    public Node pageNode;       //  Serves as the 'rootNode' for the tile map
    public Geometry[][] tileGeo;// Geometries attach the Quads to the scene graph, making them renderable
    
    /** External material references. **/
    public Material[][] i_tileMatList;

    /** Prototype, still using globaly defined Materials. **/
    //Material[][] tileMatIndex;

    /** We're going to start using TileMapPages, like chunks, each needs to track  it's own position... **/
    public int pageX = 0;
    public int pageY = 0;
    public float offsetX = 0.0f;   //  Track the view offset
    public float offsetY = 0.0f;   // The offsets let us move the tiles relative to the viewport
    
    /** SERIALIZATION. **/
    public TileMapPage(){}

    /** Constructor to assemble the tile page. **/
    public TileMapPage(int tileSize, int x, int y){
        /** Initialize our data. Same as TileMap prototype constructor **/
        this.size = tileSize;
        this.data = new int[16][16];
        this.tiles = new Quad[16][16];
        this.tileGeo = new Geometry[16][16];
        this.pageNode = new Node("TileMap_root");

        /** Build the map-page. **/
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
                //  For now, we're using a global material
                //  When this class is refactored, we will need a reference to the available Materials
                this.tileGeo[dx][dy].setMaterial(i_tileMatList[3][2]);

                /** For our brute-force method, every geometry is added to the mapNode. **/
                this.pageNode.attachChild(this.tileGeo[dx][dy]);
            }
        }
        /** After creation, set the global position index. **/
        this.pageX = x;
        this.pageY = y;

        this.pageNode.setLocalTranslation(x * (tileSize * 16),y * (tileSize * 16),0);

        /** Kepp the local page offset updated. **/
        offsetX = x * (tileSize * 16);
        offsetY = y * (tileSize * 16); 
    }
    
    /** Utility Constructor to assemble the tile page from app state. **/
    public TileMapPage(int tileSize, int x, int y, Material[][] tileMatList){
        this.i_tileMatList = tileMatList;
        /** Initialize our data. Same as TileMap prototype constructor **/
        this.size = tileSize;
        this.data = new int[16][16];
        this.tiles = new Quad[16][16];
        this.tileGeo = new Geometry[16][16];
        this.pageNode = new Node("TileMap_root");

        /** Build the map-page. **/
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
                //  For now, we're using a global material
                //  When this class is refactored, we will need a reference to the available Materials
                this.tileGeo[dx][dy].setMaterial(tileMatList[3][2]);

                /** For our brute-force method, every geometry is added to the mapNode. **/
                this.pageNode.attachChild(this.tileGeo[dx][dy]);
            }
        }
        /** After creation, set the global position index. **/
        this.pageX = x;
        this.pageY = y;

        this.pageNode.setLocalTranslation(x * (tileSize * 16),y * (tileSize * 16),0);

        /** Kepp the local page offset updated. **/
        offsetX = x * (tileSize * 16);
        offsetY = y * (tileSize * 16); 
    }
    
    public void setTile(int x, int y, int mX, int mY){
        this.data[x][y] = (mX * 16) + mY;
        this.tileGeo[x][y].setMaterial(i_tileMatList[3][2]);
    }
    
    public void setTileMaterial(int x, int y, Material mat){ this.tileGeo[x][y].setMaterial(mat);}

    public Node getNode(){
        return this.pageNode;
    }
}