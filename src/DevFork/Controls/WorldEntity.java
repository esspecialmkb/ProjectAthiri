/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Controls;

import DevFork.Tiles.TileChunk;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
class WorldEntity extends Entity{


    public int interact(int flag, int value){ return 0;};

    @Override
    public int getType(){ return -1;}

    @Override
    public void buildMesh(){
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

        for(int i = 0; i < 4; i++){
            m.addColor(1f, 0.8f, 0.8f, 1.0f);
        }

        for(int i = 0; i < 4; i++){
            m.addColor(1f, 0.0f, 0.0f, 1.0f);
        }

    }

    @Override
    public void buildEntity(TileChunk chunk, Material mat) {
    }

    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
}
