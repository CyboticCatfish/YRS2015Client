package net.cybotic.catfish.src.game.object;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import net.cybotic.catfish.src.Main;
import net.cybotic.catfish.src.game.Game;

public class Ghost extends GameObject {
	
	private List<Animation> animations = new ArrayList<Animation>();

	public Ghost(int x, int y, int dir, String script, Game game) throws SlickException {
		
		super(x, y, 5, dir, script, false, game, "ghost", true, 0);
		
		SpriteSheet sheet = new SpriteSheet(Main.loadImage("res/ghost.png"), 30, 40);
		
		for (int i = 0; i < 4; i++) {
			
			Animation anim = new Animation(sheet, 0, i, 3, i, true, 1, true);
				anim.setSpeed(0.005f);
				anim.setLooping(true);
				anim.start();
			
			animations.add(anim);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		for (GameObject object : game.getGameObjects()) {
			
			if (object instanceof Robot) {
				
				if (getDir() == 0 && object.getX() == this.getX() && object.getY() == this.getY() - 1) object.die();
				if (getDir() == 2 && object.getX() == this.getX() && object.getY() == this.getY() + 1) object.die();
				if (getDir() == 3 && object.getX() == this.getX() - 1 && object.getY() == this.getY()) object.die();
				if (getDir() == 1 && object.getX() == this.getX() + 1 && object.getY() == this.getY()) object.die();
				
			}
			
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		
		animations.get(getDir()).draw(getRenderingX() + 1, getRenderingY() - 8);
		
	}

	@Override
	public int getObjectTypeID() {
		
		return 6;
		
	}

	@Override
	public void trigger() {
		
		
	}
	
}
