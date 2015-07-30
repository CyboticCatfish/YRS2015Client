package net.cybotic.catfish.src.game.editor;

import net.cybotic.catfish.src.game.Game;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class EditorKeyListener implements KeyListener {
	
	private Game game;
	
	public EditorKeyListener(Game game) {
		
		this.game = game;
		
	}
	
	@Override
	public void inputEnded() {
		
	}

	@Override
	public void inputStarted() {
		
	}

	@Override
	public boolean isAcceptingInput() {
		
		return game.isEditorOpen();
		
	}

	@Override
	public void setInput(Input id) {
		
	}

	@Override
	public void keyPressed(int id, char character) {
		
		if (id < 58 && id != 1 && id != 14 && id != 15 && id != 28 && id != 29 && id != 42 && id != 54) {
			
			game.getCurrentEditor().appendCharacter(character);
			
			if (character == '{') {
				
				game.getCurrentEditor().appendCharacter('}');
				game.getCurrentEditor().cursorLeft();
				
			} else if (character == '[') {
				
				game.getCurrentEditor().appendCharacter(']');
				game.getCurrentEditor().cursorLeft();
				
			} else if (character == '(') {
				
				game.getCurrentEditor().appendCharacter(')');
				game.getCurrentEditor().cursorLeft();
				
			} else if (character == '"') {
				
				game.getCurrentEditor().appendCharacter('"');
				game.getCurrentEditor().cursorLeft();
				
			} else if (character == '\'') {
				
				game.getCurrentEditor().appendCharacter('\'');
				game.getCurrentEditor().cursorLeft();
				
			} 
			
		} else if (id == 14) game.getCurrentEditor().backSpace();
		else if (id == 28) game.getCurrentEditor().newLine();
		else if (id == 203) game.getCurrentEditor().cursorLeft();
		else if (id == 205) game.getCurrentEditor().cursorRight();
		else if (id == 200) game.getCurrentEditor().cursorUp();
		else if (id == 208) game.getCurrentEditor().cursorDown();
		else if (id == 15) {
			
			game.getCurrentEditor().appendString("   ");
			game.getCurrentEditor().cursorRight();
			game.getCurrentEditor().cursorRight();
			game.getCurrentEditor().cursorRight();
			
		}
		
		game.getCurrentEditor().getTarget().setScript(game.getCurrentEditor().getCurrentScript());
		
	}

	@Override
	public void keyReleased(int id, char character) {
		
	}

}
