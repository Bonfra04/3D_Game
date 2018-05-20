package gameEngine.collision;

import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

public class CollisionBox {

	private float width;
	private float height;
	private float length;
	private Vector3f position;
	private Vector3f offsets;
	private Vector3f center;

	private Rectangle2D frontRectangle;
	private Rectangle2D topRectangle;

	public CollisionBox(float width, float height, float length, Vector3f offsets) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = new Vector3f(0, 0, 0);
		this.offsets = offsets;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		frontRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x, this.position.y + this.offsets.y,
				width, height);

		topRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x,
				this.position.z + this.length + this.offsets.z, width, length);
	}

	public CollisionBox(float width, float height, float length, Vector3f offsets, Vector3f position) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.position = position;
		this.offsets = offsets;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		frontRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x, this.position.y + this.offsets.y,
				width, height);

		topRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x,
				this.position.z + this.length + this.offsets.z, width, length);
	}

	public boolean collide(CollisionBox box) {

		if (this.frontRectangle.intersects(box.getFrontRectangle())
				& this.topRectangle.intersects(box.getTopRectangle()))
			return true;

		return false;
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

		frontRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x, this.position.y + this.offsets.y,
				width, height);

		topRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x,
				this.position.z + this.length + this.offsets.z, width, length);
	}

	public Vector3f getPosition() {
		return new Vector3f(position.x + offsets.x, position.y + offsets.y, position.z + offsets.z);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		this.center = new Vector3f(position.x + width / 2, position.y + height / 2, position.z + length / 2);

		frontRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x, this.position.y + this.offsets.y,
				width, height);

		topRectangle = new Rectangle2D.Float(this.position.x + this.offsets.x,
				this.position.z + this.length + this.offsets.z, width, length);
	}

	public Vector3f getOffsets() {
		return offsets;
	}

	public Rectangle2D getFrontRectangle() {
		return frontRectangle;
	}

	public Rectangle2D getTopRectangle() {
		return topRectangle;
	}

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}
}
