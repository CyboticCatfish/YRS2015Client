package net.cybotic.catfish.src.game.editor;

import java.util.ArrayList;
import java.util.List;

import net.cybotic.catfish.src.game.Game;
import net.cybotic.catfish.src.game.object.GameObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class EditorPane {
	
	private List<String> currentScript = new ArrayList<String>();
	private float editorTimer = 0f, flash = 0f, x = 0f;
	private int cursorPosition = 0, currentLine = 0;
	private boolean firstPress = true, cursorShow = false, closing = false;
	private Game game;
	private GameObject object;
	
	
	public EditorPane(Game game, GameObject target) throws SlickException {
		
		String[] script = target.getScript().split("\n");
		for (int i = 0; i < script.length; i++) currentScript.add(script[i]);
		
		this.game = game;
		this.object = target;
		
	}
	
	public void appendCharacter(char character) {
		
		String start = currentScript.get(currentLine).substring(0, cursorPosition);
		String end = currentScript.get(currentLine).substring(cursorPosition, currentScript.get(currentLine).length());
		
		currentScript.set(currentLine, start + character + end);
		cursorPosition += 1;
		
	}
	
	public void appendString(String string) {
		
		String start = currentScript.get(currentLine).substring(0, cursorPosition);
		String end = currentScript.get(currentLine).substring(cursorPosition, currentScript.get(currentLine).length());
		
		currentScript.set(currentLine, start + string + end);
		
	}

	public void backSpace() {
		
		if (currentScript.get(currentLine).length() > 0 && cursorPosition > 0) {
			
			String start = currentScript.get(currentLine).substring(0, cursorPosition - 1);			
			String end = currentScript.get(currentLine).substring(cursorPosition, currentScript.get(currentLine).length());
			
			if (start.endsWith("  ")) {
			
				start = currentScript.get(currentLine).substring(0, cursorPosition - 2);
				cursorPosition -= 2;
			
			}
			
			currentScript.set(currentLine, start + end);
			cursorPosition -= 1;
			
		} else if (currentLine != 0) {
			
			String temp = currentScript.get(currentLine);
			currentScript.remove(currentLine);
			currentLine -= 1;
			cursorPosition = currentScript.get(currentLine).length();
			this.appendString(temp);
			
		}
		
	}
	
	public void render(GameContainer gc, Graphics g) {
		
		g.setColor(new Color(60, 60 , 60));
		g.fillRect(gc.getWidth() - x,  0, 200, gc.getHeight());
		
		g.setColor(new Color(0, 0 , 0, 70));
		g.fillRect(gc.getWidth() - x, 3 + 13 * currentLine, 200, 13);
		
		g.setColor(new Color(0, 0 , 0, 51));
		g.fillRect(gc.getWidth() - x,  0, 6, gc.getHeight());
		
		g.setColor(Color.white);
		
		for (int i = 0; i < currentScript.size(); i++) g.drawString(currentScript.get(i), gc.getWidth() - x + 8, 3 + i * 13);
		if (cursorShow) g.drawLine(gc.getWidth() - x + 8 + cursorPosition * 6.55f, 3 + currentLine * 13f, gc.getWidth() - x + 8 + cursorPosition * 6.55f, 15 + currentLine * 13f);
		
	}

	public String getCurrentScript() {
		
		String script = "";
		
		for (int i = 0; i < currentScript.size(); i++) script += currentScript.get(i) + "\n";
		
		return script;
		
		
	}
	
	public void done() {
		
		
		
	}

	public void update(GameContainer gc, int delta) {
		
		if (closing) game.closeEditor(gc);
		else {
		
			if (x < 200f && this.game.isEditorOpen()) x += 1f * delta;
			else if (x > 0f && !this.game.isEditorOpen()) x -= 1f * delta;
			
			flash += 0.1f * delta;
			if (flash > 30f) {
				
				cursorShow = !cursorShow;
				flash = 0f;
				
			}
			
			if (gc.getInput().isKeyDown(Input.KEY_BACK) ||
					gc.getInput().isKeyDown(Input.KEY_UP) ||
					gc.getInput().isKeyDown(Input.KEY_DOWN) ||
					gc.getInput().isKeyDown(Input.KEY_LEFT) ||
					gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
				
				if (editorTimer < 50f && firstPress) editorTimer += 0.1f * delta;
				
				else if (editorTimer  < 5f && !firstPress) editorTimer += 0.1f * delta;
					
				else {
					
					if (gc.getInput().isKeyDown(Input.KEY_BACK)) backSpace();
					else if (gc.getInput().isKeyDown(Input.KEY_UP)) cursorUp();
					else if (gc.getInput().isKeyDown(Input.KEY_DOWN)) cursorDown();
					else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) cursorLeft();
					else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) cursorRight();
					
					firstPress = false;
					editorTimer = 0f;
					
				}
				
			} else if (!gc.getInput().isKeyDown(Input.KEY_BACK) && 
					!gc.getInput().isKeyDown(Input.KEY_UP) && 
					!gc.getInput().isKeyDown(Input.KEY_DOWN) && 
					!gc.getInput().isKeyDown(Input.KEY_LEFT) && 
					!gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
				
				firstPress = true;
				editorTimer = 0f;
			}
		
		}
		
	}

	public void newLine() {
		
		currentScript.add(currentLine + 1, currentScript.get(currentLine).substring(cursorPosition, currentScript.get(currentLine).length()));
		currentScript.set(currentLine, currentScript.get(currentLine).substring(0, cursorPosition));
		currentLine += 1;
		cursorPosition = 0;
		
	}

	public void cursorLeft() {
		
		if (cursorPosition > 0) cursorPosition -= 1;
		else {
			
			if (currentLine > 0) {
			
				currentLine -= 1;
				cursorPosition = currentScript.get(currentLine).length();
				
			}
			
		}
		
	}

	public void cursorRight() {
		
		if (cursorPosition < currentScript.get(currentLine).length()) cursorPosition += 1;
		else {
			
			if (currentLine < currentScript.size() - 1) {
				
				currentLine += 1;
				cursorPosition = 0;
				
			}
			
		}
		
	}

	public void cursorUp() {
		
		if (currentLine > 0) {
			
			currentLine -= 1;
			
			if (cursorPosition > currentScript.get(currentLine).length()) cursorPosition = currentScript.get(currentLine).length();
			
		}
		
	}

	public void cursorDown() {
		
		if (currentLine < currentScript.size() - 1) {
			
			currentLine += 1;
			
			if (cursorPosition > currentScript.get(currentLine).length()) cursorPosition = currentScript.get(currentLine).length();
			
		}
		
	}

	public GameObject getTarget() {
		
		return this.object;
		
	}

	public void close() {
		
		this.closing = true;
		
	}

}
