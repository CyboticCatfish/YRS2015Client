package net.cybotic.catfish.src;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.BufferedImageUtil;
import org.xml.sax.InputSource;

import com.github.kevinsawicki.http.HttpRequest;

public class Menu extends BasicGameState {
	
	private Image logo, back;
	private boolean loading = true;
	private int selected = 0;
	private MouseOverArea left, right, play, mute, exit;
	private List<LevelCard> subscriptions;
	private MenuLoadingThread loadingThread;
	private boolean muted = false, init = false, changing = true, changingUp = true;
	private float alpha = 0f;
	
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
					 
					URL u = new URL(Main.SERVER_URL + "/level/get/image?id=" + id + "&x=140&y=100");

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
		
		loadingThread = new MenuLoadingThread();
		
		subscriptions = new ArrayList<LevelCard>();
		
		logo = new Image("res/logo.png");
		logo.setFilter(Image.FILTER_NEAREST);
		logo = logo.getScaledCopy(5f);
		
		back = Main.loadImage("res/login.png");
		
		exit = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 3), 4, 4);
			exit.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 3));
		mute = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 5), 44, 4);
			mute.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 5));
		left = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1).getFlippedCopy(true, false), gc.getWidth() / 2 - 190, gc.getHeight() / 2 - 16);
			left.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1).getFlippedCopy(true, false));
		right = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1), gc.getWidth() / 2 + 158, gc.getHeight() / 2 - 16);
			right.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		
		g.setBackground(new Color(30, 36, 38));
		
		g.drawImage(logo, gc.getWidth() / 2 - logo.getWidth() / 2, 40);
		
		if (loading) {
			
			if (!loadingThread.hasErrored()) Main.GAME_FONT.drawString(gc.getWidth() / 2 - 9 * 11 + 20, gc.getHeight() / 2 - 8, "LOADING...");
			else Main.GAME_FONT.drawString(gc.getWidth() / 2 - 14 * 10, gc.getHeight() / 2 - 8, "SERVER TIMED OUT!");
			
		} else {
			
			g.drawImage(back, gc.getWidth() / 2 - 150, gc.getHeight() / 2 - 100);
			
		}
		
		exit.render(gc, g);
		mute.render(gc, g);
		right.render(gc, g);
		left.render(gc, g);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		back.setAlpha(alpha);
		
		if (!init) {
			
			(new Thread(loadingThread)).run();
			
			init = true;
			
		}
		
		if (loadingThread.isDone() && !loadingThread.hasErrored()) loading = false;
		
		if (changing && !loading) {
				
			if (changingUp) {
				
				if (alpha < 1f) alpha += 0.001f * delta;
				if (alpha > 1f) {
					
					alpha = 1f;
					changingUp = false;
					changing = false;
					
				}
				
			} else {
				
				if (alpha > 0f) alpha -= 0.01f * delta;
				if (alpha < 0f) {
					
					alpha = 0f;
					changingUp = true;
					
				}
				
			}
			
		}
		
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			/**Game game = new Game(3);
			sbg.addState(game);
			sbg.enterState(3, new FadeOutTransition(), new FadeInTransition());
			game.init(gc, sbg);**/
			
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
