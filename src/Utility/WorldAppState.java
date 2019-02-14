/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import GameControls.PlayerControl;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.RootNodeState;

/**
 *  
 *  
 *
 * @author esspe 
 */
public class WorldAppState extends AbstractAppState {
    public int tileScreenSize;
    public int screenWidth, screenHeight;
    
    public Node worldNode;
    public Node screenNode;     // Stand-in for guiNode
    public TileMapControl worldMap;
    public PlayerControl player;
    
    private Node rootNode;
    private Node guiNode = new Node("GUI Node");
    
    private Material[] terrainMat;
    private Material[] archMat;
    private Material[][] playerMat;
    private InputManager inputManager;
    private boolean[] playerInputMask;
    private int movementFlag = 0;
    private float movementX = 0, movementY = 0, movementSpeed = 2.2f;  //Use move speed as 'Tile per second'
    
    public WorldAppState(int scrnW, int scrnH, int tileDiv){
        screenWidth = scrnW;
        screenHeight = scrnH;
        tileScreenSize = scrnH/tileDiv;
    }
    
    public Node getUINode(){return guiNode;}
    
    public void loadTextureAssets(AssetManager assetManager){
        Texture[] playerTextures = new Texture[20];
        player.playerMat = new Material[6][4];
        
        playerTextures[0] = assetManager.loadTexture("Textures/Player/South/Stand_South.png");
        playerTextures[1] = assetManager.loadTexture("Textures/Player/South/Run_1_South.png");
        playerTextures[2] = assetManager.loadTexture("Textures/Player/South/Run_2_South.png");
        playerTextures[3] = assetManager.loadTexture("Textures/Player/South/Run_3_South.png");
        playerTextures[4] = assetManager.loadTexture("Textures/Player/South/Run_4_South.png");
        
        playerTextures[5] = assetManager.loadTexture("Textures/Player/North/Stand_North.png");
        playerTextures[6] = assetManager.loadTexture("Textures/Player/North/Run_1_North.png");
        playerTextures[7] = assetManager.loadTexture("Textures/Player/North/Run_2_North.png");
        playerTextures[8] = assetManager.loadTexture("Textures/Player/North/Run_3_North.png");
        playerTextures[9] = assetManager.loadTexture("Textures/Player/North/Run_4_North.png");
        
        playerTextures[10] = assetManager.loadTexture("Textures/Player/East/Stand_East.png");
        playerTextures[11] = assetManager.loadTexture("Textures/Player/East/Run_1_East.png");
        playerTextures[12] = assetManager.loadTexture("Textures/Player/East/Run_2_East.png");
        playerTextures[13] = assetManager.loadTexture("Textures/Player/East/Run_3_East.png");
        playerTextures[14] = assetManager.loadTexture("Textures/Player/East/Run_4_East.png");
        
        playerTextures[15] = assetManager.loadTexture("Textures/Player/West/Stand_West.png");
        playerTextures[16] = assetManager.loadTexture("Textures/Player/West/Run_1_West.png");
        playerTextures[17] = assetManager.loadTexture("Textures/Player/West/Run_2_West.png");
        playerTextures[18] = assetManager.loadTexture("Textures/Player/West/Run_3_West.png");
        playerTextures[19] = assetManager.loadTexture("Textures/Player/West/Run_4_West.png");
        
        int matId = 0;
        int dirId = 0;
        for(int i = 0; i < 4; i++){
            playerTextures[0 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[0 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            playerTextures[1 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[1 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            playerTextures[2 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[2 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            playerTextures[3 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[3 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            playerTextures[4 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[4 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            player.playerMat[0][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[0][i].setTexture("ColorMap", playerTextures[0 + (i* 5)]);
            player.playerMat[0][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[1][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[1][i].setTexture("ColorMap", playerTextures[1 + (i* 5)]);
            player.playerMat[1][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[2][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[2][i].setTexture("ColorMap", playerTextures[2 + (i* 5)]);
            player.playerMat[2][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[3][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[3][i].setTexture("ColorMap", playerTextures[3 + (i* 5)]);
            player.playerMat[3][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[4][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[4][i].setTexture("ColorMap", playerTextures[4 + (i* 5)]);
            player.playerMat[4][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matId++;
        }
        
        player.sMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        Texture sTex = assetManager.loadTexture("Textures/Player/Shadow.png");
        sTex.setMagFilter(Texture.MagFilter.Nearest);
        sTex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        player.sMat.setTexture("ColorMap", sTex);
        player.sMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        Texture[] terrainTextures = new Texture[15];
        
        terrainTextures[0] = assetManager.loadTexture("Textures/Tiles/Landscape/Grass/Grass_1.png");
        terrainTextures[1] = assetManager.loadTexture("Textures/Tiles/Landscape/Grass/Grass_2.png");
        terrainTextures[2] = assetManager.loadTexture("Textures/Tiles/Landscape/Grass/Grass_3.png");
        terrainTextures[3] = assetManager.loadTexture("Textures/Tiles/Landscape/Grass/Grass_4.png");
        
        terrainTextures[4] = assetManager.loadTexture("Textures/Tiles/Landscape/Stone/Stone_1.png");
        terrainTextures[5] = assetManager.loadTexture("Textures/Tiles/Landscape/Stone/Stone_2.png");
        terrainTextures[6] = assetManager.loadTexture("Textures/Tiles/Landscape/Stone/Stone_3.png");
        terrainTextures[7] = assetManager.loadTexture("Textures/Tiles/Landscape/Stone/Stone_4.png");
        
        terrainTextures[8] = assetManager.loadTexture("Textures/Tiles/Landscape/Dirt/Dirt_1.png");
        terrainTextures[9] = assetManager.loadTexture("Textures/Tiles/Landscape/Dirt/Dirt_2.png");
        terrainTextures[10] = assetManager.loadTexture("Textures/Tiles/Landscape/Dirt/Dirt_3.png");
        terrainTextures[11] = assetManager.loadTexture("Textures/Tiles/Landscape/Dirt/Dirt_4.png");
        
        terrainTextures[12] = assetManager.loadTexture("Textures/Tiles/Landscape/Water/Water_1.png");
        terrainTextures[13] = assetManager.loadTexture("Textures/Tiles/Landscape/Water/Water_2.png");
        terrainTextures[14] = assetManager.loadTexture("Textures/Tiles/Landscape/Water/Water_3.png");
        //terrainTextures[15] = assetManager.loadTexture("Textures/Tiles/Landscape/Water/Water_4.png");
        
        worldMap.terrainMat = new Material[15];
        for(int m = 0; m < worldMap.terrainMat.length; m++){
            worldMap.terrainMat[m] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            worldMap.terrainMat[m].setTexture("ColorMap", terrainTextures[m]);
            worldMap.terrainMat[m].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        
        /** Texture for Architecture. **/
        Texture archText = assetManager.loadTexture("Textures/Tiles/Architecture/Arch_1.png");
        ImageRaster tileRaster = ImageRaster.create(archText.getImage());
        worldMap.archMat = new Material[256];
        Texture2D[] tempText = new Texture2D[256];
        
        int imgWidth = 16, imgHeight = 16, depth = 4;
        
        for(int mX = 0; mX < 16; mX++){
            for(int mY = 0; mY < 16; mY++){
                //tempText[mX + (mY * 16)] = new Texture2D();
                Image img = new Image(Image.Format.BGRA8, imgWidth, imgHeight, BufferUtils.createByteBuffer(depth * imgWidth * imgHeight), null, ColorSpace.sRGB); 
                
                ImageRaster raster = ImageRaster.create(img);
                
                /** Copy pixels from source, paste to destination. **/
                for(int copyX = 0; copyX < 16; copyX++){
                    for(int copyY = 0; copyY < 16; copyY++){
                        ColorRGBA pixel = tileRaster.getPixel(copyX + (mX * imgWidth),copyY + (mY * imgHeight));
                        raster.setPixel(copyX, copyY, pixel);
                    }
                }
                /** Create new texture from the copied tile... **/
                tempText[mX + (mY * 16)] = new Texture2D(img);
                // Set these filters for pixellated look
                tempText[mX + (mY * 16)].setMagFilter(Texture.MagFilter.Nearest);
                tempText[mX + (mY * 16)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
                worldMap.archMat[mX + (mY * 16)] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
                worldMap.archMat[mX + (mY * 16)].setTexture("ColorMap", tempText[mX + (mY * 16)]);
                worldMap.archMat[mX + (mY * 16)].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
        
        playerInputMask = new boolean[6];
        
        playerInputMask[0] = false;
        playerInputMask[1] = false;
        playerInputMask[2] = false;
        playerInputMask[3] = false;
        playerInputMask[4] = false;
        playerInputMask[5] = false;
        
        rootNode = new Node("Root Node");        
        worldMap = new TileMapControl();
        player = new PlayerControl();
        
        /** We need to tell the guiNode to render and to never cull itself. **/
        guiNode.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.setCullHint(Spatial.CullHint.Never);
        
        loadTextureAssets(app.getAssetManager());
        
        player.setTileSize(tileScreenSize);
        player.createPlayer();
        
        /** We need to tell the guiNode to render and to never cull itself. **/
        player.pNode.setQueueBucket(RenderQueue.Bucket.Gui);
        player.pNode.setCullHint(Spatial.CullHint.Never);
        guiNode.attachChild(player.pNode);
        player.pNode.setLocalTranslation(screenWidth/2,screenHeight/2, 0.5f);
        player.pNode.addControl(player);
        
        worldMap.buildMap(tileScreenSize);
        
        guiNode.attachChild(worldMap.getMapNode());
        worldMap.getMapNode().addControl(worldMap);
        
        
        /** INPUTS. **/
        /** Setup inputManager. **/
        
        inputManager = app.getInputManager();
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_TAB));
        
        inputManager.addListener(actionListener, "Up","Down","Left","Right","Attack","Menu");
    }
    
    @Override
    public void update(float tpf) {
        /** Read the player input mask and determine movement direction we're moving in . **/
        movementFlag = 0;
        movementX = 0;
        movementY = 0;
        if(playerInputMask[0]){
            movementFlag += 1;
            //UP
            movementY -= movementSpeed;
        }if(playerInputMask[1]){
            movementFlag += 2;
            //DOWN
            movementY += movementSpeed;
        }if(playerInputMask[2]){
            movementFlag += 4;
            //LEFT
            movementX += movementSpeed;
        }if(playerInputMask[3]){
            movementFlag += 8;
            //RIGHT
            movementX -= movementSpeed;
        }
        /** Process the movementSpeed before translating the map. **/
        Vector2f calc = new Vector2f(movementX, movementY).normalize();
        calc.multLocal(0.1f * movementSpeed);
        // The TileMapControl handles translating the map and paging tiles, the PlayerControl will handle animations
        worldMap.moveMap(calc.x * tpf * tileScreenSize, calc.y * tpf * tileScreenSize);
        
        // If up and down are true, animate player either left or right
        if((playerInputMask[0] == true) && (playerInputMask[1] == true)){
            if((playerInputMask[2] != playerInputMask[3])){
            
            }
        }
        // If left and right are true, animate either up or right
        if((playerInputMask[2] == true) && (playerInputMask[3] == true)){
            if((playerInputMask[0] != playerInputMask[1])){
            
            }
        }
    }
    
    @Override
    public void stateAttached(AppStateManager stateManager) {
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        
    }
    
    public void setupInput(InputManager inputManager){
        
    }
    
    @Override
    public void cleanup() {
        
    }
    
    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            if(name.equals("Up")){
                playerInputMask[0] = keyPressed; //Move up
            }if(name.equals("Down")){
                playerInputMask[1] = keyPressed; //Move down
            }if(name.equals("Left")){
                playerInputMask[2] = keyPressed; //Move left
            }if(name.equals("Right")){
                playerInputMask[3] = keyPressed; //Move right
            }if(name.equals("Attack")){
                playerInputMask[4] = keyPressed; //Move attack
            }
        }
    };
    
    public void savej3o(){
        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome+"/Models/"+"MyModel.j3o");
        try {
          exporter.save(rootNode, file);
        } catch (IOException ex) {
          Logger.getLogger(RootNodeState.class.getName()).log(Level.SEVERE, "Error: Failed to unpack tile texture!", ex);
        }
    }
    
}
