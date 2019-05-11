/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import GameControls.PlayerControl;
import Utility.WorldAppState;
import com.jme3.app.BasicApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

/**
 *  NEW ANIMATIONS UNDER DEVELOPMENT!!!
 * @author Michael A. Bradford
 * @version 2.0.1 - Sub-system overhaul - branch of NewAppDemo.java
 */
public class AnimationDev extends BasicApplication{
    // Version Info
    static private int version_maj = 1;
    static private int version_min = 1;
    static private int version_revision = 3;
    static String version_fork = "App Development";
    
    public Texture[] loadTextures;
    public Texture[] combatTextures;
    
    public Material[] cMat;
    public boolean[] playerInputMask;
    public WorldAppState worldstate;
    public PlayerControl player;
    public int movementFlag;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AnimationDev app = new AnimationDev();
        AppSettings settings = new AppSettings(false);
        //settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri (Dev)- v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
        app.setSettings(settings);
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        
        player = new PlayerControl();
        player.setTileSize(cam.getHeight()/6);
        loadPlayerTextures();
        loadTextureAssets();
        
        
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Updated Animations!!! Project Athiri - Sankofa Digital Media");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        
        player.createPlayer();
        player.createShadow();
        
        player.pNode.setQueueBucket(RenderQueue.Bucket.Gui);
        player.pNode.setCullHint(Spatial.CullHint.Never);
        
        player.pNode.setLocalTranslation(cam.getWidth()/2,cam.getHeight()/4, 0.5f);
        player.pNode.addControl(player);
        
        guiNode.attachChild(player.pNode);
        /*worldstate = new WorldAppState(cam.getWidth(),cam.getHeight(),24);
        stateManager.attach(worldstate);
        guiNode.attachChild(worldstate.getUINode());*/
        //inputManager = app.getInputManager();
        
        playerInputMask = new boolean[6];
        
        playerInputMask[0] = false;
        playerInputMask[1] = false;
        playerInputMask[2] = false;
        playerInputMask[3] = false;
        playerInputMask[4] = false;
        playerInputMask[5] = false;
        
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Menu", new KeyTrigger(KeyInput.KEY_TAB));
        
        inputManager.addListener(actionListener, "Up","Down","Left","Right","Attack","Menu");
        
        
    }
    
    public void loadPlayerTextures(){
        loadTextures = new Texture[24];
        player.playerMat = new Material[6][4];
        
        loadTextures[0] = assetManager.loadTexture("Textures/Player/South/Stand_South.png");
        loadTextures[1] = assetManager.loadTexture("Textures/Player/South/Run_1_South.png");
        loadTextures[2] = assetManager.loadTexture("Textures/Player/South/Run_2_South.png");
        loadTextures[3] = assetManager.loadTexture("Textures/Player/South/Run_3_South.png");
        loadTextures[4] = assetManager.loadTexture("Textures/Player/South/Run_4_South.png");
        
        loadTextures[5] = assetManager.loadTexture("Textures/Player/North/Stand_North.png");
        loadTextures[6] = assetManager.loadTexture("Textures/Player/North/Run_1_North.png");
        loadTextures[7] = assetManager.loadTexture("Textures/Player/North/Run_2_North.png");
        loadTextures[8] = assetManager.loadTexture("Textures/Player/North/Run_3_North.png");
        loadTextures[9] = assetManager.loadTexture("Textures/Player/North/Run_4_North.png");
        
        loadTextures[10] = assetManager.loadTexture("Textures/Player/East/Stand_East.png");
        loadTextures[11] = assetManager.loadTexture("Textures/Player/East/Run_1_East.png");
        loadTextures[12] = assetManager.loadTexture("Textures/Player/East/Run_2_East.png");
        loadTextures[13] = assetManager.loadTexture("Textures/Player/East/Run_3_East.png");
        loadTextures[14] = assetManager.loadTexture("Textures/Player/East/Run_4_East.png");
        
        loadTextures[15] = assetManager.loadTexture("Textures/Player/West/Stand_West.png");
        loadTextures[16] = assetManager.loadTexture("Textures/Player/West/Run_1_West.png");
        loadTextures[17] = assetManager.loadTexture("Textures/Player/West/Run_2_West.png");
        loadTextures[18] = assetManager.loadTexture("Textures/Player/West/Run_3_West.png");
        loadTextures[19] = assetManager.loadTexture("Textures/Player/West/Run_4_West.png");
        
        loadTextures[20] = assetManager.loadTexture("Textures/Player/South/Attack_South.png");
        loadTextures[21] = assetManager.loadTexture("Textures/Player/North/Attack_North.png");
        loadTextures[22] = assetManager.loadTexture("Textures/Player/East/Attack_East.png");
        loadTextures[23] = assetManager.loadTexture("Textures/Player/West/Attack_West.png");
        
        
        
        int matId = 0;
        int dirId = 0;
        for(int i = 0; i < 4; i++){
            loadTextures[0 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[0 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            loadTextures[1 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[1 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            loadTextures[2 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[2 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            loadTextures[3 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[3 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            loadTextures[4 + (i* 5)].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[4 + (i* 5)].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            
            //Attack frames
            loadTextures[20 + i].setMagFilter(Texture.MagFilter.Nearest);
            loadTextures[20 + i].setMinFilter(Texture.MinFilter.NearestNoMipMaps);

            player.playerMat[0][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[0][i].setTexture("ColorMap", loadTextures[0 + (i* 5)]);
            player.playerMat[0][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[1][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[1][i].setTexture("ColorMap", loadTextures[1 + (i* 5)]);
            player.playerMat[1][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[2][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[2][i].setTexture("ColorMap", loadTextures[2 + (i* 5)]);
            player.playerMat[2][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[3][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[3][i].setTexture("ColorMap", loadTextures[3 + (i* 5)]);
            player.playerMat[3][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            player.playerMat[4][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[4][i].setTexture("ColorMap", loadTextures[4 + (i* 5)]);
            player.playerMat[4][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            
            //Attack frames
            player.playerMat[5][i] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.playerMat[5][i].setTexture("ColorMap", loadTextures[20 + i]);
            player.playerMat[5][i].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matId++;
        }
        
        player.sMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        Texture sTex = assetManager.loadTexture("Textures/Player/Shadow.png");
        sTex.setMagFilter(Texture.MagFilter.Nearest);
        sTex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        player.sMat.setTexture("ColorMap", sTex);
        player.sMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }
    
    public void loadTextureAssets(){
        combatTextures = new Texture[6];
        player.cMat = new Material[6];
        
        combatTextures[0] = assetManager.loadTexture("Textures/Combat/AttackSlash_0.png");
        combatTextures[1] = assetManager.loadTexture("Textures/Combat/AttackSlash_1.png");
        combatTextures[2] = assetManager.loadTexture("Textures/Combat/AttackSlash_2.png");
        combatTextures[3] = assetManager.loadTexture("Textures/Combat/AttackSlash_3.png");
        combatTextures[4] = assetManager.loadTexture("Textures/Combat/AttackSlash_4.png");
        combatTextures[5] = assetManager.loadTexture("Textures/Combat/AttackSlash_5.png");
        
        int matId = 0;
        for(Texture tList : combatTextures){
            tList.setMagFilter(Texture.MagFilter.Nearest);
            tList.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            
            player.cMat[matId] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            player.cMat[matId].setTexture("ColorMap", tList);
            player.cMat[matId].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matId++;
        }
    }

    @Override
    public void simpleUpdate(float tpf){
        movementFlag = 0;
        if(playerInputMask[0]){
            movementFlag += 1;
            //UP
        }if(playerInputMask[1]){
            movementFlag += 2;
            //DOWN
        }if(playerInputMask[2]){
            movementFlag += 4;
            //LEFT
        }if(playerInputMask[3]){
            movementFlag += 8;
            //RIGHT
        }if(playerInputMask[4]){
            player.setActionMask(1);
        }
        
        player.setMovementMask(movementFlag);
    }
    
    public class WeaponEffect extends AbstractControl{
        
        private Geometry geo;
        public Material[] mat;
        private int matIndex;
        private float time,life = 100;
        private boolean active;
        
        public WeaponEffect(){}

        @Override
        protected void controlUpdate(float tpf) {
            this.spatial.setMaterial(mat[matIndex]);
            
            if(time > (life /4)){
                
            }else if(time > (life /3)){
                
            }else if(time > (life /2)){
                
            }else if(time > life){
                //End cycle and remove control
                active = false;
                this.spatial.removeControl(this);
            }
            
            time += tpf;
            
            
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
        
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
    
}
