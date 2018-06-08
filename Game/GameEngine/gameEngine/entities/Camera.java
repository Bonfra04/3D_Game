package gameEngine.entities;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;

public class Camera {

	private float distanceFromPlayer;
	private float angleAroundPlayer;

	private Vector3f position;
	private float pitch;
	private float yaw;

	private float terrainPitch;

	private Entity player;

	public Camera(Entity player) {
		this.player = player;
		pitch = 20;
		yaw = 0;
		position = new Vector3f(0, 0, 70);
		angleAroundPlayer = 0;
		distanceFromPlayer = 24f;

		Mouse.setCursorPosition(DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);
		Mouse.setGrabbed(true);
	}

	public void move(List<Terrain> terrains) {

		Terrain terrain = null;

		for (Terrain t : terrains)
			if (t.getHeightOfTerrain(this.getPosition().x, this.getPosition().z) != null)
				terrain = t;

		calculatePitch();
		calculateZoom();
		calculateAngleAroundPlayer();

		calculatePosition(calculateHorizontalDistance(), calculateVerticalDistance(), terrain);

		this.yaw = -angleAroundPlayer;

		player.setRotY(180 - yaw);
	}

	private void calculatePosition(float horizDistance, float vertiDistance, Terrain terrain) {
		float theta = 180 + angleAroundPlayer;
		float xOffset = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float zOffset = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - xOffset;
		position.z = player.getPosition().z - zOffset;
		position.y = player.getPosition().y + vertiDistance + 4;

		// if (terrain != null)
		// if (terrain.getHeightOfTerrain(position.x, position.z) != null) {
		// if ((player.getPosition().y + vertiDistance + 1) <
		// terrain.getHeightOfTerrain(position.x, position.z)
		// + 1) {
		// position.y = terrain.getHeightOfTerrain(position.x, position.z) + 1;
		// terrainPitch = pitch;
		// } else {
		// position.y = player.getPosition().y + vertiDistance + 1;
		// terrainPitch = 0;
		// }
		// } else {
		// position.y = player.getPosition().y + vertiDistance + 1;
		// terrainPitch = 0;
		// }
		// else {
		// position.y = player.getPosition().y + vertiDistance + 1;
		// terrainPitch = 0;
		// }
	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom() {
		float distanceFormPlayer = this.distanceFromPlayer;
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFormPlayer -= zoomLevel;
		if (distanceFormPlayer < 0.0)
			distanceFormPlayer = 0.0f;
		else if (distanceFormPlayer > 36.0f)
			distanceFormPlayer = 36.0f;
		this.distanceFromPlayer = distanceFormPlayer;
	}

	private void calculatePitch() {
		float pitch = this.pitch;
		float pitchChange = Mouse.getDY() * 0.3f;
		pitch -= pitchChange;

		if (terrainPitch == 0) {
			if (pitch < 90)
				this.pitch = pitch;
			if (pitch < -90)
				this.pitch = -90;
		} else if (this.pitch < pitch)
			this.pitch = pitch;
	}

	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * 0.3f;
		angleAroundPlayer -= angleChange;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void invertPich() {
		this.pitch = -pitch;
	}
}
