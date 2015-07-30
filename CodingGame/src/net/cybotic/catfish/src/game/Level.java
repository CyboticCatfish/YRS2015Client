package net.cybotic.catfish.src.game;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.cybotic.catfish.src.game.object.Coin;
import net.cybotic.catfish.src.game.object.Door;
import net.cybotic.catfish.src.game.object.GameObject;
import net.cybotic.catfish.src.game.object.PressurePlate;
import net.cybotic.catfish.src.game.object.Robot;
import net.cybotic.catfish.src.game.object.Wall;

public class Level {
	
	private int width, height;
	private List<GameObject> objects;
	private String XML;
	private Game game;
	private int totalCoins = 0;
	
	public Level(String XML, Game game) throws ParserConfigurationException, SAXException, IOException, SlickException {
		
		objects = new ArrayList<GameObject>();
		
		this.XML = XML;
		this.game = game;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(XML));
		Document doc = dBuilder.parse(is);
		
		doc.getDocumentElement().normalize();
			
		Element rootElement = doc.getDocumentElement();
		if (rootElement.hasAttribute("width")) this.width = Integer.parseInt(rootElement.getAttribute("width"));
		else this.width = 16;
		if (rootElement.hasAttribute("height")) this.height = Integer.parseInt(rootElement.getAttribute("height"));
		else this.height = 16;
		
		this.objects = this.getObjects();
		
	}
	
	public List<GameObject> getObjects() throws ParserConfigurationException, SAXException, IOException, SlickException {
		
		totalCoins = 0;
		
		objects = new ArrayList<GameObject>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(XML));
		Document doc = dBuilder.parse(is);
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("object");
			
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			Element eElement = (Element) nNode;
				
			if (eElement.hasAttribute("typeid")) {
				
				int id = Integer.parseInt(eElement.getAttribute("typeid"));
				int x = 0, y = 0, z = 0, dir = 2, listenerLevel = 0;
				boolean scriptable = false, collidable = true;
				String script = eElement.getTextContent();
				
				if (eElement.hasAttribute("x")) x = Integer.parseInt(eElement.getAttribute("x"));
				if (x >= width) x = width - 1;
				if (eElement.hasAttribute("y")) y = Integer.parseInt(eElement.getAttribute("y"));
				if (y >= width) y = height - 1;
				if (eElement.hasAttribute("z")) z = Integer.parseInt(eElement.getAttribute("z"));
				if (eElement.hasAttribute("direction")) dir = Integer.parseInt(eElement.getAttribute("direction"));
				while (dir > 3) dir -= 4;
				if (eElement.hasAttribute("scriptable")) scriptable = Boolean.parseBoolean(eElement.getAttribute("scriptable"));
				if (eElement.hasAttribute("collidable")) collidable = Boolean.parseBoolean(eElement.getAttribute("collidable"));
				if (eElement.hasAttribute("listenerLevel")) listenerLevel = Integer.parseInt(eElement.getAttribute("listenerLevel"));
				
				if (id == 0) this.objects.add(new Robot(x, y, dir, script, game, collidable));
				else if (id == 1) this.objects.add(new Wall(x, y, game));
				else if (id == 2) {
					
					this.objects.add(new Coin(x, y, game));
					totalCoins += 1;
					
				} else if (id == 3) this.objects.add(new PressurePlate(x, y, game, listenerLevel));
				 else if (id == 4) this.objects.add(new Door(x, y, dir, game, listenerLevel));
				
			}
			
		}
		
		return new ArrayList<GameObject>(this.objects);
		
	}

	public int getWidth() {
		
		return this.width;
		
	}
	
	public int getHeight() {
		
		return this.height;
		
	}
	
	public int getTotalCoins() {
		
		return this.totalCoins;
		
	}

}
