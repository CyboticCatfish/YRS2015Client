package net.cybotic.catfish.src.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGameState {
	
	private boolean editorOpen = false;
	public static UnicodeFont FONT;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		FONT = new UnicodeFont("res/font.ttf", 20, false, false);

		FONT.addAsciiGlyphs();
		FONT.getEffects().add(new ColorEffect());
		FONT.loadGlyphs();
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setFont(Game.FONT);
		g.drawString("Test", 100, 100);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		
		
	}

	@Override
	public int getID() {
		// TODO actually make this appropriate
		return 0;
	}
	
	public boolean isEditorOpen() {
		
		return editorOpen;
		
	}
	
}
