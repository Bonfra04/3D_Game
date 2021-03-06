package gameEngine.guis;

import org.lwjgl.util.vector.Vector2f;

import gameEngine.resourcesManager.ResourceManager;

public class GuiTexture {
	
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	private boolean visible;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.visible = true;
		
		ResourceManager.addGui(this);
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}
