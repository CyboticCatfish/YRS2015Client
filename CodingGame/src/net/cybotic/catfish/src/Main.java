package net.cybotic.catfish.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {
	
	public static int WIDTH = 800, HEIGHT = 600;
	public static String LOGIN_TOKEN;
	public static boolean FULLSCREEN = false;
	public static SpriteSheet CURSOR_IMAGES;
	public static boolean MUST_LOGIN = true;
	public static SpriteSheetFont GAME_FONT;
	
	public Main() {
		
		super("YRS2015");
		
	}
	
	public static void main(String args[]) throws SlickException {
		
		File f = new File("config.data");
		
		if (f.exists()) {
			
			try {
				
				BufferedReader br = new BufferedReader(new FileReader(f));
				
				WIDTH = Integer.parseInt(br.readLine());
				HEIGHT = Integer.parseInt(br.readLine());
				LOGIN_TOKEN = br.readLine();
				
				MUST_LOGIN = false;
				
				br.close();
				
			} catch (NumberFormatException | IOException e) {
				
				f.delete();
				
			}
			
		}
		
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(Main.WIDTH, Main.HEIGHT, Main.FULLSCREEN);
		app.setAlwaysRender(true);
		app.setShowFPS(false);
		app.start();
		
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		
		GAME_FONT = new SpriteSheetFont(new SpriteSheet(Main.loadImage("res/font.png"), 16, 22), ' ');
		CURSOR_IMAGES = new SpriteSheet(Main.loadImage("res/cursors.png"), 32, 32);
		gc.setMouseCursor(CURSOR_IMAGES.getSprite(2, 0), 0, 0);
		
		/**
		
			HttpRequest request = HttpRequest.get("https://dev.mrmindimplosion.co.uk:5000/level/get?id=3");
			request.trustAllCerts();
			request.trustAllHosts();
		
		**/
		
		this.addState(new CyboticCatfish());
		this.enterState(0);
		
	}
	
	public static Image loadImage(String file) throws SlickException {
		
		Image temp = new Image(file);
		temp.setFilter(Image.FILTER_NEAREST);
		
		return temp.getScaledCopy(2f);
		
	}
	
}
