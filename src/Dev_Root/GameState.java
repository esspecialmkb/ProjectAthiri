/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dev_Root;

// TODO LIST:
//  1.  Refactor this class into GameRunningAppState
//  2.  Implement a WorldManagerState to maintain TileChunk life-cycle and CameraController
//  3.  Implement PhysicsAppState

import DevFork.OpenSimplexNoise;
import Utility.SimplexNoise;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class GameState extends AbstractAppState{
    String gameName;
    OpenSimplexNoise noise;
    float FEATURE_SIZE = 64;
    float FREQ = 0.5f;
    float OCT = 0.5f;

    InputManager inputManager;
    int chunkSize;
    int tileSize;
    int screenWidth;
    int screenHeight;
    int worldX;
    int worldY;
    MapGen mapGen;
    ArrayList<TileChunk> tileChunks;
    
    Humanoid player;

    //  Stuff for textures
    private TextureAtlas atlas;
    private Texture[] texture = new Texture[64];
    private Material material;

    //  Stuff for scene graph
    Node gui;
    Node world;
    Material tileMat;
    Material playerMat;
    
    boolean[] movement = new boolean[4];

    public GameState(Node gui){ 
        this.gui = gui;
        world = new Node("World");
        gui.attachChild(world);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        screenHeight = app.getCamera().getHeight();
        screenWidth = app.getCamera().getWidth();
        tileSize = screenHeight / 10;
        
        noise = new OpenSimplexNoise( (int) (Math.random() *1000) );
        
        //  Populate the TextureAtlas
        for(int t = 0; t < landscapeFiles.length; t++ ){
            //texture[t] = app.getAssetManager().loadTexture(landscapeFiles[t]);
            //atlas.addTexture(texture[t], "ColorMap");
        }

        //  Create the tile material
        //material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //material.setTexture("ColorMap", atlas.getAtlasTexture("ColorMap"));
        
        tileMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        tileMat.setBoolean("VertexColor", true);
        tileMat.setColor("Color", ColorRGBA.White);
        
        //  Prepare the TileChunk
        tileChunks = new ArrayList<>();
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                //TileChunk chunk = new TileChunk(x,y);
                //chunk.generateChunkTerrain();
                tileChunks.add(new TileChunk(x,y));
            }
        }

        //tileChunks.add(new TileChunk(1,1));

        for(int i = 0; i < tileChunks.size(); i++){
            tileChunks.get(i).generateChunkTerrain();
        }

        System.out.println(tileChunks.size() + " TileChunks");
        for(int i = 0; i < tileChunks.size(); i++){
            tileChunks.get(i).buildMesh();

            tileChunks.get(i).getNode().setQueueBucket(RenderQueue.Bucket.Gui);
            world.attachChild(tileChunks.get(i).getNode());
        }

        //setDisplayStatView(false);

        //  Generate chunk data
        //mapGen = new MapGen();

        //  Write chunk data

        //  Player
        playerMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        playerMat.setBoolean("VertexColor", true);
        playerMat.setColor("Color", ColorRGBA.Orange);
        
        Material shadowMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        shadowMat.setBoolean("VertexColor", true);
        shadowMat.setColor("Color", ColorRGBA.Black);
        
        player = new Humanoid();
        player.setScreenDim(screenWidth, screenHeight);
        player.buildPlayer(tileSize);
        player.getGeomtery().setMaterial(playerMat);
        player.getShadowGeomtery().setMaterial(playerMat);
        
        gui.attachChild(player.getNode());
        
        //  Input
        movement[0] = false;
        movement[1] = false;
        movement[2] = false;
        movement[3] = false;
        inputManager = app.getInputManager();
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));

        //public void addListener(InputListener listener, String... mappingNames)
        inputManager.addListener(actionListener, "Up", "Down", "Left", "Right");
    }

    public void preLoad(){

    }

    public void create(){

    }

    public void getChunk(int x, int y){

    }

    @Override
    public void update(float tpf) {
        if(movement[0]){
            world.move(0,-tpf * tileSize * 6,0);
        }if(movement[1]){
            world.move(0,tpf * tileSize * 6,0);
        }if(movement[2]){
            world.move(tpf * tileSize * 6,0,0);
        }if(movement[3]){
            world.move(-tpf * tileSize * 6,0,0);
        }
    }

    private ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean keyPressed, float tpf){
            System.out.println(name + " Input " + keyPressed);
            if(name.equals("Up")){
                movement[0] = keyPressed;
            }
            if(name.equals("Down")){
                movement[1] = keyPressed;
            }
            if(name.equals("Left")){
                movement[2] = keyPressed;
            }
            if(name.equals("Right")){
                movement[3] = keyPressed;
            }

        }
    };

    double genLowValue = 0;
    double genHighValue = 0;
    double genDeltaValue = 0;
    
    public class Humanoid{
        Geometry geoBody;
        Geometry geoShadow;
        Geometry head;
        
        //  Dimensions
        float headWidth_Half = 0.5f;
        float headHeight_Half = 0.5f;
        float headDrop = 0.125f;
        float bodyWidth_Half = 0.75f;
        float bodyHeight = 2f;
        float bodyHeightSlant = 0.25f;
        float legWidth = 0.5f;
        float legHeight = 1;
        
        //  Flags
        boolean legsPresent = true;
        boolean headPresent = true;
        short[] legsIndex;
        short[] headIndex;
        
        Node node;
        VirtualMesh bodyMesh;
        VirtualMesh shadowMesh;
        VirtualMesh waveMesh;
        
        float mapX, mapY;
        float scrnW, scrnH;
        float tileSize;
        
        public void setScreenDim(int w, int h){
            scrnW = w;
            scrnH = h;
        }
        public Node getNode(){ return this.node; }
        public Geometry getGeomtery(){ return this.geoBody; }
        public Geometry getShadowGeomtery(){ return this.geoShadow; }
        public void setBodyMat(Material mat){ this.geoBody.setMaterial(material);}
        public void setShadowMat(Material mat){ this.geoShadow.setMaterial(material);}
        private void buildShadow(){
            shadowMesh = new VirtualMesh();
            
            shadowMesh.addVertex(new Vector3f(-1    ,0      ,0));
            shadowMesh.addVertex(new Vector3f(-0.5f ,0.5f   ,0));
            shadowMesh.addVertex(new Vector3f(0.5f  ,0.5f   ,0));
            shadowMesh.addVertex(new Vector3f(1     ,0      ,0));
            shadowMesh.addVertex(new Vector3f(0.5f  ,-0.5f  ,0));
            shadowMesh.addVertex(new Vector3f(-0.5f ,-0.5f  ,0));
            
            shadowMesh.addIndex((short) 1, (short) 0, (short) 5);
            shadowMesh.addIndex((short) 1, (short) 5, (short) 4);
            shadowMesh.addIndex((short) 4, (short) 2, (short) 1);
            shadowMesh.addIndex((short) 2, (short) 4, (short) 3);
            
            for(int v = 0; v < shadowMesh.getVertexCount(); v++){
                shadowMesh.addColor(0.2f, 0.2f, 0.2f, 0.8f);
                
                shadowMesh.addNormal(new Vector3f(0,0,1) );
            }
            shadowMesh.buildMesh();
        }
        private void buildAtkWave(){
            waveMesh = new VirtualMesh();
            
            waveMesh.addVertex(new Vector3f(0,2,0));
            waveMesh.addVertex(new Vector3f(1,2,0));
            waveMesh.addVertex(new Vector3f(2,1,0));
            waveMesh.addVertex(new Vector3f(2,-1,0));
            waveMesh.addVertex(new Vector3f(1,-2,0));
            waveMesh.addVertex(new Vector3f(0,-2,0));
            waveMesh.addVertex(new Vector3f(1,-1,0));
            waveMesh.addVertex(new Vector3f(1,1,0));
            
            waveMesh.addIndex((short) 1, (short) 0, (short) 7 );
            waveMesh.addIndex((short) 1, (short) 7, (short) 2 );
            waveMesh.addIndex((short) 2, (short) 7, (short) 6 );
            waveMesh.addIndex((short) 6, (short) 3, (short) 2 );
            waveMesh.addIndex((short) 6, (short) 5, (short) 4 );
            waveMesh.addIndex((short) 4, (short) 3, (short) 6 );
            
            for(int v = 0; v < waveMesh.getVertexCount(); v++){
                waveMesh.addColor(0.9f, 0.9f, 0.9f, 0.9f);
                
                waveMesh.addNormal(new Vector3f(0,0,1) );
            }
            waveMesh.buildMesh();
        }
        private void buildBody(){
            bodyMesh = new VirtualMesh();
            
            //  Upper body
            bodyMesh.addVertex(new Vector3f(0      ,(bodyHeight + legHeight)      ,0));
            bodyMesh.addVertex(new Vector3f(-bodyWidth_Half ,(bodyHeight + legHeight) - bodyHeightSlant  ,0));
            bodyMesh.addVertex(new Vector3f(bodyWidth_Half  ,(bodyHeight + legHeight) - bodyHeightSlant  ,0));
            
            bodyMesh.addIndex((short) 0, (short) 1, (short) 2);
            
            //  Lower body
            bodyMesh.addVertex(new Vector3f(-bodyWidth_Half ,legHeight + bodyHeightSlant  ,0));
            bodyMesh.addVertex(new Vector3f(bodyWidth_Half  ,legHeight + bodyHeightSlant  ,0));
            bodyMesh.addVertex(new Vector3f(0      ,legHeight      ,0));
            
            bodyMesh.addIndex((short) 3, (short) 5, (short) 4);
            
            //  Mid body
            bodyMesh.addIndex((short) 2, (short) 1, (short) 3);
            bodyMesh.addIndex((short) 3, (short) 4, (short) 2);
            
            if(legsPresent){
                short offset = (short) bodyMesh.getVertexCount();
                //  Left foot
                bodyMesh.addVertex(new Vector3f(-bodyWidth_Half ,0,0));
                bodyMesh.addVertex(new Vector3f(-(bodyWidth_Half - legWidth) ,0,0));
                
                bodyMesh.addIndex((short) 3, (short) (0 + offset), (short) (1 + offset));
                bodyMesh.addIndex((short) (1 + offset), (short) 5, (short) 3);

                //  Right foot
                bodyMesh.addVertex(new Vector3f(bodyWidth_Half - legWidth  ,0,0));
                bodyMesh.addVertex(new Vector3f(bodyWidth_Half  ,0,0));
                
                bodyMesh.addIndex((short) 5, (short) (2 + offset), (short) 4);
                bodyMesh.addIndex((short) (2 + offset), (short) (3 + offset), (short) 4);
            }
            if(headPresent){
                short offset = (short) bodyMesh.getVertexCount();
                //  Head
                bodyMesh.addVertex(new Vector3f(-headWidth_Half             ,(bodyHeight + legHeight) + (headHeight_Half * 2) - headDrop    ,0));
                bodyMesh.addVertex(new Vector3f(-headWidth_Half             ,(bodyHeight + legHeight) - headDrop                            ,0));
                bodyMesh.addVertex(new Vector3f(headWidth_Half              ,(bodyHeight + legHeight) - headDrop                            ,0));
                bodyMesh.addVertex(new Vector3f(headWidth_Half              ,(bodyHeight + legHeight) + (headHeight_Half * 2) - headDrop    ,0));
                
                bodyMesh.addIndex((short) (3 + offset), (short) (0 + offset), (short) (1 + offset));
                bodyMesh.addIndex((short) (1 + offset), (short) (2 + offset), (short) (3 + offset));
            }
            
            for(int v = 0; v < bodyMesh.getVertexCount(); v++){
                bodyMesh.addColor(1, 0, 0, 1);
                
                bodyMesh.addNormal(new Vector3f(0,0,1) );
            }
            
            bodyMesh.buildMesh();
        }
        public void buildPlayer(float size){
            this.tileSize = size;
            
            buildBody();
            buildShadow();
            
            geoBody = new Geometry("Body", bodyMesh.getMesh());
            geoBody.setLocalScale((size * 2)/6);
            
            geoShadow = new Geometry("Shadow", shadowMesh.getMesh());
            geoShadow.setLocalScale((size)/3);
            geoShadow.setLocalTranslation(0,0,-1);
            
            node = new Node("Player");
            node.attachChild(geoBody);
            node.attachChild(geoShadow);
            node.setLocalTranslation(scrnW/2,scrnH/2,1);
        }
    }

    //  Exported
    public class TileChunk{
        int x;
        int y;
        double[][] tileData;
        int[][] colData;
        Tile[][] tiles;
        //Node tileChunkNode;
        boolean isLoaded;

        //  Scene element
        VirtualMesh m;
        Geometry g;
        Node n;
        public TileChunk(){
            isLoaded = false;
        }
        public TileChunk(int x, int y){
            this.x = x;
            this.y = y;
            tileData = new double[16][16];
            colData = new int[16][16];
            tiles = new Tile[16][16];
            m = new VirtualMesh();
            //isLoaded = false;
        }
        public void unloadChunk(){
            if(isLoaded){
                //clear tile array

                isLoaded = false;
            }
        }

        public void loadChunk(){
            if(!isLoaded){
                // Load the chunk data
                for(int iX = 0; iX < 24; iX++){
                    for(int iY = 0; iY < 24; iY++){
                        //int tileX = (this.x * (chunkSize * tileSize)) + (x * tileSize);
                        //int tileY = (this.y * (chunkSize * tileSize)) + (y * tileSize);

                        // Generate or load tile data
                        // var perlinValue = noise.perlin2(tileX / 100, tileY / 100); 

                        // var key = “”;
                        // var animationKey = “”;
                    }
                }
            }
        }
        public Node getNode(){return n;}
        public void generateChunkTerrain(){
            //if(!isLoaded){
                //tileData = new double[16][16];
                // Load the chunk data
                for(int iX = 0; iX < 16; iX++){
                    for(int iY = 0; iY < 16; iY++){
                        int tileX = (this.x * (16)) + (iX);
                        int tileY = (this.y * (16)) + (iY);
                        // Generate or load tile data
                        // var perlinValue = noise.perlin2(tileX / 100, tileY / 100);
                        double value = SimplexNoise.noise(tileX / FEATURE_SIZE,tileY / FEATURE_SIZE,0) + 0.5d;


                        //System.out.println("ChunkGen " + x + ", " + y + ": Tile "+ iX +", "+ iY + ": " + value);
                        //System.out.println("ChunkGen " + tileX + ", " + tileY + ": " + value);

                        tileData[iX][iY] = ((value * 100));

                        //System.out.println("POSTGen " + tileX + ", " + tileY + ": " + value);

                        if( tileData[iX][iY] < genLowValue){
                            genLowValue = tileData[iX][iY];
                        }else if( tileData[iX][iY] > genHighValue){
                            genHighValue = tileData[iX][iY];
                        }

                        // var key = “”;
                        // var animationKey = “”;
                    }
                }
                isLoaded = true;
            //}
        }
        public void buildMesh(){
            double range = genHighValue - genLowValue;
            if(genLowValue < 0){
                genDeltaValue = 0 -genLowValue;
            }else{
                genDeltaValue = genLowValue;
            }
            //System.out.println("Low val: " + genLowValue);
            //System.out.println("High val: " + genHighValue);
            //System.out.println("Range: " + range);

            //if(isLoaded){
                for(int x =0; x < 16; x++){
                    for(int y = 0; y < 16; y++){
                        double value = (tileData[x][y] - genLowValue);
                        value = (float)((value / range) * 100);
                        tiles[x][y] = new Tile(x,y,tileSize);
                        //value =value + 10;

                        //System.out.println(x + ", " + y + " Build value: " + value);

                        if(value < 10){
                            //
                            tiles[x][y].setColor(ColorRGBA.Blue);
                            colData[x][x] = 0;

                        }else if((value >= 10) && (value < 20)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Cyan);
                            colData[x][x] = 0;

                        }else if((value >= 20) && (value < 30)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Yellow);
                            colData[x][x] = 0;

                        }else if((value >= 30) && (value < 40)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Green);
                            colData[x][x] = 0;

                        }else if((value >= 40) && (value < 50)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Green);
                            colData[x][x] = 0;

                        }else if((value >= 50) && (value < 60)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Green);
                            colData[x][x] = 0;

                        }else if((value >= 60) && (value < 70)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Brown);
                            colData[x][x] = 0;

                        }else if((value >= 70) && (value < 80)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Brown);
                            colData[x][x] = 0;

                        }else if((value >= 80) && (value < 90)){
                            //
                            tiles[x][y].setColor(ColorRGBA.Black);
                            colData[x][x] = 0;

                        }else if((value >= 90) /*&& (value < 100)*/){
                            //
                            tiles[x][y].setColor(ColorRGBA.White);
                            colData[x][x] = 0;

                        }

                        tiles[x][y].createTile(m);

                    }
                }

                m.buildMesh();
                n = new Node();
                n.setLocalTranslation(x*16*tileSize,y*16*tileSize,0);
                g = new Geometry("Chunk", m.getMesh());
                g.setMaterial(tileMat);
                n.attachChild(g);
            //}                
        }
    }// End of TileChunk.class

    //  Exported
    public class Tile{
        // TODO add Tile Animation
        //Quad quad;
        //Geometry geo;
        int x;
        int y;
        int size;
        ColorRGBA color;

        public Tile(int x, int y, int size){
            this.x = x;
            this.y = y;
            this.size = size;
        }
        public void setColor(ColorRGBA c){
            //System.out.println("Tile "+ x + ", "+ y + "Color: "+ c.toString());
            color = c;
        }

        public void createTile(VirtualMesh mesh){
            int indexOffset = mesh.getVertexCount();
            mesh.addVertex(new Vector3f(x * size        ,  y * size,0));
            mesh.addVertex(new Vector3f(((x + 1) * size), (y * size),0));
            mesh.addVertex(new Vector3f((x * size)      ,((y + 1) * size),0));
            mesh.addVertex(new Vector3f(((x + 1) * size),((y + 1) * size),0));

            mesh.addIndex((short)(2 + indexOffset), (short)(0 + indexOffset), (short)(1 + indexOffset));
            mesh.addIndex((short)(1 + indexOffset), (short)(3 + indexOffset), (short)(2 + indexOffset));

            mesh.addNormal(new Vector3f(0,0,-1));
            mesh.addNormal(new Vector3f(0,0,-1));
            mesh.addNormal(new Vector3f(0,0,-1));
            mesh.addNormal(new Vector3f(0,0,-1));

            mesh.addColor(color.r, color.g, color.b, color.a);
            mesh.addColor(color.r, color.g, color.b, color.a);
            mesh.addColor(color.r, color.g, color.b, color.a);
            mesh.addColor(color.r, color.g, color.b, color.a);
        }

    }

    public class VirtualMesh{
        //  Mesh
        ArrayList<Vector3f> vert;
        ArrayList<Vector3f> norm;
        ArrayList<Vector2f> tCoords;
        ArrayList<Short> index;
        ArrayList<Float> color;

        boolean colors = true;

        Mesh m;
        //Geometry geo;
        Material mat;

        VirtualMesh(){
            vert = new ArrayList<>();
            norm = new ArrayList<>();
            index = new ArrayList<>();
            color = new ArrayList<>();
            //tCoords = new ArrayList<>();
        }

        public Mesh getMesh(){ return m; }
        public Material getMaterial(){ return mat; }
        public int getVertexCount(){ return vert.size(); }

        public void addVertex(Vector3f vert){ this.vert.add(vert); }
        public void addNormal(Vector3f norm){ this.norm.add(norm); }
        public void addTCoord(Vector2f coord){ tCoords.add(coord); }
        public void addIndex(short a, short b, short c){ 
            index.add(a);
            index.add(b);
            index.add(c);
        }
        public void addColor(float r, float g, float b, float a){
            color.add(r);
            color.add(g);
            color.add(b);
            color.add(a);
        }

        public void clearMesh(){
            vert.clear();
            norm.clear();
            tCoords.clear();
            color.clear();
            index.clear();
        }
        public void buildMesh(){
            m = new Mesh();

            Vector3f[] vertices = vert.toArray(new Vector3f[ vert.size() ]);
            Vector3f[] normals = norm.toArray(new Vector3f[ norm.size() ]);

            //Float[] colorIndex = color.toArray(new Float[ color.size() ]);
            //Short[] indices = index.toArray( new Short[ index.size() ]);

            short[] triIndex = new short[index.size()];
            for(int i = 0; i < index.size(); i++){
                triIndex[i] = index.get(i);
            }


            if(colors == true){
                //mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                //mat.setBoolean("VertexColor", true);
                //mat.setColor("Color", ColorRGBA.White);

                float[] colorIndex = new float[color.size()];
                for(int c = 0; c < color.size(); c++){
                    colorIndex[c] = color.get(c);
                }

                m.setBuffer(VertexBuffer.Type.Color, 4, colorIndex);
                //mat.getAdditionalRenderState().setWireframe(true);
            }else{
                //Vector2f[] texCoords = tCoords.toArray(new Vector2f[ tCoords.size() ]);
                //m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
                //mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                //mat.setBoolean("VertexColor", true);
                //mat.setColor("Color", ColorRGBA.Blue);
            }

            // Setting buffers for independent mesh
            m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));

            m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(triIndex));

            m.updateBound();
        }
    }

    private String[] landscapeFiles = {
        "Textures/Tiles/Landscape/Water/Water_1.png",
        "Textures/Tiles/Landscape/Water/Water_2.png",
        "Textures/Tiles/Landscape/Water/Water_3.png",

        "Textures/Tiles/Landscape/Dirt/Dirt_1.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_2.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_3.png",
        "Textures/Tiles/Landscape/Dirt/Dirt_4.png",

        "Textures/Tiles/Landscape/Grass/Grass_1.png",
        "Textures/Tiles/Landscape/Grass/Grass_2.png",
        "Textures/Tiles/Landscape/Grass/Grass_3.png",
        "Textures/Tiles/Landscape/Grass/Grass_4.png",

        "Textures/Tiles/Landscape/Stone/Stone_1.png",
        "Textures/Tiles/Landscape/Stone/Stone_2.png",
        "Textures/Tiles/Landscape/Stone/Stone_3.png",
        "Textures/Tiles/Landscape/Stone/Stone_4.png"            
    };

    public class ChunkPrototype{
        int[][] elevation;
        int[][] moisture;
        int[][] temperature;
        double[][] perlinNoise;
        int x,y;
        int currentX, currentY, size;
        int proc;

        public ChunkPrototype(int x, int y){
            this.x = x;
            this.y = y;
            currentX = 0;
            currentY = 0;
            proc = 0;
            size = 24;
        }

        public float getProc(){ return  (proc/(size * size)) * 100;} 

        public void update(int numElements){
            updateElevation(numElements);               

        }

        public void updateElevation(int numElements){
            for(int iX = currentX; iX < 24; iX++){
                for(int iY = currentY; iY < 24; iY++){
                    //int tileX = (this.x * (chunkSize * tileSize)) + (x * tileSize);
                    //int tileY = (this.y * (chunkSize * tileSize)) + (y * tileSize);
                    // Generate or load tile data
                    // var perlinValue = noise.perlin2(tileX / 100, tileY / 100);
                    //double noise = SimplexNoise.noise(iY, iY) + 1;
                    double value = noise.eval(iX + (x *24)/ FEATURE_SIZE, iY + (x *24)/ FEATURE_SIZE, 0.0);

                    // We need to save the value to build the tile map later in the GameState app
                    perlinNoise[iX][iY] = value;
                    numElements--;
                    proc++;

                    if(numElements < 0){
                        currentY = iY;
                        break;
                    }
                }
                if(numElements < 0){
                        currentY = iX;
                        break;
                }
            } // End of chunk Loop
        }
    }

    //Whit the MapGen state being internal to game state, we should be able to force load the firat chunks
    public class MapGen extends AbstractAppState {
        boolean isLoaded;
        int chunkMax = 16;
        int valueMax = 16;
        // 
        int listIndex = 0;
        // Performance gives the higher class a look into proc speed/cost
        int performance = 24;
        int progress = 0;
        int progressMax = 0;
        // After generating the perlin noise data, recording for loading into the GameState
        double[][] tableData;
        ArrayList<ChunkPrototype> chunkList;

        public int getPerformance(){ return performance;}
        public float getProgress(){ return progress/progressMax; }

        @Override
        public void initialize(AppStateManager stateManager, Application app){
            super.initialize(stateManager, app);
            //  Initialize Map Generator at NewGame start
            progressMax = (chunkMax * chunkMax) * (valueMax * valueMax);
            chunkList = new ArrayList<>();

            // For testing, let's start with 8 x 8 = 64 chunks
            // For an infinite world, these are the starter chunks
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    chunkList.add(new ChunkPrototype(x,y));
                }
            }
        }

        @Override
        public void update(float tpf) {
            int fps =(int)(1/tpf);

            if(listIndex <= chunkList.size()){
                chunkList.get(listIndex).update(1024);

                if(chunkList.get(listIndex).getProc() == 100){
                    listIndex++;
                }
            }else if(listIndex > chunkList.size()){
                //Finished with generation, dump data

            }
        }
    } 
}
