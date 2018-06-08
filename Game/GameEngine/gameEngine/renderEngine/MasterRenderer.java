package gameEngine.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import cube.cubeRenderer;
import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.EntityRenderer;
import gameEngine.entities.EntityShader;
import gameEngine.entities.Light;
import gameEngine.models.TexturedModel;
import gameEngine.shadows.ShadowBox;
import gameEngine.shadows.ShadowMapMasterRenderer;
import gameEngine.skybox.SkyboxRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.terrains.TerrainRenderer;
import gameEngine.terrains.TerrainShader;

public class MasterRenderer {

	public static final float FOV = 40;
	public static final float NEAR_PLANE = 0.01f;
	public static final float FAR_PLANE = SkyboxRenderer.SIZE * 2 + SkyboxRenderer.SIZE / 2;

	public static float skyRED = 175.0f / 255.0f;
	public static float skyGREEN = 208.0f / 255.0f;
	public static float skyBLUE = 254.0f / 255.0f;

	private Matrix4f projectionMatrix;

	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer;

	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	private List<Terrain> terrains = new ArrayList<Terrain>();

	private SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;

	private cubeRenderer cubeRenderer;

	public MasterRenderer(Loader loader, Camera camera) {
		enableCulling();
		createProjectionMatrix();

		entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);

		cubeRenderer = new cubeRenderer(projectionMatrix);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();

		entityShader.start();
		entityShader.loadClipPlane(clipPlane);
		entityShader.loadSkyColor(skyRED, skyGREEN, skyBLUE);
		entityShader.loadLights(lights);
		entityShader.loadViewMatrix(camera);
		entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
		entityShader.stop();

		terrainShader.start();
		terrainShader.loadShadowDistance(ShadowBox.SHADOW_DISTANCE);
		terrainShader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(skyRED, skyGREEN, skyBLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();

		// skyboxRenderer.render(camera, skyRED, skyGREEN, skyBLUE);
		
		disableCulling();
		cubeRenderer.render(camera);
		enableCulling();
		
		terrains.clear();
		entities.clear();
	}

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public void renderShadowMap(List<Entity> entityList, Light light) {
		for (Entity entity : entityList) {
			processEntity(entity);
		}

		shadowMapRenderer.render(entities, light);
		entities.clear();

	}

	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}

	public void cleanUp() {
		entityShader.cleanUp();
		terrainShader.cleanUp();
		shadowMapRenderer.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(skyRED, skyGREEN, skyBLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, Camera camera,
			Vector4f clipPlane) {

		for (Terrain terrain : terrains)
			this.processTerrain(terrain);

		for (Entity entity : entities)
			this.processEntity(entity);
		render(lights, camera, clipPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
