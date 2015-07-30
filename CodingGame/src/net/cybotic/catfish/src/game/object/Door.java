package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Door extends GameObject {
	
	private boolean opened = false;
	private Image open, closed;

	public Door(int x, int y, int dir, Game game, int listenerLevel) throws SlickException {
		
		super(x, y, 5, dir, "", false, game, "door", true, listenerLevel);
		
		SpriteSheet sheet = new SpriteSheet(Main.loadImage("res/door.png"), 32, 40);
		
		if (dir == 0 | dir == 2) {
			
			open = sheet.getSprite(1, 1);
			closed = sheet.getSprite(1, 0);
			
		} else {
			
			open = sheet.getSprite(2, 1);
			closed = sheet.getSprite(2, 0);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		if (opened) g.drawImage(open, getRenderingX(), getRenderingY() - 8);
		else g.drawImage(closed, getRenderingX(), getRenderingY() - 8);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 4;
		
	}

	@Override
	public void trigger() {
		
		opened = true;
		collidable = false;
		setZ(0);
		
	}
	
}
