package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.SoundBank;
import net.cybotic.catfish.src.game.Game;

public class PressurePlate extends GameObject {
	
	private boolean active = false, trigger = false;
	private Image normal, pressed;

	public PressurePlate(int x, int y, Game game, int listenerLevel) throws SlickException {
		
		super(x, y, 0, 0, "", false, game, "pressureplate", false, listenerLevel);
		
		SpriteSheet image = new SpriteSheet(Main.loadImage("res/pressureplate.png"), 32, 34);
		
		normal = image.getSprite(0, 0);
		pressed = image.getSprite(1, 0);
		
		active = false;
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
		active = false;
		
		for (GameObject object : game.getGameObjects()) {
				
			if (object.getX() == this.getX() && object.getY() == this.getY() && !(object instanceof PressurePlate) && !object.isDead() && object.isCollidable()) {
				
				active = true;
				if (!trigger) {
					
					game.addTrigger(getListenerLevel());
					SoundBank.PLATE.play();
					trigger = true;
					
				}
				
			}
		
		}
		
		if (active == false) trigger = false;
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		if (active) g.drawImage(pressed, this.getRenderingX(), this.getRenderingY() - 2);
		else g.drawImage(normal, this.getRenderingX(), this.getRenderingY() - 2);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 3;
		
	}

	@Override
	public void trigger() {
		
		
		
	}
	
}
