package net.cybotic.catfish.src.game.object;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import net.cybotic.catfish.src.game.Game;

public class Test extends GameObject {

	public Test(Game game) {
		
		super(3, 3, 0, "object.moveForward();\nobject.turnClockwise();\nobject.moveForward();", true, true, game, "test");
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
		
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		g.setColor(Color.white);
		g.fillRect(this.getRenderingX(), this.getRenderingY(), 64, 64);
		
	}

	@Override
	public void interactedWith(GameObject object) {
		
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 0;
	}

	
	
}
