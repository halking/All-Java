package com.hal.imporve.chapter01;

import java.io.FileReader;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class EngineMain {

	public static void main(String[] args) throws Exception {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
			Bindings bind = engine.createBindings();
			bind.put("factor", 2);
			engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				int first =  scanner.nextInt();
				int second = scanner.nextInt();
				System.out.println("input params:"+first+"  ,"+second);
				engine.eval(new FileReader("E:/test.js"));
				if (engine instanceof Invocable) {
					Invocable invocable = (Invocable)engine;
					Double result = (Double) invocable.invokeFunction("add", first,second);
					System.out.println("result : "+result.intValue());
				}
			}
	}

}
