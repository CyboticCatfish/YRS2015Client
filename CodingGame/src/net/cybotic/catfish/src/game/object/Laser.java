package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Laser extends GameObject {

	private SpriteSheet sheet;
	private boolean shooting = false, killed = false;
	private float cool = 0f;
	private int minX, minY, maxX, maxY;
	
	public Laser(int x, int y, Game game, int listenerLevel) throws SlickException {
		
		super(x, y, 6, 0, "", false, game, "laser", true, listenerLevel);
		
		sheet = new SpriteSheet(Main.loadImage("res/laser.png"), 32, 32);
		
		minX = this.getX() - 1;
		maxX = this.getX() + 1;
		minY = this.getY() - 1;
		maxY = this.getY() + 1;
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		if (shooting) {
			
			cool += 0.1f * delta;
			if (cool > 100f) {
				
				cool = 0f;
				shooting = false;
				
			}
			
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		if (shooting) {
			
			for (int i = minX; i < maxX; i++) {
				
				g.drawImage(sheet.getSprite(1, 1), i * 32, this.getY() * 32);
				
			}
			
			for (int i = minY; i < maxY; i++) {
				
				g.drawImage(sheet.getSprite(2, 0), this.getX() * 32, i * 32);
				
			}
			
		}
		
		if (!shooting) g.drawImage(sheet.getSprite(0, 0), this.getRenderingX(), this.getRenderingY());
		else g.drawImage(sheet.getSprite(2, 1), this.getRenderingX(), this.getRenderingY());
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 7;
		
	}

	@Override
	public void trigger() {
		
		if (!shooting) {
		
			shooting = true;
			cool = 0f;
			
			for (int x = this.getX() - 1; x >= 0; x--) {
				
				boolean found = false;
				
				for (GameObject object : game.getGameObjects()) {
					
					if (object.getY() == this.getY() && object instanceof Wall && object.getX() == x) {
						
						found = true;
						break;
						
					}
						
				}
				
				if (!found) minX = x;
				else break;
					
			}
			
			for (int x = this.getX() + 1; x <= game.getWidth(); x++) {
				
				boolean found = false;
				
				for (GameObject object : game.getGameObjects()) {
					
					if (object.getY() == this.getY() && object instanceof Wall && object.getX() == x) {
						
						found = true;
						break;
						
					}
						
				}
				
				if (!found) maxX = x;
				else break;
					
			}
			
			for (int y = this.getY() - 1; y >= 0; y--) {
				
				boolean found = false;
				killed = false;
				
				for (GameObject object : game.getGameObjects()) {
					
					if (object.getX() == this.getX() && object instanceof Wall && object.getY() == y) {
						
						found = true;
						break;
						
					}
						
				}
				
				if (!found) minY = y;
				else break;
					
			}
			
			for (int y = this.getY() + 1; y <= game.getHeight(); y++) {
				
				boolean found = false;
				
				for (GameObject object : game.getGameObjects()) {
					
					if (object.getX() == this.getX() && object instanceof Wall && object.getY() == y) {
						
						found = true;
						break;
						
					}
						
				}
				
				if (!found) maxY = y + 1;
				else break;
					
			}
		
		} else if (cool > 40f && !killed) {
			
			for (GameObject object : game.getGameObjects()) {
				
				if ((object.getX() == this.getX() | object.getY() == this.getY()) && !(object instanceof Wall) && object.collidable && !(object.equals(this))) {
					
					object.die();
					
				}
				
			}
			
			killed = true;
			
		}
		
	}

}
