package net.cybotic.catfish.src.script;

import java.io.IOException;
import java.io.StringReader;

import net.cybotic.catfish.src.game.object.GameObject;
import net.cybotic.catfish.src.game.object.GameObjectController;

import org.mozilla.javascript.*;

public class ScriptEnv {
	
	private GameObject target;
	
	public ScriptEnv(GameObject target) {
		
		this.target = target;
	}
	
	public void launchScript() {
		
		(new Thread(new ScriptThread())).start();
		
	}

	public class ScriptThread implements Runnable {
		
		@Override
		public void run() {
			
			Context context = Context.enter();
			Scriptable scope = context.initStandardObjects();
			
            scope.put("object", scope, new GameObjectController(target));

            Script executable;
			
            try {
				
            	executable = context.compileReader(new StringReader(target.getScript()), "", 1, null);
				executable.exec(context, scope);
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
            
		}
		
		
	}
}
