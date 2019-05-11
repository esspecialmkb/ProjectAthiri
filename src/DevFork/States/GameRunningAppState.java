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
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *  The GameAppState maintains global game level state.
 *  A separate AppState (WorldManager) will maintain the TileChunks
 *  A 
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class GameRunningAppState extends AbstractAppState{
    String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    BasicApplication app;
    
    // Should 'Final' keyword be used for guiNode, rootNode and Camera?
    Node guiNode;
    // 3D components -> 
    Node rootNode; Camera cam;
    
    AssetManager assetManager;
    AppStateManager stateManager;
    InputManager inputManager;
    
    int gameScreenWidth;
    int gameScreenHeight;
    
    // WorldManager
    WorldManagerState world;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Basic setup
        this.app = (BasicApplication) app;
        this.stateManager = stateManager;
        this.inputManager = this.app.getInputManager();
        guiNode = this.app.getGuiNode();
        assetManager = this.app.getAssetManager();
        assetManager.registerLocator(directoryName, FileLocator.class);
        
        // 3D components
        rootNode = this.app.getRootNode();
        cam = this.app.getCamera();
        
        gameScreenWidth = cam.getWidth();
        gameScreenHeight = cam.getHeight();
        
        // Create the World
        world = new WorldManagerState(guiNode,24);
        
        // Set the world in motion!
        this.stateManager.attach(world);
        attachInput();
    }
    
    @Override
    public void update(float tpf) {
        world.setInputBuffer(action.getInputMask());
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    private void attachInput(){
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        
        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Q", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("E", new KeyTrigger(KeyInput.KEY_E));
        
        inputManager.addMapping("L_SHIFT", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("R_SHIFT", new KeyTrigger(KeyInput.KEY_RSHIFT));
        
        inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_RETURN));
        
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(action, "Up", "Down", "Left", "Right");
    }
    private void detachInput(){
        inputManager.deleteMapping("Up");
        inputManager.deleteMapping("Down");
        inputManager.deleteMapping("Enter");
        inputManager.deleteMapping("Click");
    }
    
    public class Listen implements ActionListener{

        @Override
        public void onAction(String name, boolean keyPressed, float tpf){
            if(name.equals("Enter") && !keyPressed){
                //ready = true;
            }else if(name.equals("Up")){
                inputMask[0] = keyPressed;
            }if(name.equals("Down")){
                inputMask[1] = keyPressed;
            }if(name.equals("Left")){
                inputMask[2] = keyPressed;
            }if(name.equals("Right")){
                inputMask[3] = keyPressed;
            }
        }
        boolean inputMask[] = new boolean[8];
        
        public boolean[] getInputMask(){ return this.inputMask; }
        
    }
    
    Listen action = new Listen();
    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            if(name.equals("Enter") && !keyPressed){
                //ready = true;
            }else if(name.equals("Up")){
                inputMask[0] = keyPressed;
            }if(name.equals("Down")){
                inputMask[1] = keyPressed;
            }if(name.equals("Left")){
                inputMask[2] = keyPressed;
            }if(name.equals("Right")){
                inputMask[3] = keyPressed;
            }
        }
        boolean inputMask[] = new boolean[8];
        
        public boolean[] getInputMask(){ return this.inputMask; }
    };
}
