package cube;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import gameEngine.entities.Camera;
import gameEngine.models.RawModel;
import gameEngine.resourcesManager.ResourceManager;

public class cubeRenderer {

	private static final float[] VERTICES = { -1f, 1f, -1f, -1f, -1f, -1f, 1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f, -1f, -1f,
			1f, -1f, -1f, -1f, 1f, -1f, -1f, -1f, -1f, 1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f, -1f, -1f, 1f, 1f, -1f, -1f,
			1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, -1f, 1f, -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f,
			1f, 1f, 1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f, -1f, 1f, 1f, -1f, 1f,
			-1f, -1f, -1f, -1f, -1f, -1f, 1f, 1f, -1f, -1f, 1f, -1f, -1f, -1f, -1f, 1f, 1f, -1f, 1f };

	private static final RawModel MODEL = ResourceManager.LOADER.loadToVAO(VERTICES, 3);

	private CubeShader shader;

	public cubeRenderer(Matrix4f projectionMatrix) {
		shader = new CubeShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);

		for (Cube cube : ResourceManager.getCubes()) {
			GL30.glBindVertexArray(MODEL.getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cube.getTexture());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, MODEL.getVertexCount());
		}

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.stop();
	}

}
