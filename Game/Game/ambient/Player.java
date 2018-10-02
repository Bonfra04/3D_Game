package ambient;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import game.Main;
import gameEngine.audio.AudioMaster;
import gameEngine.audio.Source;
import gameEngine.collision.CollisionBox;
import gameEngine.entities.Entity;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;

public class Player extends Entity {

	private static final float WALK_SPEED = 40f;
	public static final float GRAVITY = 40.0f;
	private static final float JUMP_POWER = 15.0f;

	private boolean canUseCommands = true;

	private float currentWalkSpeed = WALK_SPEED;

	private float currentSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;
	private boolean isOnSomething = false;
	private boolean flyng = false;

	private Source source = AudioMaster.generateSource();
	private int jumpSound = AudioMaster.loadSound("bounce");

	private CollisionBox collisionBox = new CollisionBox(2, 5, 2, new Vector3f(0, 0, 0));

	public Player(String model, String texture, int totalTextureRows, int textureIndex, float scale, Vector3f position,
			Vector3f rotation) {
		super(model, texture, totalTextureRows, textureIndex, scale, position, rotation);

		super.addCollisionBox(collisionBox);

		super.increaseRotation(0, 180, 0);
	}

	public Player(String model, String texture, float scale, Vector3f position, Vector3f rotation) {
		super(model, texture, scale, position, rotation);

		super.addCollisionBox(collisionBox);

		super.increaseRotation(0, 180, 0);
	}

	@Override
	public void increasePosition(float dx, float dy, float dz) {
		super.increasePosition(dx, dy, dz);

		AudioMaster.setListenerData(super.getPosition().x, super.getPosition().y, super.getPosition().z);
	}

	public void move(List<Entity> enitities) {
		this.currentSpeed = 0;
		this.checkInputs();

		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		float dy = this.gravityFlow(Terrain.getHeight(super.getPosition().x, super.getPosition().z));

		Vector3f velocity = new Vector3f(dx, dy, dz);

		boolean[][] enable = this.checkCollision(enitities, velocity);

		if (!enable[0][1]) {
			this.isOnSomething = true;
			this.isInAir = false;
		} else
			this.isOnSomething = false;

		this.increasePosition(enable[0][0] ? velocity.x : 0, enable[0][1] ? velocity.y : 0,
				enable[0][2] ? velocity.z : 0);
	}

	private boolean[][] checkCollision(List<Entity> enitities, Vector3f velocity) {
		boolean[][] enable = new boolean[2][3];
		for (int i = 0; i < enable[0].length; i++) {
			enable[0][i] = true;
			enable[1][i] = true;
		}

		for (CollisionBox box : super.getCollisionBoxes())
			box.update(super.getPosition());

		for (CollisionBox tBox : super.getCollisionBoxes())
			for (Entity e : enitities)
				if (e != this)
					for (CollisionBox box : e.getCollisionBoxes()) {

						CollisionBox xBox = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(velocity.x, 0, 0), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());
						CollisionBox yBox = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(0, velocity.y, 0), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());
						CollisionBox zBox = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(0, 0, velocity.z), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());

						CollisionBox xBoxN = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(-velocity.x, 0, 0), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());
						CollisionBox yBoxN = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(0, -velocity.y, 0), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());
						CollisionBox zBoxN = new CollisionBox(
								Vector3f.add(super.getPosition(), new Vector3f(0, 0, -velocity.z), new Vector3f()),
								tBox.getWidth(), tBox.getHeight(), tBox.getLength());

						if (box.collide(xBox))
							enable[0][0] = false;
						if (box.collide(yBox))
							enable[0][1] = false;
						if (box.collide(zBox))
							enable[0][2] = false;

						if (box.collide(xBoxN))
							enable[1][0] = false;
						if (box.collide(yBoxN))
							enable[1][1] = false;
						if (box.collide(zBoxN))
							enable[1][2] = false;

						if (!enable[0][1] & enable[1][1] & velocity.y > 0) {
							velocity.y = 0;
							this.upwardsSpeed = 0;
							enable[0][1] = true;
						}
					}

		return enable;

	}

	private void jump() {
		if (!isInAir) {
			source.play(jumpSound, false);
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private float gravityFlow(Float terrainHeight) {

		if (!this.isOnSomething) {
			isInAir = true;
			upwardsSpeed -= GRAVITY * DisplayManager.getFrameTimeSeconds();
			if (flyng & !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				if (upwardsSpeed < 0)
					upwardsSpeed = 0;
		} else
			this.flyng = false;

		if (terrainHeight != null)
			if (super.getPosition().y < terrainHeight) {
				upwardsSpeed = 0;
				isInAir = false;
				super.getPosition().y = terrainHeight;
				flyng = false;
			}

		return upwardsSpeed * DisplayManager.getFrameTimeSeconds();
	}

	private void checkInputs() {
		if (this.canUseCommands) {

			float rotation = 0;
			float speed = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {

				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) & !Keyboard.isKeyDown(Keyboard.KEY_S))
					speed += (float) (currentWalkSpeed * 1.5);
				else
					speed += currentWalkSpeed;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {

				speed += -(currentWalkSpeed / 3) * 2;

				if (Keyboard.isKeyDown(Keyboard.KEY_W))
					speed = 0;

			}

			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				rotation += Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? 25
						: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? -25 : 90;

				if (!Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S))
					speed += currentWalkSpeed / 2;
				else if (Keyboard.isKeyDown(Keyboard.KEY_W) & Keyboard.isKeyDown(Keyboard.KEY_S))
					speed += currentWalkSpeed / 2;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				rotation -= Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? 25
						: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? -25 : 90;

				if (!Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S))
					speed += currentWalkSpeed / 2;
				else if (Keyboard.isKeyDown(Keyboard.KEY_W) & Keyboard.isKeyDown(Keyboard.KEY_S))
					speed += currentWalkSpeed / 2;

			}

			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				if (!flyng)
					jump();
				else if (flyng)
					upwardsSpeed = JUMP_POWER * 2;

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) & !flyng)
				speed *= 0.5;

			super.setRotY(super.getRotY() + rotation);

			if (speed != 0)
				this.currentSpeed = speed;

			if (Keyboard.isKeyDown(Keyboard.KEY_F))
				flyng = true;

			if (Keyboard.isKeyDown(Keyboard.KEY_G))
				flyng = false;

			if (Main.enableController) {

				// TODO add controller stuffs

			}
		}

	}

	public void setFlying(boolean flying) {
		this.flyng = flying;
	}

	public void disableCommands() {
		this.canUseCommands = false;
	}

	public void enableCommands() {
		this.canUseCommands = true;
	}
}
