package net.tylermurphy.Minecraft.Scene;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.World.ResourceManager;

public class SceneCreator {

	public static void createScene() {
		
		Scene.currentScene = new Scene();
		
		Camera camera = ResourceManager.loadCamera("test");
		if(camera!=null) {
			Scene.currentScene.camera = camera;
		} else{
			Scene.currentScene.addCamera(new Camera());
		}
		
		if(!ResourceManager.loadWorldData("test")) {
			Chunk.SEED = new Random().nextInt(1000000000);
			Scene.currentScene.world_origin =  new Vector2f(0,0);
		} 
		Player player = ResourceManager.loadPlayer("test");
		if(player!=null) {
			Scene.currentScene.player = player;
		} else{
			Chunk c = ResourceManager.loadChunk("test", 0, 0);
			if(c == null) {
				c = new Chunk(0,0);
				c.generate();
			}
			Scene.currentScene.chunks.add(c);
			Scene.currentScene.createPlayer(0, Chunk.getHighestBlock(0, 0)+1, 0, 0f, 0f, 0f, 0.6f);
		}
		
	}
	
}
	
