/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.States;

import DevFork.OpenSimplexNoise;
import DevFork.Controls.Entity;
import DevFork.Controls.Player;
import DevFork.Tiles.TileChunk;
import DevFork.Vector2i;
import com.jme3.app.Application;
import com.jme3.app.BasicApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  The WorldManagerState maintains the active TileChunks and Humanoids
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class WorldManagerState extends AbstractAppState{
    String directoryName = "C:\\Users\\esspe\\Documents\\jMonkeyProjects\\ProjectAthiri\\database\\";
    BasicApplication app;
    
    // Should 'Final' keyword be used for guiNode, rootNode and Camera?
    Node guiNode;
    Node world;
    
    AssetManager assetManager;
    Material tileMat;
    
    //  State control
    int state = -1;
    int nextState = -1;
    
    //  Responsibility #1 TileChunks
    boolean useList = false;
    ArrayList<TileChunk> chunkList;
    HashMap<Vector2i, TileChunk> chunkMap;  // HashMap using Vector2i for integer chunk positions
    ArrayList<Vector2f> savedChunks;
    int chunkLoadX;
    int chunkLoadY;
    
    float tileSize;
    
    OpenSimplexNoise noise;
    long seed;
    float FEATURE_SIZE = 64;
    float FREQ = 0.5f;
    float OCT = 0.5f;
    
    //  Respsonibility #2 Map Position
    float MOV_X_RAW;
    float MOV_Y_RAW;
    float mapX; // This is the map's intended location in the center in tile units. Directly manipulated
    float mapY;
    float mapScreenX;   // mapX * tileSize; used to position the tile chunks on the screen. Calculated from map position
    float mapScreenY;
    int chunkX;
    int chunkY;
    
    float screenWidth, screenHeight;    // In-game screen resolution, allows main screen to be a different size
    float camOffsetX, camOffsetY;       // mapScreen = (mapX + camOffset) * tileSize; Used for camera shake and indepent camera movement
    float sceneRootPosX, sceneRootPosY; // Used to calculate global movement relative to viewport origin;
    
    //  Responsibility #3 Input implemenation
    boolean[] inputBuffer  = new boolean[8];
    
    //  Responsibility #4 Entity tracking
    Player player;
    ArrayList<Entity> entityList;
    
    public WorldManagerState(){
        this.tileSize = 24;
    }
    
    public WorldManagerState(Node guiNode, float tileSize){
        this.tileSize = tileSize;
        this.guiNode = guiNode;
    }
    
    //  Utility methods
    public void setInputBuffer(boolean input[]){
        this.inputBuffer = input;
    }
    public void setResolution(float width, float height){
        this.screenWidth = width;
        this.screenHeight = height;
    }
    public void setMapPosition(float x, float y){
        this.mapX = x;
        this.mapY = y;
        
        this.mapScreenX = this.mapX * tileSize;
        this.mapScreenY = this.mapY * tileSize;
        
        this.sceneRootPosX = (this.mapScreenX) - (screenWidth/2);
        this.sceneRootPosY = (this.mapScreenY) - (screenHeight/2);
        updateMapPosition();
    }
    public void updateMapPosition(){
        if(useList){

            }else{

            }
        for (TileChunk chunk : chunkList) {
            chunk.getNode().setLocalTranslation( (chunk.getX() * 16 * tileSize)- this.sceneRootPosX ,(chunk.getY() * 16 * tileSize)-this.sceneRootPosY,0);
        }
    }
    
    //  -----------------------------------------------------
    //  INITIALIZATION
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        // Basic setup
        this.app = (BasicApplication) app;
        assetManager = this.app.getAssetManager();
        assetManager.registerLocator(directoryName, FileLocator.class);
        
        //  Build chunks
        generateMap(8,8);
        //Player setup
        this.createPlayer();
                
        //setMapPosition(60,0);
        
        world.move(0,0,-10);
        guiNode.attachChild(world);
        
        // Testing file save
        saveAllChunks();
    }
    
    //  Methods used by initialize()
    private void generateMap(int xSize, int ySize){
        //  Seed for generator
        seed = (long) (Math.random() *1000);
        noise = new OpenSimplexNoise(seed);
        
        //  Materials
        tileMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        tileMat.setBoolean("VertexColor", true);
        tileMat.setColor("Color", ColorRGBA.White);
        
        //  World node
        world = new Node("World");
        sceneRootPosX = 0;
        sceneRootPosY = 0;
        
        mapScreenX = sceneRootPosX + (screenWidth/2);
        mapScreenY = sceneRootPosY + (screenHeight/2);
        
        mapX = mapScreenX / tileSize;
        mapY = mapScreenY / tileSize;
        
        //  Chunk list
        if(useList){

        }else{

        }
        chunkList = new ArrayList<>();
        chunkMap = new HashMap<>();
        savedChunks = new ArrayList<>();
        
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                //  Create chunk object
                TileChunk chunk = new TileChunk(x,y, tileSize);
                chunk.setState(1);
                
                //  Generate terrain tiles
                chunk.generateChunkTerrain(noise, FEATURE_SIZE);
                chunk.setState(2);
                
                //  Build chunk meshes
                chunk.setTileMat(tileMat);
                chunk.buildMesh();
                chunk.setState(3);
                world.attachChild(chunk.getNode());
                chunk.setState(4);
                
                if(useList){
                    chunkList.add(chunk);
                }else{
                    chunkMap.put(new Vector2i(x, y), chunk);
                }
                saveTileChunk(chunk);
            }
        }
    }
    private void createPlayer(){
        //  Manually build the player
        player = new Player();
        player.setLocation(mapX, mapY);
        
        int chunkX = (int) Math.floor(mapX/16);
        int chunkY = (int) Math.floor(mapY/16);
        
        System.out.println(chunkX + ", " + chunkY);
        if(useList){
            for (TileChunk chunk : chunkList) {
                if((chunk.getX() == chunkX) && (chunk.getY() == chunkY)){
                    player.buildEntity(chunk, tileMat);

                    break;
                }
            }
        }else{
            chunkMap.forEach((pos, chunk) -> {
                if((chunk.getX() == chunkX) && (chunk.getY() == chunkY)){
                    player.buildEntity(chunk, tileMat);

                }
            });
        }
        
        
        world.attachChild(player.getNode());
        player.getNode().setLocalTranslation(screenWidth/2, screenHeight/2,0);
    }
    private void saveAllChunks(){
        if(useList){
            for(TileChunk chunk: chunkList){
                saveTileChunk(chunk);
            }
        }else{
            
            chunkMap.forEach((position, chunk) -> {
                saveTileChunk(chunk);
            });
        }
        
    }
    
    //  Method used by generateMap()
    private void saveTileChunk(TileChunk chunk){
        
        String userHome = System.getProperty("user.home");
        
        userHome += "/ProjectAthiri";
        
        System.out.println("Save Chunk");
        
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome+"/LastMap/Chunks/"+"Chunk_" + chunk.getX() + "_" + chunk.getY() + ".j3o");
        
        System.out.println(userHome+"/LastMap/Chunks/"+"Chunk_" + chunk.getX() + "_" + chunk.getY() + ".j3o");
        try {
            exporter.save(chunk, file);
        } catch (IOException ex) {
            Logger.getLogger(WorldManagerState.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
        }
    }
    
    //  ---------------------------------------------------
    //  UPDATE LOOP
    @Override
    public void update(float tpf) {
        // Check for player input mask
        float movSpeed = 6;
        float movX = 0; 
        float movY = 0;
        
        //  Calculate current chunk location
        chunkX = (int)Math.floor(mapX / 16);
        chunkY = (int)Math.floor(mapY / 16);
        
        //System.out.println("current chunk: " + chunkX + ", " + chunkY);
        
        if(inputBuffer[0]){
            movY = -(tpf * movSpeed) * this.tileSize;
        }if(inputBuffer[1]){
            movY = (tpf * movSpeed) * this.tileSize;
        }if(inputBuffer[2]){
            movX = (tpf * movSpeed) * this.tileSize;
        }if(inputBuffer[3]){
            movX = -(tpf * movSpeed) * this.tileSize;
        }
        
        // Chunk processing -> Control candidate!
        // movX and Y based on tileSize Units (Screen Pixels per Tile)
        moveMap(movX, movY);
        
        updateChunks();
    }
    
    //  Methods used by update()
    private void moveMap(float movX, float movY){
        if(useList){
            for (TileChunk chunk : chunkList) {
                chunk.getNode().move(movX, movY, 0);
                chunk.move(movX, movY);
            }
        }else{
            chunkMap.forEach((position, chunk) -> {
                chunk.getNode().move(movX, movY, 0);
                chunk.move(movX, movY);
            });
        }
        
        
        // Update screen coordinates
        updateCoordinates(movX, movY);
    }
    private void updateChunks(){
        //chunkX = (int) Math.floor(mapX / 16);
        //chunkY = (int) Math.floor(mapY / 16);
        // We need to update the chunk's state
        if(useList){
            for( TileChunk chunk : chunkList){
                switch(chunk.getState()){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                if((chunk.getX() == chunkX) && (chunk.getY() == chunkY)){
                    // This is the chunk that the player is in
                }
            }
        }else{
            chunkMap.forEach((position, chunk) -> {
                switch(chunk.getState()){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                if((chunk.getX() == chunkX) && (chunk.getY() == chunkY)){
                    // This is the chunk that the player is in
                }
            });   
        }
        
        
    }
    
    //  Method used by moveMap()
    private void updateCoordinates(float movX, float movY){
        // Update the sceneRootPos
        sceneRootPosX -= movX;
        sceneRootPosY -= movY;
        
        mapScreenX = sceneRootPosX + (screenWidth/2);
        mapScreenY = sceneRootPosY + (screenHeight/2);
        
        mapX = mapScreenX / tileSize;
        mapY = mapScreenY / tileSize;
        //System.out.println("Map Location: " + mapX + ", " + mapY);
        //System.out.println("Chunk (0) Translation: " + chunkList.get(0).getNode().getLocalTranslation());
    }
    //  --------------------------------------------------
    
    
    //  TileChunk operations
    private void generateTowns(){
        //  Randomly distribute a set of points from map Min to Max
        ArrayList<Vector2f> pointsList = new ArrayList<>();
        for(int p = 0; p < 20; p++){
            pointsList.add( new Vector2f( (int)(Math.random() * (7 * 16)) + 16, (int)(Math.random() * (7 * 16)) + 16));
        }
        
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
    
    //
    // Creating interface to interact with the WorldManagerState
    public interface WorldStateEntity{
        public void update(float tpf);
        public void addEvent(int flag, int value, Entity entity);
    }
}
