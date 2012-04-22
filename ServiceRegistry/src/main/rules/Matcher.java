package main.rules;

import java.util.Set;

public abstract class Matcher {

	protected Matcher next;
	protected static final Class TOP_CLASS = Class.class;
	
	public void setNext(Matcher matcher) {
		next = matcher;
	}
	
	
	public abstract Class<?> match(Class<?> lookupType, Set<Class<?>> registryKeys) ;
}
