package gameEngine.resourcesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import cube.Cube;
import gameEngine.audio.AudioMaster;
import gameEngine.entities.Camera;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.font.GUIText;
import gameEngine.fontRendering.TextMaster;
import gameEngine.guis.GuiRenderer;
import gameEngine.guis.GuiTexture;
import gameEngine.models.TexturedModel;
import gameEngine.particles.ParticleMaster;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.renderEngine.Loader;
import gameEngine.renderEngine.MasterRenderer;
import gameEngine.terrains.Terrain;
import gameEngine.water.WaterFrameBuffers;
import gameEngine.water.WaterRenderer;
import gameEngine.water.WaterTile;

public class ResourceManager {

	public static final Loader LOADER = new Loader();

	private static final Map<String, TexturedModel> MODELS = new HashMap<String, TexturedModel>();
	private static final List<Entity> ENTITIES = new ArrayList<Entity>();
	private static final List<Terrain> TERRAINS = new ArrayList<Terrain>();
	private static final List<WaterTile> WATERS = new ArrayList<WaterTile>();
	private static final List<GuiTexture> GUIS = new ArrayList<GuiTexture>();
	private static final List<Light> LIGHTS = new ArrayList<Light>();
	private static final List<GUIText> TEXTS = new ArrayList<GUIText>();
	private static final List<Cube> CUBES = new ArrayList<Cube>();

	private static Camera camera;

	private static MasterRenderer masterRenderer;
	private static GuiRenderer guiRenderer;

	private static WaterFrameBuffers fbos;
	private static WaterRenderer waterRenderer;

	public static void preInit() {
		DisplayManager.createDisplay();

		AudioMaster.init();
	}

	public static void postInit(Entity player) {
		camera = new Camera(player);

		masterRenderer = new MasterRenderer(LOADER, camera);

		ParticleMaster.init(LOADER, masterRenderer.getProjectionMatrix());

		fbos = new WaterFrameBuffers();
		waterRenderer = new WaterRenderer(LOADER, masterRenderer.getProjectionMatrix(), fbos);

		TextMaster.init(LOADER);
		guiRenderer = new GuiRenderer(LOADER);
	}

	public static void updateDisplay() {
		camera.move(TERRAINS);

		ParticleMaster.update(camera);

		masterRenderer.renderShadowMap(ENTITIES, LIGHTS.get(0));

		if (!ResourceManager.getWaters().isEmpty()) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			fbos.bindReflectionFrameBuffer();

			float distance = 2 * (camera.getPosition().y - ResourceManager.getWaters().get(0).getHeight());
			camera.getPosition().y -= distance;

			camera.invertPich();
			masterRenderer.renderScene(ResourceManager.getEntities(), ResourceManager.getTerrains(), LIGHTS, camera,
					new Vector4f(0, 1, 0, -ResourceManager.getWaters().get(0).getHeight() + 1));
			camera.getPosition().y += distance;
			camera.invertPich();

			fbos.bindRefractionFrameBuffer();
			masterRenderer.renderScene(ResourceManager.getEntities(), ResourceManager.getTerrains(), LIGHTS, camera,
					new Vector4f(0, -1, 0, ResourceManager.getWaters().get(0).getHeight()));

			fbos.unbindCurrentFrameBuffer();

			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		}

		masterRenderer.renderScene(ResourceManager.getEntities(), ResourceManager.getTerrains(), LIGHTS, camera,
				new Vector4f(0, 0, 0, 0));
		waterRenderer.render(ResourceManager.getWaters(), camera, LIGHTS.get(0));
		ParticleMaster.renderParticles(camera);

		guiRenderer.render(GUIS);
		TextMaster.render();

		DisplayManager.updateDisplay();
	}

	public static void cleanUp() {
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		fbos.cleanUp();
		waterRenderer.getShader().cleanUp();
		guiRenderer.cleanUp();
		masterRenderer.cleanUp();
		LOADER.cleanUp();
		AudioMaster.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void addModel(String name, TexturedModel model) {
		MODELS.put(name, model);
	}

	public static TexturedModel getModel(String name) {
		return MODELS.get(name);
	}

	public static void addEntity(Entity entity) {
		ENTITIES.add(entity);
	}

	public static void removeEntity(Entity entity) {
		ENTITIES.remove(entity);
	}

	public static List<Entity> getEntities() {
		return ENTITIES;
	}

	public static void addTerrain(Terrain terrain) {
		TERRAINS.add(terrain);
	}

	public static void removeTerrain(Terrain terrain) {
		TERRAINS.remove(terrain);
	}

	public static List<Terrain> getTerrains() {
		return TERRAINS;
	}

	public static void addWater(WaterTile water) {
		WATERS.add(water);
	}

	public static void removeWater(WaterTile water) {
		WATERS.remove(water);
	}

	public static List<WaterTile> getWaters() {
		return WATERS;
	}

	public static void addGui(GuiTexture gui) {
		GUIS.add(gui);
	}

	public static void removeGui(GuiTexture gui) {
		GUIS.remove(gui);
	}

	public static List<GuiTexture> getGuis() {
		return GUIS;
	}

	public static void addLight(Light light) {
		LIGHTS.add(light);
	}

	public static void removeLight(Light light) {
		LIGHTS.remove(light);
	}

	public static List<Light> getLights() {
		return LIGHTS;
	}

	public static void addText(GUIText text) {
		TEXTS.add(text);
	}

	public static void removeText(GUIText text) {
		TEXTS.remove(text);
	}

	public static List<GUIText> getTexts() {
		return TEXTS;
	}
	
	public static void addCube(Cube cube) {
		CUBES.add(cube);
	}

	public static void removeCube(Cube cube) {
		CUBES.remove(cube);
	}

	public static List<Cube> getCubes() {
		return CUBES;
	}
}
