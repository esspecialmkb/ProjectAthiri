/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package mygame;

import com.jme3.app.Application;
import com.jme3.app.BasicApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;


/**
 *  New AppState based app harness.
 *
 *  @author    Michael A. Bradford
 *  @version 0.1.1 - New in-game UI
 *  version 0.1.0 - Menu
 *  version 0.0.4 - Mobs update
 *  version 0.0.3 - Animation update
 *  version 0.0.2 - Harness completed
 */
public class NewAppDemo extends BasicApplication {
    
    // Version Info
    static private int version_maj = 0;
    static private int version_min = 1;
    static private int version_revision = 1;
    static String version_fork = "Athiri - App System Demo";
    
    public RootNodeState demoState;
    public MainMenuState menu;

    public static void main(String[] args){
        NewAppDemo app = new NewAppDemo();
        AppSettings settings = new AppSettings(false);
        //settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri (InDev)- v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Load previous session data???
        
        System.out.println("Initialize");
        flyCam.setEnabled(false);
        menu = new MainMenuState();
        menu.setRes(cam.getWidth(), cam.getHeight());
        
        stateManager.attach(menu);
        
        demoState = new RootNodeState();
        demoState.setRes(cam.getWidth(), cam.getHeight());
        /*viewPort.attachScene(demoState.getRootNode());
        getGuiViewPort().attachScene(demoState.getGUIRootNode());
        stateManager.attach(demoState);  */      
    }

    @Override
    public void simpleUpdate(float tpf) { 
        /** This code watches the status flags for both states to determine when to change screens. **/
        if(demoState.statusFlag == -1){
            //Go back to menu
            
            stateManager.attach(menu);
            //stateManager.detach(demoState);
            
            guiNode.attachChild(menu.menuNode);
            guiNode.detachChild(demoState.getGUIRootNode());
            demoState.setEnabled(false);
            demoState.statusFlag = 0;
        }if(demoState.statusFlag == -2){
            
            demoState.onPauseMenu();
            demoState.statusFlag = 2;
        }if(demoState.statusFlag == 3){
            demoState.onResumeGame();
            demoState.statusFlag = 0;
        }
        
        if(menu.statusFlag == -1){
            //Start game
            guiNode.detachChild(menu.menuNode);
            stateManager.attach(demoState);
            demoState.setEnabled(true);
            guiNode.attachChild(demoState.getGUIRootNode());
            menu.statusFlag = 0;
        }
    }
    
    @Override
    public void stop(){
        // Save session data???
        
        
        super.stop(); // continue quitting the game
    }
    
    class MainMenuState extends AbstractAppState{
        //private Node rootNode = new Node("Root Node");
        //private Node guiNode = new Node("GUI Node");
        
        public Geometry mainMenu;
        public Geometry gameButton;
        public Geometry optionsButton; 
        public Geometry quitButton;
        
        public Node menuNode;
        public Node buttonsNode;
        public BitmapFont fnt;
        
        /** Screen size definitions. **/
        public int screenUnitSize;
        public int screenWidth;
        public int screenHeight;
        
        /** Materials definitions. **/
        private Material greenMat;
        private Material grayMat;
        private Material redMat;
        
        private int currBtn = 0;
        private int prevBtn = 0;
        private boolean btnChange = false;
        public int statusFlag = 0;
        
        public void setRes(int width, int height){
            this.screenWidth = width;
            this.screenHeight = height;
            this.screenUnitSize = height / 9;
        }
        
        public void buildMainMenu(){
            /** Quads for the menu and button geometries. **/
            Quad menuQuad = new Quad(screenWidth/2, screenUnitSize * 4);
            Quad buttonQuad = new Quad(screenWidth/3, screenUnitSize);
            
            /** Create and position geometries. **/
            mainMenu = new Geometry("Main Menu", menuQuad);
            mainMenu.setLocalTranslation(-menuQuad.getWidth()/2, -menuQuad.getHeight()/2, 0.5f);
            
            gameButton = new Geometry("Game Button", buttonQuad);
            gameButton.setLocalTranslation(-buttonQuad.getWidth()/2, -buttonQuad.getHeight()/2, 0);
            
            optionsButton = new Geometry("Options But   aton", buttonQuad);
            optionsButton.setLocalTranslation(-buttonQuad.getWidth()/2, -buttonQuad.getHeight()/2, 0);
            
            quitButton = new Geometry("Quit Button", buttonQuad);
            quitButton.setLocalTranslation(-buttonQuad.getWidth()/2, -buttonQuad.getHeight()/2, 0);
            
            /** Setup node to maintain menu elements. **/
            menuNode = new Node("Menu Node");
            menuNode.attachChild(mainMenu);
            menuNode.setLocalTranslation(screenWidth/2, screenHeight - (screenUnitSize * 3), -1);
            
            /** The buttonsNode is a group-node for all clickable buttons. **/
            buttonsNode = new Node("Buttons Node");
            
            /** These nodes help us center the button. **/
            Node gameNode = new Node("Game btn");
            Node optionNode = new Node("Option btn");
            Node quitNode = new Node("Quit btn");
            
            gameNode.attachChild(gameButton);
            optionNode.attachChild(optionsButton);
            quitNode.attachChild(quitButton);            
            
            buttonsNode.attachChild(gameNode);
            buttonsNode.attachChild(optionNode);
            buttonsNode.attachChild(quitNode);
            
            /** Setup GUI text elements. **/
            fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText titleTxt = new BitmapText(fnt, false);
            titleTxt.setBox(new Rectangle(0, 0, menuQuad.getWidth(), menuQuad.getHeight()));
            titleTxt.setSize(fnt.getPreferredSize() * 4);
            titleTxt.setText("Eternal Athiri");
            titleTxt.setLocalTranslation(-titleTxt.getLineWidth()/4, 0, 1);
            //titleTxt.setLocalTranslation(0, 0, 1);
            
            BitmapText gameTxt = new BitmapText(fnt, false);
            gameTxt.setBox(new Rectangle(0, 0, buttonQuad.getWidth(), buttonQuad.getHeight()));
            gameTxt.setSize(fnt.getPreferredSize() * 2);
            gameTxt.setText("Game");
            gameTxt.setLocalTranslation(-gameTxt.getLineWidth()/4, 0, 1);
            
            BitmapText optionsTxt = new BitmapText(fnt, false);
            optionsTxt.setBox(new Rectangle(0, 0, buttonQuad.getWidth(), buttonQuad.getHeight()));
            optionsTxt.setSize(fnt.getPreferredSize() * 2);
            optionsTxt.setText("Options");
            optionsTxt.setLocalTranslation(-optionsTxt.getLineWidth()/4, 0, 1);
            
            BitmapText quitTxt = new BitmapText(fnt, false);
            quitTxt.setBox(new Rectangle(0, 0, buttonQuad.getWidth(), buttonQuad.getHeight()));
            quitTxt.setSize(fnt.getPreferredSize() * 2);
            quitTxt.setText("Quit");
            quitTxt.setLocalTranslation(-quitTxt.getLineWidth()/4, 0, 1);
            
            /** Scene graph setup for GUI.**/
            menuNode.attachChild(titleTxt);
            gameNode.attachChild(gameTxt);
            optionNode.attachChild(optionsTxt);
            quitNode.attachChild(quitTxt);
            
            gameNode.setLocalTranslation(0, (screenUnitSize * 1.25f), 0);
            //optionNode.setLocalTranslation(0, -(screenUnitSize * 4), 0);
            quitNode.setLocalTranslation(0, -(screenUnitSize * 1.25f), 0);
            
            menuNode.attachChild(buttonsNode);
            buttonsNode.setLocalTranslation(0, -(screenUnitSize * 4), 1);
            greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            greenMat.setColor("Color", ColorRGBA.Green);
            
            grayMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            grayMat.setColor("Color", ColorRGBA.Gray);
            
            mainMenu.setMaterial(grayMat);
            gameButton.setMaterial(grayMat);
            optionsButton.setMaterial(grayMat);
            quitButton.setMaterial(grayMat);
            
            //Attach to scene-graph
            guiNode.attachChild(menuNode);
        }
        
        @Override
        public void initialize(AppStateManager stateManager, Application app){
            super.initialize(stateManager, app);
            statusFlag = 1;
            
            buildMainMenu();
            
            inputManager.addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            inputManager.addListener(actionListener, "Select");
            //viewPort.attachScene(menuNode);
            //getGuiViewPort().attachScene(menuNode);
        }
        
        @Override
        public void update(float tpf){
            super.update(tpf);
            Vector2f cursorPosition = inputManager.getCursorPosition();
            checkButtons((int)cursorPosition.x,(int)cursorPosition.y);
            menuNode.updateLogicalState(tpf);
            menuNode.updateGeometricState();
        }
        
        @Override
        public void cleanup(){
            inputManager.deleteMapping("Select");
            //inputManager.deleteTrigger("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            
            super.cleanup();
        }
        
        /** This method checks to see if mouse coords are over a button. **/
        public void checkButtons(int x, int y){
            btnChange = false;
            if(x > screenWidth/3 && x < screenWidth - (screenWidth/3)){
                if(y > screenUnitSize * 0.25f && y < screenUnitSize * 1.25f ){
                    if(prevBtn != 3){
                        btnChange = true;
                        prevBtn = currBtn;
                        currBtn = 3;
                    }
                }
                
                if(y > screenUnitSize * 1.5f && y < screenUnitSize * 2.5f ){
                    if(prevBtn != 2){
                        btnChange = true;
                        prevBtn = currBtn;
                        currBtn = 2;
                    }
                }
                
                if(y > screenUnitSize * 2.75f && y < screenUnitSize * 3.75f ){
                    if(prevBtn != 1){
                        btnChange = true;
                        prevBtn = currBtn;
                        currBtn = 1;
                    }
                }
                
                if(btnChange == true){
                    switch(currBtn){
                        case 1:
                            gameButton.setMaterial(greenMat);
                            
                            //gameButton.setMaterial(grayMat);
                            optionsButton.setMaterial(grayMat);
                            quitButton.setMaterial(grayMat);
                            break;
                        case 2:
                            optionsButton.setMaterial(greenMat);
                            
                            gameButton.setMaterial(grayMat);
                            //optionsButton.setMaterial(grayMat);
                            quitButton.setMaterial(grayMat);
                            break;
                        case 3:
                            quitButton.setMaterial(greenMat);
                            
                            gameButton.setMaterial(grayMat);
                            optionsButton.setMaterial(grayMat);
                            //quitButton.setMaterial(grayMat);
                            break;
                    }
                }
            }
        }
        
        private ActionListener actionListener = new ActionListener(){
            public void onAction(String name, boolean keyPressed, float tpf){
                if(name.equals("Select") && !keyPressed){
                    /** Take an action depending on what button is selected. **/
                    switch(currBtn){
                        case 1:
                            //START Game
                            System.out.println("Start game");
                            statusFlag = -1;
                            
                            break;
                        case 2:
                            //OPTIONS Screen
                            System.out.println("Options screen");
                            break;
                        case 3:
                            //QUIT Game
                            System.out.println("Quit the application");
                            break;
                    }
                }
            }
        };
    }
      
}
