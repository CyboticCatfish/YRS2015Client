package net.cybotic.catfish.src.game.object;

public class GameObjectController {
	
	private GameObject target;
	
	public GameObjectController(GameObject target) {
		
		this.target = target;
		
	}
	
	public void moveForward() {
		
		while (target.isMoving()) {
			
			try {
				
				Thread.sleep(50);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		target.moveForward();
		
	}
	
	public void turnClockwise() {
		
		while (target.isMoving()) {
			
			try {
				
				Thread.sleep(50);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		
		
		target.turnClockwise();
		
	}
	
	public void turnAntiClockwise() {
		
		while (target.isMoving()) {
			
			try {
				
				Thread.sleep(50);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		target.turnAntiClockwise();
		
	}
	
	public void interact() {
		
		//TODO make interacted with execute on the nearest object
		
	}
	
}
