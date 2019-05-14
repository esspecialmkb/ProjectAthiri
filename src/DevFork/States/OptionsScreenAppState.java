/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.States;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.system.AppSettings;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class OptionsScreenAppState extends AbstractAppState{
    // Constants use public static final
    public static class Options{
        private Options(){}
        public static int screenW;
        public static int screenH;
        public static float tileSizeDefault;
    }
    
    public OptionsScreenAppState(AppSettings settings){
        settings.getBitsPerPixel();
        settings.getDepthBits();
        settings.getHeight();
        settings.getWidth();
        settings.getRenderer();
        
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
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
    
}
