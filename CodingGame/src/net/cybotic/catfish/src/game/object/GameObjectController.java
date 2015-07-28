package net.cybotic.catfish.src.game.object;

public class GameObjectController {
	
	private GameObject target;
	private boolean enabled = true;
	
	public GameObjectController(GameObject target) {
		
		this.target = target;
		
	}
	
	public void moveForward() {
		
		if (enabled) {
		
			while (target.isMoving()) {
				
				try {
					
					Thread.sleep(50);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
			target.moveForward();
		
		}
		
	}
	
	public void turnClockwise() {
		
		if (enabled) {
		
			while (target.isMoving()) {
				
				try {
					
					Thread.sleep(50);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
			target.turnClockwise();
		
		}
		
	}
	
	public void turnAntiClockwise() {
		
		if (enabled) {
		
			while (target.isMoving()) {
				
				try {
					
					Thread.sleep(50);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
			target.turnAntiClockwise();
		
		}
		
	}
	
	public void interact() {
		
		//TODO make interacted with execute on the nearest object
		
	}
	
	public void disable() {
		
		enabled = false;
		
	}
	
	public int getX() {
		
		return target.getX();
		
	}
	
	public int getY() {
		
		return target.getY();
		
	}
	
	public boolean objectToFront() {
		
		
		for (GameObject object : target.getGame().getGameObjects()) {
			
			if (this.target.getDir() == 0) {
				
				if (object.getY() == this.target.getY() - 1) return true;
				
			} else if (this.target.getDir() == 1) {
				
				if (object.getX() == this.target.getX() + 1) return true;
				
			} else if (this.target.getDir() == 2) {
				
				if (object.getY() == this.target.getY() + 1) return true;
				
			} else if (this.target.getDir() == 3) {
				
				if (object.getX() == this.target.getX() - 1) return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean objectToBack() {
		
		
		for (GameObject object : target.getGame().getGameObjects()) {
			
			if (this.target.getDir() == 2) {
				
				if (object.getY() == this.target.getY() - 1) return true;
				
			} else if (this.target.getDir() == 3) {
				
				if (object.getX() == this.target.getX() + 1) return true;
				
			} else if (this.target.getDir() == 0) {
				
				if (object.getY() == this.target.getY() + 1) return true;
				
			} else if (this.target.getDir() == 1) {
				
				if (object.getX() == this.target.getX() - 1) return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean objectToLeft() {
		
		
		for (GameObject object : target.getGame().getGameObjects()) {
			
			if (this.target.getDir() == 1) {
				
				if (object.getY() == this.target.getY() - 1) return true;
				
			} else if (this.target.getDir() == 2) {
				
				if (object.getX() == this.target.getX() + 1) return true;
				
			} else if (this.target.getDir() == 3) {
				
				if (object.getY() == this.target.getY() + 1) return true;
				
			} else if (this.target.getDir() == 0) {
				
				if (object.getX() == this.target.getX() - 1) return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public boolean objectToRight() {
		
		for (GameObject object : target.getGame().getGameObjects()) {
			
			if (this.target.getDir() == 3) {
				
				if (object.getY() == this.target.getY() - 1) return true;
				
			} else if (this.target.getDir() == 0) {
				
				if (object.getX() == this.target.getX() + 1) return true;
				
			} else if (this.target.getDir() == 1) {
				
				if (object.getY() == this.target.getY() + 1) return true;
				
			} else if (this.target.getDir() == 2) {
				
				if (object.getX() == this.target.getX() - 1) return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public String getObjectInFront() {
		
		if (objectToFront()) {
			
			for (GameObject object : target.getGame().getGameObjects()) {
				
				if (this.target.getDir() == 0) {
					
					if (object.getY() == this.target.getY() - 1) return object.getName();
					
				} else if (this.target.getDir() == 1) {
					
					if (object.getX() == this.target.getX() + 1) return object.getName();
					
				} else if (this.target.getDir() == 2) {
					
					if (object.getY() == this.target.getY() + 1) return object.getName();
					
				} else if (this.target.getDir() == 3) {
					
					if (object.getX() == this.target.getX() - 1) return object.getName();
					
				}
				
			}
			
		}
		
		return null;
		
	}
	
}
