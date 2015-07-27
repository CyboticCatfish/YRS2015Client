package net.cybotic.catfish.src.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.*;

public class ScriptEnv {
	
	public class JSObject {
	
		public Object object;
		public String name;
		
		public JSObject(Object object, String name) {
		
			this.name= name;
			this.object = object;
			
		} 
		
	}
	
	public Context context;
	List<JSObject> objects = new ArrayList<JSObject>();
	
	public ScriptEnv() {
		
		context = Context.enter();
		
	}

	public void addObject(Object object, String name) {
		
		objects.add(new JSObject(object, name));
		
	}
	
	private void addJSObject(JSObject jso, Scriptable scope) {
		
		scope.put(jso.name, scope, jso.object);
		
	}
	
	public boolean launchScript(String script) {
		
		boolean result = true;
		
		try {
			
			Context.enter();
			Scriptable scope = context.initStandardObjects();
			
            for (int i = 0; i < objects.size(); i++) {
        
            	addJSObject(objects.get(i), scope);
            	
            }

            Script executable = context.compileReader(new StringReader(script), "", 1, null);
            executable.exec(context, scope);
            
		} catch (Exception e) {
			
			result = false;
			
		} finally {
			
			Context.exit();
			
		}
		
		return result;
	}
	
	public boolean launchScript(File file) throws IOException {
		
		String script = "", s = "";
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		while((s = br.readLine()) != null) script += s;
		
		br.close();
		
		return this.launchScript(script);
		
	}
}
