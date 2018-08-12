package gameEngine.collision;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.toolbox.Parallelepiped;

public class CollisionBox {

	private float width;
	private float height;
	private float length;
	private Vector3f position;
	private Vector3f offsets;
	private Vector3f center;

	private Parallelepiped box;

	public CollisionBox(float width, float height, float length) {
		this(width, height, length, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
	}

	public CollisionBox(float width, float height, float length, Vector3f offsets) {
		this(width, height, length, offsets, new Vector3f(0, 0, 0));
	}

	public CollisionBox(Vector3f position, float width, float height, float length) {
		this(width, height, length, new Vector3f(0, 0, 0), position);
	}

	public CollisionBox(float width, float height, float length, Vector3f offsets, Vector3f position) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = position;
		this.offsets = offsets;
		this.center = new Vector3f(position.x + offsets.x + width / 2, position.y + offsets.y + height / 2,
				position.z + offsets.z + length / 2);

		this.box = new Parallelepiped(Vector3f.add(position, offsets, new Vector3f()), this.width, this.height,
				this.length);
	}

	public void update(Vector3f position) {
		this.position = Vector3f.add(position, this.offsets, new Vector3f());
		this.center = new Vector3f(this.position.x + this.offsets.x + this.width / 2,
				this.position.y + this.offsets.y + this.height / 2, this.position.z + this.offsets.z + this.length / 2);

		this.box.modify(Vector3f.add(this.position, this.offsets, new Vector3f()), this.width, this.height,
				this.length);
	}

	public boolean collide(CollisionBox box) {
		return this.box.collide(box.box);
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	public float getLength() {
		return this.length;
	}

	public Vector3f getPosition() {
		return new Vector3f(this.position.x + this.offsets.x, this.position.y + this.offsets.y,
				this.position.z + this.offsets.z);
	}

	public void setPosition(Vector3f position) {
		this.update(position);
	}

	public Vector3f getOffsets() {
		return this.offsets;
	}

	public Vector3f getCenter() {
		return this.center;
	}
}
