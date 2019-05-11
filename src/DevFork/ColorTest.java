/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork;

import Utility.SimplexNoise;
import com.jme3.app.BasicApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class ColorTest extends BasicApplication{
    
    ArrayList<ColorRGBA> colorList;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ColorTest app = new ColorTest();
        AppSettings settings = new AppSettings(false);
        
        settings.setFrameRate(60);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        int cX = 1;
        int cY = 1;
        
        TileChunk test = new TileChunk(cX,cY);
        test.loadChunk();
        
        colorList = new ArrayList<>();
        colorList.add(ColorRGBA.Blue);
        colorList.add(ColorRGBA.Cyan);
        colorList.add(ColorRGBA.Yellow);
        colorList.add(ColorRGBA.Green);
        colorList.add(ColorRGBA.Brown);
        colorList.add(ColorRGBA.Black);
        colorList.add(ColorRGBA.White);
        
        for(int iX = 0; iX < 16; iX++){
            for(int iY = 0; iY < 16; iY++){
                int tileX = (cX * (16)) + (iX);
                int tileY = (cY * (16)) + (iY);

                test.setTileData(iX, iY, 0);
                
                float numerator = iY + (iX * 16);
                float percent = numerator / (16 * 16);
                test.setTileColor(iX, iY, interpolateColor(percent));
            }
        }
        
        test.buildMesh();
        
        guiNode.attachChild(test.getNode());
    }
    
    public ColorRGBA interpolateColor(float value){
        float numElements = colorList.size();
        
        float step = 1/numElements;
        ColorRGBA result = new ColorRGBA();
        
        for(int i = 0; i < numElements-1; i++){
            if(value < (step * (i+ 1))){
                float range = step;
                float baseValue = step * i;
                float delta = value - baseValue;
                float interpolation = delta / range;
                
                result.a = FastMath.interpolateLinear(interpolation, colorList.get(i).a, colorList.get(i + 1).a);
                result.r = FastMath.interpolateLinear(interpolation, colorList.get(i).r, colorList.get(i + 1).r);
                result.g = FastMath.interpolateLinear(interpolation, colorList.get(i).g, colorList.get(i + 1).g);
                result.b = FastMath.interpolateLinear(interpolation, colorList.get(i).b, colorList.get(i + 1).b);
            }
        }
        
        return result;
    }
    
    int tileSize = 24;
    int FEATURE_SIZE = 2;
    
    double genLowValue;
    double genHighValue;
    double genDeltaValue;
    
    //  LIFTED FROM AppHarness.GameState
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
        
        public double getTileData(int iX, int iY){ return tileData[iX][iY]; }
        public void setTileData(int iX, int iY, double value){ tileData[iX][iY] = value; }
        
        public void unloadChunk(){
            if(isLoaded){
                //clear tile array

                isLoaded = false;
            }
        }

        public void loadChunk(){
            if(!isLoaded){
                // Load the chunk data
                for(int iX = 0; iX < 16; iX++){
                    for(int iY = 0; iY < 16; iY++){
                        //int tileX = (this.x * (chunkSize * tileSize)) + (x * tileSize);
                        //int tileY = (this.y * (chunkSize * tileSize)) + (y * tileSize);

                        // Generate or load tile data
                        // var perlinValue = noise.perlin2(tileX / 100, tileY / 100); 

                        // var key = “”;
                        // var animationKey = “”;
                        tiles[iX][iY] = new Tile(iX, iY, 24);
                    }
                }
            }
        }
        public void setTileColor(int iX, int iY, ColorRGBA color){
            tiles[iX][iY].setColor(color);
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
                        
                        scanRange();
                    }
                }
                isLoaded = true;
            //}
        }
        public void scanRange(){
            for(int iX = 0; iX < 16; iX++){
                for(int iY = 0; iY < 16; iY++){
                    int tileX = (this.x * (16)) + (iX);
                    int tileY = (this.y * (16)) + (iY);
                    
                    if( tileData[iX][iY] < genLowValue){
                        genLowValue = tileData[iX][iY];
                    }else if( tileData[iX][iY] > genHighValue){
                        genHighValue = tileData[iX][iY];
                    }
                }
            }
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
    
}
