package net.cybotic.catfish.src;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundBank {
	
	public static Sound CLICK, COIN, EXPLODE, HURT, LASER, PLATE;
	
	public SoundBank() throws SlickException {
		
		CLICK = new Sound("res/sound/click.wav");
		COIN = new Sound("res/sound/coin.wav");
		EXPLODE = new Sound("res/sound/explode.wav");
		HURT = new Sound("res/sound/hurt.wav");
		LASER = new Sound("res/sound/laser.wav");
		PLATE = new Sound("res/sound/plate.wav");
		
	}
	
}
