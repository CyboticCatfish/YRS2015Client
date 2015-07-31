package net.cybotic.catfish.src;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.cybotic.catfish.src.game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.BufferedImageUtil;

import com.github.kevinsawicki.http.HttpRequest;

public class Menu extends BasicGameState {
	
	private Image logo, back, background;
	private boolean loading = true;
	private int selected = 0;
	private MouseOverArea left, right, play, mute, exit, levels;
	private List<LevelCard> subscriptions;
	private MenuLoadingThread loadingThread;
	private boolean muted = false, init = false;
	private float x = 0f;
	
	public class MenuLoadingThread implements Runnable {
		
		boolean done = false;
		boolean error = false;
		
		@Override
		public void run() {
			
			 try {
			
				Map<String, String> data = new HashMap<String, String>();
				data.put("token", Main.LOGIN_TOKEN);
				
				String subscribed;
				
				HttpRequest subscriptionRequest = HttpRequest.post(Main.SERVER_URL + "/user/subscriptions");
					subscriptionRequest.trustAllCerts();
					subscriptionRequest.trustAllHosts();
					
				subscribed = subscriptionRequest.form(data).body();
				
				List<Integer> levelIDs = new ArrayList<Integer>();
				
				for (String id : subscribed.split(",")) {
					
					levelIDs.add(Integer.parseInt(id));
					
				}
				
				for (int id : levelIDs) {
					
					HttpRequest levelRequest = HttpRequest.get(Main.SERVER_URL + "/level/get/details?id=" + id);
						levelRequest.trustAllCerts();
						levelRequest.trustAllHosts();
						
					String[] responses = levelRequest.body().split(",");
					
					String name = responses[0], creator = responses[1];
					Image image;
					 
					URL u = new URL(Main.SERVER_URL + "/level/get/image?id=" + id + "&x=292&y=144");

				    BufferedImage bi = ImageIO.read(u);
				    Texture texture = BufferedImageUtil.getTexture("picture", bi);
				    image = new Image(texture);
				    
					subscriptions.add(new LevelCard(id, name, creator, image));
					
				}
					
			} catch (Exception e) {
				
				e.printStackTrace();
				error = true;
				return;
				
			}
			
			done = true;
			
		}
		
		public boolean isDone() {
			
			return done;
			
		}
		
		public boolean hasErrored() {
			
			return error;
			
		}
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		background = new Image("res/background.png");
		
		x = gc.getWidth();
		loadingThread = new MenuLoadingThread();
		
		subscriptions = new ArrayList<LevelCard>();
		
		logo = new Image("res/logo.png");
		logo.setFilter(Image.FILTER_NEAREST);
		logo = logo.getScaledCopy(3f);
		
		back = Main.loadImage("res/login.png");
		
		exit = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 3), 4, 4);
			exit.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 3));
		mute = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 5), 44, 4);
			mute.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 5));
		left = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1).getFlippedCopy(true, false), gc.getWidth() / 2 - 190, gc.getHeight() / 2 + 4);
			left.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1).getFlippedCopy(true, false));
		right = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1), gc.getWidth() / 2 + 158, gc.getHeight() / 2 + 4);
			right.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
			
		play = new MouseOverArea(gc, Main.BIG_BUTTON.getSprite(0, 0), gc.getWidth() / 2 - 64, gc.getHeight() / 2 + back.getHeight() / 2 + 52);
			play.setMouseDownImage(Main.BIG_BUTTON.getSprite(0, 1));
		levels = new MouseOverArea(gc, Main.BIG_BUTTON.getSprite(0, 0), gc.getWidth() / 2 - 64, gc.getHeight() / 2 + back.getHeight() / 2 + 92);
			levels.setMouseDownImage(Main.BIG_BUTTON.getSprite(0, 1));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		
		g.setBackground(new Color(30, 36, 38));
		g.drawImage(background, 0, 0);
		
		g.drawImage(logo, gc.getWidth() / 2 - logo.getWidth() / 2, 60);
		
		Main.GAME_FONT.drawString(gc.getWidth() / 2 - 18 * 8, 180, "YOUR SUBSCRIPTIONS");
		
		g.drawImage(back, gc.getWidth() / 2 - 150, gc.getHeight() / 2 - 80);
		
		if (loading) {
			
			if (!loadingThread.hasErrored()) Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 9 * 11 + 20, gc.getHeight() / 2 + 12, "LOADING...");
			else Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 14 * 10, gc.getHeight() / 2 + 12, "SERVER TIMED OUT!");
			
		} else {
			
			Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 150 + 4, gc.getHeight() / 2 - 100 + 24, subscriptions.get(selected).getName().toUpperCase());
			Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 150 + 4, gc.getHeight() / 2 - 100 + 44, "BY " + subscriptions.get(selected).getCreator().toUpperCase());
			g.drawImage(subscriptions.get(selected).getImage(), gc.getWidth() / 2 - 150 + 4, gc.getHeight() / 2 - 100 + 68);
			
		}
		
		right.render(gc, g);
		left.render(gc, g);
		
		play.render(gc, g);
		levels.render(gc, g);
		
		Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 30, gc.getHeight() / 2 + back.getHeight() / 2 + 56, "PLAY");
		Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 46, gc.getHeight() / 2 + back.getHeight() / 2 + 96, "LEVELS");
		
		exit.render(gc, g);
		mute.render(gc, g);
		
		Main.GAME_FONT.drawString(x, gc.getHeight() - 40, "(C) 2015 CYBOTIC CATFISH // MADE IN A WEEK FOR YRS 2015 // FRONT END BY NATE // BACK END BY LEVI // MUSIC BY LENNO LIU // AVAILABLE ON GITHUB // SPECIAL THANKS TO THE NOTTINGHAM HACKSPACE FOR LETTING US EXIST // ALSO THANKS TO TAIIWO JUST BECAUSE");
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (!init) {
			
			Music music = new Music("res/music.wav");
			music.loop();
			
			(new Thread(loadingThread)).run();
			
			init = true;
			
		}
		
		if (loadingThread.isDone() && !loadingThread.hasErrored()) {
			
			loading = false;
			
		}
		
		if (!loading) {
			
			x -= 0.1f * delta;
			if (x < - 5000) x = gc.getWidth();
			
		}
		
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			SoundBank.CLICK.play();
			
			if (mute.isMouseOver()) {
				
				if (!muted) {
					
					mute.setNormalImage(Main.USEFUL_BUTTONS.getSprite(0, 4));
						mute.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(0, 4));
						mute.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 4));
					gc.setMusicOn(false);
					gc.setSoundOn(false);
					gc.setSoundVolume(0f);
					gc.setMusicVolume(0f);
					
					muted = true;
					
				} else {
					
					mute.setNormalImage(Main.USEFUL_BUTTONS.getSprite(0, 5));
						mute.setMouseOverImage(Main.USEFUL_BUTTONS.getSprite(0, 5));
						mute.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 5));
					gc.setMusicOn(true);
					gc.setSoundOn(true);
					gc.setSoundVolume(1f);
					gc.setMusicVolume(1f);
					
					muted = false;
					
				}
				
			} else if (exit.isMouseOver()) {
				
				gc.exit();
				
			} else if (play.isMouseOver() && !this.loading) {
				
				Game game = new Game(this.subscriptions.get(selected).getID(), this.subscriptions.get(selected).getName(), this.subscriptions.get(selected).getCreator());
				sbg.addState(game);
				game.init(gc, sbg);
				sbg.enterState(3, new FadeOutTransition(), new FadeInTransition());
				
			} else if (right.isMouseOver() && !this.loading) {
				
				selected += 1;
				if (selected > this.subscriptions.size() - 1) selected = 0;
				
			} else if (left.isMouseOver() && !this.loading) {
				
				selected -= 1;
				if (selected < 0) selected = this.subscriptions.size() - 1;
				
			} else if (levels.isMouseOver()) {
				
				try {
					
					Main.openWebpage(new URL("http://c404.mrmindimplosion.co.uk:5000?token=" + Main.LOGIN_TOKEN));
					
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	}

	@Override
	public int getID() {
		
		return 2;
		
	}
	
	public class LevelCard {
		
		private int id;
		private String name, creator;
		private Image image;
		
		public LevelCard(int id, String name, String creator, Image image) {
			
			this.id = id;
			this.name = name;
			this.creator = creator;
			this.image = image;
			
			if (name.length() > 15) name = name.substring(0, 12) + "...";
			
		}
		
		public Image getImage() {
			
			return image;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
		public String getCreator() {
			
			return creator;
			
		}
		
		public int getID() {
			
			return id;
			
		}
		
	}
	
}
