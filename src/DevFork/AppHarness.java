/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork;


// TODO: Chunks generate single-value when feature size is used
// FIX: Vertex indexes need to be relative
// FIX: GameState map not attaching to scene graph properly
// FIX: UIScreen freeze when selecting game 
// FIX: Fix missing chunk material -> Solved

import Utility.SimplexNoise;
import com.jme3.app.Application;
import com.jme3.app.BasicApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class AppHarness extends BasicApplication{
    UIState ui;
    GameState game;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AppHarness app = new AppHarness();
        AppSettings settings = new AppSettings(false);
        
        settings.setFrameRate(60);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        
        ui = new UIState(guiNode,cam.getWidth(),cam.getHeight());
        stateManager.attach(ui);
        game = new GameState(guiNode);
    }
    
    @Override
    public void simpleUpdate(float tpf){
        boolean hasUIState = stateManager.hasState(ui);
        if(hasUIState){
            switch(ui.getCurrentBtn()){
                case 0:
                    stateManager.detach(ui);
                    stateManager.attach(game);
            }
        }
    }
    
    public class UIState extends AbstractAppState{
        Geometry[] buttonsMain;
        BitmapText[] bTextMain;
        BitmapFont uiFont;
        Node[] bNodeMain;
        Node gui;
        
        Material[] mats;
        
        int width, height, currentBtn;
        
        InputManager inputManager;
        
        public UIState(Node gui, int width, int height){
            this.gui = gui;
            this.width = width;
            this.height = height;
            currentBtn = -1;
        }
        
        private void buildMainMenu(){
            //  Text
            bTextMain = new BitmapText[3];
            bTextMain[0] = new BitmapText(uiFont,false);
            bTextMain[0].setSize(guiFont.getCharSet().getRenderedSize());
            bTextMain[0].setText("START");
            bTextMain[0].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);
            
            bTextMain[1] = new BitmapText(uiFont,false);
            bTextMain[1].setSize(guiFont.getCharSet().getRenderedSize());
            bTextMain[1].setText("SETTINGS");
            bTextMain[1].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);
            
            bTextMain[2] = new BitmapText(uiFont,false);
            bTextMain[2].setSize(guiFont.getCharSet().getRenderedSize());
            bTextMain[2].setText("EXIT");
            bTextMain[2].setLocalTranslation(0, bTextMain[0].getLineHeight(), 1);
            
            //  Buttons
            buttonsMain = new Geometry[3];
            buttonsMain[0] = new Geometry("Start", new Quad(width/3,height/10));
            buttonsMain[0].setMaterial(mats[0]);
            
            buttonsMain[1] = new Geometry("Settings", new Quad(width/3,height/10));
            buttonsMain[1].setMaterial(mats[0]);
            
            buttonsMain[2] = new Geometry("Exit", new Quad(width/3,height/10));
            buttonsMain[2].setMaterial(mats[0]);
            
            //  Nodes
            bNodeMain = new Node[4];
            bNodeMain[0] = new Node("Start Node");
            bNodeMain[0].setLocalTranslation(width/2,(height/10) + 5 + (height/2),0);
            bNodeMain[0].attachChild(bTextMain[0]);
            bNodeMain[0].attachChild(buttonsMain[0]);
            
            bNodeMain[1] = new Node("Settings Node");
            bNodeMain[1].setLocalTranslation(width/2,(height/2),0);
            bNodeMain[1].attachChild(bTextMain[1]);
            bNodeMain[1].attachChild(buttonsMain[1]);
            
            bNodeMain[2] = new Node("Exit Node");
            bNodeMain[2].setLocalTranslation(width/2,(height/2)-((height/10) + 5),0);
            bNodeMain[2].attachChild(bTextMain[2]);
            bNodeMain[2].attachChild(buttonsMain[2]);
            
            bNodeMain[3] = new Node("Host Node");
            bNodeMain[3].setLocalTranslation(0,0,0);
            bNodeMain[3].attachChild(bNodeMain[0]);
            bNodeMain[3].attachChild(bNodeMain[1]);
            bNodeMain[3].attachChild(bNodeMain[2]);
        }
        
        private void checkUI(Vector2f pos){
            //  Button 0
            if(pos.x > width/2 && pos.x < ((width/2) + (width/3)) && pos.y > ((height/10) + 5 + (height/2)) && pos.y < ((height/10) + 5 + (height/2) + (height/10))){
                if(currentBtn != 0 && currentBtn != -1){
                    buttonsMain[currentBtn].setMaterial(mats[0]);
                }
                currentBtn = 0;
                buttonsMain[0].setMaterial(mats[1]);
            }else{
                if(currentBtn == 0){
                    buttonsMain[currentBtn].setMaterial(mats[0]);
                }
                currentBtn = -1;
            }
        }
        
        public int getCurrentBtn(){ return currentBtn;}
        
        @Override
        public void initialize(AppStateManager stateManager, Application app){
            inputManager = app.getInputManager();
            
            mats = new Material[3];
            mats[0] = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
            mats[0].setColor("Color", ColorRGBA.Gray); 
            mats[1] = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
            mats[1].setColor("Color", ColorRGBA.Green); 
            
            uiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
            buildMainMenu();
            attachMainMenu();
            attachInput();
            
        }
        
        @Override
        public void stateAttached(AppStateManager stateManager) {
            
        }

        @Override
        public void stateDetached(AppStateManager stateManager) {
            detachMainMenu();
            detachInput();
        }
        
        private void attachMainMenu(){
            gui.attachChild(bNodeMain[3]);
        }
        private void detachMainMenu(){
            bNodeMain[3].removeFromParent();
        }
        private void attachInput(){
            inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
            inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
            inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_RETURN));
            inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        }
        private void detachInput(){
            inputManager.deleteMapping("Up");
            inputManager.deleteMapping("Down");
            inputManager.deleteMapping("Enter");
            inputManager.deleteMapping("Click");
        }
        
        @Override
        public void update(float tpf) {
            Vector2f cursorPosition = inputManager.getCursorPosition();
            checkUI( cursorPosition );
        }
        
        private ActionListener actionListener = new ActionListener(){
            public void onAction(String name, boolean keyPressed, float tpf){
                
            }
        };
    }
    
    
    
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
        
        //  Stuff for textures
        private TextureAtlas atlas;
        private Texture[] texture = new Texture[64];
        private Material material;
        
        //  Stuff for scene graph
        Node gui;
        Node world;
        boolean[] movement = new boolean[4];
        
        public GameState(Node gui){ 
            this.gui = gui;
            world = new Node("World");
            gui.attachChild(world);
        }
        
        @Override
        public void initialize(AppStateManager stateManager, Application app){
            super.initialize(stateManager, app);
            tileSize = 24;
            noise = new OpenSimplexNoise( (int) (Math.random() *1000) );
            
            //  Populate the TextureAtlas
            for(int t = 0; t < landscapeFiles.length; t++ ){
                //texture[t] = app.getAssetManager().loadTexture(landscapeFiles[t]);
                //atlas.addTexture(texture[t], "ColorMap");
            }
            
            //  Create the tile material
            //material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            //material.setTexture("ColorMap", atlas.getAtlasTexture("ColorMap"));
            
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
                
                tileChunks.get(i).getNode().setQueueBucket(Bucket.Gui);
                world.attachChild(tileChunks.get(i).getNode());
            }
            
            setDisplayStatView(false);
            
            //  Generate chunk data
            //mapGen = new MapGen();
            
            //  Write chunk data
            
            //Input
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
        
        public class TileChunk{
            int x;
            int y;
            double[][] tileData;
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
                            System.out.println("ChunkGen " + tileX + ", " + tileY + ": " + value);
                            
                            tileData[iX][iY] = ((value * 100));
                            
                            System.out.println("POSTGen " + tileX + ", " + tileY + ": " + value);
                            
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
                                
                            }else if((value >= 10) && (value < 20)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Cyan);
                                
                            }else if((value >= 20) && (value < 30)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Brown);
                                
                            }else if((value >= 30) && (value < 40)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Green);
                                
                            }else if((value >= 40) && (value < 50)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Green);
                                
                            }else if((value >= 50) && (value < 60)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Green);
                                                                
                            }else if((value >= 60) && (value < 70)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Brown);
                                
                            }else if((value >= 70) && (value < 80)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Brown);
                                
                            }else if((value >= 80) && (value < 90)){
                                //
                                tiles[x][y].setColor(ColorRGBA.Black);
                                
                            }else if((value >= 90) /*&& (value < 100)*/){
                                //
                                tiles[x][y].setColor(ColorRGBA.White);
                                
                            }
                            
                            tiles[x][y].createTile(m);
                            
                        }
                    }
                    
                    m.buildMesh();
                    n = new Node();
                    n.setLocalTranslation(x*16*tileSize,y*16*tileSize,0);
                    g = new Geometry("Chunk", m.getMesh());
                    g.setMaterial(m.getMaterial());
                    n.attachChild(g);
                //}                
            }
        }// End of TileChunk.class
        
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
        
        class VirtualMesh{
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
                    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setBoolean("VertexColor", true);
                    mat.setColor("Color", ColorRGBA.White);

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
        
    
}
