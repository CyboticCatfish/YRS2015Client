package net.cybotic.catfish.src.game;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.editor.EditorKeyListener;
import net.cybotic.catfish.src.game.editor.EditorPane;
import net.cybotic.catfish.src.game.object.GameObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.xml.sax.SAXException;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class Game extends BasicGameState {
	
	private boolean editorOpen = false, loading = true, init = false, error = false, playing = false, paused = false;
	private EditorPane currentEditor;
	public static UnicodeFont FONT;
	private List<GameObject> objects;
	private int cursorMode = 1;
	private float translateX = 0f, translateY = 0f, lastMouseX, lastMouseY;
	private Level level;
	private int width, height, id = 0;
	private SpriteSheet tiles;
	private MouseOverArea play, stop, failed, pause, menu, tweet;
	private boolean errorCursor = false, completed = false;
	private int coins = 0;
	private Image coin;
	private String name, creator;
	
	public class LevelLoadThread implements Runnable {
		
		private int id;
		private Game game;
		
		public LevelLoadThread(int id, Game game) {
			
			this.id = id;
			this.game = game;
			
		}

		@Override
		public void run() {
			
			try {
				
				HttpRequest request = HttpRequest.get(Main.SERVER_URL + "/level/get?id=" + id);
					request.trustAllCerts();
					request.trustAllHosts();
				
				objects = new ArrayList<GameObject>();
				
				level = new Level(request.body(), game);
				objects = new ArrayList<GameObject>(level.getObjects());
				width = level.getWidth();
				height = level.getHeight();
				
				loading = false;
				
				translateX = level.getWidth() * 24;
				translateY = level.getHeight() * 16;
				
			} catch (HttpRequestException | ParserConfigurationException
					| SAXException | IOException | SlickException e) {
				
				error = true;
				
			}
			
		}
		
	}
	
	public Game(int id, String name, String creator) {

		this.id = id;
		this.name = name;
		this.creator = creator;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		lastMouseX = gc.getInput().getAbsoluteMouseX();
		lastMouseY = gc.getInput().getAbsoluteMouseY();
		
		gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 16, 16);
		
		FONT = new UnicodeFont("res/Anonymous_Pro.ttf", 12, false, false);

		FONT.addAsciiGlyphs();
		FONT.getEffects().add(new ColorEffect());
		FONT.loadGlyphs();
		
		coin = Main.loadImage("res/coin.png");
	
		EditorKeyListener listener = new EditorKeyListener(this);
		gc.getInput().addKeyListener(listener);
		
		tiles = new SpriteSheet(Main.loadImage("res/tiles.png"), 32, 32);
		
		play = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1), 4, gc.getHeight() - 40);
			play.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
		stop = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 2), 44, gc.getHeight() - 40);
			stop.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 2));
		failed = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 3), gc.getWidth() / 2 - 16, gc.getHeight() / 2 + 40);
			failed.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 3));
		pause = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 0), 8, 8);
			pause.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 0));
		menu = new MouseOverArea(gc, Main.BIG_BUTTON.getSprite(0, 0), gc.getWidth() / 2 - 64, gc.getHeight() / 2 + 16);
			menu.setMouseDownImage(Main.BIG_BUTTON.getSprite(0, 1));
			
		SpriteSheet sheet = new SpriteSheet(Main.loadImage("res/tweet.png"), 128, 32);
		
		tweet = new MouseOverArea(gc, sheet.getSprite(0, 0), gc.getWidth() / 2 - 64, gc.getHeight() / 2 + 56);
			tweet.setMouseDownImage(sheet.getSprite(0, 1));
	
		coins = 0;
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setBackground(new Color(30, 36, 38));
		
		if (!loading) {
	
			if (!g.getFont().equals(FONT)) g.setFont(FONT);
			
			g.translate((int) Math.ceil(translateX), (int) Math.ceil(translateY));
			
			for (int i = 0; i < width; i++) {
				
				if (height % 2 == 0) {
				
					if (i % 2 == 0) g.drawImage(tiles.getSprite(2, 2), i * 32, height * 32);
					else g.drawImage(tiles.getSprite(1, 2), i * 32, height * 32);
				
				} else {
					
					if (i % 2 == 0) g.drawImage(tiles.getSprite(1, 2), i * 32, height * 32);
					else g.drawImage(tiles.getSprite(2, 2), i * 32, height * 32);
					
				}
				
			}
			
			for (int i = 0; i < height; i++) {
				
				if (i % 2 == 0) g.drawImage(tiles.getSprite(0, 0), - 32, i * 32);
				else g.drawImage(tiles.getSprite(0, 1), - 32, i * 32);
				
			}
			
			g.drawImage(tiles.getSprite(0, 2), - 32, height * 32);
			
			for (int i = 0; i < width; i++) {
				
				for (int j = 0; j < height; j++) {
					
					if (i % 2 == 0) {
						
						if (j % 2 == 0) g.drawImage(tiles.getSprite(2, 0), i * 32, j * 32);
						else g.drawImage(tiles.getSprite(2, 1), i * 32, j * 32);
					
					} else {
						
						if (j % 2 == 0) g.drawImage(tiles.getSprite(2, 1), i * 32, j * 32);
						else g.drawImage(tiles.getSprite(2, 0), i * 32, j * 32);
						
					}
					
				}
				
			}
			
			for (int z = 0; z < 6; z++) {
			
				for (int x = 0; x < this.getWidth(); x++) {
				
					for (int y = 0; y < this.getHeight(); y++) {
						
						for (GameObject object : objects) {
							
							if (object.getX() == x && object.getY() == y && object.getZ() == z && !object.isDead()) object.render(gc, g);
							
						}
				
					}
				
				}
			
			}
			
			g.resetTransform();
			
			Main.GAME_FONT.drawString(gc.getWidth() / 2 - Main.GAME_FONT.getWidth(coins + "/" + level.getTotalCoins()) / 2, 50, coins + "/" + level.getTotalCoins());
			
			g.drawImage(coin, gc.getWidth() / 2 - Main.GAME_FONT.getWidth(coins + "/" + level.getTotalCoins()) / 2 - 20, 52);
			
			play.render(gc, g);
			stop.render(gc, g);
			
			if (this.currentEditor != null) currentEditor.render(gc, g);
			
			if (paused | completed) {
				
				g.setColor(new Color(0, 0, 0, 125));
				g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
				g.setColor(Color.white);
				
				menu.render(gc, g);
				Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 4 * 9 + 4, gc.getHeight() / 2 + 20, "MENU");
				if (paused) Main.GAME_FONT.drawString(gc.getWidth() / 2 - 6 * 9 + 8, gc.getHeight() / 2 - 20, "PAUSED");
				else if (completed) {
					
					Main.GAME_FONT.drawString(gc.getWidth() / 2 - 16 * 9 + 8, gc.getHeight() / 2 - 52, "LEVEL COMPLETE!!");
					Main.GAME_FONT.drawString(gc.getWidth() / 2 - (new String("YOU SCORED " + (1000 - this.getScore()) + "!!!")).length() * 9 + 8, gc.getHeight() / 2 - 20, "YOU SCORED " + (1000 - this.getScore()) + "!!!");
					
					tweet.render(gc, g);
					Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 5 * 9 + 4, gc.getHeight() / 2 + 60, "TWEET");
					
				}
				
			}
			
			pause.render(gc, g);
		
		} else {
			
			if (!error) Main.GAME_FONT.drawString(gc.getWidth() / 2 - 10 * 11 + 24, gc.getHeight() / 2 - 8, "LOADING...");
			else {
				
				Main.GAME_FONT.drawString(gc.getWidth() / 2 - 14 * 10, gc.getHeight() / 2 - 8, "SERVER TIMED OUT!");
				failed.render(gc, g);
				
			}
			
		}
				
	}

	private int getScore() {
		
		int score = 0;
		
		
		for (GameObject object : objects) {
			
			score += object.getScript().length();
			
		}
		
		return score;
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (!loading && !paused && !completed) {
		
			if (this.currentEditor != null) currentEditor.update(gc, delta);
			
			boolean cursorChange = false;
			
			if (play.isMouseOver() | stop.isMouseOver() | pause.isMouseOver()) {
				
				cursorChange = true;
				if (cursorMode != 2) gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(2, 0), 0, 0);
				cursorMode = 2;
				
			} 
			
			if (!this.editorOpen && !cursorChange) {
			
				for (GameObject object : objects) {
					
					if (gc.getInput().getAbsoluteMouseX() < object.getRenderingX() + 32 + translateX && gc.getInput().getAbsoluteMouseX() > object.getRenderingX() + translateX
						&& gc.getInput().getAbsoluteMouseY() < object.getRenderingY() + 32 + translateY && gc.getInput().getAbsoluteMouseY() > object.getRenderingY() + translateY
						&& object.isScriptable()) {
						
						if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) this.openEditor(object, gc);
						
						cursorChange = true;
						if (cursorMode != 0) gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(0, 0), 0, 0);
						cursorMode = 0;
						break;
						
					}
					
				}
				
				if (!cursorChange && cursorMode != 1) {
					
					gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 16, 16);
					cursorMode = 1;
					
				}
			
			} else if (editorOpen && !cursorChange) {
				
				if (cursorMode != 2 && gc.getInput().getAbsoluteMouseX() > gc.getWidth() - currentEditor.getTargetWidth() + 10) {
					
					gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(2, 0), 0, 0);
					cursorMode = 2;
					
				} else if (cursorMode != 3 && gc.getInput().getAbsoluteMouseX() < gc.getWidth() - currentEditor.getTargetWidth() + 10
						 && gc.getInput().getAbsoluteMouseX() > gc.getWidth() - currentEditor.getTargetWidth() - 10) {
					
					gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(3, 0), 16, 16);
					cursorMode = 3;
					
				} else if (cursorMode != 1 && gc.getInput().getAbsoluteMouseX() < gc.getWidth() - currentEditor.getTargetWidth() - 10) {
					
					gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 16, 16);
					cursorMode = 1;
					
				}
				
			}
			
			for (GameObject object : objects) {
				
				object.preUpdate(gc, delta);
				
			}
			
			if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && cursorMode == 1 && !play.isMouseOver() && !stop.isMouseOver()) {
				
				this.translateX += gc.getInput().getAbsoluteMouseX() - lastMouseX;
				this.translateY += gc.getInput().getAbsoluteMouseY() - lastMouseY;
				
			} else if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && cursorMode == 3 && !play.isMouseOver() && !stop.isMouseOver()) {
				
				this.currentEditor.resizeBy(-(int) Math.ceil(gc.getInput().getAbsoluteMouseX() - lastMouseX), gc);
				
			} else if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				
				if (this.currentEditor != null) this.currentEditor.checkExit(gc);
				
				if (play.isMouseOver() && !playing) {
					
					playing = true;
					
					play.setNormalImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
					play.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
					
					for (GameObject object : objects) {
						
						object.runScript();
						
					}
				
				} else if (stop.isMouseOver()) {
					
					coins = 0;
					
					playing = false;
					play.setNormalImage(Main.USEFUL_BUTTONS.getSprite(0, 1));
					play.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(0, 1));
					
					List<String> scripts = new ArrayList<String>();
					
					for (GameObject object : objects) {
						
						scripts.add(object.getScript());
						
					}
					
					try {
						
						this.objects = new ArrayList<GameObject>(level.getObjects());
						
					} catch (ParserConfigurationException | SAXException
							| IOException e) {
						
						e.printStackTrace();
						
					}
					
					for (GameObject object : this.objects) {
						
						object.setScript(scripts.get(objects.indexOf(object)));
						
					}
					
				} else if (pause.isMouseOver()) {
					
					this.paused = true;
					pause.setNormalImage(Main.USEFUL_BUTTONS.getSprite(0, 1));
					pause.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(0, 1));
					pause.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
					
				}
				
			} 
			
			lastMouseX = gc.getInput().getAbsoluteMouseX();
			lastMouseY = gc.getInput().getAbsoluteMouseY();
		
		} else if (!init) {
			
			(new Thread(new LevelLoadThread(id, this))).run();
			init = true;
			
		} else if (paused && gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			if (pause.isMouseOver()) {
				
				this.paused = false;
				pause.setNormalImage(Main.USEFUL_BUTTONS.getSprite(0, 0));
				pause.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(0, 0));
				pause.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 0));
				
			} else if (menu.isMouseOver()) {
				
				sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
				
			}
			
		} else if (error) {
			
			if (!errorCursor ) {
				
				gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(2, 0), 0, 0);
				errorCursor = true;
				
			}
			
			if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) && failed.isMouseOver()) {
				
				sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
				
			}
			
		} else if (completed && gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			if (menu.isMouseOver()) {
				
				sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
				
			} else if (tweet.isMouseOver()) {
				
				try {
					
					Main.openWebpage(new URL("https://twitter.com/intent/tweet?text=I%20just%20scored%20950%20points%20playing%20%23Code404%20on%20" + name.replaceAll(" ", "%20") + "%20by%20" + creator.replaceAll(" ", "%20") + "!!%20%23YRS2015"));
					
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}

	@Override
	public int getID() {
		
		return 3;
		
	}
	
	public boolean isEditorOpen() {
		
		return editorOpen;
		
	}
	
	public void openEditor(GameObject object, GameContainer gc) throws SlickException {
		
		this.currentEditor = new EditorPane(this, object, gc);
		this.editorOpen = true;
		
	}

	public void closeEditor(GameContainer gc) {
		
		this.editorOpen = false;
		
	}

	public EditorPane getCurrentEditor() {
		
		return currentEditor;
		
	}
	
	public void addObject(GameObject object) {
		
		this.objects.add(object);
		
	}
	
	public void removeObject(GameObject object) {
		
		this.objects.remove(object);
		
	}

	public List<GameObject> getGameObjects() {
		
		return this.objects;
	
	}

	public int getHeight() {
		
		return this.height;
		
	}
	
	public int getWidth() {
		
		return this.width;
		
	}
	
	public void addTrigger(int triggerLevel) {
		
		for (GameObject object : objects) {
			
			if (object.getListenerLevel() == triggerLevel) object.trigger();
			
		}
		
	}
	
	public void getCoin(GameContainer gc) throws SlickException {
		
		this.coins += 1;
		if (coins == level.getTotalCoins()) completed = true;
		gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(2, 0), 0, 0);
		
	}
	
}
