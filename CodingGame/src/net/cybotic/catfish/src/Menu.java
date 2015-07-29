package net.cybotic.catfish.src;

import net.cybotic.catfish.src.game.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.github.kevinsawicki.http.HttpRequest;

public class Menu extends BasicGameState {

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		Main.GAME_FONT.drawString(30, 30, "CLICK TO START THE DEFAULT LEVEL");
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			HttpRequest request = HttpRequest.get("https://dev.mrmindimplosion.co.uk:5000/level/get?id=3");
			request.trustAllCerts();
			request.trustAllHosts();
			
			Game game = new Game(request.body());
			sbg.addState(game);
			game.init(gc, sbg);
			sbg.enterState(3, new FadeOutTransition(), new FadeInTransition());
			
		}
		
	}

	@Override
	public int getID() {
		
		return 2;
		
	}
	
}
