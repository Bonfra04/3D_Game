package gameEngine.collision;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.toolbox.Parallelepiped;
import gameEngine.toolbox.Rectangle3D;

public class CollisionBox {

	private float width;
	private float height;
	private float length;
	private Vector3f position;
	private Vector3f offsets;
	private Vector3f center;

	private Parallelepiped box;

	public CollisionBox(float width, float height, float length) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = new Vector3f(0, 0, 0);
		this.offsets = new Vector3f(0, 0, 0);
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	public CollisionBox(float width, float height, float length, Vector3f offsets) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = new Vector3f(0, 0, 0);
		this.offsets = offsets;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	public CollisionBox(Vector3f position, float width, float height, float length) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = position;
		this.offsets = new Vector3f(0, 0, 0);
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	public CollisionBox(float width, float height, float length, Vector3f offsets, Vector3f position) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = position;
		this.offsets = offsets;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	public boolean collide(CollisionBox box) {
		return (this.box.intersects(box.box));
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getLength() {
		return length;
	}

	public void update(Vector3f position) {
		this.position = position;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	public Vector3f getPosition() {
		return new Vector3f(position.x + offsets.x, position.y + offsets.y, position.z + offsets.z);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		this.updateParallelepiped();
	}

	private void updateParallelepiped() {
		this.box = new Parallelepiped(new Rectangle3D(this.position, this.width, this.length), this.height);
	}

	public Vector3f getOffsets() {
		return offsets;
	}

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}
}
