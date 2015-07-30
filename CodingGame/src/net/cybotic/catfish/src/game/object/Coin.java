package net.cybotic.catfish.src.game.object;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Coin extends GameObject {
	
	private Image coin;
	private boolean up = true;
	private float cool = 0f;

	public Coin(int x, int y, Game game) throws SlickException {
		
		super(x, y, 5, 0, "", false, game, "coin", false, 0);
		coin = Main.loadImage("res/coin.png");
		
		cool = (new Random()).nextInt(5);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		if (up) {
			
			cool += 0.01f * delta;
			if (cool > 4f) {
				
				up = false;
				cool = 4f;
				
			}
			
		} else {
			
			cool -= 0.01f * delta;
			if (cool < 0f) {
				
				up = true;
				cool = 0f;
				
			}
			
		}
		
		if (!this.isDead()) {
		
			for (GameObject object : game.getGameObjects()) {
				
				if (object.getX() == this.getX() && object.getY() == this.getY() && (object instanceof Robot) && !object.isDead() && object.isCollidable()) {
					
					game.getCoin(gc);
					this.die();
					
				}
				
			}
		
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		if (up && cool < 1f) coin.drawFlash(this.getRenderingX() + 8, this.getRenderingY() + 4 - cool); 
		else g.drawImage(coin, this.getRenderingX() + 8, this.getRenderingY() + 4 - cool);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 2;
		
	}

	@Override
	public void trigger() {
		
		
		
	}
	
}
