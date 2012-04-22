package main.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * calculates the weight of each entry in the hieraqrchy
 * in general, closest parents are preferred.
 * 
 * @author Rahul
 *
 */
public class ParentMatcher extends Matcher {

	private Class matchedClass = TOP_CLASS;	
	
	private Map<Class<?>, Integer> matchedParentsAndWeight = new HashMap<Class<?>, Integer>();
	
	private static final int STARTING_WEIGHT_CLASS = 10;
	private static final int CLASS_WEIGHT_OFFSET = 1000;
	
	private static final int STARTING_WEIGHT_INTERFACE = 20;
	private static final int INTERFACE_WEIGHT_OFFSET = 1000;
	
	@Override
	public Class<?> match(Class<?> lookupType, Set<Class<?>> registryKeys) {

		matchAndWeighAllParents(lookupType, registryKeys);
		
		System.out.println(matchedParentsAndWeight);
		matchedClass = parentWithMaxWeight();
		
		return matchedClass;
		
	}


	private void matchAndWeighAllParents(Class<?> lookupType, Set<Class<?>> registryKeys) {
		
		 if (! lookupType.isInterface()) {
			 
			 matchAndWeighSuperClassAndItsParents(lookupType, registryKeys, STARTING_WEIGHT_CLASS);
			 matchAndWeighInterfacesAndTheirParents(lookupType.getInterfaces(), registryKeys, STARTING_WEIGHT_INTERFACE);
			 
		 } else {
			 matchAndWeighInterfacesAndTheirParents(lookupType.getInterfaces(), registryKeys, STARTING_WEIGHT_INTERFACE);
		 }
	}


	private void matchAndWeighSuperClassAndItsParents(Class<?> lookupType, Set<Class<?>> registryKeys, int weight ) {
		
		 Class<?> superType = lookupType.getSuperclass();
		 
		 if (superType == TOP_CLASS) {
			 if (registryKeys.contains(superType)) {
				 matchedParentsAndWeight.put(superType, weight);
			 }
			 
			 //nothing more to do
			 return;
			 
		 } else {
			 
			 if (registryKeys.contains(superType)) {
				 
				 //match found. look no further
				 matchedParentsAndWeight.put(superType, weight);
			 } else {
				 //look some more
				 matchAndWeighSuperClassAndItsParents(superType, registryKeys, weight + CLASS_WEIGHT_OFFSET);
			 }
		 }		
		
	}

	
	private void matchAndWeighInterfacesAndTheirParents(Class<?>[] interfaces, Set<Class<?>> registryKeys, int weight) {

		for (Class<?> anInterface : interfaces) {  //will check in sequence of definition

			if (registryKeys.contains(anInterface)) {
				matchedParentsAndWeight.put(anInterface, weight);
				//look no further for this interface
			} else {
				matchAndWeighInterfacesAndTheirParents(anInterface.getInterfaces(), registryKeys, weight + INTERFACE_WEIGHT_OFFSET);
			}
			
			weight+= STARTING_WEIGHT_INTERFACE ;
		}		
	}
	

	private Class<?> parentWithMaxWeight() {

		Class<?> matchedClassWithMinWeight = null;
		int minWeight = 1000000;
		for (Class<?> matchedClass : matchedParentsAndWeight.keySet()) {
			
			int weight = matchedParentsAndWeight.get(matchedClass);
			if (weight <= minWeight) {
				minWeight = weight;
				matchedClassWithMinWeight = matchedClass;
			}
		}
		
		return matchedClassWithMinWeight;
	}

}
