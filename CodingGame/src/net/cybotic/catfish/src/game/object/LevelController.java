package net.cybotic.catfish.src.game.object;

import net.cybotic.catfish.src.game.Game;

public class LevelController {
	
	private Game game;
	
	public LevelController(Game game) {
		
		this.game = game;
		
	}
	
	public int currentCoins() {
		
		return game.getCurrentCoins();
		
	}
	
}
