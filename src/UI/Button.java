/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author esspe
 */
public class Button{
        private int id, parentId, type;
        private Node buttonNode;
        private BitmapText buttonText;
        private BitmapFont buttonFnt;
        private Material buttonMat,borderMat;
        private boolean hasBorder;
        private Geometry buttonGeo;
        private Geometry borderGeo;
        private int x,y,width,height;
        private String name;
        
        public Button(String name, int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            this.hasBorder = false;
            //this.buttonFnt = appFont;
        }
        
        public Button(String name, int x, int y, int width, int height, BitmapFont fnt, Material mat){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            this.buttonFnt = fnt;
            this.buttonMat = mat;
            this.hasBorder = false;
        }
        
        public Button(String name, int x, int y, int width, int height, BitmapFont fnt, Material mat, Material bMat){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            this.buttonFnt = fnt;
            this.buttonMat = mat;
            this.borderMat = bMat;
            this.hasBorder = true;
        }
        
        public void buildGeometry(){
            Quad quad = new Quad(width,height);
            buttonGeo = new Geometry("Button Geo", quad);
            buttonGeo.setLocalTranslation(0,0,2);
            buttonGeo.setMaterial(buttonMat);
            
            buttonText = new BitmapText(buttonFnt, false);
            buttonText.setBox(new Rectangle(0, 0, quad.getWidth(), quad.getHeight()));
            buttonText.setSize(buttonFnt.getPreferredSize() * 1);
            buttonText.setText(name);
            buttonText.setLocalTranslation((width/2) - (buttonFnt.getLineWidth(name)/2), buttonText.getLineHeight(), 2.5f);
            
            buttonNode = new Node("Button Node");
            buttonNode.setLocalTranslation(x,y,0);
            buttonNode.attachChild(buttonGeo);
            buttonNode.attachChild(buttonText);
            if(hasBorder == true){
                Quad border = new Quad(width + 4, height + 4);
                borderGeo = new Geometry("Border", border);
                buttonGeo.setLocalTranslation(-2,-2,0f);
                buttonGeo.setMaterial(borderMat);
                buttonNode.attachChild(buttonGeo);
            }
        }
        
        public int getId(){return this.id;}
        public void setId(int id){this.id = id;}
        public String getName(){return this.name;}
        public Node getNode(){return this.buttonNode;}
        public Geometry getGeometry(){return this.buttonGeo;}
        
        public boolean checkBoundary(int xP, int yP){
            if(xP > x && xP < (x + width) ){
                if(yP > y && yP < (y + height) ){
                    return true;
                }
            }
            return false;
        }
    }
