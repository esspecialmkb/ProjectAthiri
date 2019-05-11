/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAppStates;

import Cartography.MapGenerator;
import Cartography.TileMapManager;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class GameStateManager extends AbstractAppState{
        private TextureAtlas atlas;
        private Texture[] textureList;
        private Material material;
        private TileMapManager tileManager;
        private MapGenerator mapGenerator;
        
        private String[] terrainTextureList = {
            "Textures/Tiles/Landscape/Grass/Grass_1.png",   // 0
            "Textures/Tiles/Landscape/Grass/Grass_2.png",
            "Textures/Tiles/Landscape/Grass/Grass_3.png",
            "Textures/Tiles/Landscape/Grass/Grass_4.png",

            "Textures/Tiles/Landscape/Stone/Stone_1.png",   // 4
            "Textures/Tiles/Landscape/Stone/Stone_2.png",
            "Textures/Tiles/Landscape/Stone/Stone_3.png",
            "Textures/Tiles/Landscape/Stone/Stone_4.png",

            "Textures/Tiles/Landscape/Dirt/Dirt_1.png",     // 8
            "Textures/Tiles/Landscape/Dirt/Dirt_2.png",
            "Textures/Tiles/Landscape/Dirt/Dirt_3.png",
            "Textures/Tiles/Landscape/Dirt/Dirt_4.png",

            "Textures/Tiles/Landscape/Water/Water_1.png",   // 12
            "Textures/Tiles/Landscape/Water/Water_2.png",
            "Textures/Tiles/Landscape/Water/Water_3.png"
        };
        
        private String[] architectureTextureList = {
            "Textures/Tiles/Arcitecture/Walls/Wall_Blank.png",
            "Textures/Tiles/Arcitecture/Walls/Wall_Bottom_Cap.png"
        };    
        
        private String[] playerTextureList = {
            "Textures/Player/East/Stand_East.png",
            "Textures/Player/East/Run_1_East.png",
            "Textures/Player/East/Run_2_East.png",
            "Textures/Player/East/Run_3_East.png",
            "Textures/Player/East/Run_4_East.png",
            "Textures/Player/East/Attack_East.png",
            
            "Textures/Player/North/Stand_North.png",
            "Textures/Player/North/Run_1_North.png",
            "Textures/Player/North/Run_2_North.png",
            "Textures/Player/North/Run_3_North.png",
            "Textures/Player/North/Run_4_North.png",
            "Textures/Player/North/Attack_North.png",
            
            "Textures/Player/South/Stand_South.png",
            "Textures/Player/South/Run_1_South.png",
            "Textures/Player/South/Run_2_South.png",
            "Textures/Player/South/Run_3_South.png",
            "Textures/Player/South/Run_4_South.png",
            "Textures/Player/South/Attack_South.png",
            
            "Textures/Player/West/Stand_West.png",
            "Textures/Player/West/Run_1_West.png",
            "Textures/Player/West/Run_2_West.png",
            "Textures/Player/West/Run_3_West.png",
            "Textures/Player/West/Run_4_West.png",
            "Textures/Player/West/Attack_West.png"
        };
                
        public void setTerrainTextureList(String[] list){
            //this.terrainTextureList = list;
        }
        
        @Override
        public void initialize(AppStateManager stateManager, Application app){
            // Load Textures
            /*atlas = new TextureAtlas(1024, 1024);
            textureList = new Texture[terrainTextureList.length];*/
            
            /*for(int i=0; i<terrainTextureList.length; i++){
            this.textureList[i] = app.getAssetManager().loadTexture(this.terrainTextureList[i]);
            this.atlas.addTexture(textureList[i], "ColorMap");
            
            }*/
            
            tileManager = new TileMapManager(app.getAssetManager());
            
            /*this.material = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
            this.material.setTexture("ColorMap",this.atlas.getAtlasTexture("ColorMap"));*/
                        
            // Map Generation
            mapGenerator = new MapGenerator(64);
            mapGenerator.setFrequency(0.5);
            mapGenerator.setOctave(0.5);
            mapGenerator.setDivision(24);
            mapGenerator.setExponent(1.2);
            
            mapGenerator.generate();
            
            // Create MapPages
            //tileManager = new TileMapManager();
            //tileManager.generateMap(mapGenerator, this.atlas);
        }
        
        @Override
        public void update(float tpf){
            
        }
    }
