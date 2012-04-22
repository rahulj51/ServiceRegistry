package main.rules;

import java.util.Set;

public class ExactMatcher extends Matcher {
	
	private Class matchedClass = TOP_CLASS;

	@Override
	public Class<?> match(Class<?> lookupType, Set<Class<?>> registryKeys) {

		if (!exactClassNameMatch(lookupType, registryKeys) && 
				!directInheritanceMatch(lookupType, registryKeys)) {
			
			if (next != null) {
				return next.match(lookupType, registryKeys);
			}
		}
		
		return matchedClass;
	}

	private boolean exactClassNameMatch(Class<?> lookupType, Set<Class<?>> registryKeys) {

		boolean matched = false;
		
		if (registryKeys.contains(lookupType)) {
			this.matchedClass = lookupType;
			matched = true;
		}
		
		return matched;
	}

	private boolean directInheritanceMatch(Class<?> lookupType, Set<Class<?>> registryKeys) {

		boolean matched = false;
		
		 if (! lookupType.isInterface()) {
			 matched = directInheritanceMatchForAClass(lookupType, registryKeys);
		 } else {
		 
			 matched = directInheritanceMatchForAnInterface(lookupType, registryKeys);
		 }
		 
		 return matched;
		
	}

	private boolean directInheritanceMatchForAClass(Class<?> lookupType, Set<Class<?>> registryKeys) {

		 boolean matched = false;
		 
		 //get the super class if any
		 Class<?> superType = lookupType.getSuperclass();
		 if (superType != java.lang.Object.class) {
			 //check for super class or interfaces
			 matched = exactClassNameMatch(superType, registryKeys) || interfacesMatch(lookupType, registryKeys) ;
		 } else {
			 //check for implemented interfaces
			 matched = interfacesMatch(lookupType, registryKeys);
		 }
		 
		 return matched;
	}
	
	private boolean directInheritanceMatchForAnInterface(Class<?> lookupType, Set<Class<?>> registryKeys) {

		return interfacesMatch(lookupType, registryKeys);
	}	

	private boolean interfacesMatch(Class<?> lookupType, Set<Class<?>> registryKeys) {

		boolean matched = false;
		
		Class<?>[] allInterfaces = lookupType.getInterfaces();
		
		for (Class<?> anInterface : allInterfaces) {  //will check in sequence of definition
			if (exactClassNameMatch(anInterface, registryKeys)) {
				matched = true;
				break;
			}
		}
		
		return matched;
		
	}

}
