package gameEngine.toolbox;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.collision.CollisionBox;

public class Pyramid3D {

	private Polygon2D base;
	private Polygon2D front;
	private Polygon2D side;

	public Pyramid3D(Rectangle3D base, Vector3f vertex) {

		this.base = new Polygon2D(new float[] { base.getTL().x, base.getTR().x, base.getBR().x, base.getBL().x },
				new float[] { base.getTL().z, base.getTR().z, base.getBR().z, base.getBL().z }, 4);

		this.front = new Polygon2D(new float[] { base.getBL().x, base.getBR().x, vertex.x },
				new float[] { base.getBL().y, base.getBR().y, vertex.y }, 3);

		this.side = new Polygon2D(new float[] { base.getBL().z, base.getTL().z, vertex.z },
				new float[] { base.getBL().y, base.getTL().y, vertex.y }, 3);
	}

	public boolean contains(Vector3f point) {

		if (base.contains(new Point2D.Float(point.x, point.z)))
			if (front.contains(new Point2D.Float(point.x, point.y)))
				if (side.contains(new Point2D.Float(point.z, point.y)))
					return true;

		return false;
	}
}
