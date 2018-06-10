package gameEngine.toolbox;

import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

public class Rectangle3D {

	private Vector3f vertex;
	private float width;
	private float length;

	public Rectangle3D(Vector3f vertex, float width, float length) {
		this.vertex = vertex;
		this.width = width;
		this.length = length;
	}

	public boolean intersects(Rectangle3D rectangle3d) {

		Rectangle2D tRrectangle2D = new Rectangle2D.Float(this.vertex.x, this.vertex.z, this.width, this.length);

		Rectangle2D rRectangle2d = new Rectangle2D.Float(rectangle3d.vertex.x, rectangle3d.vertex.z, rectangle3d.width,
				rectangle3d.length);

		return (tRrectangle2D.intersects(rRectangle2d) & this.vertex.y == rectangle3d.vertex.y);
	}

	public Vector3f getVertex() {
		return vertex;
	}

	public void setVertex(Vector3f vertex) {
		this.vertex = vertex;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLenght() {
		return length;
	}

	public void setLenght(float lenght) {
		this.length = lenght;
	}
}
