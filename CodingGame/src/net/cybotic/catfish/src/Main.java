package net.cybotic.catfish.src;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import net.cybotic.catfish.src.game.Game;

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
	public static SpriteSheetFont GAME_FONT, GAME_FONT_2;
	public static String SERVER_URL = "http://code404.tk";
	public static SpriteSheet USEFUL_BUTTONS, BIG_BUTTON;
	
	private static boolean devMode = false;
	private static String filePath;
	
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
		
		if (args.length == 0) devMode = false;
		else {
			
			devMode = true;
			filePath = args[0];
			
		}
		
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(Main.WIDTH, Main.HEIGHT, Main.FULLSCREEN);
		//app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
		app.setAlwaysRender(true);
		app.setShowFPS(false);
		app.start();
		
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		
		new SoundBank();
		
		GAME_FONT = new SpriteSheetFont(new SpriteSheet(Main.loadImage("res/font.png"), 16, 22), ' ');
		GAME_FONT_2 = new SpriteSheetFont(new SpriteSheet(Main.loadImage("res/font2.png"), 16, 22), ' ');
		CURSOR_IMAGES = new SpriteSheet(Main.loadImage("res/cursors.png"), 32, 32);
		USEFUL_BUTTONS = new SpriteSheet(Main.loadImage("res/buttons2.png"), 32, 32);
		BIG_BUTTON = new SpriteSheet(Main.loadImage("res/button.png"), 128, 32);
		gc.setMouseCursor(CURSOR_IMAGES.getSprite(2, 0), 0, 0);
		
		if (!devMode) {
		
			this.addState(new CyboticCatfish());
			this.addState(new LoginScreen());
			this.enterState(0);
		
		} else {
			
			this.addState(new Game(filePath, "test", "test"));
			this.enterState(3);
			
		}
		
	}
	
	public static Image loadImage(String file) throws SlickException {
		
		Image temp = new Image(file);
		temp.setFilter(Image.FILTER_NEAREST);
		
		return temp.getScaledCopy(2f);
		
	}

	public static void writeConfig() throws IOException {
		
		BufferedWriter br = new BufferedWriter(new FileWriter(new File("config.data")));
		
		br.write(Main.WIDTH + "\n");
		br.write(Main.HEIGHT + "\n");
		br.write(Main.LOGIN_TOKEN);
		
		br.close();
		
	}
	
	public static void openWebpage(URI uri) {
		
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	        	
	            desktop.browse(uri);
	            
	        } catch (Exception e) {
	        	
	            e.printStackTrace();
	            
	        }
	    }
	}

	public static void openWebpage(URL url) {
		
	    try {
	    	
	        openWebpage(url.toURI());
	        
	    } catch (URISyntaxException e) {
	    	
	        e.printStackTrace();
	        
	    }
	}
	
}
