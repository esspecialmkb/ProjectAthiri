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
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;

/**
 *  This class is intended to resolve the Global-Materials list issue.
 * Let's just provide a global list
 * @author Michael A. Bradford
 */
public class Materials {
    /** Textures for tiles and player. **/
    public Texture tileTexture;
    public Texture playerTexture, mobTexture;  // New mob Texture data
    /** Grab the image data from the textures. **/
    public Image tileImage;
    public Image playerImage, mobImage;        // New Mob Image data
    /** Having the image data allows us to split the image into separate textures. **/
    public Texture2D[][] tileTextureList;
    public Image[][] tileImageList;
    public Material[][] tileMatList;
    public ImageRaster tileRaster;
    /** Data definitions for tile images. **/
    public int imgWidth;
    public int imgHeight;
    public int depth;
    /** Setup player sprite data. New Mob stuff. **/
    public Texture2D[][] playerTextureList, mobTextureList;
    public Image[][] playerImageList, mobImageList;
    public Material[][] playerMatList, mobMatList;
    public ImageRaster playerRaster, mobRaster;
    
    /** Data defs for player sprites, uses same depth as tiles * 2. **/
    public int playerWidth, mobWidth;
    public int playerHeight, mobHeight;
    
    public AssetManager assetManager;
    
    public Materials(AssetManager assetMgr){
        this.assetManager = assetMgr;
    }
    
    public void prepare(){
        this.setupTextures();
        this.setupTileData();
        this.setupPlayerSpriteData();
        this.setupMobSpriteData();
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
                playerImageList[px][py] = new Image(Image.Format.RGBA16F, playerWidth, playerHeight, BufferUtils.createByteBuffer(depth * 2 * playerWidth * playerHeight), null, ColorSpace.Linear); 
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
}
