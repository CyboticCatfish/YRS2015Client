package net.cybotic.catfish.src.game;

import java.util.ArrayList;
import java.util.List;

import net.cybotic.catfish.src.game.editor.EditorKeyListener;
import net.cybotic.catfish.src.game.editor.EditorPane;
import net.cybotic.catfish.src.game.object.GameObject;
import net.cybotic.catfish.src.game.object.Test;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGameState {
	
	private boolean editorOpen = false;
	private EditorPane currentEditor;
	public static UnicodeFont FONT;
	private List<GameObject> objects;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		objects = new ArrayList<GameObject>();
		
		FONT = new UnicodeFont("res/Anonymous_Pro.ttf", 12, false, false);

		FONT.addAsciiGlyphs();
		FONT.getEffects().add(new ColorEffect());
		FONT.loadGlyphs();
	
		EditorKeyListener listener = new EditorKeyListener(this);
		gc.getInput().addKeyListener(listener);
		
		this.addObject(new Test(this));
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		if (!g.getFont().equals(FONT)) g.setFont(FONT);
		
		if (this.currentEditor != null) currentEditor.render(gc, g);
		
		for (GameObject object : objects) {
			
			//TODO make this real
			object.render(gc, g);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (this.currentEditor != null) currentEditor.update(gc, delta);
		
		for (GameObject object : objects) {
			
			object.preUpdate(gc, delta);
			
		}
		
	}

	@Override
	public int getID() {
		
		// TODO actually make this appropriate
		return 0;
		
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
	
}
