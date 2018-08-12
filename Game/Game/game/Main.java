package game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import ambient.Block;
import ambient.Player;
import cube.Cube;
import gameEngine.biomes.MountainGenerator;
import gameEngine.biomes.PlainGenerator;
import gameEngine.collision.CollisionBox;
import gameEngine.entities.Entity;
import gameEngine.entities.Light;
import gameEngine.font.FontType;
import gameEngine.font.GUIText;
import gameEngine.guis.GuiTexture;
import gameEngine.particles.ParticleSystem;
import gameEngine.particles.ParticleTexture;
import gameEngine.renderEngine.Loader;
import gameEngine.resourcesManager.ResourceManager;
import gameEngine.terrains.Terrain;
import gameEngine.terrains.WorldGenerator;

public class Main {

	public static final float SCALE = 0.5f;

	public static final long SEED = 626452;
	public static final Loader LOADER = new Loader();

	public static Controller controller;
	public static boolean enableController = false;

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		loadController();

		ResourceManager.preInit();

		Player player = new Player("person", "playerTexture", SCALE, new Vector3f(100.0f, 100.0f, 100.0f),
				new Vector3f(0, 0, 0));

		// player.setFlying(true);

		ResourceManager.postInit(player);

		WorldGenerator.generateWorldPortion(0, 0, SEED, new PlainGenerator(0, 0, SEED));
		System.out.println("1");

		WorldGenerator.generateWorldPortion(2, 1, SEED, new PlainGenerator(2, 1, SEED));
		System.out.println("2");

		WorldGenerator.generateWorldPortion(1, 0, SEED, new MountainGenerator(1, 0, SEED));
		System.out.println("3");

		WorldGenerator.generateWorldPortion(1, 1, SEED, new PlainGenerator(1, 1, SEED));
		System.out.println("4");

		WorldGenerator.generateWorldPortion(2, 0, SEED, new PlainGenerator(2, 0, SEED));
		System.out.println("5");

		FontType font = new FontType(LOADER.loadFont("boundary"), "boundary");

		new GUIText("Game", 1, font, new Vector2f(0, 0.01f), 1f, true);

		new GuiTexture(LOADER.loadTexture("health"), new Vector2f(-0.75f, 0.9f), new Vector2f(0.25f, 0.25f));

		new Light(new Vector3f(1000000, 1500000, 1000000), new Vector3f(1.3f, 1.3f, 1.3f));

		ParticleTexture particleTexture = new ParticleTexture(LOADER.loadTexture("cosmic"), 4, false);

		ParticleSystem particleSystem = new ParticleSystem(particleTexture, 50, 25, 0.3f, 1.5f, 0.4f);
		particleSystem.randomizeRotation();
		particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
		particleSystem.setLifeError(0.1f);
		particleSystem.setSpeedError(0.4f);
		particleSystem.setScaleError(0.3f);

		Entity block = new Block("brick", 20, 1, -20);

		Cube c = new Cube("brick");

		Entity tree = new Entity("tree", "tree", Main.SCALE * 10, new Vector3f(20, Terrain.getHeight(20, 20), 20),
				new Vector3f(0, 0, 0));
		tree.addCollisionBox(new CollisionBox(5, 12, 5, new Vector3f(-1.2f, -0.5f, -1.2f)));

		while (!Display.isCloseRequested()) {

			if (enableController)
				controller.poll();

			player.move(ResourceManager.getEntities());

			// particleSystem.generateParticles(ePlayer.getPosition());

			ResourceManager.updateDisplay();
		}

		ResourceManager.cleanUp();

	}

	private static void loadController() {
		Controllers.destroy();
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			System.err.println("Error while loading controller");
		}
		Controllers.poll();

		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			if (!Controllers.getController(i).getName().equals("USB Receiver")) {
				controller = Controllers.getController(i);

				System.out.println("@Advice: Controller setted as " + controller.getName() + " (port: " + i + ")");
				enableController = true;
				break;
			}
			if (i == Controllers.getControllerCount() - 1)
				System.out.println("@Advice: No controller found");
		}
	}

}
