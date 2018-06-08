package cube;

import org.lwjgl.util.vector.Matrix4f;

import gameEngine.entities.Camera;
import gameEngine.shaders.ShaderProgram;
import gameEngine.toolbox.Maths;

public class CubeShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/cube/cubeVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/cube/cubeFragmentShader.glsl";
	
	private int location_projectionMatrix;
    private int location_viewMatrix;
     
    public CubeShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
