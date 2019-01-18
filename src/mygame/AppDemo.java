/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.AbstractApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

/**
 *
 * @author Michael A. Bradford
 */
public class AppDemo extends AbstractApplication{
    // Version Info
    static private int version_maj = 0;
    static private int version_min = 0;
    static private int version_revision = 1;
    static String version_fork = "App Demo";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AppDemo app = new AppDemo();
        
        
        
        app.start();
    }
    
    @Override
    public void start(JmeContext.Type contextType){
        AppSettings settings = new AppSettings(true);
        //settings.setResolution(1024, 768);
        settings.setFrameRate(60);
        settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri - v." + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
        setSettings(settings);

        super.start(contextType);
    }

    @Override
    public void initialize(){
        super.initialize();
        
        System.out.println("Initialize");

        RootNodeState state = new RootNodeState();
        viewPort.attachScene(state.getRootNode());
        getGuiViewPort().attachScene(state.getGUIRootNode());
        stateManager.attach(state);
        
    }

    @Override
    public void update(){
        super.update();

        // do some animation
        float tpf = timer.getTimePerFrame();

        stateManager.update(tpf);
        stateManager.render(renderManager);

        // render the viewports
        renderManager.render(tpf, context.isRenderable());
    }

    @Override
    public void destroy(){
        super.destroy();

        System.out.println("Destroy");
    }
    
}
