package main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import main.rules.ExactMatcher;
import main.rules.Matcher;
import main.rules.ParentMatcher;
import sampletypes.AnotherArbitraryClass;
import sampletypes.AnotherRandomInterface;
import sampletypes.ArbitraryClass;

public class ServiceRegistry<T> {

	private Map<Class<?>, T> registryStore = new HashMap<Class<?>,T>();
	
	private Matcher startMatcher;
	
	public ServiceRegistry() {
		
		Matcher secondMatcher = new ParentMatcher();
		startMatcher = new ExactMatcher();
		startMatcher.setNext(secondMatcher);
		
	}
	
	
	public void register(Class<?> registeredType, T registeredObject) {

		registryStore.put(registeredType, registeredObject);
		System.out.println("-----------" + registryStore);
	}

	public T get(Class<?> lookupType) {

		Class<?> matchedClass = startMatcher.match(lookupType, registryStore.keySet());
		return registryStore.get(matchedClass);
	}
	
	public static void main(String[] args) {
		
		ServiceRegistry<String> sr = new ServiceRegistry<String>();
		sr.register(AnotherRandomInterface.class, "hello");
		sr.register(ArbitraryClass.class, "world");
		System.out.println(sr.registryStore);
		
		System.out.println(sr.get(AnotherArbitraryClass.class));
		
	}	

}
