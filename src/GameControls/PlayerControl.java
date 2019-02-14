/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameControls;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.io.IOException;

/**
 *
 * @author esspe
 */
public class PlayerControl extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    
    public Node pNode;
    private Quad pQuad;
    private Geometry pGeo;
    private Geometry shadowGeo;
    
    public Material[][] playerMat;
    public Material sMat;

    private int pWidth = 4;
    private int pHeight = 4;
    private int pCenterX;
    private int pCenterY;
    private int tileSize = 720/24;

    private float frameTime = 0;
    private float stepTime = 0.21f;
    private int moveMask = 0,dirMask = 1; // No movement, SouthDir
    private int frame=0;
    
    public void setTileSize(int newSize){this.tileSize = newSize;}
    public void setMovementMask(int mask){this.moveMask = mask;}

    public void createShadow(){

        shadowGeo = new Geometry("Shadow", pQuad);
        shadowGeo.setMaterial(sMat);

        pNode.attachChild(shadowGeo);
        shadowGeo.setLocalTranslation(-(pWidth * tileSize)/2,-(tileSize/4),0.5f);
    }

    public void createPlayer(){            
        pQuad = new Quad(pWidth * tileSize,pHeight * tileSize);
        pGeo = new Geometry("Player",pQuad);
        pGeo.setMaterial(playerMat[0][0]);

        pNode = new Node("Player Node");
        pNode.attachChild(pGeo);

        pGeo.setLocalTranslation(-(pWidth * tileSize)/2,-(tileSize/4),1);
        createShadow();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(frameTime < stepTime){
            pGeo.setMaterial(playerMat[1][0]);
            frameTime += tpf;
        }else if(frameTime < (2 * stepTime)){
            pGeo.setMaterial(playerMat[2][0]);
            frameTime += tpf;
        }else if(frameTime < (3 * stepTime)){
            pGeo.setMaterial(playerMat[3][0]);
            frameTime += tpf;
        }else if(frameTime < (4 * stepTime)){
            pGeo.setMaterial(playerMat[4][0]);
            frameTime += tpf;
        }else{
            frameTime = 0;
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
    
    public Control cloneForSpatial(Spatial spatial) {
        PlayerControl control = new PlayerControl();
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
