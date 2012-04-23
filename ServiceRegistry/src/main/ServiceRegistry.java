package main;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.rules.ExactMatcher;
import main.rules.Matcher;
import main.rules.ParentMatcher;
import sampletypes.AnotherArbitraryClass;
import sampletypes.AnotherRandomSubInterface;
import sampletypes.ArbitraryClass;

/*
 * Example application for chain-of-responsibility and java reflection API
 */
public class ServiceRegistry<T> {

	private Map<Class<?>, T> registryStore = new HashMap<Class<?>,T>();
	
	private Matcher startMatcher;
	
	public ServiceRegistry() {
		
		Matcher secondMatcher = new ParentMatcher();
		startMatcher = new ExactMatcher();
		startMatcher.setNext(secondMatcher);
		
	}
	
  public ServiceRegistry(Matcher matcher) {
    startMatcher = matcher;
  }	
	
	
	public void register(Class<?> registeredType, T registeredObject) {

		registryStore.put(registeredType, registeredObject);
		//System.out.println("-----------" + registryStore);
	}

	public T get(Class<?> lookupType) {

		Class<?> matchedClass = startMatcher.match(lookupType, registryStore.keySet());
		return registryStore.get(matchedClass);
	}
	
	
	Map<Class<?>, T> getRegistryStore() {
	  //for sniffing
	  return Collections.unmodifiableMap(registryStore);
	}
	
	public static void main(String[] args) {
		
		ServiceRegistry<String> sr = new ServiceRegistry<String>();
		sr.register(AnotherRandomSubInterface.class, "hello");
		sr.register(ArbitraryClass.class, "world");
		
		System.out.println(sr.registryStore);
		System.out.println(sr.get(AnotherArbitraryClass.class));
		
	}	

}
