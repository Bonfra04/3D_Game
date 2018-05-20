package gameEngine.toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Rectangle3D {

	private Vector3f tL;
	private Vector3f tR;
	private Vector3f bL;
	private Vector3f Br;

	public Rectangle3D(Vector3f tL, Vector3f tR, Vector3f bL, Vector3f Br) {
		this.tL = tL;
		this.tR = tR;
		this.bL = bL;
		this.Br = Br;
	}

	public Vector3f getTL() {
		return tL;
	}

	public Vector3f getTR() {
		return tR;
	}

	public Vector3f getBL() {
		return bL;
	}

	public Vector3f getBR() {
		return Br;
	}
}
