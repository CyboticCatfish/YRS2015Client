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
	
}
