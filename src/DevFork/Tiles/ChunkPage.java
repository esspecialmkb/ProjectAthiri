/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Tiles;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 */
public class ChunkPage {
    Node pageNode;
    ArrayList<TileChunk> chunks;
    
    public void initialize(float tileSize, float FEATURE_SIZE, Material tileMat){
        // Build chunks
        //  Prepare the TileChunk
        chunks = new ArrayList<>();
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                //TileChunk chunk = new TileChunk(x,y);
                //chunk.generateChunkTerrain();
                chunks.add(new TileChunk(x,y, tileSize));
            }
        }
        
        for(int i = 0; i < chunks.size(); i++){
            //chunks.get(i).generateChunkTerrain(FEATURE_SIZE);
        }
        
        System.out.println(chunks.size() + " TileChunks");
        for(int i = 0; i < chunks.size(); i++){
            chunks.get(i).setTileMat(tileMat);
            chunks.get(i).buildMesh();
            
            chunks.get(i).getNode().setQueueBucket(RenderQueue.Bucket.Gui);
        }
        
    }
    
    public Node getNode(){ return this.pageNode; }
}
