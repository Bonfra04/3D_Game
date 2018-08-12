package gameEngine.toolbox;

import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

public class Parallelepiped {

	private Rectangle2D top;
	private Rectangle2D side;
	private Rectangle2D front;

	public Parallelepiped(Vector3f position, float width, float height, float lenght) {
		this.update(position, width, height, lenght);
	}

	public boolean collide(Parallelepiped p) {
		return top.intersects(p.top) & side.intersects(p.side) & front.intersects(p.front);
	}

	public void modify(Vector3f position, float width, float height, float lenght) {
		this.update(position, width, height, lenght);
	}

	private void update(Vector3f position, float width, float height, float lenght) {
		this.top = new Rectangle2D.Float(position.x, position.z, width, lenght);
		this.side = new Rectangle2D.Float(position.z, position.y, lenght, height);
		this.front = new Rectangle2D.Float(position.x, position.y, width, height);
	}

}
