package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Robot extends GameObject {
	
	private SpriteSheet robot;

	public Robot(int x, int y, int z, int dir, String script, boolean scriptable,
			Game game, String name, boolean collidable, int listenerLevel) throws SlickException {
		
		super(x, y, z, dir, script, scriptable, game, name, collidable, listenerLevel);
		
		robot = new SpriteSheet(Main.loadImage("res/robot.png"), 48, 48);
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		g.drawImage(robot.getSprite(this.getDir(), 0), this.getRenderingX() - 12, this.getRenderingY() - 12);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 0;
		
	}

	@Override
	public void trigger() {
		
		
		
	}
	
}
