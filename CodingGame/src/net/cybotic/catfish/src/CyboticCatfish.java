package net.cybotic.catfish.src;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class CyboticCatfish extends BasicGameState {
	
	private float cool = 0f;
	private Image catfish;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		catfish = Main.loadImage("res/catfish.png");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setBackground(new Color(0, 26, 26));
		g.drawImage(catfish, gc.getWidth() / 2 - catfish.getWidth() / 2, gc.getHeight() / 2 - catfish.getHeight() / 2);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		cool += delta;
		
		if (cool > 5000 | gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			
			if (Main.MUST_LOGIN) {
				
				//TODO login screen redirect
				
			} else {
				
				
				
			}
			
		}
		
	}

	@Override
	public int getID() {
		
		return 0;
		
	}

	
	
}
