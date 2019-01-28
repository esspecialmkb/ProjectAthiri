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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;


/**
 *  New AppState based app harness.
 *
 *  @author    Michael A. Bradford
 *  @version 0.0.4 - Mobs update
 *  version 0.0.3 - Animation update
 *  version 0.0.2 - Harness completed
 */
public class NewAppDemo extends BasicApplication {
    
    // Version Info
    static private int version_maj = 0;
    static private int version_min = 0;
    static private int version_revision = 4;
    static String version_fork = "App System Demo";
    
    public RootNodeState demoState;

    public static void main(String[] args){
        NewAppDemo app = new NewAppDemo();
        AppSettings settings = new AppSettings(false);
        //settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri - v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        System.out.println("Initialize");
        flyCam.setEnabled(false);
        demoState = new RootNodeState();
        demoState.setRes(cam.getWidth(), cam.getHeight());
        viewPort.attachScene(demoState.getRootNode());
        getGuiViewPort().attachScene(demoState.getGUIRootNode());
        stateManager.attach(demoState);        
    }

    @Override
    public void simpleUpdate(float tpf) {  
        
    }
    
    class Game extends AbstractAppState{
        //private Node rootNode = new Node("Root Node");
        //private Node guiNode = new Node("GUI Node");
        
        public Geometry mainMenu;
        public int screenUnitSize;
        
        @Override
        public void initialize(AppStateManager stateManager, Application app){
            super.initialize(stateManager, app);
            
        }
        
        @Override
        public void update(float tpf){
            
        }
    }
      
}
