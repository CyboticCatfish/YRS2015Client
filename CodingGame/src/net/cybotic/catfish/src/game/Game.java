package net.cybotic.catfish.src.game;

import net.cybotic.catfish.src.game.editor.EditorKeyListener;
import net.cybotic.catfish.src.game.editor.EditorPane;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends BasicGameState {
	
	private boolean editorOpen = false;
	private EditorPane currentEditor;
	public static UnicodeFont FONT;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		FONT = new UnicodeFont("res/Anonymous_Pro.ttf", 12, false, false);

		FONT.addAsciiGlyphs();
		FONT.getEffects().add(new ColorEffect());
		FONT.loadGlyphs();
	
		EditorKeyListener listener = new EditorKeyListener(this);
		gc.getInput().addKeyListener(listener);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		if (!g.getFont().equals(FONT)) g.setFont(FONT);
		
		if (this.isEditorOpen()) {
			
			currentEditor.render(g);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && !this.isEditorOpen()) this.openEditor(gc);
		else if (this.isEditorOpen()) {
			
			currentEditor.update(gc, delta);
			
		}
		
	}

	private void openEditor(GameContainer gc) throws SlickException {
		
		EditorPane pane = new EditorPane(100, 100);
		currentEditor = pane;
		editorOpen = true;
		
	}

	@Override
	public int getID() {
		
		// TODO actually make this appropriate
		return 0;
		
	}
	
	public boolean isEditorOpen() {
		
		return editorOpen;
		
	}

	public void closeEditor(GameContainer gc) {
		
		editorOpen = false;
		
	}

	public EditorPane getCurrentEditor() {
		
		return currentEditor;
		
	}
	
}
