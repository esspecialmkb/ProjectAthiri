/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAppStates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

/**
 *  The intention of the TemplateAppState is to encapsulate common elements used in all app states for this project!
 * 
 * @author esspe
 */
public class TemplateAppState extends AbstractAppState{
    /**
     * <code>initialized</code> is set to true when the method
     * {@link AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) }
     * is called. When {@link AbstractAppState#cleanup() } is called, <code>initialized</code>
     * is set back to false.
     */
    protected boolean initialized = false;
    private boolean enabled = true;

    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void stateAttached(AppStateManager stateManager) {
    }

    public void stateDetached(AppStateManager stateManager) {
    }

    public void update(float tpf) {
    }

    public void render(RenderManager rm) {
    }

    public void postRender(){
    }

    public void cleanup() {
        initialized = false;
    }

    
}
