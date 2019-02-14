/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAppStates;

import mygame.*;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;

/**
 *  
 *  
 *
 * @author esspe 
 */
public class BlankAppState extends AbstractAppState {
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode    
    }
    
    @Override
    public void cleanup() {
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }
        
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime    
    }
    
}
