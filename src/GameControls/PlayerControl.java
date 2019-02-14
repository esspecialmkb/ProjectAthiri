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
    private int frame=0,animID=0;
    
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
    
    /** Parse the movement mask and determine best animation to play or continue playing. **/
    public void updateMoveMask(float tpf){
        switch (moveMask) {
        //No movement
            case 0:
                updateAnimation(0, tpf);
                break;
        //Up
            case 1:
                updateAnimation(5, tpf);
                break;
        //Down
            case 2:
                updateAnimation(4, tpf);
                break;
        //Up + Down = OPPOSITE STATE
            case 3:
                break;
        //Left
            case 4:
                updateAnimation(7, tpf);
                break;
        //Up + Left
            case 5:
                break;
        //Down + Left
            case 6:
                break;
        //Up + Down + Left = BAD STATE
            case 7:
                break;
        //Right
            case 8:
                updateAnimation(6, tpf);
                break;
        //Up + Right
            case 9:
                break;
        //Down + Right
            case 10:
                break;
        //Up + Down + Right = BAD STATE
            case 11:
                break;
        //Left + Right = OPPOSITE STATE
            case 12:
                break;
        //Up + Left + Right = BAD STATE
            case 13:
                break;
        //Down + Left + Right = BAD STATE
            case 14:
                break;
        //Up + Down + Left + Right = BAD STATE
            case 15:
                break;
            default:
                break;
        }
    }
    public void updateAnimation(int id, float tpf){
        if(id == animID){
            // Continue current Animation
            // No animations (yet) for standing
            switch(animID){
                case 0:
                    /** Stand south. **/
                    break;
                case 1:
                    /** Stand north. **/
                    break;
                case 2:
                    /** Stand east. **/
                    break;
                case 3:
                    /** Stand west. **/
                    break;
                case 4:
                    /** Run south. **/
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
                    break;
                case 5:
                    /** Run north. **/
                    if(frameTime < stepTime){
                        pGeo.setMaterial(playerMat[1][1]);
                        frameTime += tpf;
                    }else if(frameTime < (2 * stepTime)){
                        pGeo.setMaterial(playerMat[2][1]);
                        frameTime += tpf;
                    }else if(frameTime < (3 * stepTime)){
                        pGeo.setMaterial(playerMat[3][1]);
                        frameTime += tpf;
                    }else if(frameTime < (4 * stepTime)){
                        pGeo.setMaterial(playerMat[4][1]);
                        frameTime += tpf;
                    }else{
                        frameTime = 0;
                    }
                    break;
                case 6:
                    /** Run east. **/
                    if(frameTime < stepTime){
                        pGeo.setMaterial(playerMat[1][2]);
                        frameTime += tpf;
                    }else if(frameTime < (2 * stepTime)){
                        pGeo.setMaterial(playerMat[2][2]);
                        frameTime += tpf;
                    }else if(frameTime < (3 * stepTime)){
                        pGeo.setMaterial(playerMat[3][2]);
                        frameTime += tpf;
                    }else if(frameTime < (4 * stepTime)){
                        pGeo.setMaterial(playerMat[4][2]);
                        frameTime += tpf;
                    }else{
                        frameTime = 0;
                    }
                    break;
                case 7:
                    /** Run west. **/
                    if(frameTime < stepTime){
                        pGeo.setMaterial(playerMat[1][3]);
                        frameTime += tpf;
                    }else if(frameTime < (2 * stepTime)){
                        pGeo.setMaterial(playerMat[2][3]);
                        frameTime += tpf;
                    }else if(frameTime < (3 * stepTime)){
                        pGeo.setMaterial(playerMat[3][3]);
                        frameTime += tpf;
                    }else if(frameTime < (4 * stepTime)){
                        pGeo.setMaterial(playerMat[4][3]);
                        frameTime += tpf;
                    }else{
                        frameTime = 0;
                    }
                    break;
                /** Basic attack frame. **/
                case 8:
                    /** Attack south. **/
                    break;
                case 9:
                    /** Attack north. **/
                    break;
                case 10:
                    /** Attack east. **/
                    break;
                case 11:
                    /** Attack west. **/
                    break;
                default:
                    break;
            }
        }else{
            // Start new Animation
            switch(id){
                case 0:
                    /** Stand south. **/
                    pGeo.setMaterial(playerMat[0][0]);
                    frameTime = 0;
                    break;
                case 1:
                    /** Stand north. **/
                    pGeo.setMaterial(playerMat[0][1]);
                    frameTime = 0;
                    break;
                case 2:
                    /** Stand east. **/
                    pGeo.setMaterial(playerMat[0][2]);
                    frameTime = 0;
                    break;
                case 3:
                    /** Stand west. **/
                    pGeo.setMaterial(playerMat[0][3]);
                    frameTime = 0;
                    break;
                case 4:
                    /** Run south. **/
                    pGeo.setMaterial(playerMat[1][0]);
                    frameTime = 0;
                    break;
                case 5:
                    /** Run north. **/
                    pGeo.setMaterial(playerMat[1][1]);
                    frameTime = 0;
                    break;
                case 6:
                    /** Run east. **/
                    pGeo.setMaterial(playerMat[1][2]);
                    frameTime = 0;
                    break;
                case 7:
                    /** Run west. **/
                    pGeo.setMaterial(playerMat[1][3]);
                    frameTime = 0;
                    break;
                case 8:
                    /** Attack south. **/
                    break;
                case 9:
                    /** Attack north. **/
                    break;
                case 10:
                    /** Attack east. **/
                    break;
                case 11:
                    /** Attack west. **/
                    break;
                default:
                    break;
            }
            animID = id;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        updateMoveMask(tpf);

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
