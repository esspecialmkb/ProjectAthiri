/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Utility.WorldAppState;
import com.jme3.app.BasicApplication;
import com.jme3.font.BitmapText;
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
public class AppDev extends BasicApplication{
    // Version Info
    static private int version_maj = 1;
    static private int version_min = 1;
    static private int version_revision = 1;
    static String version_fork = "App Development";
    
    public Texture[] loadTextures;
    
    public Material[] pMat;
    
    public WorldAppState worldstate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppDev app = new AppDev();
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
        
        loadTextureAssets();
        
        
                /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Updated Animations!!! Project Athiri - Sankofa Digital Media");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        
        worldstate = new WorldAppState(cam.getWidth(),cam.getHeight(),24);
        stateManager.attach(worldstate);
        guiNode.attachChild(worldstate.getUINode());
        
    }
    
    public void loadTextureAssets(){
        loadTextures = new Texture[20];
        pMat = new Material[20];
        
        
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
        
        int matId = 0;
        for(Texture tList : loadTextures){
            tList.setMagFilter(Texture.MagFilter.Nearest);
            tList.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            
            pMat[matId] = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            pMat[matId].setTexture("ColorMap", tList);
            pMat[matId].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            matId++;
        }
        
    }

    @Override
    public void simpleUpdate(float tpf){
        
    }

    
    
}
