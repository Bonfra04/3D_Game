package gameEngine.toolbox;

import java.awt.geom.Rectangle2D;

public class Parallelepiped {

	private Rectangle3D base;
	private float height;

	public Parallelepiped(Rectangle3D base, float height) {
		this.base = base;
		this.height = height;
	}

	public boolean intersects(Parallelepiped parallelepiped) {

		Rectangle2D tBase = new Rectangle2D.Float(this.base.getVertex().x, this.base.getVertex().z,
				this.getBase().getWidth(), this.getBase().getLenght());

		Rectangle2D rBase = new Rectangle2D.Float(parallelepiped.base.getVertex().x, parallelepiped.base.getVertex().z,
				parallelepiped.getBase().getWidth(), parallelepiped.getBase().getLenght());

		Rectangle2D tFront = new Rectangle2D.Float(this.base.getVertex().x, this.base.getVertex().y,
				this.base.getWidth(), this.height);

		Rectangle2D pFront = new Rectangle2D.Float(parallelepiped.base.getVertex().x, parallelepiped.base.getVertex().y,
				parallelepiped.base.getWidth(), parallelepiped.height);

		Rectangle2D tSide = new Rectangle2D.Float(this.base.getVertex().z, this.base.getVertex().y,
				this.base.getLenght(), this.height);

		Rectangle2D pSide = new Rectangle2D.Float(parallelepiped.base.getVertex().z, parallelepiped.base.getVertex().y,
				parallelepiped.base.getLenght(), parallelepiped.height);

		return (tBase.intersects(rBase) & (tFront.intersects(pFront) | tSide.intersects(pSide)));

	}

	public Rectangle3D getBase() {
		return base;
	}

	public void setBase(Rectangle3D base) {
		this.base = base;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
