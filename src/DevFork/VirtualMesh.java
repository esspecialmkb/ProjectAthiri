/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
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

    public VirtualMesh(){
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
        m.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(triIndex));

        m.updateBound();
    }
}
