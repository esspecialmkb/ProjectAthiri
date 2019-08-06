/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.States;

import com.jme3.app.Application;
import com.jme3.app.BasicApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class BlankAppState extends AbstractAppState{
    String directoryName;
    BasicApplication app;
    
    // Should 'Final' keyword be used for guiNode, rootNode and Camera?
    Node guiNode;
    // 3D components -> 
    Node rootNode; Camera cam;
    
    AssetManager assetManager;
    InputManager inputManager;
    AppStateManager stateManager;
    
    int gameScreenWidth;
    int gameScreenHeight;
	
	public BlankAppState(String directory){
		this.directoryName = directory;
	}
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Basic setup
        this.app = (BasicApplication) app;
        
        guiNode = this.app.getGuiNode();
        assetManager = this.app.getAssetManager();
        assetManager.registerLocator(directoryName, FileLocator.class);
        inputManager = this.app.getInputManager();
        
        // 3D components
        rootNode = this.app.getRootNode();
        cam = this.app.getCamera();
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
	
	InputListener inputListener = new InputListener();
	public class InputListener implements ActionListener{

        @Override
        public void onAction(String name, boolean keyPressed, float tpf){
            
        }
        boolean inputMask[] = new boolean[8];
        
        public boolean[] getInputMask(){ return this.inputMask; }
        
    }
    
//	private ActionListener actionListener = new ActionListener(){
//	    public void onAction(String name, boolean keyPressed, float tpf){
//		
//	    }
//	};
}
