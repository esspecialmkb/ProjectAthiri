/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import static File.TextFileReading_Dev.fileName;
import GameAppStates.GameStateManager;
import Utility.SimplexNoise;
import com.jme3.app.Application;
import com.jme3.app.BasicApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.random;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TextureAtlasDev extends BasicApplication{
    public static String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    public static String mapDirectoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\MapData\\";
    public static String fileName = "test";
    public static String extension = ".txt";
    private TextureAtlas atlas;
    private Texture[] texture = new Texture[1024];
    private Material material;
    private Random random = new Random();
    private Geometry[][] tiles = new Geometry[40][40];
    private float time;
    
    private String[] landscapeFiles = {
        "Textures/Tiles/Landscape/Grass/Grass_1.png",
        "Textures/Tiles/Landscape/Grass/Grass_2.png",
        "Textures/Tiles/Landscape/Grass/Grass_3.png",
        "Textures/Tiles/Landscape/Grass/Grass_4.png",

        "Textures/Tiles/Landscape/Stone/Stone_1.png",
        "Textures/Tiles/Landscape/Stone/Stone_2.png",
        "Textures/Tiles/Landscape/Stone/Stone_3.png",
        "Textures/Tiles/Landscape/Stone/Stone_4.png",

        "Textures/Tiles/Landscape/Dirt/Dirt_1.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_2.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_3.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_4.png",

        "Textures/Tiles/Landscape/Water/Water_1.png",
        "Textures/Tiles/Landscape/Water/Water_2.png",
        "Textures/Tiles/Landscape/Water/Water_3.png"
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TextureAtlasDev app = new TextureAtlasDev();
        AppSettings settings = new AppSettings(false);
        
        settings.setFrameRate(60);
        //settings.setSettingsDialogImage("Interface/iconEA.png");
        settings.setTitle("Project Athiri (Texture Atlas Dev)- v.0");
        app.setSettings(settings);
        app.start();
    }
    
    public void buildTestCase(){
        atlas = new TextureAtlas(1024, 1024);

        String[] files = {
            "Textures/red.png",
            "Textures/green.png",
            "Textures/blue.png",
            "Textures/yellow.png"
        };

        for (int i = 0; i < 1024; i++) {
            texture[i] = assetManager.loadTexture(landscapeFiles[i % 14]);

            atlas.addTexture(texture[i], "ColorMap");
        }

        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", atlas.getAtlasTexture("ColorMap"));

        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 40; y++) {
                addTile(x, y);
            }
        }
    }
    
    public void updateTestCase(float tpf){
        time += tpf;

        if (time > 0.001f) {
            time -= 0.001f;

            int x = random.nextInt(40);
            int y = random.nextInt(40);

            tiles[x][y].removeFromParent();

            addTile(x, y);
        }
    }
    
    @Override
    public void simpleInitApp() {
        //buildTestCase();
        
        // Attaching GameStateManager force-loading a new Map
        stateManager.attach(new GameStateManager());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //updateTestCase(tpf);
    }

    private void addTile(int x, int y) {
        //Quad quad = new Quad(1, 1);
        Quad quad = new Quad(18, 18);

        tiles[x][y] = new Geometry("Tile", quad);
        //tiles[x][y].setLocalTranslation(x - 10, y - 10, -10);
        tiles[x][y].setLocalTranslation(x - 18, y - 18, -10);
        tiles[x][y].setMaterial(material);

        FloatBuffer buf = quad.getFloatBuffer(Type.TexCoord);
        atlas.getAtlasTile(texture[random.nextInt(1024)]).transformTextureCoords(buf, 0, buf);

        //rootNode.attachChild(tiles[x][y]);
        guiNode.attachChild(tiles[x][y]);
    }
    
    // GameStateManager was here...
    
    public class UIStateManager extends AbstractAppState{
        
        
    }
    
    //  The player class holds geo and position
    public class Player{
        private float tileX, tileY;
        private Geometry geo;
        private Node node;
    }    
    
    //  The TileMapManager loads/saves pages of tiles
        
    // Need to abstract map generation from map and dump data to a file
    
    
    public class TextElement{
        private BitmapText text;
        private Geometry geo;
        private Node node;
    }
    
}
