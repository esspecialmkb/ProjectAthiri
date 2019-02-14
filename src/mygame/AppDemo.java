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
 */
public class AppDemo extends BasicApplication{
    // Version Info
    static private int version_maj = 0;
    static private int version_min = 0;
    static private int version_revision = 1;
    static String version_fork = "App Dev";
    
    public Texture[] loadTextures;
    
    public Material[] pMat;
    
    public WorldAppState worldstate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppDemo app = new AppDemo();
        AppSettings settings = new AppSettings(false);
        //settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri (Debug)- v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
        app.setSettings(settings);
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        
        loadTextureAssets();
        /* A colored lit cube. Needs light source! */ 
        Box boxMesh = new Box(300f,300f,1f); 
        Geometry boxGeo = new Geometry("Colored Box", boxMesh); 
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); 
        boxMat.setColor("Color", ColorRGBA.Gray); 
        boxGeo.setMaterial(boxMat); 
        boxGeo.setLocalTranslation(cam.getWidth()/2, cam.getHeight()/2, -1);
        
        Player p = new Player();
        p.createPlayer();
        
        
        System.out.println("Width " + p.pWidth);
        
        /** We need to tell the guiNode to render and to never cull itself. **/
        p.pNode.setQueueBucket(RenderQueue.Bucket.Gui);
        p.pNode.setCullHint(Spatial.CullHint.Never);
                /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Update Animations!!! Project Athiri - Sankofa Digital Media");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        
        p.pNode.setLocalTranslation(cam.getWidth()/2,cam.getHeight()/2,0);
        p.pNode.addControl(p);
        
        guiNode.attachChild(p.pNode);
        
        //guiNode.attachChild(boxGeo);
        
        worldstate = new WorldAppState(cam.getWidth(),cam.getHeight(),24);
        stateManager.attach(worldstate);
        guiNode.attachChild(worldstate.getUINode());
        
        System.out.println("Player Pos " + p.pNode.getLocalTranslation());
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
    
    public class Player extends AbstractControl{
        public Node pNode;
        private Quad pQuad;
        private Geometry pGeo;
        private Geometry shadowGeo;
        
        private int pWidth = 4;
        private int pHeight = 4;
        private int pCenterX;
        private int pCenterY;
        private int tileSize = 720/24;
        
        private float frameTime = 0;
        private float stepTime = 0.21f;
        
        public void createShadow(){
            Material sMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
            Texture sTex = assetManager.loadTexture("Textures/Player/Shadow.png");
            sTex.setMagFilter(Texture.MagFilter.Nearest);
            sTex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            sMat.setTexture("ColorMap", sTex);
            sMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            
            shadowGeo = new Geometry("Shadow", pQuad);
            shadowGeo.setMaterial(sMat);
            
            pNode.attachChild(shadowGeo);
            shadowGeo.setLocalTranslation(-(pWidth * tileSize)/2,-(tileSize/4),0.5f);
        }
        
        public void createPlayer(){            
            pQuad = new Quad(pWidth * tileSize,pHeight * tileSize);
            pGeo = new Geometry("Player",pQuad);
            pGeo.setMaterial(pMat[0]);
            
            pNode = new Node("Player Node");
            pNode.attachChild(pGeo);
            
            pGeo.setLocalTranslation(-(pWidth * tileSize)/2,-(tileSize/4),1);
            createShadow();
        }

        @Override
        protected void controlUpdate(float tpf) {
            if(frameTime < stepTime){
                pGeo.setMaterial(pMat[11]);
                frameTime += tpf;
            }else if(frameTime < (2 * stepTime)){
                pGeo.setMaterial(pMat[12]);
                frameTime += tpf;
            }else if(frameTime < (3 * stepTime)){
                pGeo.setMaterial(pMat[13]);
                frameTime += tpf;
            }else if(frameTime < (4 * stepTime)){
                pGeo.setMaterial(pMat[14]);
                frameTime += tpf;
            }else{
                frameTime = 0;
            }
            
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            
        }
    }

    @Override
    public void simpleUpdate(float tpf){
        
    }

    
    
}
