/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *  The UIState will maintain the in-game HUD
 *  Will be used soley by RootNodeState or any other in-game state
 * 
 *  We might toggle HUD and pause screen here
 *
 * @author esspe 
 */
public class UIState extends AbstractAppState {
    
    public Geometry[] healthGeo;
    public Node healthNode;
    public Material[] matHealth;
    
    public Geometry[] menuGeo;
    public Geometry[] menuBtns;
    public Node menuNode;
    public Node btnNode;
    public Material[] matMenu;
    
    public BitmapFont fnt;
    
    public AssetManager assetManager;
    public InputManager inputManager;
    
    public int scrnWidth;
    public int scrnHeight;
    public int scrnUnit;
    
    public int currScrn = 0;
    
    public int currBtn = 0;
    private int prevBtn = 0;
    private boolean btnChange = false;
    public int statusFlag = 0;
    
    public UIState(Material[][] matList, int scrnX, int scrnY){
        matHealth = new Material[6];
        
        matHealth[0] = matList[0][5];
        matHealth[1] = matList[0][6];
        matHealth[2] = matList[7][0];
        matHealth[3] = matList[1][5];
        matHealth[4] = matList[1][6];
        matHealth[5] = matList[1][7];
        
        scrnWidth = scrnX;
        scrnHeight = scrnY;
        scrnUnit = scrnY / 15;
        healthNode = new Node("UI_Health");
        menuNode = new Node("UI_Menu");
        btnNode = new Node("UI_Btns");
    }
    
    public void buildUI(){
        Quad healthQuad = new Quad(scrnUnit,scrnUnit); 
        
        healthGeo = new Geometry[3];
        healthGeo[0] = new Geometry("Heart 1", healthQuad);
        healthGeo[1] = new Geometry("Heart 2", healthQuad);
        healthGeo[2] = new Geometry("Heart 3", healthQuad);
        
        healthGeo[0].setMaterial(matHealth[2]);
        healthGeo[1].setMaterial(matHealth[2]);
        healthGeo[2].setMaterial(matHealth[2]);
        
        healthNode.attachChild(healthGeo[0]);
        healthNode.attachChild(healthGeo[1]);
        healthNode.attachChild(healthGeo[2]);
        
        healthGeo[0].setLocalTranslation(-scrnUnit,-scrnUnit/2,1);
        healthGeo[1].setLocalTranslation(0,-scrnUnit/2,1);
        healthGeo[2].setLocalTranslation(scrnUnit,-scrnUnit/2,1);
        
        healthNode.setLocalTranslation(scrnWidth/2,scrnUnit,0);
    }
    
    public void buildMenu(){
        fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        Quad screenQuad = new Quad(scrnWidth,scrnHeight);
        Quad menuQuad = new Quad(scrnUnit * 12,scrnUnit * 7);
        Quad menuBtn = new Quad(scrnUnit * 6,scrnUnit);
        
        menuGeo = new Geometry[2];
        menuGeo[0] = new Geometry("ScreenCover",screenQuad);
        menuGeo[1] = new Geometry("MenuBackground",menuQuad);
        
        menuBtns = new Geometry[3];
        menuBtns[0] = new Geometry("Resume Btn",menuBtn);
        menuBtns[1] = new Geometry("Reset Btn",menuBtn);
        menuBtns[2] = new Geometry("Exit Btn",menuBtn);
        
        /** Setup materials. **/
        matMenu = new Material[4];
        
        matMenu[0] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matMenu[0].setColor("Color", ColorRGBA.Black);
        
        matMenu[1] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matMenu[1].setColor("Color", ColorRGBA.Green);
        
        matMenu[2] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matMenu[2].setColor("Color", ColorRGBA.Gray);
        
        matMenu[3] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matMenu[3].setColor("Color", new ColorRGBA(0,0,0,0.8f));
        
        menuGeo[0].setMaterial(matMenu[3]);
        menuGeo[1].setMaterial(matMenu[0]);
        
        menuBtns[0].setMaterial(matMenu[2]);
        menuBtns[1].setMaterial(matMenu[2]);
        menuBtns[2].setMaterial(matMenu[2]);
        
        menuNode.attachChild(menuGeo[0]);
        menuNode.attachChild(menuGeo[1]);
        
        menuNode.setLocalTranslation(scrnWidth/2,scrnHeight/2,0);
        menuGeo[0].setLocalTranslation(-scrnWidth/2,-scrnHeight/2,0);
        menuGeo[1].setLocalTranslation(-scrnUnit * 6,-scrnUnit * 3,1);
        
        menuNode.attachChild(btnNode);
        btnNode.attachChild(menuBtns[0]);
        btnNode.attachChild(menuBtns[1]);
        btnNode.attachChild(menuBtns[2]);
        
        btnNode.setLocalTranslation(0,0,2);
        menuBtns[0].setLocalTranslation(-scrnUnit * 3,scrnUnit * 1.75f,0);
        menuBtns[1].setLocalTranslation(-scrnUnit * 3,0,0);
        menuBtns[2].setLocalTranslation(-scrnUnit * 3,-scrnUnit * 1.75f,0);
        
        BitmapText resumeTxt = new BitmapText(fnt, false);
        resumeTxt.setBox(new Rectangle(0, 0, menuBtn.getWidth(), menuBtn.getHeight()));
        resumeTxt.setSize(fnt.getPreferredSize());
        resumeTxt.setText("Resume");
        resumeTxt.setLocalTranslation(-resumeTxt.getLineWidth()/4, (scrnUnit/2) + scrnUnit * 1.75f, 1);
        btnNode.attachChild(resumeTxt);
        
        BitmapText resetTxt = new BitmapText(fnt, false);
        resetTxt.setBox(new Rectangle(0, 0, menuBtn.getWidth(), menuBtn.getHeight()));
        resetTxt.setSize(fnt.getPreferredSize());
        resetTxt.setText("Reset Map");
        resetTxt.setLocalTranslation(-resetTxt.getLineWidth()/4, (scrnUnit/2), 1);
        btnNode.attachChild(resetTxt);
        
        BitmapText quitTxt = new BitmapText(fnt, false);
        quitTxt.setBox(new Rectangle(0, 0, menuBtn.getWidth(), menuBtn.getHeight()));
        quitTxt.setSize(fnt.getPreferredSize());
        quitTxt.setText("Quit");
        quitTxt.setLocalTranslation(-quitTxt.getLineWidth()/4, (-scrnUnit * 1.75f) + (scrnUnit/2), 1);
        btnNode.attachChild(quitTxt);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode  
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        buildUI();
        buildMenu();
    }
    
    @Override
    public void cleanup() {
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }
        
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime    
        if(this.currScrn == 1){
            Vector2f cursorPosition = inputManager.getCursorPosition();
            checkButtons((int)cursorPosition.x, (int)cursorPosition.y);
        }
    }
    
    /** This method checks to see if mouse coords are over a button. **/
    public void checkButtons(int x, int y){
        btnChange = false;
        if(x > ((scrnWidth/2)-(scrnUnit * 3)) && x < ((scrnWidth/2 )+ (scrnUnit * 3)) ){
            if(y > ((scrnHeight/2) - (scrnUnit * 2.5f)) && y < ((scrnHeight/2) - (scrnUnit * 1.5f)) ){
                if(prevBtn != 3){
                    btnChange = true;
                    prevBtn = currBtn;
                    currBtn = 3;
                }
            }

            if(y > ((scrnHeight/2) - (scrnUnit)) && y < ((scrnHeight/2) + (scrnUnit)) ){
                if(prevBtn != 2){
                    btnChange = true;
                    prevBtn = currBtn;
                    currBtn = 2;
                }
            }

            if(y > ((scrnHeight/2) + (scrnUnit * 1.5f)) && y < ((scrnHeight/2) + (scrnUnit * 2.5f)) ){
                if(prevBtn != 1){
                    btnChange = true;
                    prevBtn = currBtn;
                    currBtn = 1;
                }
            }

            if(btnChange == true){
                switch(currBtn){
                    case 1:
                        menuBtns[0].setMaterial(matMenu[1]);

                        //menuBtns[0].setMaterial(matMenu[2]);
                        menuBtns[1].setMaterial(matMenu[2]);
                        menuBtns[2].setMaterial(matMenu[2]);
                        break;
                    case 2:
                        menuBtns[1].setMaterial(matMenu[1]);

                        menuBtns[0].setMaterial(matMenu[2]);
                        //menuBtns[1].setMaterial(matMenu[2]);
                        menuBtns[2].setMaterial(matMenu[2]);
                        break;
                    case 3:
                        menuBtns[2].setMaterial(matMenu[1]);

                        menuBtns[0].setMaterial(matMenu[2]);
                        menuBtns[1].setMaterial(matMenu[2]);
                        //menuBtns[2].setMaterial(matMenu[2]);
                        break;
                }
            }
        }
    }
    
}
