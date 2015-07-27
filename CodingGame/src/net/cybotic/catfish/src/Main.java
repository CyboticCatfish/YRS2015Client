package net.cybotic.catfish.src;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {
	
	public static int WIDTH = 800, HEIGHT = 600;
	public static boolean FULLSCREEN = false;
	
	public Main() {
		
		super("YRS2015");
		
	}
	
	public static void main(String args[]) throws SlickException {
		
		//TODO add load screen dimennsions from config
		
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(Main.WIDTH, Main.HEIGHT, Main.FULLSCREEN);
		app.start();
		
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		
		
		
	}
	
}
