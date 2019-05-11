/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cartography;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TileMapManager{
    public static String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    public static String mapDirectoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\MapData\\";
    public static String fileName = "test";
    public static String extension = ".txt";
    
    private TextureAtlas atlas;
    private Texture[] textureList;
    private String[] terrainTextureList = {
            "Textures/Tiles/Landscape/Grass/Grass_1.png",   // 0
            "Textures/Tiles/Landscape/Grass/Grass_2.png",
            "Textures/Tiles/Landscape/Grass/Grass_3.png",
            "Textures/Tiles/Landscape/Grass/Grass_4.png",

            "Textures/Tiles/Landscape/Stone/Stone_1.png",   // 4
            "Textures/Tiles/Landscape/Stone/Stone_2.png",
            "Textures/Tiles/Landscape/Stone/Stone_3.png",
            "Textures/Tiles/Landscape/Stone/Stone_4.png",

            "Textures/Tiles/Landscape/Dirt/Dirt_1.png",     // 8
            "Textures/Tiles/Landscape/Dirt/Dirt_2.png",
            "Textures/Tiles/Landscape/Dirt/Dirt_3.png",
            "Textures/Tiles/Landscape/Dirt/Dirt_4.png",

            "Textures/Tiles/Landscape/Water/Water_1.png",   // 12
            "Textures/Tiles/Landscape/Water/Water_2.png",
            "Textures/Tiles/Landscape/Water/Water_3.png"
        };
    
    private Material material;
    
    private ArrayList<MapPage> pages;
    
    public TileMapManager(AssetManager assetManager){
        pages = new ArrayList<>();
        atlas = new TextureAtlas(512, 512);
        textureList = new Texture[terrainTextureList.length];
        
        for(int i=0; i<terrainTextureList.length; i++){
            this.textureList[i] = assetManager.loadTexture(this.terrainTextureList[i]);
            this.atlas.addTexture(textureList[i], "ColorMap");
        }
        
        this.material = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        this.material.setTexture("ColorMap",this.atlas.getAtlasTexture("ColorMap"));
    }

    public TileMapManager(TextureAtlas atlas, Texture[] textureSet, Material mat){
        pages = new ArrayList<>();
        this.atlas = atlas;
        this.textureList = textureSet;
        this.material = mat;
    }

    // Need a method to generate terrain
    public void generateMap(MapGenerator generator, TextureAtlas atlas){

        //Load the map data file - especially when loading saved game

        int fieldSize = generator.getFieldSize();
        for(int x=0; x < fieldSize; x++){
            for(int y=0; y<fieldSize; y++){


                readMapPage(x,y);
            }
        }

    }

    public void readMapPage(int x, int y){
        try {
            //File file = new File(mapDirectoryName + "page."+x+"."+y+".txt");

            //augemented utf encoding
            InputStreamReader reader = new InputStreamReader(new FileInputStream(mapDirectoryName + "page."+x+"."+y+".txt"), "UTF-8");

            BufferedReader br = new BufferedReader(reader);
            //reader
            //new FileReader(file)
            String st = br.readLine();
            
            boolean valid = false;
            if(st.equals("MapGenPage")){
                String pageID = br.readLine();
                valid = true;

                MapPage newPage = new MapPage(x,y);
                
                while ((st = br.readLine()) != null && valid == true){
                    System.out.println(": " + st); //This is the data
                    String[] data = st.split(":");
                    String[] location = data[0].split(" ");

                    int tx = Integer.parseInt(location[0]);
                    int ty = Integer.parseInt(location[1]);
                    int value = Integer.parseInt(data[1]);
                    newPage.setTile(tx, ty, value);
                }
                
                newPage.buildMap(this.atlas);
                this.pages.add(newPage);
            }else{
                System.out.println("Invalid Format");
                //break;
            }
                                

            br.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(TileMapManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Need a method to populate MapPage ArrayList
    // Need a method to remove map pages too far from the player
    // Need a method to add map pages close enough to the player

    //  The MapPage holds a page of Tiles
    public class MapPage{
        private int pageX,pageY;
        private int pageState;
        private Geometry[][] tiles;
        private int[][] tileData;
        private double[][] tileValue;
        private Node node;
        private String fileReference;

        public MapPage(int x, int y){
            tiles = new Geometry[16][16];
            tileData = new int[16][16];
            node = new Node("Page");
            node.setLocalTranslation(x*10,y*10,-10);
            pageX = x;
            pageY = y;
            pageState = -1;
        }
        
        // Method to change state
        public void setState(String state){
            switch(state){
                case "Load":
                    break;
                case "Generate":
                    break;
                default:
            }
        }
        // Need a method to read a MapPage file for tile construction
        private void loadMap(String fileName){
            InputStreamReader reader = null;
            try {
                // We need to provide file path as the parameter:
                // double backquote is to avoid compiler interpret words
                // like \test as \t (ie. as a escape sequence)
                File file = new File(fileName);
                //augemented utf encoding
                //reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
                reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader);
                
                String st;
                if(br.readLine().equals("ManGenPage") == true){
                    String pageID = br.readLine();

                    while ((st = br.readLine()) != null){
                        String[] split = st.split(":");
                    }

                }


                br.close();
                //reader.close();

            } catch (IOException ex) {
                Logger.getLogger(TileMapManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Need a method to build page
        public void buildMap(TextureAtlas atlas){
            for(int x=0; x<tileData[0].length; x++){
                for(int y=0; y<tileData[0].length; y++){
                    Quad quad = new Quad(1,1);

                    tiles[x][y] = new Geometry("Tile", quad);
                    tiles[x][y].setLocalTranslation(x*10, y*10, 0);
                    tiles[x][y].setMaterial(material);

                    FloatBuffer buf = quad.getFloatBuffer(Type.TexCoord);
                    atlas.getAtlasTile(textureList[0]).transformTextureCoords(buf, 0, buf);

                    this.node.attachChild(tiles[x][y]);
                }
            }
        }
        // Need a method to setTile
        public void setTile(int x, int y, int index){
            tileData[x][y] = index;

        }
        public void setTileValue(int x, int y, double value){
            //
        }
        // Method to add a tile to the page during construction
        private void addTile(int x, int y, int index) {
            Quad quad = new Quad(10, 10);

            tiles[x][y] = new Geometry("Tile", quad);
            tiles[x][y].setLocalTranslation(x * 10, y * 10, 0);
            tiles[x][y].setMaterial(material);

            FloatBuffer buf = quad.getFloatBuffer(Type.TexCoord);
            atlas.getAtlasTile(textureList[index]).transformTextureCoords(buf, 0, buf);

            this.node.attachChild(tiles[x][y]);
        }
        // Need a method to get tile collision data

    }
}
