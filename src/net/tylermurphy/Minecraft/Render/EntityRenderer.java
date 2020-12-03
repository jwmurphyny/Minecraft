package net.tylermurphy.Minecraft.Render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.tylermurphy.Minecraft.Chunk.Chunk;
import net.tylermurphy.Minecraft.Render.Data.Model;
import net.tylermurphy.Minecraft.Render.Data.Vao;
import net.tylermurphy.Minecraft.Render.Shaders.EntityShader;
import net.tylermurphy.Minecraft.Scene.Entity;
import net.tylermurphy.Minecraft.Scene.Scene;
import net.tylermurphy.Minecraft.Util.Constants;
import net.tylermurphy.Minecraft.Util.Maths;

public class EntityRenderer {

	private EntityShader shader;
	
	private Map<Model, List<Entity>> entities = new HashMap<Model, List<Entity>>();
	
	public EntityRenderer(Matrix4f projectionMatrix) {
		this.shader = new EntityShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Matrix4f projectionMatrix) {
		FrustumCuller frustum = FrustumCuller.getFrustum(Maths.createViewMatrix(Scene.currentScene.camera),projectionMatrix);
		for(Chunk chunk : Scene.currentScene.chunks) {
			for (Entity entity : chunk.entities) {
				if(entity.visible == true) processEntity(entity);
			}
			if(chunk.mesh != null && frustum.cubeInFrustum(
					chunk.gridX*16 - Scene.currentScene.world_origin.x, 0, 
					chunk.gridZ*16 - Scene.currentScene.world_origin.y, 
					chunk.gridX*16 + 16 - Scene.currentScene.world_origin.x , 256, 
					chunk.gridZ*16 + 16 - Scene.currentScene.world_origin.y
				)) 
				processEntity(chunk.mesh);
		}
		for(Entity entity : Scene.currentScene.global_entities) {
			if(entity.visible == true) processEntity(entity);
		}
		prepare();
		shader.start();
		if(Scene.currentScene.player.isDead)
			shader.loadTint(new Vector3f(100,0,0));
		else
			shader.loadTint(new Vector3f(0,0,0));
		shader.loadViewMatrix(Scene.currentScene.camera);
		for (Model model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				if(entity.drawMode == 0 ) GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				else GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVao().getVertexCount());
			}
			unbindModel();
		}
		shader.stop();
		entities.clear();
	}
	
	private void processEntity(Entity entity) {
		Model entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(Constants.RED, Constants.GREEN, Constants.BLUE, 1);
	}
	
	private void prepareTexturedModel(Model model) {
		Vao rawModel = model.getVao();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture());
	}

	private void unbindModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(
						entity.getPosition().x - Scene.currentScene.world_origin.x,
						entity.getPosition().y,
						entity.getPosition().z - Scene.currentScene.world_origin.y
				),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
