package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Bomb extends GameObject {
	
	private Image bomb;
	private Animation explode;
	private boolean exploding = false;
	
	public Bomb(int x, int y, String script, boolean scriptable, Game game, int listenerLevel) throws SlickException {
		
		super(x, y, 5, 0, script, scriptable, game, "bomb", true, listenerLevel);
		bomb = Main.loadImage("res/bomb.png");
		
		SpriteSheet sheet = new SpriteSheet(Main.loadImage("res/explode.png"), 64, 64);
		
		explode = new Animation(sheet, 0, 0, 2, 0, true, 1, true);
		explode.setLooping(false);
		explode.setSpeed(0.05f);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		if (explode.isStopped() && this.exploding) {
			
			this.die();
			for (GameObject object : game.getGameObjects()) {
				
				if (object.getX() >= this.getX() - 1 && object.getX() <= this.getX() + 1 &&
						object.getY() >= this.getY() - 1 && object.getY() <= this.getY() + 1) {
					
					object.die();
					
				}
				
			}
			
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		if (!exploding) g.drawImage(bomb, this.getRenderingX() - 4, this.getRenderingY());
		else g.drawAnimation(explode, this.getRenderingX() - 16, this.getRenderingY() - 16);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 5;
		
	}

	@Override
	public void trigger() {
		
		this.explode();
		
	}
	
	public void explode() {
		
		explode.start();
		this.exploding = true;
		
	}
	
}
