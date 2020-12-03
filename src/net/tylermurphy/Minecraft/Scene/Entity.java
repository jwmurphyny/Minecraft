package net.tylermurphy.Minecraft.Scene;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Chunk.Cube;
import net.tylermurphy.Minecraft.Render.Data.Model;

public class Entity implements Serializable{

	private static final long serialVersionUID = -1273546184611580473L;
	
	private Model model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	public boolean visible = true;
	
	public int health;
	
	protected static final float RUN_SPEED = 4;
	protected static final float JUMP_POWER = 6;
	protected static final float GRAVITY = -15;
	
	protected float currentForwardSpeed = 0;
	protected float currentSideSpeed = 0;
	protected float upwardsSpeed = 0;
	
	public boolean isInAir = true;
	public boolean isFlying = false;
	public boolean isSwiming = false;
	public boolean isFalling = false;
	public boolean wasFalling = false;
	public boolean isDead = false;
	
	public int drawMode = 0;
	
	public Entity(Model model, Vector3f position, float rotX, float rotY, float rotZ,float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(Vector3f position, float rotX, float rotY, float rotZ,float scale) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Vector3f getGlobalPosition() {
		return new Vector3f(position.x+Scene.currentScene.world_origin.x,position.y,position.z+Scene.currentScene.world_origin.y);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotX,rotY,rotZ);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public boolean willCollide(float dx, float dy, float dz) {
		float px = getPosition().x;
		float py = getPosition().y;
		float pz = getPosition().z;
		if(getPosition().x<0)
			px--;
		if(getPosition().z<0)
			pz--;
		int[] nbc = {
				(int) (px+dx+.25f),
				(int) (py+dy),
				(int) (pz+dz+.25f),
				(int) (px+dx+.75f),
				(int) (py+dy+1.9f),
				(int) (pz+dz+.75f)
			};
		
		for(int x = nbc[0]; x<=nbc[3]; x++) {
			for(int y = nbc[1]; y<=nbc[4]; y++) {
				for(int z = nbc[2]; z<=nbc[5]; z++) {
					byte block = Chunk.getBlock(x+Scene.currentScene.world_origin.x,y,z+Scene.currentScene.world_origin.y);
					byte block_below = Chunk.getBlock(x,y-2,z);
					if(block == 17 || block_below == 17) isSwiming = true;
					else isSwiming = false;
					if(dy != 0) {
						if(block != Cube.AIR && block != 17) {
							if(y <= getGlobalPosition().y) isInAir = false;
							else isInAir = true;
							return true;
						} else isInAir = true;
					}else {
						if(block != Cube.AIR && block != 17) return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean collides(float x, float y, float z) {
		float px = getPosition().x;
		float py = getPosition().y;
		float pz = getPosition().z;
		if(getPosition().x<0)
			px--;
		if(getPosition().z<0)
			pz--;
		int[] nbc = {
				(int) (px+.25f),
				(int) (py),
				(int) (pz+.25f),
				(int) (px+.75f),
				(int) (py+1.9f),
				(int) (pz+.75f)
			};
		if(
				x >= nbc[0] && x <= nbc[3] &&
				y >= nbc[1] && y <= nbc[4] &&
				z >= nbc[2] && z <= nbc[5]
			) return true;
		return false;
	}

}
