package net.tylermurphy.Minecraft.Chunk;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Cube;


public class Cube{

	public static final byte NULL  = -2;
	public static final byte AIR   = -1;
	
	Vector2f[] tc_px,tc_nx,tc_py,tc_ny,tc_pz,tc_nz;
	
	public final boolean transparent;
	public final boolean palceable;
	public final String name;
	public final long tick_update_delay;

	public static Cube[] cubes = {
		new Cube(1,1,0,2,1,1,false,"grass",3000L),
		new Cube(2,false,"dirt",3000L),
		new Cube(3,false,"stone",0L),
		new Cube(11,false,"cobblestone",0L),
		new Cube(12,false,"mossy_cobblestone",0L),
		new Cube(14,false,"stone_bricks",0L),
		new Cube(15,false,"mossy_stone_bricks",0L),
		new Cube(16,false,"cracked_stone_bricks",0L),
		new Cube(17,false,"chisled_stone_bricks",0L),
		new Cube(18,false,"bricks",0L),
		new Cube(4,4,5,5,4,4,false,"log",0L),
		new Cube(13,false,"planks",0L),
		new Cube(6,true,"leaf",0L),
		new Cube(7,false,"sand",0L),
		new Cube(21,21,23,22,21,21,false,"sandstone",0L),
		new Cube(9,9,20,20,9,9,false,"cactus",0L),
		new Cube(10,true,"glass",0L),
		new Cube(8,true,false,"water",250L),
		new Cube(19,false,"wool",0L),
	};
	
	private Cube(int px, int nx, int py, int ny, int pz, int nz,boolean transparent,String name, long tick_update_delay) {
		tc_px = genTC(px);
		tc_nx = genTC(nx);
		tc_py = genTC(py);
		tc_ny = genTC(ny);
		tc_pz = genTC(pz);
		tc_nz = genTC(nz);
		this.transparent = transparent;
		this.name = name;
		this.palceable = true;
		this.tick_update_delay = tick_update_delay;
	}
	
	private Cube(int i, boolean transparent,String name, long tick_update_delay) {
		tc_px = genTC(i);
		tc_nx = genTC(i);
		tc_py = genTC(i);
		tc_ny = genTC(i);
		tc_pz = genTC(i);
		tc_nz = genTC(i);
		this.transparent = transparent;
		this.name = name;
		this.palceable = true;
		this.tick_update_delay = tick_update_delay;
	}
	
	private Cube(int i, boolean transparent,boolean placable,String name, long tick_update_delay) {
		tc_px = genTC(i);
		tc_nx = genTC(i);
		tc_py = genTC(i);
		tc_ny = genTC(i);
		tc_pz = genTC(i);
		tc_nz = genTC(i);
		this.transparent = transparent;
		this.name = name;
		this.palceable = placable;
		this.tick_update_delay = tick_update_delay;
	}
	
	private Vector2f[] genTC(int i) {
		Vector2f[] tc = {
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16)),
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)+1.f / 16),
				new Vector2f(1.f / 16.f*(i+1), 1.f / 16.f*(i/16)),
				new Vector2f(1.f / 16.f*i, 1.f / 16.f*(i/16))	
		};
		return tc;
	}
	
	public static Vector3f[] PX_POS = {
			
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(0.5f,0.5f,-0.5f)
			
	};
	
	public static Vector3f[] NX_POS = {
			
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f)
			
	};
	
	public static Vector3f[] PY_POS = {
			
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f)
			
	};
	
	public static Vector3f[] NY_POS = {
			
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f)
			
	};
	
	public static Vector3f[] PZ_POS = {
			
			new Vector3f(-0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,-0.5f,0.5f),
			new Vector3f(0.5f,0.5f,0.5f),
			new Vector3f(-0.5f,0.5f,0.5f)
			
	};
	
	public static Vector3f[] NZ_POS = {
			
			new Vector3f(-0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,-0.5f,-0.5f),
			new Vector3f(0.5f,0.5f,-0.5f),
			new Vector3f(-0.5f,0.5f,-0.5f)
			
	};
	
	public static Vector3f[] NORMALS = {
			
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f),
			new Vector3f(0.f, 0.f, 0.f)
			
	};
	
	public static int[] INDICES = {
			
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			16,17,19,
			19,17,18,
			20,21,23,
			23,21,22
			
	};
	
}
