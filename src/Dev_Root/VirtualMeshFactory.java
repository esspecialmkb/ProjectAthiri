/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dev_Root;

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
public class VirtualMeshFactory {
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
        
        
        
        class VirtualMesh{
            public ArrayList<Vector3f> vert;
            public ArrayList<Vector3f> norm;
            public ArrayList<Vector2f> tCoords;
            public ArrayList<Short> index;
            public ArrayList<Float> color;
            
            boolean colors = true;
            public Mesh m;
            public Material mat;
            VirtualMesh(){
                this.vert = new ArrayList<>();
                this.norm = new ArrayList<>();
                this.index = new ArrayList<>();
                this.color = new ArrayList<>();
                this.tCoords = new ArrayList<>();
            }
        }

        ArrayList<VirtualMesh> meshList;
        int meshCount;

        VirtualMeshFactory(){
            vert = new ArrayList<>();
            norm = new ArrayList<>();
            index = new ArrayList<>();
            color = new ArrayList<>();
            tCoords = new ArrayList<>();
            
            meshList = new ArrayList<>();
        }
        
        /**
         *  newMesh() adds a mesh to the list
        */
        public int newMesh(){
            meshList.add(new VirtualMesh());
            return meshCount++;
        }

        public Mesh getMesh(int mIndex){ return m; }
        public Material getMaterial(int mIndex){ return mat; }
        public int getVertexCount(int mIndex){ 
            return vert.size(); 
        }

        public void addVertex(int mIndex, Vector3f vert){ this.meshList.get(mIndex).vert.add(vert); }
        public void addNormal(int mIndex, Vector3f norm){ this.meshList.get(mIndex).norm.add(norm); }
        public void addTCoord(int mIndex, Vector2f coord){ this.meshList.get(mIndex).tCoords.add(coord); }
        public void addIndex(int mIndex, short a, short b, short c){ 
            this.meshList.get(mIndex).index.add(a);
            this.meshList.get(mIndex).index.add(b);
            this.meshList.get(mIndex).index.add(c);
        }
        public void addColor(int mIndex, float r, float g, float b, float a){
            this.meshList.get(mIndex).color.add(r);
            this.meshList.get(mIndex).color.add(g);
            this.meshList.get(mIndex).color.add(b);
            this.meshList.get(mIndex).color.add(a);
        }

        public void clearMesh(int mIndex){
            this.meshList.get(mIndex).vert.clear();
            this.meshList.get(mIndex).norm.clear();
            this.meshList.get(mIndex).tCoords.clear();
            this.meshList.get(mIndex).color.clear();
            this.meshList.get(mIndex).index.clear();
        }
        public void buildMesh(int mIndex){
            if(this.meshList.get(mIndex).m == null){
                this.meshList.get(mIndex).m = new Mesh();
            }else{
                this.meshList.get(mIndex).m.clearBuffer(VertexBuffer.Type.Color);
                this.meshList.get(mIndex).m.clearBuffer(VertexBuffer.Type.Position);
                this.meshList.get(mIndex).m.clearBuffer(VertexBuffer.Type.Index);
                this.meshList.get(mIndex).m.clearBuffer(VertexBuffer.Type.TexCoord);
            }
            

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
