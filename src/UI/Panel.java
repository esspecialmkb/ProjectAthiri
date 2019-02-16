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
public class Panel{
        private int id, parentId, type;
        private Node panelNode;
        private BitmapText panelText;
        private Geometry panelGeo,borderGeo;
        private Material guiMat;
        private BitmapFont appFont;
        private int x,y,width,height;
        private int xMini,yMini,wMini,hMini;
        private String name;
        
        public Panel(String name, int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
        }
        
        public Panel(String name, int x, int y, int width, int height, BitmapFont fnt, Material mat){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            
            this.guiMat = mat;
            this.appFont = fnt;
        }
        
        public int getX(){return this.x;}
        public int getY(){return this.y;}
        public int getWidth(){return this.width;}
        public int getHeight(){return this.height;}
        
        public void buildGeometry(){
            Quad quad = new Quad(width,height);
            panelGeo = new Geometry("Panel Geo", quad);
            panelGeo.setLocalTranslation(0,0,0);
            panelGeo.setMaterial(guiMat);
            
            panelText = new BitmapText(appFont, false);
            panelText.setBox(new Rectangle(0, 0, quad.getWidth(), quad.getHeight()));
            panelText.setSize(appFont.getPreferredSize() * 1f);
            panelText.setText(name);
            panelText.setLocalTranslation((width/2) - ((appFont.getLineWidth(name) * 1)/2) , height - panelText.getLineHeight()/2, 1);
            
            panelNode = new Node("Panel Node");
            panelNode.setLocalTranslation(x,y,0);
            panelNode.attachChild(panelGeo);
            panelNode.attachChild(panelText);
        }
        
        public int getId(){return this.id;}
        public void setId(int id){this.id = id;}
        public Node getNode(){ return this.panelNode;}
        
        public boolean checkBoundary(int xP, int yP){
            if(xP > x && xP < (x + width) ){
                if(yP > y && yP < (y + height) ){
                    return true;
                }
            }
            return false;
        }
    }
