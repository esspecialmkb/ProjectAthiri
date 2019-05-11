/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dev_Root;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *  
 *  
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com> 
 */
public class UIState extends AbstractAppState{
    Geometry[] buttonsMain;
    BitmapText[] bTextMain;
    BitmapFont uiFont;
    Node[] bNodeMain;
    
    class MenuState{
        Geometry[] panels;
        Geometry[] buttons;
        BitmapText[] bText;
        BitmapFont uiFont;
        Node[] bNodeMain;
        
        int width, height, currentBtn;
    }
    
    Node gui;

    Material[] mats;

    int width, height, currentBtn;

    InputManager inputManager;

    public UIState(Node gui, int width, int height){
        this.gui = gui;
        this.width = width;
        this.height = height;
        currentBtn = -1;
    }

    private void buildMainMenu(){
        //  Text
        bTextMain = new BitmapText[3];
        bTextMain[0] = new BitmapText(uiFont,false);
        bTextMain[0].setSize(uiFont.getCharSet().getRenderedSize());
        bTextMain[0].setText("START");
        bTextMain[0].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);

        bTextMain[1] = new BitmapText(uiFont,false);
        bTextMain[1].setSize(uiFont.getCharSet().getRenderedSize());
        bTextMain[1].setText("SETTINGS");
        bTextMain[1].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);

        bTextMain[2] = new BitmapText(uiFont,false);
        bTextMain[2].setSize(uiFont.getCharSet().getRenderedSize());
        bTextMain[2].setText("EXIT");
        bTextMain[2].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);

        //  Buttons
        buttonsMain = new Geometry[3];
        buttonsMain[0] = new Geometry("Start", new Quad(width/3,height/10));
        buttonsMain[0].setMaterial(mats[0]);

        buttonsMain[1] = new Geometry("Settings", new Quad(width/3,height/10));
        buttonsMain[1].setMaterial(mats[0]);

        buttonsMain[2] = new Geometry("Exit", new Quad(width/3,height/10));
        buttonsMain[2].setMaterial(mats[0]);

        //  Nodes
        bNodeMain = new Node[4];
        bNodeMain[0] = new Node("Start Node");
        bNodeMain[0].setLocalTranslation(width/2,(height/10) + 5 + (height/2),0);
        bNodeMain[0].attachChild(bTextMain[0]);
        bNodeMain[0].attachChild(buttonsMain[0]);

        bNodeMain[1] = new Node("Settings Node");
        bNodeMain[1].setLocalTranslation(width/2,(height/2),0);
        bNodeMain[1].attachChild(bTextMain[1]);
        bNodeMain[1].attachChild(buttonsMain[1]);

        bNodeMain[2] = new Node("Exit Node");
        bNodeMain[2].setLocalTranslation(width/2,(height/2)-((height/10) + 5),0);
        bNodeMain[2].attachChild(bTextMain[2]);
        bNodeMain[2].attachChild(buttonsMain[2]);

        bNodeMain[3] = new Node("Host Node");
        bNodeMain[3].setLocalTranslation(0,0,0);
        bNodeMain[3].attachChild(bNodeMain[0]);
        bNodeMain[3].attachChild(bNodeMain[1]);
        bNodeMain[3].attachChild(bNodeMain[2]);
    }

    private void checkUI(Vector2f pos){
        //  Button 0
        if(pos.x > width/2 && pos.x < ((width/2) + (width/3)) && pos.y > ((height/10) + 5 + (height/2)) && pos.y < ((height/10) + 5 + (height/2) + (height/10))){
            if(currentBtn != 0 && currentBtn != -1){
                buttonsMain[currentBtn].setMaterial(mats[0]);
            }
            currentBtn = 0;
            buttonsMain[0].setMaterial(mats[1]);
        }else{
            if(currentBtn == 0){
                buttonsMain[currentBtn].setMaterial(mats[0]);
            }
            currentBtn = -1;
        }
    }

    public int getCurrentBtn(){ return currentBtn;}

    @Override
    public void initialize(AppStateManager stateManager, Application app){
        inputManager = app.getInputManager();

        mats = new Material[3];
        mats[0] = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        mats[0].setColor("Color", ColorRGBA.Gray); 
        mats[1] = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        mats[1].setColor("Color", ColorRGBA.Green); 

        uiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        buildMainMenu();
        attachMainMenu();
        attachInput();

    }

    @Override
    public void stateAttached(AppStateManager stateManager) {

    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        detachMainMenu();
        detachInput();
    }

    private void attachMainMenu(){
        gui.attachChild(bNodeMain[3]);
    }
    private void detachMainMenu(){
        bNodeMain[3].removeFromParent();
    }
    private void attachInput(){
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(actionListener, "Click");
    }
    private void detachInput(){
        inputManager.deleteMapping("Up");
        inputManager.deleteMapping("Down");
        inputManager.deleteMapping("Enter");
        inputManager.deleteMapping("Click");
    }

    @Override
    public void update(float tpf) {
        Vector2f cursorPosition = inputManager.getCursorPosition();
        checkUI( cursorPosition );
    }

    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){

        }
    };
}
