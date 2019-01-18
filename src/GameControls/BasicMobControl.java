/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameControls;

import GameEntities.BasicMob;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import mygame.RootNodeState;

/**
 *
 * @author esspe
 */
public class BasicMobControl extends AbstractControl {
    private Vector2f mobPosTile = new Vector2f();
    private Vector2f mobPosScreen = new Vector2f();
    private Vector2f playerPos = new Vector2f();
    private float tileScreenSize;
    private float deltaX = 0.0f;
    private float deltaY = 0.0f;
    private float walkSpeed = 3.5f; // Faster movement on screen
    private int id;
    private Node controlNode;
    //private BasicMob mob;

    private boolean nearPlayer = false;
    private boolean hitByPlayer = false;
    private boolean hitDebounce = false;
    private float damageThisFrame = 0;

    private float health = 10f;
    private float hitTime = 1.8f;
    private float hitTimeLeft = 1.8f;
    private float debounceTime = 0.4f;
    private float debounceTimeLeft = 0.4f;
    
    public void setTileSize(float newSize){ tileScreenSize = newSize;}    
    public void preUpdate(float pX, float pY){ playerPos.set(pX,pY);}

    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
        Vector3f localTranslation = spatial.getLocalTranslation();
        mobPosScreen.set(localTranslation.x,localTranslation.y);
        
        Vector2f diff = playerPos.subtract(mobPosScreen);
        if((diff.length() < (10 * tileScreenSize) && diff.length() > (1.5f * tileScreenSize))|| (diff.length() > -(10 * tileScreenSize) && diff.length() < (-1.5f * tileScreenSize))){
            Vector2f nDiff = diff.normalize();
            
            if(hitByPlayer == true){
                
            }
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        BasicMobControl control = new BasicMobControl();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
    
}
