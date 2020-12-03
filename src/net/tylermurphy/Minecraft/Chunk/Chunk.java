package net.tylermurphy.Minecraft.Chunk;

import java.util.ArrayList;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Render.Data.Model;
import net.tylermurphy.Minecraft.Render.Data.Texture;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Scene.Entity;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Tick.BlockUpdate;

public class Chunk{

	
	public static final int TEXTURE = Texture.loadTexture("Blocks");
	public static int SEED;
	public static int RENDER_DISTANCE = 5;
	
	public int gridX;
	public int gridZ;
	public int chunk_seed;
	
	public List<Entity> entities;
	
	public byte[][][] cubes;
	private ChunkMesh mesh_data;
	private Vao mesh_vao;
	public Entity mesh;
	
	public boolean hasBeenModified = false;
	public boolean wasModified = false;
	public boolean wasModifiedLast = false;
	
	public int getVaoID() {
		return mesh_vao.getVaoID();
	}
	
	public Chunk(int gridX,int gridZ) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.chunk_seed = gridX * SEED ^ gridZ;
		entities = new ArrayList<Entity>();
		cubes = new byte[16][256][16];
		mesh_data = new ChunkMesh();
	}
	
	public void updateMesh() {
		if(mesh_vao != null) Vao.disposeVAO(mesh_vao.getVaoID());
		mesh_data.update(this);
		mesh_vao = Vao.loadToVAO(mesh_data.positions, mesh_data.tcs);
		mesh_data.positions = null;
		mesh_data.tcs = null;
		mesh = new Entity(new Model(mesh_vao, TEXTURE), new Vector3f(gridX*16,9,gridZ*16), 0, 0, 0, 1f);
		mesh.drawMode = 1;
	}
	
	public static void updateMesh(int gridX, int gridZ) {
		for(Chunk chunk : Scene.currentScene.chunks) {
			if(chunk.gridX == gridX && chunk.gridZ == gridZ) {
				chunk.updateMesh();
			}
		}
	}
	
	public void generate() {
		Generator.generate(this);
	}


	
	public static byte getBlock(float x, float y, float z) {

		if(y<0 || y> 255) return Cube.AIR;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x)/16);
		else
			cx = (int) (((x)-15)/16);
		if(z>-1)
			cz = (int) ((z)/16);
		else
			cz = (int) (((z)-15)/16);
		for(Chunk chunk : Scene.currentScene.chunks) {
			if(chunk.gridX == cx && chunk.gridZ == cz) {
				int rx,rz;
				if(x>-1) 
					rx = (int)(x)%16;
				else {
					int r16 = (int)(Math.abs((x)/16)+1);
					rx = (int)((r16*16-Math.abs(x))%16);
				}
				if(z>-1) 
					rz = (int)(z)%16;
				else {
					int r16 = (int)(Math.abs((z)/16)+1);
					rz = (int)((r16*16-Math.abs(z))%16);
				}
				return chunk.cubes[rx][(int)y][rz];
			}
		}
		return Cube.NULL;
	}
	
	public static int getHighestBlock(int fx, int fz) {
		for(int y=255;y>=0;y--) {
			if(getBlock(fx,y,fz) != -1) return y;
		}
		return 255;
	}
	
	public static boolean setBlock(float x, float y,float z, byte id) {
		
		if(y<0 || y> 255) return false;
		int cx,cz;
		if(x>-1)
			cx = (int) ((x)/16);
		else
			cx = (int) (((x)-15)/16);
		if(z>-1)
			cz = (int) ((z)/16);
		else
			cz = (int) (((z)-15)/16);
		for(Chunk chunk : Scene.currentScene.chunks) {
			if(chunk.gridX == cx && chunk.gridZ == cz) {
				int rx,rz;
				if(x>-1) 
					rx = (int)(x%16)%16;
				else {
					int r16 = (int)(Math.abs((x%16)/16)+1);
					rx = (int)((r16*16-Math.abs(x%16))%16);
				}
				if(z>-1) 
					rz = (int)(z%16)%16;
				else {
					int r16 = (int)(Math.abs((z%16)/16)+1);
					rz = (int)((r16*16-Math.abs(z%16))%16);
				}
				
				BlockUpdate.createBlockUpdate((int)x, (int)y, (int)z, chunk.cubes[rx][(int)y][rz], id);
				
				chunk.hasBeenModified = true;
				chunk.wasModified = true;
				chunk.cubes[rx][(int)y][rz] = id;
				if(id != -1 && y > 0 && chunk.cubes[rx][(int)y - 1][rz] == 0)
					chunk.cubes[rx][(int)y-1][rz] = 1;
				int ox = (int)(rx%16) == 0 ? -1 : (int)(rx%16) == 15 ? 1 : 0;
				int oz = (int)(rz%16) == 0 ? -1 : (int)(rz%16) == 15 ? 1 : 0;
				Chunk coc = null,coz = null;
				if(ox != 0) coc = Chunk.getChunk(chunk.gridX + ox, chunk.gridZ);
				if(oz != 0) coz = Chunk.getChunk(chunk.gridX, chunk.gridZ + oz);
				if(coc != null) {
					coc.hasBeenModified = true;
					coc.wasModified = true;
				}
				if(coz != null) {
					coz.hasBeenModified = true;
					coz.wasModified = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static Chunk getChunk(int x,int z) {
		for(Chunk c : Scene.currentScene.chunks) {
			if(c.gridX == x && c.gridZ == z)
				return c;
		}
		return null;
	}
	
}
