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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
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
 *  @Line 177 - TileMapControl arch tiles
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
    private BitmapFont gameFont;
    private BitmapText[] debugText;
    
    private Material[] terrainMat;
    private Material[] archMat;
    private Material[][] playerMat;
    private InputManager inputManager;
    private boolean[] playerInputMask;
    private int movementFlag = 0;
    private float movementX = 0, movementY = 0, movementSpeed = 0.5f;  //Use move speed as 'Tile per second'
    
    public WorldAppState(int scrnW, int scrnH, int tileDiv){
        screenWidth = scrnW;
        screenHeight = scrnH;
        tileScreenSize = scrnH/tileDiv;
    }
    
    public Node getUINode(){return guiNode;}
    
    public void loadTextureAssets(AssetManager assetManager){
        Texture[] playerTextures = new Texture[24];
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
        
        playerTextures[20] = assetManager.loadTexture("Textures/Player/South/Attack_South.png");
        playerTextures[21] = assetManager.loadTexture("Textures/Player/North/Attack_North.png");
        playerTextures[22] = assetManager.loadTexture("Textures/Player/East/Attack_East.png");
        playerTextures[23] = assetManager.loadTexture("Textures/Player/West/Attack_West.png");
        
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
            
            //Attack frames
            playerTextures[20 + i].setMagFilter(Texture.MagFilter.Nearest);
            playerTextures[20 + i].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

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
            
            //Attack frames
            player.playerMat[5][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[5][i].setTexture("ColorMap", playerTextures[20 + i]);
            player.playerMat[5][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matId++;
        }
        
        Texture[] combatTextures = new Texture[6];
        player.cMat = new Material[6];
        
        combatTextures[0] = assetManager.loadTexture("Textures/Combat/AttackSlash_0.png");
        combatTextures[1] = assetManager.loadTexture("Textures/Combat/AttackSlash_1.png");
        combatTextures[2] = assetManager.loadTexture("Textures/Combat/AttackSlash_2.png");
        combatTextures[3] = assetManager.loadTexture("Textures/Combat/AttackSlash_3.png");
        combatTextures[4] = assetManager.loadTexture("Textures/Combat/AttackSlash_4.png");
        combatTextures[5] = assetManager.loadTexture("Textures/Combat/AttackSlash_5.png");
        
        matId = 0;
        for(Texture tList : combatTextures){
            tList.setMagFilter(Texture.MagFilter.Nearest);
            tList.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            
            player.cMat[matId] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.cMat[matId].setTexture("ColorMap", tList);
            player.cMat[matId].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
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
        
        /** New textures. **/
        Texture[] wallTexture = new Texture[21];
        
        wallTexture[0] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Bottom_Cap.png");
        wallTexture[1] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Cap.png");
        wallTexture[2] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Center.png");
        wallTexture[3] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Center_Top.png");
        wallTexture[4] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Horiz_Row.png");
        wallTexture[5] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_Cap.png");
        wallTexture[6] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_DnCorner.png");
        wallTexture[7] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_Mid.png");
        wallTexture[8] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_Turn_Bottom.png");
        wallTexture[9] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_Turn_Top.png");
        
        wallTexture[10] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Left_UpCorner.png");
        wallTexture[11] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Mid_Bottom.png");
        wallTexture[12] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_Cap.png");
        wallTexture[13] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_DnCorner.png");
        wallTexture[14] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_Mid.png");
        wallTexture[15] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_Turn_Bottom.png");
        wallTexture[16] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_Turn_Top.png");
        wallTexture[17] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Right_UpCorner.png");
        wallTexture[18] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Top_Cap.png");
        wallTexture[19] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Vert_Col.png");
        
        wallTexture[20] = assetManager.loadTexture("Textures/Tiles/Architecture/Walls/Wall_Blank.png");
        
        worldMap.archMat = new Material[21];
        for(int m = 0; m < 21; m++){
            worldMap.archMat[m] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            worldMap.archMat[m].setTexture("ColorMap", wallTexture[m]);
            worldMap.archMat[m].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        
        Texture debugText = assetManager.loadTexture("Textures/Tiles/Debug_Red.png");
        worldMap.debugMat = new Material[4];
        //worldMap.debugPageGeo = new Geometry[9];
        worldMap.debugMat[0]  = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        worldMap.debugMat[0].setTexture("ColorMap", debugText);
        worldMap.debugMat[0].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        worldMap.debugGeo = new Geometry("Debug Red",new Quad(tileScreenSize,tileScreenSize));
        
        worldMap.debugMat[1]  = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        worldMap.debugMat[1].setTexture("ColorMap", assetManager.loadTexture("Textures/Tiles/PageDebug_Red.png"));
        worldMap.debugMat[1].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //worldMap.debugGeo = new Geometry("Debug Red",new Quad(tileScreenSize * 16,tileScreenSize * 16));
        
        worldMap.debugMat[2]  = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        worldMap.debugMat[2].setTexture("ColorMap", assetManager.loadTexture("Textures/Tiles/PageDebug_Blue.png"));
        worldMap.debugMat[2].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //worldMap.debugGeo = new Geometry("Debug Red",new Quad(tileScreenSize * 16,tileScreenSize * 16));
        
        worldMap.debugMat[3]  = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        worldMap.debugMat[3].setTexture("ColorMap", assetManager.loadTexture("Textures/Tiles/PageDebug_Yellow.png"));
        worldMap.debugMat[3].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //worldMap.debugGeo = new Geometry("Debug Red",new Quad(tileScreenSize * 16,tileScreenSize * 16));
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
        worldMap.playerX = (screenWidth/2);
        worldMap.playerY = (screenHeight/2);
        
        player.pNode.addControl(player);
        
        //worldMap.buildMap(tileScreenSize);
        worldMap.buildPagedMap(tileScreenSize);
        
        guiNode.attachChild(worldMap.getMapNode());
        worldMap.getMapNode().addControl(worldMap);
        
        gameFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        setupDebugText();
        
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
    
    public void setupDebugText(){
        debugText = new BitmapText[4];
        debugText[0] = new BitmapText(gameFont, false);
        debugText[0].setSize(gameFont.getCharSet().getRenderedSize());
        debugText[0].setText("Map Position: ");
        debugText[0].setLocalTranslation(5, screenHeight - debugText[0].getLineHeight(), 0);
        guiNode.attachChild(debugText[0]);
        
        debugText[1] = new BitmapText(gameFont, false);
        debugText[1].setSize(gameFont.getCharSet().getRenderedSize());
        debugText[1].setText("Page Position: ");
        debugText[1].setLocalTranslation(5, screenHeight - (2 * (debugText[1].getLineHeight() + 5)), 0);
        guiNode.attachChild(debugText[1]);
        
        debugText[2] = new BitmapText(gameFont, false);
        debugText[2].setSize(gameFont.getCharSet().getRenderedSize());
        debugText[2].setText("Page Index: ");
        debugText[2].setLocalTranslation(5, screenHeight - (3 * (debugText[2].getLineHeight() + 5)), 0);
        guiNode.attachChild(debugText[2]);
        
        debugText[3] = new BitmapText(gameFont, false);
        debugText[3].setSize(gameFont.getCharSet().getRenderedSize());
        debugText[3].setText("Time per frame: FPS: ");
        debugText[3].setLocalTranslation(5, screenHeight - (4 * (debugText[3].getLineHeight() + 5)), 0);
        guiNode.attachChild(debugText[3]);
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
        }if(playerInputMask[4] == true){
            player.setActionMask(1);
            movementX = 0;
            movementY = 0;
        }else if(playerInputMask[4] == false){
            /** Process the movementSpeed before translating the map. **/
            Vector2f calc = new Vector2f(movementX, movementY).normalize();
            calc.multLocal(1f * movementSpeed);
            
            // The TileMapControl handles translating the map and paging tiles, the PlayerControl will handle animations
            worldMap.moveMap(calc.x * 1, calc.y * 1);
            player.setMovementMask(movementFlag);
        }
        
        updateDebugText(tpf);
    }
    
    public void updateDebugText(float tpf){
        Vector2f mapCoords = worldMap.getMapCoords();
        debugText[0].setText("Map Position: " + mapCoords.x + ", " + mapCoords.y);
        
        debugText[1].setText("Page Position: " + mapCoords.x % 16 + ", " + mapCoords.y % 16);
        
        debugText[2].setText("Page Index: " + -FastMath.floor(mapCoords.x / 16 )+ ", " + -FastMath.floor(mapCoords.y / 16));
        
        debugText[3].setText(" FPS: " + Math.floor((1/tpf)) + "Time per frame: " + tpf);
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
