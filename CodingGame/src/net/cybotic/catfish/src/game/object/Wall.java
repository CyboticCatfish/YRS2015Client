package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Wall extends GameObject {
	
	private Image wall;

	public Wall(int x, int y, Game game) throws SlickException {
		
		super(x, y, 0, 0, "", false, game, "wall", true, 0);
		
		wall = Main.loadImage("res/wall.png");
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		g.drawImage(wall, getRenderingX(), getRenderingY() - 6);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 1;
		
	}

	@Override
	public void trigger() {
		
	}
	
	
}
