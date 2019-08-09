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
    
	// Application-level members
	BasicApplication app;
	AssetManager assetManager;
    InputManager inputManager;
    AppStateManager stateManager;
    
    // Scene-graph members
    Node guiNode;
    Node rootNode; 
	Camera cam;
    
	// General members
    int gameScreenWidth;
    int gameScreenHeight;
	
	// Message system members
	List messageList = new ArrayList<String>;
	
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
	
	// Prototype message system
	public void postMessage(String message){
		// Add message to list
		messageList.add(message);
	}
	
	public void checkMessages(){
		if(messageList.size() > 0){
			// Loop through messages and remove messages that were worked on
			// We could either look at all messages or a certain number
		}
	}
	
    InputListener inputListener = new InputListener();
	public class InputListener implements ActionListener{

        @Override
        public void onAction(String name, boolean keyPressed, float tpf){
            
        }
        boolean inputMask[] = new boolean[8];
        
        public boolean[] getInputMask(){ return this.inputMask; }
		
		public void registerInputMappings(){
			//Add custom input mappings here using inputManager
		}
		public void removeInputMappings(){
			//Remove custom input mappings here
		}
        
    }
    
//	private ActionListener actionListener = new ActionListener(){
//	    public void onAction(String name, boolean keyPressed, float tpf){
//		
//	    }
//	};
}
