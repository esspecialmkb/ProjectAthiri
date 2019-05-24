/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DevFork.Tiles;

import DevFork.VirtualMesh;
import com.jme3.math.Vector3f;


public class TownSeedTile extends Tile {

    public TownSeedTile(int x, int y, float size) {
        super(x, y, size);
    }
    
    @Override
    public void createTile(VirtualMesh mesh){
        int indexOffset = mesh.getVertexCount();
        mesh.addVertex(new Vector3f(x * size        ,  y * size,0));
        mesh.addVertex(new Vector3f(((x + 1) * size), (y * size),0));
        mesh.addVertex(new Vector3f((x * size)      ,((y + 1) * size),0));
        mesh.addVertex(new Vector3f(((x + 1) * size),((y + 1) * size),0));
        
        mesh.addVertex(new Vector3f((x * size) + (size/4)           ,  (y * size)  + (size/4),0));
        mesh.addVertex(new Vector3f(((x + 1) * size) - (size / 4)   , (y * size)  + (size/4),0));
        mesh.addVertex(new Vector3f((x * size)  + (size/4)          ,((y + 1) * size) - (size / 4),0));
        mesh.addVertex(new Vector3f(((x + 1) * size) - (size / 4)   ,((y + 1) * size) - (size / 4),0));

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
