package gameEngine.collision;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.toolbox.Pyramid3D;
import gameEngine.toolbox.Rectangle3D;

public class CreatureCollisionBox extends CollisionBox {

	private Pyramid3D bottomPyramid;
	private Pyramid3D topPyramid;
	private Pyramid3D frontPyramid;
	private Pyramid3D backPyramid;
	private Pyramid3D leftPyramid;
	private Pyramid3D rightPyramid;

	public CreatureCollisionBox(float width, float height, float length, Vector3f offsets) {
		super(width, height, length, offsets);

		Vector3f vertex = new Vector3f(super.getPosition().x + super.getWidth() / 2,
				super.getPosition().y + super.getHeight() / 2, super.getPosition().z + super.getLength() / 2);

		// tL, tR, bL, bR
		Rectangle3D baseBottomo = new Rectangle3D(
				new Vector3f(super.getPosition().x, super.getPosition().y + super.getHeight(),
						super.getPosition().z + super.getLength()),
				new Vector3f(super.getPosition().x + super.getWidth(), super.getPosition().y + super.getHeight(),
						super.getPosition().z + super.getLength()),
				new Vector3f(super.getPosition().x, super.getPosition().y + super.getHeight(), super.getPosition().z),
				new Vector3f(super.getPosition().x + super.getWidth(), super.getPosition().y + super.getHeight(),
						super.getPosition().z));
		Rectangle3D baseTop = new Rectangle3D(
				new Vector3f(super.getPosition().x, super.getPosition().y, super.getPosition().z + super.getLength()),
				new Vector3f(super.getPosition().x + super.getWidth(), super.getPosition().y,
						super.getPosition().z + super.getLength()),
				new Vector3f(super.getPosition().x, super.getPosition().y, super.getPosition().z),
				new Vector3f(super.getPosition().x + super.getWidth(), super.getPosition().y, super.getPosition().z));

		bottomPyramid = new Pyramid3D(baseBottomo, vertex);
		topPyramid = new Pyramid3D(baseTop, vertex);
	}

	public CreatureCollisionBox(float width, float height, float length, Vector3f offsets, Vector3f position) {
		super(width, height, length, offsets, position);
	}

	public Position collideTo(CollisionBox box) {
		if (!super.collide(box))
			return Position.EXTERNAL;

		return Position.EXTERNAL;
	}

}
