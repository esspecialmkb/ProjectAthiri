/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package mygame;

import Cartography.TileMap;
import Utility.Materials;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
/**
 *  AppState based app harness.
 *
 *  @author    Michael A. Bradford
 *  @version 0.1.0 - Steering Behaviors and AbstractControls. 
 *  version 0.0.1 - Combat animation
 *  version 0.0.0 - TileMap Demo refactor completed
 */
public class RootNodeState extends AbstractAppState {
    // Version Info
    static private int version_maj = 0;
    static private int version_min = 1;
    static private int version_revision = 2;
    static String version_fork = "App System Demo";
    
    /** Keep track of the objects vital to the scene graph root. **/
    public AssetManager assetManager;
    public InputManager inputManager;
    //public Node guiNode;
    
    Material debugPosMat;

    /** Textures for tiles and player. **/
    private Texture tileTexture;
    private Texture playerTexture, mobTexture;  // New mob Texture data
    /** Grab the image data from the textures. **/
    private Image tileImage;
    private Image playerImage, mobImage;        // New Mob Image data
    /** Having the image data allows us to split the image into separate textures. **/
    private Texture2D[][] tileTextureList;
    private Image[][] tileImageList;
    private Material[][] tileMatList;
    private ImageRaster tileRaster;
    /** Data definitions for tile images. **/
    private int imgWidth;
    private int imgHeight;
    private int depth;
    /** Setup player sprite data. New Mob stuff. **/
    private Texture2D[][] playerTextureList, mobTextureList, weaponTextureList;
    private Image[][] playerImageList, mobImageList, weaponImageList;
    private Material[][] playerMatList, mobMatList, weaponMatList;
    private ImageRaster playerRaster, mobRaster, weaponRaster;
    
    /** Data defs for player sprites, uses same depth as tiles * 2. **/
    private int playerWidth, mobWidth;
    private int playerHeight, mobHeight;
    
    /** NEW STUFF. **/
    //Map member needed for movement updates
    private TileMap map;
    //Player position members
    private float player_Scrn_X;
    private float player_Scrn_Y;
    private float playerWorldX; // Local coords
    private float playerWorldY;
    //Player movement members
    private float walkSpeed = 3.5f; // Faster movement on screen
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private int facing = 0; // 1 - South, 2 - North, 4 - West, 8 - East
    /** Player scene-graph data. **/
    private Quad playerQuad;
    private Geometry playerGeo;
    private Player playerObj;
    
    /** The player class will be used to encapsulate player data. 
     *  Still need to refactor animation into player class
     **/
    public class Player{
        /** Player scene-stuff. **/
        private Quad playerQuad;
        private Geometry playerGeo;
        private Node playerNode;
        
        /** Weapon scene-stuff. **/
        private Geometry weaponGeo;
        private Node weaponNode;
        private Node weaponPivot;
        
        /** Animation data. **/
        private float frameSpeed = 0.3f; // Adjusting the speed slower
        private float frameTime = 0.0f;
        private int currentFrame = 0;
        private int facing = 0;

        /** Movement update data **/
        private float deltaX = 0.0f;
        private float deltaY = 0.0f;
        private float walkSpeed = 3.5f; // Faster movement on screen
        private Vector2f playerPosTile;
        private Vector2f playerPosScreen;
        
        /** Input bit-field. **/
        private boolean attack = false;
        //private boolean up = false;
        //private boolean down = false;
        //private boolean left = false;
        //private boolean right = false; 
        
        /** Attack timing information. **/
        private boolean preAttack = false;
        private boolean isAttacking = false;
        private float attackPreTime = 0.05f;
        private float attackPreTimeLeft = 0.05f;
        private float attackTime = 0.1f;
        private float attackTimeLeft = 0.1f;
        
        public Vector3f getPos(){return new Vector3f(playerPosScreen.x,playerPosScreen.y,0);}
        public Vector2f getTilePos(){return new Vector2f(playerPosScreen.x / tileScreenSize,playerPosScreen.y / tileScreenSize);}
        
        public Player(){
            
        }
        
        /** Places player in the world. **/
        public void createPlayer(){
            // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
            playerGeo = new Geometry("Player", new Quad((playerWidth / 16) * tileScreenSize, (playerHeight / 16) * tileScreenSize));

            /** Data members for the player's weapon. **/
            weaponGeo = new Geometry("Player Weapon", new Quad((playerWidth / 16) * tileScreenSize, (playerHeight / 16) * tileScreenSize));

            /** Set the proper Material to the Geometry and attach to guiNode. **/
            playerGeo.setMaterial(playerMatList[0][0]);
            weaponGeo.setMaterial(weaponMatList[0][0]);
            // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
            // v3 Addition, set the player position members
            player_Scrn_X = (screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2);
            player_Scrn_Y = (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2);
            
            playerNode = new Node("Player Node");
            weaponNode = new Node("Weapon Node");
            weaponPivot = new Node("Weapon Pivot");
            
            /** We're going to use a node to keep track of the player. **/
            playerNode.setLocalTranslation((screenWidth / 2), (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2), 0);
            
            playerPosScreen = new Vector2f((screenWidth / 2), (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2));
            playerPosTile = new Vector2f(playerPosScreen.x / tileScreenSize , playerPosScreen.y / tileScreenSize);
            playerNode.attachChild(playerGeo);
            playerGeo.setLocalTranslation(-tileScreenSize,0,0);
            
            /** The weapon's local offset will now be easier to calculate, even if we let the player leave the center of the screen. **/
            weaponPivot.attachChild(weaponGeo);
            weaponNode.attachChild(weaponPivot);
            
            weaponNode.setLocalTranslation(-tileScreenSize/2,(2 * tileScreenSize) / 2,0);
            weaponGeo.setLocalTranslation(-tileScreenSize,0,0);
            
            playerNode.attachChild(weaponNode);
            guiNode.attachChild(playerNode);
        }
        
        /** Update player frame animation and movement. **/
        public void update(float tpf){
            //System.out.println(playerPosScreen);
            deltaX = 0.0f;
            deltaY = 0.0f;

            frameTime += tpf;
            /** When the player presses any of the movement keys, play the animation. **/
            // For now, no real movement, just making sure the right frames play for the right direction
            // v3 move the player (X and Y ) according to the animation that's played
            if(down){
                facing = 0;
                //Move the player DOWN
                deltaY -= (walkSpeed * tileScreenSize);
                
                // Simulate walk cycle - south
                if(frameTime > frameSpeed){
                    frameTime -= frameSpeed;
                    
                    //weaponGeo.setLocalTranslation(-(tileScreenSize / 2),(2 * tileScreenSize) / 2,0);
                    //Increment to next frame  
                    switch(currentFrame){
                        case 0:
                            currentFrame = 1;
                            playerGeo.setMaterial(playerMatList[0][1]);
                            break;
                        case 1:
                            currentFrame = 2;
                            playerGeo.setMaterial(playerMatList[0][0]);
                            break;
                        case 2:
                            currentFrame = 3;
                            playerGeo.setMaterial(playerMatList[0][2]);
                            break;
                        case 3:
                            currentFrame = 0;
                            playerGeo.setMaterial(playerMatList[0][0]);
                            break;
                    }
                } 
                //  */
            }if(up){
                facing = 1;
                // Simulate walk cycle - north
                //Move the player
                deltaY += (walkSpeed * tileScreenSize);
                
                if(frameTime > frameSpeed){
                    frameTime -= frameSpeed;

                    //weaponGeo.setLocalTranslation(tileScreenSize / 2,(2 * tileScreenSize) / 2,-0.5f);
                    //Increment to next frame  
                    switch(currentFrame){
                        case 0:
                            currentFrame = 1;
                            playerGeo.setMaterial(playerMatList[1][1]);
                            break;
                        case 1:
                            currentFrame = 2;
                            playerGeo.setMaterial(playerMatList[1][0]);
                            break;
                        case 2:
                            currentFrame = 3;
                            playerGeo.setMaterial(playerMatList[1][2]);
                            break;
                        case 3:
                            currentFrame = 0;
                            playerGeo.setMaterial(playerMatList[1][0]);
                            break;
                    }
                } // */
            }if(left){
                facing = 2;
                // Simulate walk cycle - west
                //Move the player
                deltaX -= (walkSpeed * tileScreenSize);
                
                
                if(frameTime > frameSpeed){
                    frameTime -= frameSpeed;

                    //weaponGeo.setLocalTranslation(0,(2 * tileScreenSize) / 2,0);
                    //Increment to next frame  
                    switch(currentFrame){
                        case 0:
                            currentFrame = 1;
                            playerGeo.setMaterial(playerMatList[2][1]);
                            break;
                        case 1:
                            currentFrame = 2;
                            playerGeo.setMaterial(playerMatList[2][0]);
                            break;
                        case 2:
                            currentFrame = 3;
                            playerGeo.setMaterial(playerMatList[2][2]);
                            break;
                        case 3:
                            currentFrame = 0;
                            playerGeo.setMaterial(playerMatList[2][0]);
                            break;
                    }
                } // */
            }if(right){
                facing = 3;
                // Simulate walk cycle - east
                //Move the player
                deltaX += (walkSpeed * tileScreenSize);
                
                if(frameTime > frameSpeed){
                    frameTime -= frameSpeed;

                    //weaponGeo.setLocalTranslation(0,(2 * tileScreenSize) / 2,0);
                    //Increment to next frame  
                    switch(currentFrame){
                        case 0:
                            currentFrame = 1;
                            playerGeo.setMaterial(playerMatList[3][1]);
                            break;
                        case 1:
                            currentFrame = 2;
                            playerGeo.setMaterial(playerMatList[3][0]);
                            break;
                        case 2:
                            currentFrame = 3;
                            playerGeo.setMaterial(playerMatList[3][2]);
                            break;
                        case 3:
                            currentFrame = 0;
                            playerGeo.setMaterial(playerMatList[3][0]);
                            break;
                    }
                } // */
            }
            
            /** Instead of moving the player directly, let's displace the map. **/
            map.getNode().move(deltaX * tpf * -1, deltaY * tpf * -1, 0);
            map.offsetX -= deltaX *tpf;
            map.offsetY -= deltaY *tpf;
            playerPosScreen.addLocal(new Vector2f(deltaX * tpf,deltaY * tpf));
            playerPosTile.addLocal(new Vector2f((deltaX * tpf)/tileScreenSize,(deltaY * tpf)/tileScreenSize));
        }
        
        boolean attackDebounce = false;
        /** Update weapon position and attack frame animation. **/
        public void updateWeapon(float tpf){
            /** Check for attack input. **/
            if(attack){
                // Debounce
                if(!attackDebounce){
                    if(!preAttack){
                        // Transition to start position of attack
                        attackPreTimeLeft = attackPreTime;
                        attackTimeLeft = attackTime;
                        preAttack = true;
                        isAttacking = true;
                        attackDebounce = true;
                    }
                }
                
            }
            
            if(preAttack){
                //Start transition! Translation is interpolated
                Vector3f calcPT = new Vector3f(0,0,0); 
                Vector3f calcP = new Vector3f(0,0,0);
                Quaternion calcR = new Quaternion();
                Quaternion calcRT = new Quaternion();
                /** Calculate an interpolated position between start and end. **/
                
                //down
                if(facing == 0){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackPreTime - attackPreTimeLeft)/attackPreTime, 0, 90), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 90, Vector3f.UNIT_Z));
                    calcP.set(-(tileScreenSize / 2),tileScreenSize, 0);
                    calcPT.set(0,0,0);
                }
                //up
                if(facing == 1){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackPreTime - attackPreTimeLeft)/attackPreTime, 360, 270), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 270, Vector3f.UNIT_Z));
                    calcP.set((tileScreenSize / 2),tileScreenSize, 0);
                    calcPT.set(0 ,3 * tileScreenSize ,0);
                }
                //left
                if(facing == 2){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackPreTime - attackPreTimeLeft)/attackPreTime, 0, 0), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 0, Vector3f.UNIT_Z));
                    calcP.set(0,tileScreenSize, 0);
                    calcPT.set(-(tileScreenSize / 2) ,1.5f * tileScreenSize,0);
                }
                //right
                if(facing == 3){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackPreTime - attackPreTimeLeft)/attackPreTime, 0, 180), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 180, Vector3f.UNIT_Z));
                    calcP.set(0,tileScreenSize, 0);
                    calcPT.set(tileScreenSize / 2 ,1.5f * tileScreenSize,0);
                }
                attackPreTimeLeft -= tpf;
                /** Test for frame time transition. **/
                if(attackPreTimeLeft < 0){
                    isAttacking = true;
                    preAttack = false;
                    attackPreTimeLeft = attackPreTime;
                    attackTimeLeft = attackTime;
                    weaponPivot.setLocalRotation(calcRT);
                    weaponNode.setLocalTranslation(calcPT);
                }else{
                    /** Move weapon to calculated position and rotation. **/
                    weaponPivot.setLocalRotation(calcR);
                    weaponNode.setLocalTranslation(calcP.interpolateLocal(calcPT,(attackPreTime - attackPreTimeLeft)/attackPreTime));
                }
            }else if(isAttacking && !preAttack){
                //Start attack! Rotation is interpolated
                Vector3f calcPT = new Vector3f(0,0,0); 
                Vector3f calcP = new Vector3f(0,0,0);
                Quaternion calcR = new Quaternion();
                Quaternion calcRT = new Quaternion();
                /** Calculate an interpolated rotation between start and end. **/
                //down
                if(facing == 0){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackTime - attackTimeLeft)/attackTime, 90, 270), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 270, Vector3f.UNIT_Z));
                    calcP.set(0,0,0);
                    calcPT.set(0,0,0);
                    testWeapon(playerPosScreen.x - (1.25f * tileScreenSize),playerPosScreen.y - (2 * tileScreenSize),(4 * tileScreenSize),(2 * tileScreenSize));
                }
                //up
                if(facing == 1){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackTime - attackTimeLeft)/attackTime, 270, 360 + 90), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 360 + 90, Vector3f.UNIT_Z));
                    calcP.set(0 ,3 * tileScreenSize ,0);
                    calcPT.set(0 ,3 * tileScreenSize ,0);
                    testWeapon(playerPosScreen.x - (1.25f * tileScreenSize),playerPosScreen.y + (2.5f * tileScreenSize),(4.5f * tileScreenSize),(2.5f * tileScreenSize));
                }
                //left
                if(facing == 2){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackTime - attackTimeLeft)/attackTime, 0, 180), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 180, Vector3f.UNIT_Z));
                    calcP.set(-(tileScreenSize / 2) ,1.5f * tileScreenSize,0);
                    calcPT.set(-(tileScreenSize / 2) ,1.5f * tileScreenSize,0);
                    testWeapon(playerPosScreen.x - (2.5f * tileScreenSize), playerPosScreen.y - (0.75f * tileScreenSize),(2.5f * tileScreenSize),(4.5f * tileScreenSize));
                }
                //right
                if(facing == 3){
                    calcR.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * FastMath.interpolateLinear((attackTime - attackTimeLeft)/attackTime, 180, 360), Vector3f.UNIT_Z));
                    calcRT.set(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 360, Vector3f.UNIT_Z));
                    calcP.set(tileScreenSize / 2 ,1.5f * tileScreenSize,0);
                    calcPT.set(tileScreenSize / 2 ,1.5f * tileScreenSize,0);
                    testWeapon(playerPosScreen.x, playerPosScreen.y - (0.75f * tileScreenSize),(2.5f * tileScreenSize),(4.5f * tileScreenSize));
                }
                
                
                /** Move weapon to calculated position and rotation. **/
                /** Reduce attackTimeLeft counter. **/
                attackTimeLeft -= tpf;
                /** Test for frame time end. **/
                if(attackTimeLeft < 0){
                    isAttacking = false;
                    preAttack = false;
                    attackDebounce = false;
                    /** Reset rotation after animation. **/
                    
                    /** Move the weapon depending on the direction the player is facing when not atacking. **/
                    weaponNode.setLocalTranslation(calcPT);
                    weaponPivot.setLocalRotation(calcRT);
                    attackPreTimeLeft = attackPreTime;
                }else{
                    weaponNode.setLocalTranslation(calcPT);
                    weaponPivot.setLocalRotation(calcR);
                }
            }else if(isAttacking == false){                
                /** Move the sword back when not moving. **/
                if(facing == 0){
                    weaponNode.setLocalTranslation(-(tileScreenSize / 2) , (2 * tileScreenSize) / 2,0);
                    weaponPivot.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 0, Vector3f.UNIT_Z));
                }if(facing == 1){
                    weaponNode.setLocalTranslation(tileScreenSize / 2,(2 * tileScreenSize) / 2,-0.5f);
                    weaponPivot.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 0, Vector3f.UNIT_Z));
                }if(facing == 2){
                    weaponNode.setLocalTranslation(0,(2 * tileScreenSize) / 2,0);
                    weaponPivot.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 0, Vector3f.UNIT_Z));
                }if(facing == 3){
                    weaponNode.setLocalTranslation(0,(2 * tileScreenSize) / 2,0);
                    weaponPivot.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * 0, Vector3f.UNIT_Z));
                }
            }
            
        }
        
        public boolean testWeapon(float x, float y ,float sX, float sY){
            boolean hitDetected = false;
            if(mobControls.size() > 0){
                for(int m = 0; m < mobControls.size(); m++){
                    Vector2f mPos = mobControls.get(m).getPos();
                    mPos.subtractLocal((6 * tileScreenSize) + (tileScreenSize/2),0);
                    
                    
                    
                    boolean dXn = mPos.x > x;
                    boolean dXp = mPos.x < (x + sX);
                    
                    boolean dYn = mPos.y > y;
                    boolean dYn2 = mPos.y > (y + (1.5f *tileScreenSize));
                    boolean dYp = mPos.y < (y + sY);
                    boolean dYp2 = mPos.y < (y + sY + (1.5f *tileScreenSize));
                    if((dXn && dXp) && ((dYn && dYp) || (dYn2 && dYp2))){
                        hitDetected = true;
                        System.out.println("HIT!!!");
                        mobControls.get(m).hit(10f);
                    }
                }
            }
            return hitDetected;
        }
    }
    
    /** Mob scene-graph data. **/
    private Quad mobQuad;
    private Geometry mobGeoPrefab;
    private ArrayList<Mob> mobList;
    private ArrayList<Mob> mobControls;
    private ArrayList<MobControl> activeMobControls;
    
    /** The Mob class encapsulates the creation of Mobs. 
     *  BasicMobControl prototype
     **/
    public class Mob extends AbstractControl{
        private Quad mobQuad;
        private Geometry mobGeo;
        private Node mobNode;
        
        /** We need to track world coords. **/
        private float mobWorldX;
        private float mobWorldY;
        private float vX;
        private float vY;
        public float health;
        
        private float deltaX = 0.0f;
        private float deltaY = 0.0f;
        private float walkSpeed = 3.5f; // Faster movement on screen
        private int id;
        
        private boolean nearPlayer = false;
        private boolean hitByPlayer = false;
        private boolean hitDebounce = false;
        public volatile float damageThisFrame = 0;
        
        private float hitTime = 1.8f;
        private float hitTimeLeft = 1.8f;
        private float debounceTime = 0.4f;
        private float debounceTimeLeft = 0.4f;
        
        public Node getNode(){return mobNode;}
        
        public Vector2f getPos(){return new Vector2f(mobWorldX,mobWorldY);}
        
        /** Serialization constructor. **/
        public Mob(){
            vX = 0;
            vY = 0;
            mobWorldX = 0;
            mobWorldY = 0;
            health = 1f;
        }
        
        public Mob(float posX, float posY, int id){
            vX = posX;
            vY = posY;
            mobWorldX = (vX * tileScreenSize);
            mobWorldY = (vY * tileScreenSize);
            health = 10f;
            this.id = id;
        }
        
        /** Tell the mob that it was hit. **/
        public void hit(float damage){
            if(!hitDebounce){
                //System.out.println("IM HIT!!! #" + this.id + ", " + damageThisFrame + " damage");
                hitDebounce = true;
                hitByPlayer = true;
                hitTimeLeft = hitTime;
                debounceTimeLeft = debounceTime;
                damageThisFrame += damage;
            }
        }
        
        /** Create mob. **/
        public void createMob(){
            // Create the required scene-graph elements for a Zombie
            mobGeo = new Geometry("Mob - Zombie", new Quad(2 * tileScreenSize, 3 * tileScreenSize));
            mobGeo.setLocalTranslation(-tileScreenSize,(1.5f) * tileScreenSize,0);
            /** Giving the mob a Node might help with Controls later... **/
            mobNode = new Node("Zombie Node");
            
            /** Set the proper Material to the Geometry and attach to guiNode.  **/
            mobGeo.setMaterial(mobMatList[0][0]);
            // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
            // v3 Addition, set the player position members
            
            mobNode.attachChild(mobGeo);
            mobNode.setLocalTranslation(mobWorldX, mobWorldY, 0);
            
            /** Since this class is also a control, let's control the node via this class. **/
            //mobNode.addControl(this);
            
            /** Data members for the debug position marker for mobs. **/
            Quad quad = new Quad((1) * tileScreenSize, (1) * tileScreenSize);
            Geometry geo = new Geometry("Player Weapon", quad);

            /** Set the proper Material to the Geometry and attach to Node. **/
            geo.setMaterial(debugPosMat);
            mobNode.attachChild(geo);
            geo.setLocalTranslation(-tileScreenSize,-tileScreenSize,0.5f);
        }
        
        public void destroyMob(){
            mobGeo.removeFromParent();
            mobGeo.updateGeometricState();
        }

        @Override
        protected void controlUpdate(float tpf) {
            //if(spatial != null) {     
                // spatial.rotate(tpf,tpf,tpf); // example behaviour
                Vector3f pPos = playerObj.getPos();
                Vector3f diff = pPos.subtract(mobWorldX,mobWorldY,0);
                diff.subtractLocal((6 * tileScreenSize),0,0);
                deltaX = 0;
                deltaY = 0;
                //System.out.println("length " + diff.length());
                
                //Save the cpu-heavy stuff for active mobs
                if((diff.length() < (10 * tileScreenSize) && diff.length() > (2 * tileScreenSize))|| (diff.length() > -(10 * tileScreenSize) && diff.length() < (-2 * tileScreenSize))){
                    
                    diff.normalizeLocal();
                    
                    //Test if this mob is hit
                    if(hitByPlayer == true){
                        //Decrement timer and stun when hit
                        hitTimeLeft -= tpf;
                        debounceTimeLeft -= tpf;
                        if(hitTimeLeft < 0){
                            hitByPlayer = false;
                        }if(debounceTimeLeft < 0){
                            hitDebounce = false;
                        }
                    }else{
                        //Move toward player after stun time expires
                        deltaX += (walkSpeed * tileScreenSize) * diff.x;
                        deltaY += (walkSpeed * tileScreenSize) * diff.y;
                        spatial.move(deltaX * tpf,deltaY * tpf,0);
                                                
                        mobWorldX += deltaX * tpf;
                        mobWorldY += deltaY * tpf;
                        vX = mobWorldX / tileScreenSize;
                        vY = mobWorldY / tileScreenSize;
                    }
                    
                    //If we have not done it yet, add to active mobs list
                    if(!nearPlayer){
                        nearPlayer = true;
                    }
                }else{
                    //Check for removal at the end of the loop
                    if(health < 0.0f){
                        //mob.destroyMob();
                        System.out.println("IM DEAD!!! #" + this.id);
                        map.getNode().detachChild(spatial);
                        spatial.removeControl(this);
                    }else{
                        //System.out.println("Mob #" + id + " Health :" + health);
                    }
                    //Remove from active mob list, if we havent already
                    if(nearPlayer){
                        nearPlayer = false;
                    }
                    if(damageThisFrame > 0){
                        System.out.println("IM HIT!!! #" + this.id + ", " + damageThisFrame + " damage: " + (health - damageThisFrame));
                        health = health - damageThisFrame;
                        damageThisFrame = 0;
                    }
                    
                }
                
            //} 
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="1st draft MobControl">
    public class MobControl extends AbstractControl{
        private Vector2f mobPosTile = new Vector2f();
        private Vector2f mobPosScreen = new Vector2f();
        private float deltaX = 0.0f;
        private float deltaY = 0.0f;
        private float walkSpeed = 3.5f; // Faster movement on screen
        private int id;
        private Node controlNode;
        private Mob mob;
        
        private boolean nearPlayer = false;
        private boolean hitByPlayer = false;
        private boolean hitDebounce = false;
        private float damageThisFrame = 0;
        
        private float health = 10f;
        private float hitTime = 1.8f;
        private float hitTimeLeft = 1.8f;
        private float debounceTime = 0.4f;
        private float debounceTimeLeft = 0.4f;
        
        public MobControl(){}
        
        public MobControl(int id){
            this.id = id;
        }
        
        public MobControl(int id, int sX, int sY){
            this.id = id;
            //mob = new Mob(sX,sY);
            mob.createMob();
            mob.getNode().addControl(this);
        }
        
        public MobControl(int id, Mob mob){
            this.id = id;
            this.mob = mob;
        }
        
        public void setPos(float x, float y){
            mobPosScreen.set(x,y);
            mobPosTile.set(x/tileScreenSize,y/tileScreenSize);
        }
        public Vector2f getPos(){ return mobPosScreen;}
        public Mob getMob(){ return mob;}
        public void hit(float damage){
            if(!hitDebounce){
                System.out.println("IM HIT!!! #" + this.id);
                hitDebounce = true;
                hitByPlayer = true;
                hitTimeLeft = hitTime;
                debounceTimeLeft = debounceTime;
                damageThisFrame += damage;
            }
        }
        
        @Override
        public void setSpatial(Spatial spatial) {
            super.setSpatial(spatial);
            /* Example:  */
            if (spatial != null && mob == null){
                // initialize pre-existing mob
                
                controlNode = (Node) spatial;
                Vector3f localTranslation = controlNode.getLocalTranslation();
                System.out.println("setSpatial @: " + localTranslation);
                mobPosScreen.set(localTranslation.x,localTranslation.y);
            }else if (spatial != null && mob != null){
                // initialize and create Mob
                this.spatial = spatial;
                controlNode = mob.getNode();
                Vector3f localTranslation = controlNode.getLocalTranslation();
                System.out.println("setSpatial @: " + localTranslation);
                mobPosScreen.set(localTranslation.x,localTranslation.y);
            }else{
                // cleanup
                //spatial.removeFromParent();
                //mob.destroyMob();
            }
        }
        
        /** Implement your spatial's behaviour here.
         * From here you can modify the scene graph and the spatial
         * (transform them, get and set userdata, etc).
         * This loop controls the spatial while the Control is enabled.
         */
        @Override
        protected void controlUpdate(float tpf){
            if(spatial != null) {
                // spatial.rotate(tpf,tpf,tpf); // example behaviour
                Vector3f pPos = playerObj.getPos();
                Vector3f diff = pPos.subtract(mobPosScreen.x,mobPosScreen.y,0);
                diff.subtractLocal((5 * tileScreenSize),0,0);
                deltaX = 0;
                deltaY = 0;
                //System.out.println("length " + diff.length());
                
                //Save the cpu-heavy stuff for active mobs
                if((diff.length() < (10 * tileScreenSize) && diff.length() > (2 * tileScreenSize))|| (diff.length() > -(10 * tileScreenSize) && diff.length() < (-2 * tileScreenSize))){
                    //System.out.println("controlUpdate id: " + id);
                    //System.out.println("mob pos: " + mobPosScreen);
                    //System.out.println("delta: " + diff.mult(tpf));
                    diff.normalizeLocal();
                    
                    //Test if this mob is hit
                    if(hitByPlayer == true){
                        //Decrement timer and stun when hit
                        hitTimeLeft -= tpf;
                        debounceTimeLeft -= tpf;
                        if(hitTimeLeft < 0){
                            hitByPlayer = false;
                        }if(debounceTimeLeft < 0){
                            hitDebounce = false;
                        }
                    }else{
                        //Move toward player after stun time expires
                        deltaX += (walkSpeed * tileScreenSize) * diff.x;
                        deltaY += (walkSpeed * tileScreenSize) * diff.y;
                        mob.getNode().move(deltaX * tpf,deltaY * tpf,0);
                        //controlNode.rotate(new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * tpf * 10, Vector3f.UNIT_Z));
                        mobPosScreen.addLocal(new Vector2f(deltaX * tpf,deltaY * tpf));
                        mobPosTile.addLocal(new Vector2f((deltaX * tpf)/tileScreenSize,(deltaY * tpf)/tileScreenSize));
                    }
                    
                    //If we have not done it yet, add to active mobs list
                    if(!nearPlayer){
                        nearPlayer = true;
                        activeMobControls.add(this);
                    }
                }else{
                    //Remove from active mob list, if we havent already
                    if(nearPlayer){
                        nearPlayer = false;
                        activeMobControls.remove(this);
                    }
                    if(damageThisFrame > 0){
                        System.out.println("IM HIT!!! #" + this.id);
                        mob.health -= damageThisFrame;
                        health -= damageThisFrame;
                        damageThisFrame = 0;
                    }
                    //Check for removal at the end of the loop
                    if(mob.health < 0){
                        //mob.destroyMob();
                        System.out.println("IM DEAD!!! #" + this.id);
                        guiNode.detachChild(spatial);
                        mob.getNode().detachAllChildren();
                        mob.getNode().removeControl(this);
                    }else{
                        System.out.println("Mob #" + id + " Health :" + mob.health + " , alt: " + health);
                    }
                }
                
            }
        }
        
        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            
        }
        
    }
//</editor-fold>
    
    /** We need to track world coords. **/
    private float mobWorldX;
    private float mobWorldY;
    
    /** Data for screen dimensions. **/
    private int tileScreenSize;
    private int screenHeight;
    private int screenWidth;

    private Node rootNode = new Node("Root Node");
    private Node guiNode = new Node("GUI Node");
    
    /** Animation data. **/
    private float frameSpeed = 0.3f; // Adjusting the speed slower
    private float frameTime = 0.0f;
    private int currentFrame = 0;
    
    /** Movement update data **/
    private float deltaX = 0.0f;
    private float deltaY = 0.0f;

    public Node getRootNode(){
        return rootNode;
    }
    
    public Node getGUIRootNode(){
        return guiNode;
    }
    
    public void setRes(int w, int h){
        screenHeight = h;
        screenWidth = w;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        /** Calculate the tile size according to screen resolution. **/
        tileScreenSize = screenHeight / 16;
        
        /** Setup asset sources. **/
        setupTextures();
        setupTileData();
        
        setupPlayerSpriteData();
        setupWeaponSpriteData();
        setupMobSpriteData();
        
        /** Create player Geomety. **/
        buildNewPlayer();
        
        /** Create the TileMap. **/
        map = new TileMap(tileScreenSize, 4, tileMatList);

        map.getNode().setLocalTranslation((screenWidth/2) - (tileScreenSize * 8),0,-0.5f);

        //sysLog("Gen map");
        map.genOverworld();
        
        guiNode.attachChild(map.getNode());
        
        /** We need to tell the guiNode to render and to never cull itself. **/
        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        
        /** Setup inputManager. **/
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(actionListener, "Up","Down","Left","Right","Attack");
        
        /** Create Mob Prefab. **/
        setupMobPrefab();
        debugPosImage();
        
        // Let's play around with random numbers with normalized distrobution
        System.out.println("Spawn mobs");
        mobControls = new ArrayList<>();
        for(int t = 0; t < 20; t++){
            double mobX = map.randNormDist(32d, 15d);
            double mobY = map.randNormDist(32d, 15d);
            //buildMob((int)Math.floor(mobX),(int)Math.floor(mobY));
            
            Mob newMob = new Mob((float)mobX, (float)mobY, t);
            newMob.createMob();
            newMob.getNode().addControl(newMob);
            mobControls.add(newMob);
            //mobList.add(newMob);
            
            map.getNode().attachChild(newMob.getNode());
        }
        
        System.out.println("Init RootNodeState: " + version_maj + "."  + version_min + "." +version_revision+ " - " + version_fork);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        /** Let's implement a frame system. 
         *  We will need to keep track of the animation speed
         *  We also need to flip between the proper frames
         * 
         *  These are the frames from the char tile sheet
         *  [0,0] Standing          Frame: 0
         *  [0,1] First-step        Frame: 1
         *  [0,2] Second-step       Frame: 2
         * 
         *  DISCLAIMER...
         *  This is a quick and dirty prototype. Later, I will re-organize this update loop
         **/
        
        //deltaX = 0.0f;
        //deltaY = 0.0f;
        
        //frameTime += tpf;
        
        /**Player animation and movement code moved to updatePlayer() method. **/
        playerObj.update(tpf);
        playerObj.updateWeapon(tpf);

        rootNode.updateLogicalState(tpf);
        rootNode.updateGeometricState();
        guiNode.updateLogicalState(tpf);
        guiNode.updateGeometricState();
    }
    
    /** This is the same actionListener from the TechDemo. **/
    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            // 1 - South, 2 - North, 4 - West, 8 - East
            if(name.equals("Up")){
                up = keyPressed;
                if(keyPressed){
                    facing += 2; 
                }else{
                    facing -= 2; 
                }
            }if(name.equals("Down")){
                down = keyPressed;
                if(keyPressed){
                    facing += 1; 
                }else{
                    facing -= 1; 
                }
            }if(name.equals("Left")){
                left = keyPressed;
                if(keyPressed){
                    facing += 4; 
                }else{
                    facing -= 4; 
                }
            }if(name.equals("Right")){
                right = keyPressed;
                if(keyPressed){
                    facing += 8; 
                }else{
                    facing -= 8; 
                }
            }if(name.equals("Attack")){
                playerObj.attack = keyPressed;
            }
        }
    };
    
    /** Utility method to grab master texture data assets. **/
    public void setupTextures(){
        tileTexture = assetManager.loadTexture("Textures/Tiles/TileMapDev.bmp");
        playerTexture = assetManager.loadTexture("Textures/Player/CharTileSheet0.bmp");
        mobTexture = assetManager.loadTexture("Textures/Mobs/ZombieSheet0.png");
        
        /** Grab the image data from the textures. **/
        tileImage = tileTexture.getImage();
        playerImage = playerTexture.getImage();
        mobImage = mobTexture.getImage();
    }
    
    public void debugPosImage(){
        imgWidth = 16;
        imgHeight = 16;
        depth = 4;
        Image debugImage = new Image(Image.Format.BGRA8, imgWidth, imgHeight, BufferUtils.createByteBuffer(depth * imgWidth * imgHeight), null, ColorSpace.sRGB); 
        ImageRaster raster = ImageRaster.create(debugImage);
        
        for(int copyX = 0; copyX < 16; copyX++){
            for(int copyY = 0; copyY < 16; copyY++){
                //ColorRGBA pixel = tileRaster.getPixel(copyX + (tx * imgWidth),copyY + (ty * imgHeight));
                
                if(copyX == 6 || copyX == 7){
                    raster.setPixel(copyX, copyY, ColorRGBA.Red);
                }else if(copyY == 6 || copyY == 7){
                    raster.setPixel(copyX, copyY, ColorRGBA.Red);
                }else{
                    raster.setPixel(copyX, copyY, ColorRGBA.BlackNoAlpha);
                }
            }
        }
        
        Texture2D debugTexture = new Texture2D(debugImage);
        debugTexture.setMagFilter(Texture.MagFilter.Nearest);
        debugTexture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        debugPosMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        debugPosMat.setTexture("ColorMap",debugTexture);
        debugPosMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }
    
    /** Previous code for tile texture setup moved here. **/
    public void setupTileData(){
        /** Split the image data into separate textures. **/
        tileTextureList = new Texture2D[16][16];
        tileImageList = new Image[16][16];
        tileMatList = new Material[16][16];
        tileRaster = ImageRaster.create(tileImage);
        
        /** Iterate through the list of tiles. **/
        // Dim: 16,16
        // Size 16,16 px
        
        imgWidth = 16;
        imgHeight = 16;
        depth = 4;
        
        /** Loop through the tiles. **/
        for(int tx = 0; tx < imgWidth; tx++){
            for(int ty = 0; ty < imgHeight; ty++){
                /** Create new Image data and ImageRaster for each tileTexture. **/
                tileImageList[tx][ty] = new Image(Image.Format.BGRA8, imgWidth, imgHeight, BufferUtils.createByteBuffer(depth * imgWidth * imgHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(tileImageList[tx][ty]);
                
                /** Copy pixels from source, paste to destination. **/
                for(int copyX = 0; copyX < 16; copyX++){
                    for(int copyY = 0; copyY < 16; copyY++){
                        ColorRGBA pixel = tileRaster.getPixel(copyX + (tx * imgWidth),copyY + (ty * imgHeight));
                        raster.setPixel(copyX, copyY, pixel);
                    }
                }
                
                /** Create new texture from the copied tile... **/
                tileTextureList[tx][ty] = new Texture2D(tileImageList[tx][ty]);
                // Set these filters for pixellated look
                tileTextureList[tx][ty].setMagFilter(Texture.MagFilter.Nearest);
                tileTextureList[tx][ty].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                tileMatList[tx][ty] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                tileMatList[tx][ty].setTexture("ColorMap",tileTextureList[tx][ty]);
                tileMatList[tx][ty].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** Prev code for player sprite sheet setup moved here. **/
    public void setupPlayerSpriteData(){
        /** Setup player sprite data. **/
        playerTextureList = new Texture2D[4][4];
        playerImageList = new Image[4][4];
        playerMatList = new Material[4][4];
        playerRaster = ImageRaster.create(playerImage);
        
        /** Iterate through the list of player sprites. **/
        // Dim: 4,3
        // Size: 32,48 px
        
        playerWidth = 32;
        playerHeight = 48;
        
        /** Loop through player sprite sheet. **/
        for(int px = 0; px < 4; px++){
            for(int py = 0; py < 4; py++){
                /** Image and Raster for player sprites. **/
                playerImageList[px][py] = new Image(Image.Format.RGBA16F, playerWidth, playerHeight, BufferUtils.createByteBuffer(depth * 2 * playerWidth * playerHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(playerImageList[px][py]);
                
                /** Copy pixels from source (starting at (0,256), paste to destination. **/
                for(int copyX = 0; copyX < 32; copyX++){
                    for(int copyY = 0; copyY < 48; copyY++){
                        // textureHeight - copyY inverts y from bottom-left to top-left origin
                        // needed to subtract 1 for the y-calc(add to number used for subtraction), index kept going out of bounds
                        int tempX = copyX + (px * playerWidth);
                        int tempY = playerImage.getHeight() - (copyY + 1 + (py * playerHeight));
                        //System.out.println("Player sprite sheet coords: " + tempX + "," + tempY);
                        ColorRGBA pixel = playerRaster.getPixel(tempX,tempY);
                        
                        raster.setPixel(copyX, playerHeight - (copyY + 1), pixel);
                    }
                }
                
                /** Create new texture from the copied player sprite sheet... **/
                playerTextureList[px][py] = new Texture2D(playerImageList[px][py]);
                // Set these filters for pixellated look
                playerTextureList[px][py].setMagFilter(Texture.MagFilter.Nearest);
                playerTextureList[px][py].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                playerMatList[px][py] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                playerMatList[px][py].setTexture("ColorMap",playerTextureList[px][py]);
                playerMatList[px][py].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** The player needs a weapon... **/
    public void setupWeaponSpriteData(){
        weaponTextureList = new Texture2D[3][1];
        weaponImageList = new Image[3][1];
        weaponMatList = new Material[3][1];
        //weaponRaster would be the same as playerRaster
        /** The player sprite sheet contains 3 swords. **/
        for(int w = 0; w < 3; w++){
            weaponImageList[w][0] = new Image(Image.Format.RGBA16F, playerWidth, playerHeight, BufferUtils.createByteBuffer(depth * 2 * playerWidth * playerHeight), null, ColorSpace.sRGB); 
            ImageRaster raster = ImageRaster.create(weaponImageList[w][0]);
            
            /** Copy the needed pixels. **/
            for(int copyX = 0; copyX < 32; copyX++){
                for(int copyY = 0; copyY < 48; copyY++){
                    int tempX = copyX + ((w + 4) * playerWidth);
                    int tempY = 255 - (copyY + 1 + (w * playerHeight));
                    
                    ColorRGBA pixel = playerRaster.getPixel(tempX,tempY);
                    raster.setPixel(copyX, playerHeight - (copyY + 1), pixel);
                }
            }
            weaponTextureList[w][0] = new Texture2D(weaponImageList[w][0]);
            // Set these filters for pixellated look
            weaponTextureList[w][0].setMagFilter(Texture.MagFilter.Nearest);
            weaponTextureList[w][0].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            
            /** ...and prepare a Material with the Texture. **/
            weaponMatList[w][0] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            weaponMatList[w][0].setTexture("ColorMap",weaponTextureList[w][0]);
            weaponMatList[w][0].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        
        
    }
    
    /** Prev code for mob sprite sheet setup moved here. **/
    public void setupMobSpriteData(){
        /** Setup player sprite data. **/
        mobTextureList = new Texture2D[4][4];
        mobImageList = new Image[4][4];
        mobMatList = new Material[4][4];
        mobRaster = ImageRaster.create(mobImage);
        
        /** Iterate through the list of player sprites. **/
        // Dim: 4,3
        // Size: 32,48 px
        
        mobWidth = 32;
        mobHeight = 48;
        
        /** Loop through player sprite sheet. **/
        for(int px = 0; px < 4; px++){
            for(int py = 0; py < 4; py++){
                /** Image and Raster for player sprites. **/
                mobImageList[px][py] = new Image(Image.Format.RGBA16F, mobWidth, mobHeight, BufferUtils.createByteBuffer(depth * 2 * mobWidth * mobHeight), null, ColorSpace.sRGB); 
                ImageRaster raster = ImageRaster.create(mobImageList[px][py]);
                
                /** Copy pixels from source (starting at (0,256), paste to destination. **/
                for(int copyX = 0; copyX < 32; copyX++){
                    for(int copyY = 0; copyY < 48; copyY++){
                        // textureHeight - copyY inverts y from bottom-left to top-left origin
                        // needed to subtract 1 for the y-calc(add to number used for subtraction), index kept going out of bounds
                        int tempX = copyX + (px * mobWidth);
                        int tempY = mobImage.getHeight() - (copyY + 1 + (py * mobHeight));
                        //System.out.println("Player sprite sheet coords: " + tempX + "," + tempY);
                        ColorRGBA pixel = mobRaster.getPixel(tempX,tempY);
                        raster.setPixel(copyX, mobHeight - (copyY + 1), pixel);
                    }
                }
                
                /** Create new texture from the copied player sprite sheet... **/
                mobTextureList[px][py] = new Texture2D(mobImageList[px][py]);
                // Set these filters for pixellated look
                mobTextureList[px][py].setMagFilter(Texture.MagFilter.Nearest);
                mobTextureList[px][py].setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                
                /** ...and prepare a Material with the Texture. **/
                mobMatList[px][py] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mobMatList[px][py].setTexture("ColorMap",mobTextureList[px][py]);
                mobMatList[px][py].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            }
        }
    }
    
    /** Utility to create prefab mob. **/
    public void setupMobPrefab(){
        // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
        mobGeoPrefab = new Geometry("Mob - Zombie", new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize));
        mobGeoPrefab.setLocalTranslation(-tileScreenSize,0,0);
        /** Set the proper Material to the Geometry and attach to guiNode. **/
        mobGeoPrefab.setMaterial(mobMatList[0][0]);
    }
    
    /** Build the player here. **/
    public void buildPlayer(){
        // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
        playerQuad = new Quad((playerWidth / 16) * tileScreenSize, (playerHeight / 16) * tileScreenSize);
        playerGeo = new Geometry("Player", playerQuad);
        
        /** Data members for the player's weapon. **/
        Quad weaponQuad = new Quad((playerWidth / 16) * tileScreenSize, (playerHeight / 16) * tileScreenSize);
        Geometry weaponGeo = new Geometry("Player Weapon", weaponQuad);
        
        /** Set the proper Material to the Geometry and attach to guiNode. **/
        playerGeo.setMaterial(playerMatList[0][0]);
        weaponGeo.setMaterial(weaponMatList[0][0]);
        // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
        // v3 Addition, set the player position members
        player_Scrn_X = (screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2);
        player_Scrn_Y = (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2);
        /** We will need to keep track of the weapon's offset from the player. **/
        int weaponX_offset = 48;
        int weaponY_offset = 48;
        //playerGeo.setLocalTranslation((screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2), (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2), 0);
        //weaponGeo.setLocalTranslation((screenWidth / 2) - (((weaponX_offset / 16) * tileScreenSize) / 2), (screenHeight / 2) - (((weaponY_offset / 16) * tileScreenSize) / 4), 0);
        
        Node player = new Node("Player Node");
        /** We're going to use a node to keep track of the player. **/
        player.setLocalTranslation((screenWidth / 2) - (((playerWidth / 16) * tileScreenSize) / 2), (screenHeight / 2) - (((playerHeight / 16) * tileScreenSize) / 2), 0);
        player.attachChild(playerGeo);
        /** The weapon's local offset will now be easier to calculate, even if we let the player leave the center of the screen. **/
        player.attachChild(weaponGeo);
        weaponGeo.setLocalTranslation(0,0,0);
        guiNode.attachChild(player);
    }
    
    /** New player-build method. **/
    public void buildNewPlayer(){
        playerObj = new Player();
        playerObj.createPlayer();
    }
    
    /** This method to build mob scene stuff is based on player. 
     *  If we delegate the mob data to a class, we can build the neccesary ,prefabs'
     **/
    public void buildMob(int x, int y){
        // Player pixel size has to be divided by tile pixel size to determine tile dimensions and scale to different screens
        //mobQuad = new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize);
        mobGeoPrefab = new Geometry("Mob - Zombie", new Quad((mobWidth / 16) * tileScreenSize, (mobHeight / 16) * tileScreenSize));
        
        /** Giving the mob a Node might help with Controls later... **/
        Node mobNode = new Node("Zombie Node");
        
        /** Set the proper Material to the Geometry and attach to guiNode. **/
        mobGeoPrefab.setMaterial(mobMatList[0][0]);
        // Calculate the position of the bottom left corner of the Quad to position player in the center of the screen 
        // v3 Addition, set the player position members
        mobWorldX = (x * tileScreenSize) - (((mobWidth / 16) * tileScreenSize) / 2);
        mobWorldY = (y * tileScreenSize);
        mobNode.attachChild(mobGeoPrefab);
        mobGeoPrefab.setLocalTranslation(-tileScreenSize,1.5f * tileScreenSize,0);
        mobNode.setLocalTranslation(mobWorldX, mobWorldY, 0);
        map.getNode().attachChild(mobNode);
    }
    
    /** Utility method to update player animation frame and movement per input mask. **/
    public void updatePlayer(float tpf){
        /** When the player presses any of the movement keys, play the animation. **/
        // For now, no real movement, just making sure the right frames play for the right direction
        // v3 move the player (X and Y ) according to the animation that's played
        if(down){
            //Move the player
            deltaY -= (walkSpeed * tileScreenSize);
            // Simulate walk cycle - south
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerObj.playerGeo.setMaterial(playerMatList[0][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerObj.playerGeo.setMaterial(playerMatList[0][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerObj.playerGeo.setMaterial(playerMatList[0][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerObj.playerGeo.setMaterial(playerMatList[0][0]);
                        break;
                }
            } 
            //  */
        }if(up){
            // Simulate walk cycle - north
            //Move the player
            deltaY += (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerObj.playerGeo.setMaterial(playerMatList[1][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerObj.playerGeo.setMaterial(playerMatList[1][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerObj.playerGeo.setMaterial(playerMatList[1][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerObj.playerGeo.setMaterial(playerMatList[1][0]);
                        break;
                }
            } // */
        }if(left){
            // Simulate walk cycle - west
            //Move the player
            deltaX -= (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerObj.playerGeo.setMaterial(playerMatList[2][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerObj.playerGeo.setMaterial(playerMatList[2][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerObj.playerGeo.setMaterial(playerMatList[2][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerObj.playerGeo.setMaterial(playerMatList[2][0]);
                        break;
                }
            } // */
        }if(right){
            // Simulate walk cycle - east
            //Move the player
            deltaX += (walkSpeed * tileScreenSize);
            if(frameTime > frameSpeed){
                frameTime -= frameSpeed;
                
                //Increment to next frame  
                switch(currentFrame){
                    case 0:
                        currentFrame = 1;
                        playerObj.playerGeo.setMaterial(playerMatList[3][1]);
                        break;
                    case 1:
                        currentFrame = 2;
                        playerObj.playerGeo.setMaterial(playerMatList[3][0]);
                        break;
                    case 2:
                        currentFrame = 3;
                        playerObj.playerGeo.setMaterial(playerMatList[3][2]);
                        break;
                    case 3:
                        currentFrame = 0;
                        playerObj.playerGeo.setMaterial(playerMatList[3][0]);
                        break;
                }
            } // */
        }
        //playerGeo.move(deltaX *tpf, deltaY * tpf, 0);
        //System.out.println((deltaX *tpf) + ", " + (deltaY *tpf) + ", " + tpf);
        
        /** Instead of moving the player directly, let's displace the map. **/
        map.getNode().move(deltaX * tpf * -1, deltaY * tpf * -1, 0);
        map.offsetX -= deltaX *tpf;
        map.offsetY -= deltaY *tpf;
    }

}
