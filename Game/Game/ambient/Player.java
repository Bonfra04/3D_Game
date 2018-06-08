package ambient;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import game.Main;
import gameEngine.audio.AudioMaster;
import gameEngine.audio.Source;
import gameEngine.collision.CollisionBox;
import gameEngine.collision.CreatureCollisionBox;
import gameEngine.collision.Position;
import gameEngine.entities.Entity;
import gameEngine.renderEngine.DisplayManager;
import gameEngine.terrains.Terrain;
import gameEngine.toolbox.Timer;

public class Player extends Entity {

	private static final float WALK_SPEED = 20f * 2;
	public static final float GRAVITY = 40.0f;
	private static final float JUMP_POWER = 15.0f;

	private static final float START_WALKING_TIME = 200;
	private static final float STOP_WALKING_TIME = 50;

	private float currentWalkSpeed = WALK_SPEED;
	private Timer timer = new Timer();

	private float currentSpeed = 0;
	private float assignedSpeed = 0;

	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	private boolean flyng = false;

	private Direction lastDirection = Direction.STATIC;

	private boolean isNowPressing = false;
	private boolean lastPressing = false;
	private boolean stopping = false;
	private boolean stopped = false;

	private Source source = AudioMaster.generateSource();
	private int jumpSound = AudioMaster.loadSound("bounce");

	private CreatureCollisionBox collisionBox = new CreatureCollisionBox(2, 5, 2, new Vector3f(0, 0, 0));

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
		
		gravityFlow(Terrain.getHeight(super.getPosition().x, super.getPosition().z));

		checkInputs();

		currentSpeed = assignedSpeed;

		smoothMovement();

		checkCollision(enitities);

		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

		this.increasePosition(dx, 0, dz);
	}

	private void smoothMovement() {
		if (timer.isRunning() & !stopping & !stopped) {
			timer.update();
			if (timer.getTime() >= START_WALKING_TIME)
				timer.stop();

			currentSpeed = ((timer.getTime() - timer.getTime() % (START_WALKING_TIME / assignedSpeed))
					/ (START_WALKING_TIME / assignedSpeed));

		} else if (stopping & !stopped) {

			if (timer.getTime() >= STOP_WALKING_TIME) {
				stopped = true;
				stopping = false;
				timer.stop();

				currentSpeed = 0;
			} else {
				if (!timer.running)
					timer.start();

				timer.update();

				currentSpeed = assignedSpeed
						- ((timer.getTime() - timer.getTime() % (STOP_WALKING_TIME / assignedSpeed))
								/ (STOP_WALKING_TIME / assignedSpeed));
			}
		} else if (stopped)
			currentSpeed = 0;

		if (lastDirection == Direction.FRONT)
			if (currentSpeed < 0)
				currentSpeed = 0;
		if (lastDirection == Direction.BACK)
			if (currentSpeed > 0)
				currentSpeed = 0;
	}

	private void checkCollision(List<Entity> enitities) {
		for (CollisionBox box : super.getCollisionBoxes())
			box.update(super.getPosition());

		for (Entity e : enitities)
			if (e != this)
				for (CollisionBox box : e.getCollisionBoxes())
					if (collisionBox.collideTo(box) != Position.EXTERNAL) {
						// TODO COLLISION HANDLING
					}
	}

	private void jump() {
		if (!isInAir) {
			source.play(jumpSound, false);
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	private void gravityFlow(Float terrainHeight) {
		isInAir = true;
		upwardsSpeed -= GRAVITY * DisplayManager.getFrameTimeSeconds();
		if (flyng & !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			if (upwardsSpeed < 0)
				upwardsSpeed = 0;
		this.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);

		if (terrainHeight != null)
			if (super.getPosition().y < terrainHeight) {
				upwardsSpeed = 0;
				isInAir = false;
				super.getPosition().y = terrainHeight;
				flyng = false;
			}

		if (isInAir & !flyng)
			if (currentWalkSpeed > (GRAVITY / 10) * DisplayManager.getFrameTimeSeconds())
				currentWalkSpeed -= (GRAVITY / 10) * DisplayManager.getFrameTimeSeconds();
			else
				currentWalkSpeed = (GRAVITY / 10) * DisplayManager.getFrameTimeSeconds();
		else if (flyng)
			currentWalkSpeed = 2f * WALK_SPEED;
		else
			currentWalkSpeed = WALK_SPEED;
	}

	private void checkInputs() {
		float rotation = 0;
		float speed = 0;

		lastPressing = isNowPressing;
		isNowPressing = false;

		stopping = true;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {

			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) & !Keyboard.isKeyDown(Keyboard.KEY_S))
				speed += (float) (currentWalkSpeed * 1.5);
			else
				speed += currentWalkSpeed;

			isNowPressing = true;
			if (!timer.isRunning() & !lastPressing)
				timer.start();

			stopped = false;
			stopping = false;

			lastDirection = Direction.FRONT;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {

			speed += -(currentWalkSpeed / 3) * 2;

			isNowPressing = true;
			if (!timer.isRunning() & !lastPressing)
				timer.start();

			stopped = false;
			stopping = false;

			lastDirection = Direction.BACK;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			rotation += Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? 25
					: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? -25 : 90;

			if (!Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S))
				speed += currentWalkSpeed / 2;
			else if (Keyboard.isKeyDown(Keyboard.KEY_W) & Keyboard.isKeyDown(Keyboard.KEY_S))
				speed += currentWalkSpeed / 2;

			isNowPressing = true;
			if (!timer.isRunning() & !lastPressing)
				timer.start();

			stopped = false;
			stopping = false;

			lastDirection = Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? Direction.FRONT
					: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? Direction.BACK
							: Direction.FRONT;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			rotation -= Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? 25
					: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? -25 : 90;

			if (!Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S))
				speed += currentWalkSpeed / 2;
			else if (Keyboard.isKeyDown(Keyboard.KEY_W) & Keyboard.isKeyDown(Keyboard.KEY_S))
				speed += currentWalkSpeed / 2;

			isNowPressing = true;
			if (!timer.isRunning() & !lastPressing)
				timer.start();

			stopped = false;
			stopping = false;

			lastDirection = Keyboard.isKeyDown(Keyboard.KEY_W) & !Keyboard.isKeyDown(Keyboard.KEY_S) ? Direction.FRONT
					: Keyboard.isKeyDown(Keyboard.KEY_S) & !Keyboard.isKeyDown(Keyboard.KEY_W) ? Direction.BACK
							: Direction.FRONT;
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
			this.assignedSpeed = speed;

		if (stopped)
			stopping = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_F))
			flyng = true;

		if (Keyboard.isKeyDown(Keyboard.KEY_G))
			flyng = false;

		if (Main.enableController) {

			// TODO add controller stuffs

		}

	}
	
	public void setFlying(boolean flying) {
		this.flyng = flying;
	}
}
