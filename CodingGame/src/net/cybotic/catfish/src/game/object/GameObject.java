package net.cybotic.catfish.src.game.object;

import net.cybotic.catfish.src.game.Game;
import net.cybotic.catfish.src.script.ScriptEnv;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class GameObject {
	
	private int x, y, z = 0, dir;
	private float renderingX, renderingY;
	private boolean moving = false, scriptRunning = false, scriptable = false, collidable = false;
	private ScriptEnv scriptEnv;
	private String script, name;
	Game game;
	
	public GameObject(int x, int y, int z, int dir, String script, boolean scriptable, Game game, String name, boolean collidable) {
		
		this.scriptEnv = new ScriptEnv(this);
		this.x = x;
		this.y = y;
		this.renderingX = x * 48;
		this.renderingY = y * 48;
		this.dir = dir;
		this.script = script;
		this.game = game;
		this.scriptable = scriptable;
		this.name = name;
		this.collidable =collidable;
		
	}
	
	public abstract void update(GameContainer gc, int delta);
	public abstract void render(GameContainer gc, Graphics g);
	
	public void preUpdate(GameContainer gc, int delta) throws SlickException {
		
		if (this.moving && this.scriptable) {
			
			if (renderingY < y * 48 && dir == 2) renderingY += 0.1f * delta;
			else if (renderingX > x * 48 && dir == 3) renderingX -= 0.1f * delta;
			else if (renderingY > y * 48 && dir == 0) renderingY -= 0.1f * delta;
			else if (renderingX < x * 48 && dir == 1) renderingX += 0.1f * delta;
			else {
				
				this.renderingX = x * 48;
				this.renderingY = y * 48;
				
				moving = false;
				
			}
			
		}
		
		this.update(gc, delta);
		
	}
	
	public void runScript() {
		
		if (!scriptRunning && this.scriptable) {
		
			scriptEnv.launchScript();
			scriptRunning = true;
			
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
				
		if (dir == 0) {
			
			if (y > 0 && !collidableObjectToFront()) y -= 1;
			
		} else if (dir == 1) {
			
			if (x < game.getWidth() - 1 && !collidableObjectToFront()) x += 1;
			
		} else if (dir == 2) {
			
			if (y < game.getHeight() - 1 && !collidableObjectToFront()) y += 1;
			
		} else if (dir == 3) {
			
			if (x > 0 && !collidableObjectToFront()) x -= 1;
			
		} else moving = false;
	}
	
	private boolean collidableObjectToFront() {
		
		return false;
		
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
	
	public ScriptEnv getScriptEnv() {
		
		return this.scriptEnv;
		
	}

	public void scriptComplete() {
		
		this.scriptRunning = false;
		
	}

	public boolean isScriptRunning() {
	
		return scriptRunning;
		
	}

	public int getX() {
		
		return this.x;
		
	}
	
	public int getY() {
		
		return this.y;
		
	}
	
	public Game getGame() {
		
		return this.game;
		
	}

	public int getDir() {
		
		return this.dir;
		
	}
	
	public String getName() {
		
		return name;
		
	}

	public boolean isScriptable() {
		
		return this.scriptable;
		
	}
	
	public boolean isCollidable() {
		
		return this.collidable;
		
	}

	public int getZ() {
		
		return this.z;
		
	}
	
}
