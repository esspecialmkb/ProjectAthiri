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
public class Scrollbar{
        private int id, parentId, type;
        private Node scrollNode;
        private BitmapText scrollText;
        private Geometry scrollGeo,slideGeo,trackGeo;
        private Material guiMat, guiDarkMat, guiLightMat;
        private BitmapFont scrollFnt;
        private int textWidth;
        private int x,y,width,height;
        private int xBorder, yBorder,slideWidth, slideHeight;
        private float valueMax, valueMin, value;
        private String name;
        
        public Scrollbar(String name, int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            this.valueMin = -100;
            this.valueMax = 100;
            this.value = 0;
            
            this.xBorder = 2;
            this.yBorder = 2;
            this.textWidth = 40;
        }
        
        public Scrollbar(String name, int x, int y, int width, int height, BitmapFont fnt, Material mat, Material matL, Material matD){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.name = name;
            this.valueMin = -100;
            this.valueMax = 100;
            this.value = 0;
            
            this.xBorder = 2;
            this.yBorder = 2;
            this.textWidth = 40;
            
            this.scrollFnt = fnt;
            this.guiMat = mat;
            this.guiDarkMat = matD;
            this.guiLightMat = matL;
        }
        
        public void buildGeometry(){
            Quad quad = new Quad(width + textWidth,height);
            scrollGeo = new Geometry("Scroll Geo", quad);
            scrollGeo.setLocalTranslation(0,0,0);
            scrollGeo.setMaterial(guiMat);
            
            trackGeo = new Geometry("Track Geo", new Quad(width - (2 * xBorder), 2));
            trackGeo.setLocalTranslation(textWidth + xBorder,height/2,0.5f);
            trackGeo.setMaterial(guiDarkMat);
            
            slideGeo = new Geometry("Slide Geo", new Quad(height/2, height - (2 * yBorder)));
            slideGeo.setLocalTranslation(textWidth + (width/2),yBorder,1f);
            slideGeo.setMaterial(guiLightMat);
            
            scrollText = new BitmapText(scrollFnt, false);
            scrollText.setBox(new Rectangle(0, 0, quad.getWidth(), quad.getHeight()));
            scrollText.setSize(scrollFnt.getPreferredSize() * 1);
            scrollText.setText(name);
            scrollText.setLocalTranslation(0,scrollText.getLineHeight(), 1.5f);
            
            scrollNode = new Node("Button Node");
            scrollNode.attachChild(scrollGeo);
            scrollNode.attachChild(trackGeo);
            scrollNode.attachChild(slideGeo);
            scrollNode.attachChild(scrollText);
            scrollNode.setLocalTranslation(x,y,0);
            
        }
        
        public Node getNode(){return this.scrollNode;}
        public Geometry getGeometry(){return this.scrollGeo;}
        
        public int updateValue(int xP){
            int result = xP - (textWidth + x + xBorder);
            value = (getValueRange() * result) / getPixelRange();
            // We need to move the slide to the coresponding value also!
            slideGeo.setLocalTranslation(result + textWidth,yBorder,1);
            return result;
        }
        public boolean checkBoundary(int xP, int yP){
            //textWidth
            if(xP > (textWidth + x + xBorder) && xP < (textWidth + x + (width - xBorder)) ){
                if(yP > (y) && yP < (y + (height)) ){
                    int res = updateValue(xP);
                    return true;
                }
            }
            return false;
        }
        
        public int getId(){return this.id;}
        public void setId(int id){this.id = id;}
        public float getValueRange(){return valueMax - valueMin;}
        public int getPixelRange(){return (x + (width - xBorder)) - (x + xBorder);}
        public float getValue(){return value;}
    }
