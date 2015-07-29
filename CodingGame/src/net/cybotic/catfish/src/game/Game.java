package net.cybotic.catfish.src.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.editor.EditorKeyListener;
import net.cybotic.catfish.src.game.editor.EditorPane;
import net.cybotic.catfish.src.game.object.GameObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.xml.sax.SAXException;

public class Game extends BasicGameState {
	
	private boolean editorOpen = false;
	private EditorPane currentEditor;
	public static UnicodeFont FONT;
	private List<GameObject> objects;
	private int cursorMode = 1;
	private float translateX = 0f, translateY = 0f, lastMouseX, lastMouseY;
	private Level level;
	private int width, height;
	private SpriteSheet tiles;
	private String XML;
	
	public Game(String XML) {

		this.XML = XML;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		
		try {

			objects = new ArrayList<GameObject>();
			this.level = new Level(XML, this);
			
			objects = new ArrayList<GameObject>(level.getObjects());
			this.width = level.getWidth();
			this.height = level.getHeight();
			
			lastMouseX = gc.getInput().getAbsoluteMouseX();
			lastMouseY = gc.getInput().getAbsoluteMouseY();
			
			gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 8, 8);
			
			FONT = new UnicodeFont("res/Anonymous_Pro.ttf", 12, false, false);

			FONT.addAsciiGlyphs();
			FONT.getEffects().add(new ColorEffect());
			FONT.loadGlyphs();
		
			EditorKeyListener listener = new EditorKeyListener(this);
			gc.getInput().addKeyListener(listener);
			
			tiles = new SpriteSheet(Main.loadImage("res/tiles2.png"), 48, 48);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			gc.exit();
			
		} catch (SAXException e) {
			
			e.printStackTrace();
			gc.exit();
			
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			gc.exit();
			
		}
		
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setBackground(new Color(95, 95, 87));
		
		if (!g.getFont().equals(FONT)) g.setFont(FONT);
		
		g.translate((int) Math.ceil(translateX), (int) Math.ceil(translateY));
		
		for (int i = 0; i < width; i++) {
			
			g.drawImage(tiles.getSprite(2, 0), i * 48, - 48);
			g.drawImage(tiles.getSprite(2, 2), i * 48, height * 48);
			
		}
		
		for (int i = 0; i < height; i++) {
			
			g.drawImage(tiles.getSprite(1, 1), - 48, i * 48);
			g.drawImage(tiles.getSprite(3, 1), width * 48, i * 48);
			
		}
		
		g.drawImage(tiles.getSprite(1, 0), - 48, - 48);
		g.drawImage(tiles.getSprite(1, 2), - 48, height * 48);
		g.drawImage(tiles.getSprite(3, 0), width * 48, - 48);
		g.drawImage(tiles.getSprite(3, 2), width * 48, height * 48);
		
		for (int i = 0; i < width; i++) {
			
			for (int j = 0; j < height; j++) {
				
				g.drawImage(tiles.getSprite(2, 1), i * 48, j * 48);
				
			}
			
		}
		
		for (int x = 0; x < this.getWidth(); x++) {
		
			for (int y = 0; y < this.getHeight(); y++) {
				
				List<GameObject> tempObjects = new ArrayList<GameObject>();
				
				for (GameObject object : objects) {
					
					if (object.getX() == x && object.getY() == y) tempObjects.add(object);
					
				}
				
				Collections.sort(tempObjects, new Comparator<GameObject>() {
					
			        @Override
			        public int compare(GameObject object1, GameObject object2) {
			        	
			            return  object1.getZ() - object2.getZ();
			            
			        }
			        
			    });
				
				for (GameObject object : tempObjects) {
					
					object.render(gc, g);
					
				}
		
			}
		
		}
		
		g.resetTransform();
		
		if (this.currentEditor != null) currentEditor.render(gc, g);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (this.currentEditor != null) currentEditor.update(gc, delta);
		
		boolean cursorChange = false;
		
		if (!this.editorOpen) {
		
			for (GameObject object : objects) {
				
				if (gc.getInput().getAbsoluteMouseX() < object.getRenderingX() + 48 + translateX && gc.getInput().getAbsoluteMouseX() > object.getRenderingX() + translateX
					&& gc.getInput().getAbsoluteMouseY() < object.getRenderingY() + 48 + translateY && gc.getInput().getAbsoluteMouseY() > object.getRenderingY() + translateY
					&& object.isScriptable()) {
					
					if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) this.openEditor(object, gc);
					
					cursorChange = true;
					if (cursorMode != 2) gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(0, 0), 0, 0);
					cursorMode = 2;
					break;
					
				}
				
			}
			
			if (!cursorChange && cursorMode != 1) {
				
				gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 8, 8);
				cursorMode = 1;
				
			}
		
		} else {
			
			if (cursorMode != 2 && gc.getInput().getAbsoluteMouseX() > gc.getWidth() - currentEditor.getTargetWidth() + 10) {
				
				gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(2, 0), 0, 0);
				cursorMode = 2;
				
			} else if (cursorMode != 3 && gc.getInput().getAbsoluteMouseX() < gc.getWidth() - currentEditor.getTargetWidth() + 10
					 && gc.getInput().getAbsoluteMouseX() > gc.getWidth() - currentEditor.getTargetWidth() - 10) {
				
				gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(3, 0), 8, 8);
				cursorMode = 3;
				
			} else if (cursorMode != 1 && gc.getInput().getAbsoluteMouseX() < gc.getWidth() - currentEditor.getTargetWidth() - 10) {
				
				gc.setMouseCursor(Main.CURSOR_IMAGES.getSprite(1, 0), 8, 8);
				cursorMode = 1;
				
			}
			
		}
		
		for (GameObject object : objects) {
			
			object.preUpdate(gc, delta);
			
		}
		
		if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && cursorMode == 1) {
			
			this.translateX += gc.getInput().getAbsoluteMouseX() - lastMouseX;
			this.translateY += gc.getInput().getAbsoluteMouseY() - lastMouseY;
			
		} else if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && cursorMode == 3) {
			
			this.currentEditor.resizeBy(-(int) Math.ceil(gc.getInput().getAbsoluteMouseX() - lastMouseX), gc);
			
		}
		
		lastMouseX = gc.getInput().getAbsoluteMouseX();
		lastMouseY = gc.getInput().getAbsoluteMouseY();
		
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
	
}
