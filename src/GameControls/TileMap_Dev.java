/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameControls;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import jme3tools.optimize.TextureAtlas;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class TileMap_Dev extends AbstractControl {
    //Any local variables should be encapsulated by getters/setters so they
    //appear in the SDK properties window and can be edited.
    //Right-click a local variable to encapsulate it with getters and setters.
    
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
    
    // Fields to keep track of the tile size in screen pixels and the screen resolution
    private int tileScreenSize;
    private int screenWidth;
    private int screenHeight;
    
    // Fields to keep track of the scene graph stuff
    private Node mapNode;
    private Material material;
    private Texture[] textures;
    private TextureAtlas textureAtlas;
    
    // Need some kind of list for TilePages
    private ArrayList<TilePage> pageList;
    
    private float time;
    
    private float playerX;
    private float playerY;
    
    public TileMap_Dev(){}
    
    public TileMap_Dev(int screenWidth, int screenHeight){
        this(40,screenWidth, screenHeight);
    }
    
    public TileMap_Dev(int tileScreenSize, int screenWidth, int screenHeight){
        this.tileScreenSize = tileScreenSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    
    public void initMap(){
        // Setup everything the TileMap needs
        pageList = new ArrayList<>();
        
        // Start with the default number of tiles
        for(int px = 0; px < 8; px++){
            for(int py = 0; py < 8; py++){
                TilePage newPage = new TilePage(px,py);
                
                newPage.buildPage();
                pageList.add(newPage);
            }
        }
    };
    public void moveMap(){};    

    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        TileMap_Dev control = new TileMap_Dev();
        //TODO: copy parameters to new Control
        return control;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
        AssetManager assetManager = im.getAssetManager();
        
        textureAtlas = new TextureAtlas(1024, 1024);
        
        for (int i = 0; i < 1024; i++) {
            textures[i] = assetManager.loadTexture(landscapeFiles[i % 14]);

            textureAtlas.addTexture(textures[i], "ColorMap");
        }
        
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", textureAtlas.getAtlasTexture("ColorMap"));
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
    
    public class TilePage{
        private Node node;  // Possible batch node usage
        private Geometry[][] tiles;
        private int[][] tileCollisionData;
        private int pageX;
        private int pageY;
        private int pageSize;
        
        public TilePage(){}
        public TilePage(int pageX, int pageY){
            this.pageX = pageX;
            this.pageY = pageY;
        }
        
        public void buildPage(){
            node = new Node("Page Node");
            node.setLocalTranslation(this.pageX, this.pageY,0);
            
            if((pageSize == 0)){
                pageSize = 16;
            }
            
            tiles = new Geometry[pageSize][pageSize];
            tileCollisionData = new int[pageSize][pageSize];
            
            for(int x = 0; x < pageSize; x++){
                for(int y = 0; y < pageSize; y++){
                    this.addTile(x, y);
                    
                    //Set collision data to default state
                    tileCollisionData[pageSize][pageSize] = 0;
                }
            }
        }
        
        public Node getNode(){
            return this.node;
        }
        
        private void addTile(int x, int y) {
            // Input validation
            if(x < 0 || x >= pageSize){
                return;
            }
            
            Quad quad = new Quad(tileScreenSize, tileScreenSize);

            tiles[x][y] = new Geometry("Tile", quad);
            tiles[x][y].setLocalTranslation(x * tileScreenSize, y * tileScreenSize, -10);
            tiles[x][y].setMaterial(material);

            FloatBuffer buf = quad.getFloatBuffer(VertexBuffer.Type.TexCoord);
            textureAtlas.getAtlasTile(textures[0]).transformTextureCoords(buf, 0, buf);

            node.attachChild(tiles[x][y]);
        }
        
        public void changeTile(int x, int y, int index){
            // Input validation
            if(x < 0 || x >= pageSize || index < 0 || index >= 20){
                return;
            }
            
            // All landscape tiles (so far) will be walkable
            
            FloatBuffer buf = tiles[x][y].getMesh().getFloatBuffer(VertexBuffer.Type.TexCoord);
            textureAtlas.getAtlasTile(textures[index]).transformTextureCoords(buf, 0, buf);
        }
    }
    
}
