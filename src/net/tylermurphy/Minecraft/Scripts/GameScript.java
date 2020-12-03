package net.tylermurphy.Minecraft.Scripts; 

import java.util.Random;

import net.tylermurphy.Minecraft.Audio.Sound;
import net.tylermurphy.Minecraft.Audio.SoundManager;
import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Scene.SceneCreator;
import net.tylermurphy.Minecraft.Tick.TickManager;
import net.tylermurphy.Minecraft.World.ChunkLoader;
import net.tylermurphy.Minecraft.World.ResourceManager;

public class GameScript extends Script {
	
	Sound sound;
	String[] music = {"calm1","calm2","calm3","hal1","hal2","hal3","hal4","nuance1","nuance2","piano1","piano2","piano3"};
	
	public void Init() {
		SceneCreator.createScene();
		loadNextSound();
	}
	
	public void Update() {
		ChunkLoader.run();
	}

	public void Tick() {
		if(!sound.isPlaying()) loadNextSound();
		for(Chunk c:Scene.currentScene.chunks) {
			if(!c.wasModifiedLast)
				c.wasModifiedLast = c.wasModified;
			if(c.wasModified)
				c.updateMesh();
			c.wasModified = false;
		}
		TickManager.doTick();
	}
	
	public void loadNextSound() {
		Random random = new Random();
		int i = random.nextInt(music.length);
		sound = SoundManager.loadSound(music[i]);
		sound.setLooping(false);
		sound.play();
	}
	
	public void Stop() {
		sound.stop();
		for(Chunk chunk : Scene.currentScene.chunks) {
			ResourceManager.saveChunk("test", chunk);
		}
		ResourceManager.savePlayer("test", Scene.currentScene.player);
		ResourceManager.saveCamera("test", Scene.currentScene.camera);
		ResourceManager.saveWorldData("test");
	}
	
}  