/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;

/**
 *
 * @author esspe
 */
public class Utilities {
    /** Keep track of the objects vital to the scene graph root. **/
    public AssetManager assetManager;
    public Node guiNode;
    
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
    
    /** Player scene-graph data. **/
    private Quad playerQuad;
    private Geometry playerGeo;
    
    /** Mob scene-graph data. **/
    private Quad mobQuad;
    private Geometry mobGeo;
    
    //Player position members
    private float player_Scrn_X;
    private float player_Scrn_Y;
    private float playerWorldX; // Local coords
    private float playerWorldY;
    
    /** We need to track world coords for mobs. **/
    private float mobWorldX;
    private float mobWorldY;
    
    //Player movement members
    private float walkSpeed = 3.5f; // Faster movement on screen
    
    /** Data for screen dimensions. **/
    private int tileScreenSize;
    private int screenHeight;
    private int screenWidth;
    
    /** CONSTRUCTOR. 
     *  Based off of simpleInitApp of TechDemo
     **/
    public Utilities(AssetManager assetManager, Node guiNode){
        this.assetManager = assetManager;
        this.guiNode = guiNode;
        
        this.setupTextures();
        this.setupTileData();
        this.setupPlayerSpriteData();  
        this.setupMobSpriteData();
        
        /** Move player Geometry. **/
        this.buildPlayer();
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
                tileTextureList[tx][ty].setMagFilter(Texture.MagFilter.Nearest);
                tileTextureList[tx][ty].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
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
                        raster.setPixel(copyX, playerHeight - (copyY + 1), pixel);
                    }
                }
                
                /** Create new texture from the copied player sprite sheet... **/
                playerTextureList[px][py] = new Texture2D(playerImageList[px][py]);
                // Set these filters for pixellated look
                playerTextureList[px][py].setMagFilter(Texture.MagFilter.Nearest);
                playerTextureList[px][py].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
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
                mobTextureList[px][py].setMagFilter(Texture.MagFilter.Nearest);
                mobTextureList[px][py].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
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
    
    public int getTileScreenSize(){return this.tileScreenSize;}
    public int getScreenWidth(){return this.screenWidth;}
    public Material[][] getTileMatList(){return this.tileMatList;}
}
