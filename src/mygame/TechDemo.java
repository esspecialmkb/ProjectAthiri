package mygame;

import Utility.SimplexNoise;
import com.jme3.app.BasicApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Random;

/**
 * Fifth revision of BasicDemo.java. 
 * The player needs a weapon and an enemy to fight, cue the ZOMBIES!!!
 * @author Michael B.
 */
public class TechDemo extends BasicApplication {
    //Version Info
    static private int version_maj = 0;
    static private int version_min = 0;
    static private int version_revision = 0;
    static String version_fork = "Tech Demo";
    
    /** Textures for tiles and player. **/
    private Texture tileTexture;
    private Texture playerTexture, mobTexture;  // New mob Texture data
    /** Grab the image data from the textures. **/
    private Image tileImage;
    private Image playerImage, mobImage;        // New Mob Image data
    /** Having the image data allows us to split the image into separate textures. **/
    private Texture2D[][] tileTextureList;
    private Image[][] tileImageList;
    private Material[][] tileMatList;
    private ImageRaster tileRaster;
    /** Data definitions for tile images. **/
    private int imgWidth;
    private int imgHeight;
    private int depth;
    /** Setup player sprite data. New Mob stuff. **/
    private Texture2D[][] playerTextureList, mobTextureList;
    private Image[][] playerImageList, mobImageList;
    private Material[][] playerMatList, mobMatList;
    private ImageRaster playerRaster, mobRaster;
    
    /** Data defs for player sprites, uses same depth as tiles * 2. **/
    private int playerWidth, mobWidth;
    private int playerHeight, mobHeight;
    
    /** NEW STUFF. **/
    //Map member needed for movement updates
    private TileMap map;
    //Player position members
    private float player_Scrn_X;
    private float player_Scrn_Y;
    private float playerWorldX; // Local coords
    private float playerWorldY;
    //Player movement members
    private float walkSpeed = 3.5f; // Faster movement on screen
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private int facing = 0; // 1 - South, 2 - North, 4 - West, 8 - East
    /** Player scene-graph data. **/
    private Quad playerQuad;
    private Geometry playerGeo;
    
    /** Mob scene-graph data. **/
    private Quad mobQuad;
    private Geometry mobGeo;
    
    /** We need to track world coords. **/
    private float mobWorldX;
    private float mobWorldY;
    
    /** Data for screen dimensions. **/
    private int tileScreenSize;
    private int screenHeight;
    private int screenWidth;

    public static void main(String[] args) {
        /** This is a place to set up app defaults. **/
        TechDemo app = new TechDemo();
        
        AppSettings newSetting = new AppSettings(false);

        newSetting.setFrameRate(60);
        newSetting.setSettingsDialogImage("Interface/iconEA.png");
        newSetting.setTitle("Project Athiri - v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);

        app.setSettings(newSetting);
        app.start();
    }
    
    /** Utility method to grab master texture data assets. **/
    public void setupTextures(){
        tileTexture = assetManager.loadTexture("Textures/Tiles/TileMapDev.bmp");
        playerTexture = assetManager.loadTexture("Textures/Player/CharTileSheet0.bmp");
        mobTexture = assetManager.loadTexture("Textures/Mobs/ZombieSheet0.png");
        
        /** Grab the image data from the textures. **/
        tileImage = tileTexture.getImage();
        playerImage = playerTexture.getImage();
        mobImage = mobTexture.getImage();
    }
    
    /** Previous code for tile texture setup moved here. **/
    public void setupTileData(){
        /** Split the image data into separate textures. **/
        tileTextureList = new Texture2D[16][16];
        tileImageList = new Image[16][16];
        tileMatList = new Material[16][16];
        tileRaster = ImageRaster.create(tileImage);
        
        /** Iterate through the list of tiles. **/
        // Dim: 16,16
        // Size 16,16 px
        
        imgWidth = 16;
        imgHeight = 16;
        depth = 4;
        
        /** Loop through the tiles. **/
        for(int tx = 0; tx < imgWidth; tx++){
            for(int ty = 0; ty < imgHeight; ty++){
                /** Create new Image data and ImageRaster for each tileTexture. **/
                tileImageList[tx][ty] = new Image(Image.Format.BGRA8, imgWidth, imgHeight, BufferUtils.createByteBuffer(depth * imgWidth * imgHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(tileImageList[tx][ty]);
                
                /** Copy pixels from source, paste to destination. **/
                for(int copyX = 0; copyX < 16; copyX++){
                    for(int copyY = 0; copyY < 16; copyY++){
                        ColorRGBA pixel = tileRaster.getPixel(copyX + (tx * imgWidth),copyY + (ty * imgHeight));
                        raster.setPixel(copyX, copyY, pixel);
                    }
                }
                
                /** Create new texture from the copied tile... **/
                tileTextureList[tx][ty] = new Texture2D(tileImageList[tx][ty]);
                // Set these filters for pixellated look
                tileTextureList[tx][ty].setMagFilter(MagFilter.Nearest);
                tileTextureList[tx][ty].setMinFilter(MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                tileMatList[tx][ty] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                tileMatList[tx][ty].setTexture("ColorMap",tileTextureList[tx][ty]);
                tileMatList[tx][ty].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** Prev code for player sprite sheet setup moved here. **/
    public void setupPlayerSpriteData(){
        /** Setup player sprite data. **/
        playerTextureList = new Texture2D[4][4];
        playerImageList = new Image[4][4];
        playerMatList = new Material[4][4];
        playerRaster = ImageRaster.create(playerImage);
        
        /** Iterate through the list of player sprites. **/
        // Dim: 4,3
        // Size: 32,48 px
        
        playerWidth = 32;
        playerHeight = 48;
        
        /** Loop through player sprite sheet. **/
        for(int px = 0; px < 4; px++){
            for(int py = 0; py < 4; py++){
                /** Image and Raster for player sprites. **/
                playerImageList[px][py] = new Image(Image.Format.RGBA16F, playerWidth, playerHeight, BufferUtils.createByteBuffer(depth * 2 * playerWidth * playerHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(playerImageList[px][py]);
                
                /** Copy pixels from source (starting at (0,256), paste to destination. **/
                for(int copyX = 0; copyX < 32; copyX++){
                    for(int copyY = 0; copyY < 48; copyY++){
                        // textureHeight - copyY inverts y from bottom-left to top-left origin
                        // needed to subtract 1 for the y-calc(add to number used for subtraction), index kept going out of bounds
                        int tempX = copyX + (px * playerWidth);
                        int tempY = playerImage.getHeight() - (copyY + 1 + (py * playerHeight));
                        //System.out.println("Player sprite sheet coords: " + tempX + "," + tempY);
                        ColorRGBA pixel = playerRaster.getPixel(tempX,tempY);
                        System.out.println("PlayerRaster setPixel(" + copyX + ", " + playerHeight  + " - (" + copyY + " + 1), " + pixel.toString() + ")");
                        raster.setPixel(copyX, playerHeight - (copyY + 1), pixel);
                    }
                }
                
                /** Create new texture from the copied player sprite sheet... **/
                playerTextureList[px][py] = new Texture2D(playerImageList[px][py]);
                // Set these filters for pixellated look
                playerTextureList[px][py].setMagFilter(MagFilter.Nearest);
                playerTextureList[px][py].setMinFilter(MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                playerMatList[px][py] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                playerMatList[px][py].setTexture("ColorMap",playerTextureList[px][py]);
                playerMatList[px][py].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** Prev code for mob sprite sheet setup moved here. **/
    public void setupMobSpriteData(){
        /** Setup player sprite data. **/
        mobTextureList = new Texture2D[4][4];
        mobImageList = new Image[4][4];
        mobMatList = new Material[4][4];
        mobRaster = ImageRaster.create(mobImage);
        
        /** Iterate through the list of player sprites. **/
        // Dim: 4,3
        // Size: 32,48 px
        
        mobWidth = 32;
        mobHeight = 48;
        
        /** Loop through player sprite sheet. **/
        for(int px = 0; px < 4; px++){
            for(int py = 0; py < 4; py++){
                /** Image and Raster for player sprites. **/
                mobImageList[px][py] = new Image(Image.Format.RGBA16F, mobWidth, mobHeight, BufferUtils.createByteBuffer(depth * 2 * mobWidth * mobHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(mobImageList[px][py]);
                
                /** Copy pixels from source (starting at (0,256), paste to destination. **/
                for(int copyX = 0; copyX < 32; copyX++){
                    for(int copyY = 0; copyY < 48; copyY++){
                        // textureHeight - copyY inverts y from bottom-left to top-left origin
                        // needed to subtract 1 for the y-calc(add to number used for subtraction), index kept going out of bounds
                        int tempX = copyX + (px * mobWidth);
                        int tempY = mobImage.getHeight() - (copyY + 1 + (py * mobHeight));
                        //System.out.println("Player sprite sheet coords: " + tempX + "," + tempY);
                        ColorRGBA pixel = mobRaster.getPixel(tempX,tempY);
                        raster.setPixel(copyX, mobHeight - (copyY + 1), pixel);
                    }
                }
                
                /** Create new texture from the copied player sprite sheet... **/
                mobTextureList[px][py] = new Texture2D(mobImageList[px][py]);
                // Set these filters for pixellated look
                mobTextureList[px][py].setMagFilter(MagFilter.Nearest);
                mobTextureList[px][py].setMinFilter(MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                mobMatList[px][py] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mobMatList[px][py].setTexture("ColorMap",mobTextureList[px][py]);
                mobMatList[px][py].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** Build the player here. **/
    public void buildPlayer(){
        // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
        playerQuad = new Quad((playerWidth / 16) * tileScreenSize, (playerHeight / 16) * tileScreenSize);
        playerGeo = new Geometry("Player", playerQuad);
        
        /** Set the proper Material to the Geometry and attach to guiNode. **/
        playerGeo.setMaterial(playerMatList[0][0]);
        // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
        // v3 Addition, set the player position members
        player_Scrn_X = (screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2);
        player_Scrn_Y = (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2);
        playerGeo.setLocalTranslation((screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2), (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2), 0);
        guiNode.attachChild(playerGeo);
    }
    
    /** This method to build mob scene stuff is based on player. 
     * GLOBAL PROTOTYPE
     **/
    public void buildMob(int x, int y){
        // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
        mobQuad = new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize);
        mobGeo = new Geometry("Player", mobQuad);
        
        /** Set the proper Material to the Geometry and attach to guiNode. **/
        mobGeo.setMaterial(mobMatList[0][0]);
        // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
        // v3 Addition, set the player position members
        mobWorldX = x - (((mobWidth / 16) * tileScreenSize) / 2);
        mobWorldY = y;
        mobGeo.setLocalTranslation((screenWidth / 2) - (((mobWidth / 16) * tileScreenSize) / 2), (screenHeight / 2) - (((mobHeight / 16) * tileScreenSize) / 2), 0);
        guiNode.attachChild(mobGeo);
    }

    /** TileMap prototype. **/
    public class TileMap{
        /** Data for Tile pages are being refactored. **/
        private int size;           //  Keep track of tile size (on screen)
        private int[][] data;       //  This tells the build method which tiles to use
        private Quad[][] tiles;     //  Meshes used for tiles
        private Node mapNode;       //  Serves as the 'rootNode' for the tile map
        private Geometry[][] tileGeo;// Geometries attach the Quads to the scene graph, making them renderable
        
        /** We're going to use a single chunk of tiles for now, but let's track position... **/
        public int pageX = 0;
        public int pageY = 0;
        public float offsetX = 0.0f;   // Track the view offset
        public float offsetY = 0.0f;   // The offsets let us move the tiles relative to the viewport
        
        /** NEW DATA MEMBERS FOR TILEPAGES. **/
        private ArrayList<TileMapPage> pages;
        private int maxPages;
        
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
                    //  For now, we're using a global material
                    //  When this class is refactored, we will need a reference to the available Materials
                    this.tileGeo[dx][dy].setMaterial(tileMatList[1][14]);
                    
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
        
        /** Add mob relative to map. **/
        public void buildMob(float x, float y){
            // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
            mobQuad = new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize);
            mobGeo = new Geometry("Player", mobQuad);

            /** Set the proper Material to the Geometry and attach to guiNode. **/
            mobGeo.setMaterial(mobMatList[0][0]);
            // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
            // v3 Addition, set the player position members
            mobWorldX = (x * size) - (((mobWidth / 16) * tileScreenSize) / 2);
            mobWorldY = (y * size);
            mobGeo.setLocalTranslation(mobWorldX, mobWorldY, 0);
            mapNode.attachChild(mobGeo);
        }
        
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
                    System.out.println(((pX * maxPages) + pY) + "= (" + pX + " * "+ maxPages +") + " + pY +")" );
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
                                genPage.tileGeo[tX][tY].setMaterial(tileMatList[2][0]);
                            }else if(value >= 0.1d && value < 0.6d){ // Grass layers
                                //this.setTile(pX,pY,0,0);
                                genPage.tileGeo[tX][tY].setMaterial(tileMatList[0][0]);
                            }else if(value >= 0.7d && value < 0.8d){ // Moutain layers
                                //this.setTile(pX,pY,3,1);
                                genPage.tileGeo[tX][tY].setMaterial(tileMatList[3][1]);
                            }else if(value >= 0.8d && value < 0.9d){ // Snow layers
                                //this.setTile(pX,pY,1,1);
                                genPage.tileGeo[tX][tY].setMaterial(tileMatList[4][3]);
                            }else if(value >= 0.9d ){ // Snow layers
                                //this.setTile(pX,pY,1,1);
                                genPage.tileGeo[tX][tY].setMaterial(tileMatList[1][1]);
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
                pages.get((pX * 16) + pY).tileGeo[dX][dY].setMaterial(tileMatList[mX][mY]);
            }else{
                System.out.println("ARRAY INDEX ERROR!!!!!");
                System.out.println("______________________");
                System.out.println("origin point: "+ x + ","+y + "| page:"+ pX + "," + pY + "| index:" + dX + "," + dY);
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
    
    /** v3 TileMapPage will allow us to manage dynamic-sized maps. **/
    public class TileMapPage{
        /** TileMapPage is just a junior TileMap. **/
        /** Data copied from tile maps. **/
        private int size;           //  Might be more effiecent to make the page reference a global size
        private int[][] data;       //  This tells the build method which tiles to use
        private Quad[][] tiles;     //  Meshes used for tiles
        private Node pageNode;       //  Serves as the 'rootNode' for the tile map
        private Geometry[][] tileGeo;// Geometries attach the Quads to the scene graph, making them renderable
        
        /** Prototype, still using globaly defined Materials. **/
        //Material[][] tileMatIndex;
        
        /** We're going to start using TileMapPages, like chunks, each needs to track  it's own position... **/
        public int pageX = 0;
        public int pageY = 0;
        public float offsetX = 0.0f;   //  Track the view offset
        public float offsetY = 0.0f;   // The offsets let us move the tiles relative to the viewport
        
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
        
        public Node getNode(){return this.pageNode;}
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        /** This is a good place to set up important data. **/
        screenHeight = cam.getHeight();
        screenWidth = cam.getWidth();
        tileScreenSize = screenHeight / 24;
        
        /** This demo will add terrain. 
         *  Previous code has been moved to utility functions
         *  For now, I'm adding to the main class. Class objects will be prototyped
         * here first, then refactored into separate classes
         **/
        
        /** Call the previous setup code. **/
        setupTextures();
        setupTileData();
        setupPlayerSpriteData();  
        setupMobSpriteData();
        
        /** Move player Geometry. **/
        buildPlayer();
        
        /** Create our prototype TileMap. **/
        //TileMap map = new TileMap(tileSreenSize);
        map = new TileMap(tileScreenSize, 4);
        
        guiNode.attachChild(map.getNode());
        map.getNode().setLocalTranslation((screenWidth/2) - (tileScreenSize * 8),0,-0.5f);
        
        
        map.genOverworld();
        /** Setup inputManager. **/
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        
        inputManager.addListener(actionListener, "Up","Down","Left","Right");
        
        // Let's play around with random numbers with normalized distrobution
        System.out.println("Spawn mobs");
        for(int t = 0; t < 20; t++){
            double mobX = map.randNormDist(32d, 10d);
            double mobY = map.randNormDist(32d, 10d);
            System.out.println("mob x: " + mobX + "mob y: " + mobY);
            map.buildMob((float)mobX,(float)mobY);
        }
    }
    
    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            // 1 - South, 2 - North, 4 - West, 8 - East
            if(name.equals("Up")){
                up = keyPressed;
                if(keyPressed){
                    facing += 2; 
                }else{
                    facing -= 2; 
                }
            }if(name.equals("Down")){
                down = keyPressed;
                if(keyPressed){
                    facing += 1; 
                }else{
                    facing -= 1; 
                }
            }if(name.equals("Left")){
                left = keyPressed;
                if(keyPressed){
                    facing += 4; 
                }else{
                    facing -= 4; 
                }
            }if(name.equals("Right")){
                right = keyPressed;
                if(keyPressed){
                    facing += 8; 
                }else{
                    facing -= 8; 
                }
            }
        }
    };
    
    /** Animation data. **/
    private float frameSpeed = 0.3f; // Adjusting the speed slower
    private float frameTime = 0.0f;
    private int currentFrame = 0;
    
    /** Movement update data **/
    private float deltaX = 0.0f;
    private float deltaY = 0.0f;
    
    /** Utility method to update player animation frame and movement per input mask. **/
    public void updatePlayer(float tpf){
        /** When the player presses any of the movement keys, play the animation. **/
        // For now, no real movement, just making sure the right frames play for the right direction
        // v3 move the player (X and Y ) according to the animation that's played
        if(down){
            //Move the player
            deltaY -= (walkSpeed * tileScreenSize);
            // Simulate walk cycle - south
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerGeo.setMaterial(playerMatList[0][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerGeo.setMaterial(playerMatList[0][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerGeo.setMaterial(playerMatList[0][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerGeo.setMaterial(playerMatList[0][0]);
                        break;
                }
            } 
            //  */
        }if(up){
            // Simulate walk cycle - north
            //Move the player
            deltaY += (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerGeo.setMaterial(playerMatList[1][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerGeo.setMaterial(playerMatList[1][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerGeo.setMaterial(playerMatList[1][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerGeo.setMaterial(playerMatList[1][0]);
                        break;
                }
            } // */
        }if(left){
            // Simulate walk cycle - west
            //Move the player
            deltaX -= (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerGeo.setMaterial(playerMatList[2][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerGeo.setMaterial(playerMatList[2][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerGeo.setMaterial(playerMatList[2][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerGeo.setMaterial(playerMatList[2][0]);
                        break;
                }
            } // */
        }if(right){
            // Simulate walk cycle - east
            //Move the player
            deltaX += (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerGeo.setMaterial(playerMatList[3][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerGeo.setMaterial(playerMatList[3][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerGeo.setMaterial(playerMatList[3][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerGeo.setMaterial(playerMatList[3][0]);
                        break;
                }
            } // */
        }
        //playerGeo.move(deltaX *tpf, deltaY * tpf, 0);
        //System.out.println((deltaX *tpf) + ", " + (deltaY *tpf) + ", " + tpf);
        
        /** Instead of moving the player directly, let's displace the map. **/
        map.getNode().move(deltaX * tpf * -1, deltaY * tpf * -1, 0);
        map.offsetX -= deltaX *tpf;
        map.offsetY -= deltaY *tpf;
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        /** Let's implement a frame system. 
         *  We will need to keep track of the animation speed
         *  We also need to flip between the proper frames
         * 
         *  These are the frames from the char tile sheet
         *  [0,0] Standing          Frame: 0
         *  [0,1] First-step        Frame: 1
         *  [0,2] Second-step       Frame: 2
         * 
         *  DISCLAIMER...
         *  This is a quick and dirty prototype. Later, I will re-organize this update loop
         **/
        
        deltaX = 0.0f;
        deltaY = 0.0f;
        
        frameTime += tpf;
        
        /**Player animation and movement code moved to updatePlayer() method. **/
        updatePlayer(tpf);
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        /** We have the ability to do extra things for rendering. **/
    }
    
    
}
