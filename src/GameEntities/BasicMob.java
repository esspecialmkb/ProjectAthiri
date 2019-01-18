/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEntities;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author esspe
 */
public class BasicMob {
    //private Quad mobQuad;
    private Geometry mobGeo;
    private Node mobNode;

    /** We need to track world coords. **/
    private float mobWorldX;
    private float mobWorldY;
    private float vX;
    private float vY;
    public float health;
    
    private float tileScreenSize;
    
    private Material[][] mobMatList;
    
    public BasicMob(Material[][] mobMatList){
        this.mobMatList = mobMatList;
    }
    
    /** Create mob. **/
    public void createMob(){
        // We should probably clone the required geometry
        mobGeo = new Geometry("Mob - Zombie", new Quad(2 * tileScreenSize, 3 * tileScreenSize));
        mobGeo.setLocalTranslation(-tileScreenSize,0,0);
        /** Giving the mob a Node might help with Controls later... **/
        mobNode = new Node("Zombie Node");

        /** Set the proper Material to the Geometry and attach to guiNode. **/
        mobGeo.setMaterial(mobMatList[0][0]);
        // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
        // v3 Addition, set the player position members
        mobWorldX = (vX * tileScreenSize);
        mobWorldY = (vY * tileScreenSize);
        mobNode.attachChild(mobGeo);
        mobNode.setLocalTranslation(mobWorldX, mobWorldY, 0);
    }

    public void destroyMob(){
        mobGeo.removeFromParent();
        mobGeo.updateGeometricState();
    }
}
