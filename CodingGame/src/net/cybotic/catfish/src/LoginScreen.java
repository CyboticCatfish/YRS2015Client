package net.cybotic.catfish.src;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.github.kevinsawicki.http.HttpRequest;

public class LoginScreen extends BasicGameState {
	
	private Image back, errorImage;
	private MouseOverArea user, password, go;
	private int selected = 2;
	private SpriteSheet sheet;
	private String userString = "", passwordString = "", pretendPasswordString = "";
	private float flash = 0f, errorAlpha = 0f;
	private boolean cursorShow = false, error = false;
	private boolean movingForward = false, connecting = false;
	private LoginScreenConnectThread connect;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		back = Main.loadImage("res/login.png");
		
		sheet = new SpriteSheet(Main.loadImage("res/textbox.png"), 256, 32);
		
		user = new MouseOverArea(gc, sheet.getSprite(0, 0), gc.getWidth() / 2 - 128, gc.getHeight() / 2 - back.getHeight() / 2 + 65);
			user.setMouseOverImage(sheet.getSprite(0, 1));
			user.setMouseDownImage(sheet.getSprite(0, 1));
		password = new MouseOverArea(gc, sheet.getSprite(0, 0), gc.getWidth() / 2 - 128, gc.getHeight() / 2 - back.getHeight() / 2 + 115);
			password.setMouseOverImage(sheet.getSprite(0, 1));
			password.setMouseDownImage(sheet.getSprite(0, 1));
		go = new MouseOverArea(gc, Main.USEFUL_BUTTONS.getSprite(0, 1), gc.getWidth() / 2 + back.getWidth() / 2 - 40, gc.getHeight() / 2 + back.getHeight() / 2 - 40);
			go.setMouseDownImage(Main.USEFUL_BUTTONS.getSprite(1, 1));
		
		errorImage = Main.loadImage("res/error.png");
		
		this.connect = new LoginScreenConnectThread();
			
		gc.getInput().addKeyListener(new LoginScreenKeyListener());
			
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.setBackground(new Color(30, 36, 38));
		g.drawImage(back, gc.getWidth() / 2 - back.getWidth() / 2, gc.getHeight() / 2 - back.getHeight() / 2);
		
		Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 45, gc.getHeight() / 2 - back.getHeight() / 2 + 24, "LOGIN");
		
		user.render(gc, g);
		password.render(gc, g);
		go.render(gc, g);
		
		if (userString.length() == 0) Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 128 + 6, gc.getHeight() / 2 - back.getHeight() / 2 + 71, "USERNAME", new Color(255, 255, 255, 50));
		else Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 128 + 6, gc.getHeight() / 2 - back.getHeight() / 2 + 71, userString);
		
		if (passwordString.length() == 0) Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 128 + 6, gc.getHeight() / 2 - back.getHeight() / 2 + 121, "PASSWORD", new Color(255, 255, 255, 50));
		else  Main.GAME_FONT_2.drawString(gc.getWidth() / 2 - 128 + 6, gc.getHeight() / 2 - back.getHeight() / 2 + 121, pretendPasswordString);
		
		if (cursorShow) {
			
			if (selected == 0) g.fillRect(gc.getWidth() / 2 - 128 + 6 + userString.length() * 16, gc.getHeight() / 2 - back.getHeight() / 2 + 73, 2, 18);
			else if (selected == 1) g.fillRect(gc.getWidth() / 2 - 128 + 6 + passwordString.length() * 16, gc.getHeight() / 2 - back.getHeight() / 2 + 123, 2, 18);
			
		}
		
		if (error) {
			
			g.drawImage(errorImage, gc.getWidth() / 2 - errorImage.getWidth() / 2, gc.getHeight() / 2 + back.getHeight() / 2 + 10);
			
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		flash += 0.1f * delta;
		
		if (flash > 50f) {
			
			flash = 0f;
			cursorShow = !cursorShow;
			
		}
		
		if (connect.isDone()) {
			
			if (connect.validate()) movingForward = true;
			else error = true;
			
		}
		
		if (movingForward) {
			
			gc.getInput().removeAllKeyListeners();
			
			Menu menu = new Menu();
			sbg.addState(menu);
			menu.init(gc, sbg);
			
			sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
			
		}
		
		if (error && errorAlpha < 1f) {
			
			errorAlpha += 0.001f * delta;
			errorImage.setAlpha(errorAlpha);
			
		} else if (error) {
			
			errorAlpha = 1f;
			errorImage.setAlpha(errorAlpha);
			
		}
		
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			
			if (user.isMouseOver()) {
				
				selected = 0;
				user.setNormalImage(sheet.getSprite(0, 1));
				password.setNormalImage(sheet.getSprite(0, 0));
				
			} else if (password.isMouseOver()) {
				
				selected = 1;
				password.setNormalImage(sheet.getSprite(0, 1));
				user.setNormalImage(sheet.getSprite(0, 0));
				
			}  else if (go.isMouseOver()) {
				
				submit();
				
			}
			
		}
		
	}
	
	public void submit() {
		
		if (!connecting) (new Thread(connect)).start();
		
	}
	
	public class LoginScreenConnectThread implements Runnable {
		
		private String returnToken;
		private boolean done = false;
		
		@Override
		public void run() {
			
			done = false;
			connecting = true;
			
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", userString);
			data.put("password", passwordString);
		
			HttpRequest request = HttpRequest.post(Main.SERVER_URL + "/user/login");
				request.trustAllCerts();
				request.trustAllHosts();
			
			returnToken = request.form(data).body();
			
			done = true;
			connecting = false;
			
			
		}
		
		public String getReturnToken() {
			
			return this.returnToken;
			
		}
		
		public boolean isDone() {
			
			return this.done;
			
		}
		
		public boolean validate() {
			
			if (!returnToken.equals("undefined")) {
				
				Main.LOGIN_TOKEN = returnToken;
				
				try {
					
					Main.writeConfig();
					
				} catch (IOException e) {
					
				}
				
				return true;
				
			}

			return false;
			
		}
		
	}

	@Override
	public int getID() {
		
		return 1;
		
	}
	
	public class LoginScreenKeyListener implements KeyListener {

		@Override
		public void inputEnded() {
			
		
		}

		@Override
		public void inputStarted() {
			
		}

		@Override
		public boolean isAcceptingInput() {
			
			return true;
			
		}

		@Override
		public void setInput(Input arg0) {
			
		}

		@Override
		public void keyPressed(int id, char character) {
			
			if (id < 58 && id != 1 && id != 14 && id != 15 && id != 28 && id != 29 && id != 42 && id != 54) {
				
				if (selected == 0 && userString.length() < 15) {
					
					userString += character;
					userString = userString.toUpperCase();
					
				} else if (selected == 1 && passwordString.length() < 15) {
					
					pretendPasswordString += "*";
					passwordString += character;
				
				}
			
			} else if (id == 14) {
				
				if (selected == 0 && userString.length() > 0) userString = userString.substring(0, userString.length() - 1);
				if (selected == 1 && passwordString.length() > 0) {
					
					passwordString = passwordString.substring(0, passwordString.length() - 1);
					pretendPasswordString = pretendPasswordString.substring(0, pretendPasswordString.length() - 1);
					
				}
				
			} else if (id == 15) {
				
				if (selected == 0) {
					
					selected = 1;
					password.setNormalImage(sheet.getSprite(0, 1));
					user.setNormalImage(sheet.getSprite(0, 0));
					
				}
				else {
					
					selected = 0;
					user.setNormalImage(sheet.getSprite(0, 1));
					password.setNormalImage(sheet.getSprite(0, 0));
					
				}
				
			} else if (id == 28) {
				
				submit();
				
			}
			
		}

		@Override
		public void keyReleased(int id, char character) {
			
		}
		
	}
	
}
