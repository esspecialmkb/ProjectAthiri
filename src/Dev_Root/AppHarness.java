/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dev_Root;


// TODO: Chunks generate single-value when feature size is used
// FIX: Vertex indexes need to be relative
// FIX: GameState map not attaching to scene graph properly
// FIX: UIScreen freeze when selecting game 
// FIX: Fix missing chunk material -> Solved

import DevFork.States.GameRunningAppState;
import DevFork.States.OptionsScreenAppState;
import DevFork.States.StartScreenAppState;
import com.jme3.app.BasicApplication;
import com.jme3.system.AppSettings;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class AppHarness extends BasicApplication{
    UIState ui;
    GameState game;
    
    //  2nd Gen AppStates
    StartScreenAppState startState;
    OptionsScreenAppState optionsState;
    GameRunningAppState gameState;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AppHarness app = new AppHarness();
        AppSettings settings = new AppSettings(false);
        
        settings.setFrameRate(60);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        
        /*ui = new UIState(guiNode,cam.getWidth(),cam.getHeight());
        stateManager.attach(ui);
        game = new GameState(guiNode);*/
        
        // 2nd Gen
        startState = new StartScreenAppState();
        gameState = new GameRunningAppState();
        
        stateManager.attach(startState);
    }
    
    @Override
    public void simpleUpdate(float tpf){
        boolean hasUIState = stateManager.hasState(ui);
        boolean hasGameState = stateManager.hasState(game);
        boolean hasStartState = stateManager.hasState(startState);
        if(hasUIState){
            switch(ui.getCurrentBtn()){
                case 0:
                    stateManager.detach(ui);
                    setDisplayStatView(false);
                    stateManager.attach(game);
                default:
                    //
            }
        }else if(hasStartState){
            if(startState.isReady()){
                stateManager.detach(ui);
                setDisplayStatView(false);
                //stateManager.attach(gameState);
            }
            
        }if(hasGameState){
            //  Global level game state, listen for in-game UI and exit events
        }if(stateManager.hasState(gameState)){
            
        }
    }
    
 
}
