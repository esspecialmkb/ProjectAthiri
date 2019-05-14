/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Controls;

import DevFork.Tiles.TileChunk;
import DevFork.VirtualMesh;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class Player extends Entity{
    public Player(){
        super();
    }

    @Override
    public int getType(){ return 1;}

    @Override
    public void buildMesh(){
        
        m = new VirtualMesh();
        m.addVertex(new Vector3f(-0.25f,1.5f,0));
        m.addVertex(new Vector3f(0.25f,1.5f,0));
        m.addVertex(new Vector3f(-0.25f,1,0));
        m.addVertex(new Vector3f(0.25f,1,0));

        m.addVertex(new Vector3f(-0.25f,1,0));
        m.addVertex(new Vector3f(0.25f,1,0));
        m.addVertex(new Vector3f(-0.25f,0.25f,0));
        m.addVertex(new Vector3f(0.25f,0.25f,0));

        m.addIndex((short) 1, (short) 0, (short) 2);
        m.addIndex((short) 2, (short) 3, (short) 1);
        m.addIndex((short) 5, (short) 4, (short) 6);
        m.addIndex((short) 6, (short) 7, (short) 5);

        m.addColor(1.0f, 1.0f, 1.0f, 1.0f);
        m.addColor(1.0f, 1.0f, 1.0f, 1.0f);
        m.addColor(1.0f, 1.0f, 1.0f, 1.0f);
        m.addColor(1.0f, 1.0f, 1.0f, 1.0f);

        m.addColor(0.6f, 0.0f, 0.0f, 1.0f);
        m.addColor(0.6f, 0.0f, 0.0f, 1.0f);
        m.addColor(0.6f, 0.0f, 0.0f, 1.0f);
        m.addColor(0.6f, 0.0f, 0.0f, 1.0f);
        
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        m.addNormal(new Vector3f(0,0,-1));
        
        m.buildMesh();

    }

    @Override
    public void buildEntity(TileChunk chunk, Material mat) {

        super.n = new Node("Player Node");
        //m.setMaterial(mat);
        this.buildMesh();
        System.out.println("Vertext Count: " + m.getVertexCount());
        super.g = new Geometry("Player", m.getMesh());
        super.g.setMaterial(mat);
        super.n.attachChild(super.g);
        super.g.setLocalScale(chunk.getTileSize());
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        // Move the player according to velocity
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
}
