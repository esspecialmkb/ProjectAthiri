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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class StartScreenAppState extends AbstractAppState{
    String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    BasicApplication app;
    
    // Should 'Final' keyword be used for guiNode, rootNode and Camera?
    Node guiNode;
    Node node;
    // 3D components -> 
    Node rootNode; Camera cam;
    
    AssetManager assetManager;
    InputManager inputManager;
    
    //  Menu-stuff
    Geometry[] buttonsMain;
    BitmapText[] bTextMain;
    BitmapFont uiFont;
    Node[] bNodeMain;
    
    //  State control
    int selection;
    boolean ready;
    AppStateManager stateManager;
    
    public boolean isReady(){ return ready;}
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        selection =-1;
        ready = false;
        
        // Basic setup
        this.app = (BasicApplication) app;
        this.stateManager = stateManager;
        guiNode = this.app.getGuiNode();
        assetManager = this.app.getAssetManager();
        assetManager.registerLocator(directoryName, FileLocator.class);
        inputManager = this.app.getInputManager();
        
        // 3D components
        rootNode = this.app.getRootNode();
        cam = this.app.getCamera();
        
        int scrH = cam.getHeight();
        int scrW = cam.getWidth();
        
        node = new Node("Start");
        uiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        //  Text
        bTextMain = new BitmapText[3];
        bTextMain[0] = new BitmapText(uiFont,false);
        bTextMain[0].setSize(uiFont.getCharSet().getRenderedSize());
        bTextMain[0].setText("-Devfork-");
        bTextMain[0].setLocalTranslation((scrW/2) -(bTextMain[0].getLineWidth() /2), scrH - bTextMain[0].getLineHeight(), 1);

        bTextMain[1] = new BitmapText(uiFont,false);
        bTextMain[1].setSize(uiFont.getCharSet().getRenderedSize() * 4);
        bTextMain[1].setText("PROJECT - Athiri");
        bTextMain[1].setLocalTranslation((scrW/2) -(bTextMain[1].getLineWidth() /2), (scrH/2) - (bTextMain[1].getLineHeight()/2), 1);

        bTextMain[2] = new BitmapText(uiFont,false);
        bTextMain[2].setSize(uiFont.getCharSet().getRenderedSize());
        bTextMain[2].setText("Press [ENTER] to start!");
        bTextMain[2].setLocalTranslation((scrW/2) -(bTextMain[1].getLineWidth() /2), bTextMain[0].getLineHeight(), 1);
        
        node.attachChild(bTextMain[0]);
        node.attachChild(bTextMain[1]);
        node.attachChild(bTextMain[2]);
        guiNode.attachChild(node);
        attachInput();
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
        
        detachInput();
    }
    
    public void startGame(){
        GameRunningAppState gameState = new GameRunningAppState();
        stateManager.attach(gameState);
        node.removeFromParent();
        stateManager.detach(this);
    }
    
    private void attachInput(){
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(actionListener, "Enter");
    }
    private void detachInput(){
        inputManager.deleteMapping("Up");
        inputManager.deleteMapping("Down");
        inputManager.deleteMapping("Enter");
        inputManager.deleteMapping("Click");
    }
    
    class MenuState{
        Geometry[] panels;
        Geometry[] buttons;
        BitmapText[] bText;
        BitmapFont uiFont;
        Node[] bNodeMain;
        
        int width, height, currentBtn;
    }
    
    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            if(name.equals("Enter") && !keyPressed){
                ready = true;
                
                startGame();
            }
        }
    };
    
}
