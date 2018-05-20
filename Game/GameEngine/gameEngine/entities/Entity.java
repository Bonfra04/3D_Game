package gameEngine.entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.collision.CollisionBox;
import gameEngine.models.TexturedModel;
import gameEngine.objConverter.OBJLoader;
import gameEngine.renderEngine.Loader;
import gameEngine.resourcesManager.ResourceManager;
import gameEngine.textures.ModelTexture;

public class Entity {

	private static final Loader LOADER = new Loader();

	private TexturedModel model;
	private Vector3f position;
	private Vector3f lastPosition;
	private float rotX, rotY, rotZ;
	private float scale;

	private int textureIndex = 0;

	private List<CollisionBox> collisionBoxes = new ArrayList<CollisionBox>();

	public Entity(String model, String texture, int totalTextureRows, int textureIndex, float scale, Vector3f position,
			Vector3f rotation) {
		this.textureIndex = textureIndex;
		this.position = position;
		this.rotX = rotation.x;
		this.rotY = rotation.y;
		this.rotZ = rotation.z;
		this.scale = scale;
		this.model = ResourceManager.getModel(model) == null
				? new TexturedModel(OBJLoader.loadObjModel(model, LOADER),
						new ModelTexture(LOADER.loadTexture(texture)).setNumberOfRows(totalTextureRows))
				: ResourceManager.getModel(model);

		ResourceManager.addEntity(this);
	}

	public Entity(String model, String texture, float scale, Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotX = rotation.x;
		this.rotY = rotation.y;
		this.rotZ = rotation.z;
		this.scale = scale;
		this.model = ResourceManager.getModel(model) == null ? new TexturedModel(OBJLoader.loadObjModel(model, LOADER),
				new ModelTexture(LOADER.loadTexture(texture))) : ResourceManager.getModel(model);

		ResourceManager.addEntity(this);
	}

	public Entity(TexturedModel model, float scale, Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotX = rotation.x;
		this.rotY = rotation.y;
		this.rotZ = rotation.z;
		this.scale = scale;
		this.model = model;

		ResourceManager.addEntity(this);
	}

	public List<CollisionBox> getCollisionBoxes() {
		return this.collisionBoxes;
	}

	public void addCollisionBox(CollisionBox collisionBox) {
		collisionBox.update(new Vector3f(this.getPosition().x + 1, this.getPosition().y + 1, this.getPosition().z + 1));
		this.collisionBoxes.add(collisionBox);
	}

	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		lastPosition = new Vector3f(position.x, position.y, position.z);

		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;

		for (CollisionBox box : collisionBoxes)
			box.update(position);
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public Entity setTextureTrasparency(boolean trasparency) {
		this.getModel().getTexture().setTransparency(trasparency);
		return this;
	}

	public Entity setTextureFakeLighting(boolean fakeLighting) {
		this.getModel().getTexture().setUseFakeLighting(fakeLighting);
		return this;
	}

	public Entity setNumberOfTextureRows(int rows) {
		this.getModel().getTexture().setNumberOfRows(rows);
		return this;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getLastPosition() {
		return lastPosition;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
