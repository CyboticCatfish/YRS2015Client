package net.cybotic.catfish.src.game.object;

import net.cybotic.catfish.src.game.Game;
import net.cybotic.catfish.src.script.ScriptEnv;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public abstract class GameObject {
	
	private int x, y, dir;
	private float renderingX, renderingY;
	private boolean moving = false, canPerformActions = false, scriptRunning = false, scriptable = false;
	private ScriptEnv scriptEnv;
	private String script;
	Game game;
	
	public GameObject(int x, int y, int dir, String script, boolean scriptable, boolean canPerformActions, Game game) {
		
		this.scriptEnv = new ScriptEnv(this);
		this.x = x;
		this.y = y;
		this.renderingX = x * 64;
		this.renderingY = y * 64;
		this.dir = dir;
		this.canPerformActions = canPerformActions;
		this.script = script;
		this.game = game;
		this.scriptable = scriptable;
		
	}
	
	public abstract void update(GameContainer gc, int delta);
	public abstract void render(GameContainer gc, Graphics g);
	
	public void preUpdate(GameContainer gc, int delta) throws SlickException {
		
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) && this.scriptable
				&& gc.getInput().getAbsoluteMouseX() < this.getRenderingX() + 64 && gc.getInput().getAbsoluteMouseX() > this.getRenderingX()
				&& gc.getInput().getAbsoluteMouseY() < this.getRenderingY() + 64 && gc.getInput().getAbsoluteMouseY() > this.getRenderingY()) {
			
			this.game.openEditor(this);
			
		}
		
		if (this.moving && this.canPerformActions) {
			
			if (renderingY < y * 64 && dir == 2) renderingY += 0.1f * delta;
			else if (renderingX > x * 64 && dir == 3) renderingX -= 0.1f * delta;
			else if (renderingY > y * 64 && dir == 0) renderingY -= 0.1f * delta;
			else if (renderingX < x * 64 && dir == 1) renderingX += 0.1f * delta;
			else {
				
				this.renderingX = x * 64;
				this.renderingY = y * 64;
				
				moving = false;
				
			}
			
		}
		
		this.update(gc, delta);
		
	}
	
	public void runScript() {
		
		if (!scriptRunning && this.scriptable) {
		
			scriptEnv.launchScript();
			
		}
		
	}
	
	public void setScript(String script) {
		
		this.script = script;
		
	}
	
	public abstract void interactedWith(GameObject object);
	public abstract int getObjectTypeID();

	public String getScript() {
		
		return this.script;
		
	}
	
	public float getRenderingX() {
		
		return (int) Math.ceil(renderingX);
		
	}
	
	public float getRenderingY() {
		
		return (int) Math.ceil(renderingY);
		
	}
	
	public boolean isMoving() {
		
		return this.moving;
		
	}
	
	public void moveForward() {
		
		moving = true;
				
		if (dir == 0) y -= 1;
		else if (dir == 1) x += 1;
		else if (dir == 2) y += 1;
		else if (dir == 3) x -= 1;
		
		
	}
	
	public void turnClockwise() {
		
		dir += 1;
		if (dir > 3) dir = 0;
		
	}
	
	public void turnAntiClockwise() {
		
		dir -= 1;
		if (dir < 0) dir = 3;
		
	}
	
	public void interact() {
		
		//TODO make interacted with execute on the nearest object
		
	}
	
}
